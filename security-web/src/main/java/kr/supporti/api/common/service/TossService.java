package kr.supporti.api.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Map;
import java.util.WeakHashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.dto.PaymentVirtualAccountDto;
import kr.supporti.api.common.dto.TossDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.api.common.entity.PaymentVirtualAccountEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserPaymentInfoEntity;
import kr.supporti.api.common.mapper.RegularPaymentHistoryMapper;
import kr.supporti.api.common.mapper.UserPaymentInfoMapper;
import kr.supporti.api.common.repository.UserPaymentInfoRepository;
import kr.supporti.common.Constants;

@Validated
@Service
public class TossService {

    @Value("${supporti.domain}")
    private String serverDomain;

    @Value("${supporti.toss.secretkey}")
    private String tossPaymentKey;

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @Autowired
    private PaymentVirtualAccountService paymentVirtualAccountService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserPaymentInfoMapper userPaymentInfoMapper;

    @Autowired
    private UserPaymentInfoRepository userPaymentInfoRepository;

    @Autowired
    private RegularPaymentHistoryMapper regularPaymentHistoryMapper;

    // 빌링키 발급 API
    public String createBillingKey(TossDto tossDto) throws Exception {

        UserEntity userEntity = userService.getUser(tossDto.getCustomerId());

        URL url = new URL("https://api.tosspayments.com/v1/billing/authorizations/card");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + tossPaymentKey);
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject params = new JSONObject();

        String customerKey = Base64.getEncoder().encodeToString(userEntity.getUsername().getBytes());

        params.put("cardNumber", tossDto.getCardNumber());
        params.put("cardExpirationYear", tossDto.getCardExpirationYear());
        params.put("cardExpirationMonth", tossDto.getCardExpirationMonth());
        params.put("cardPassword", tossDto.getCardPassword());
        params.put("customerBirthday", tossDto.getCustomerBirthday());
        params.put("customerKey", customerKey);

        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(params.toString());
        os.flush();

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();
        os.close();

        if(jsonObj.get("cardNumber") != null) {
            String cardCompany = jsonObj.get("cardCompany").toString();
            String cardNumber = jsonObj.get("cardNumber").toString();
            String billingKey = jsonObj.get("billingKey").toString();

            // 사용자 결제정보 저장
            UserPaymentInfoEntity userPaymentInfoEntity = UserPaymentInfoEntity.builder()
                    .cardCompany(cardCompany)
                    .cardNumber(cardNumber)
                    .billingKey(billingKey)
                    .userId(userEntity.getId())
                    .build();

            userPaymentInfoRepository.save(userPaymentInfoEntity);

            // 토스 정기결제 API
            String result = regularPayment(userEntity.getId(), tossDto.getOrderId());

            return result;
        } else {
            return "fail";
        }
    }

    // 토스 정기결제 API
    public String regularPayment(Long userId, String orderId) throws Exception {

        // 결제자 정보 조회
        UserEntity paymentUserEntity = userService.getUser(userId);

        String customerKey = Base64.getEncoder().encodeToString(paymentUserEntity.getUsername().getBytes());

        // 결제내역 조회
        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistoryByOrderId(orderId);

        // 결제요청한 강사정보 조회
        UserEntity requestUserEntity = userService.getUser(paymentEntity.getRequestLecturerId());

        // 사용자 결제정보 조회
        UserPaymentInfoEntity paymentInfoEntity = userPaymentInfoMapper.selectUserPaymentInfoByUserId(paymentUserEntity.getId());
        Integer number = regularPaymentHistoryMapper.selectMaxNumberByPaymentHistoryId(paymentEntity.getId());

        URL url = new URL("https://api.tosspayments.com/v1/billing/" + paymentInfoEntity.getBillingKey());

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + tossPaymentKey);
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject params = new JSONObject();

        params.put("customerName", paymentUserEntity.getName());
        params.put("customerKey", customerKey);
        params.put("amount", paymentEntity.getAmount());
        params.put("orderId", orderId + "-" + (number + 1));
        params.put("orderName", requestUserEntity.getName() + " 강사님이 요청한 정기결제");

        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(params.toString());
        os.flush();

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();
        os.close();


        PaymentHistoryDto dto = new PaymentHistoryDto();
        String result = "success";

        if(jsonObj.get("approvedAt").toString() != null) {
            // 결제방법 카드
            String paymentMethodCd = Constants.PAYMENT_METHOD_CARD;
            String general = Constants.PAYMENT_GENERAL_PERSON; // 결제자구분(개인)
            String paymentDiv = Constants.PAYMENT_DIV_TOSS; // 결제수단 (토스)
            String state = Constants.PAYMENT_STATE_SUCCESS;

            if(number == 0) {
                dto = PaymentHistoryDto.builder()
                        .paymentMethodCd(paymentMethodCd)
                        .paymentUserId(userId)
                        .paymentGeneral(general)
                        .state(state)
                        .paymentDiv(paymentDiv)
                        .tossPaymentKey(jsonObj.get("paymentKey").toString())
                        .regularPaymentYn("Y")
                        .regularPaymentFailDesc("결제성공")
                        .build();
            } else {
                dto = PaymentHistoryDto.builder()
                        .tossPaymentKey(jsonObj.get("paymentKey").toString())
                        .regularPaymentYn("Y")
                        .regularPaymentFailDesc("결제성공")
                        .build();
            }

            regularPaymentHistoryMapper.insertRegularPaymentByToss(paymentEntity.getId(), jsonObj.get("paymentKey").toString());
        } else {
            dto.setRegularPaymentYn("N");
            dto.setRegularPaymentFailDesc("결제실패");
            result = "fail";
        }
        paymentHistoryService.modifyPaymentHistory(paymentEntity.getId(), dto);

        return result;
    }

    // 카드 결제승인 API
    public String cardApprovalPayment(String paymentKey, String orderId, Integer amount) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        @SuppressWarnings("unchecked")
        Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
        UserEntity loginEntity = (UserEntity) token.get("user");

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + tossPaymentKey);
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistoryByOrderId(orderId);
        UserEntity userEntity = userService.getUser(paymentEntity.getRequestLecturerId());

        if(paymentEntity.getAmount() != amount) {

        }

        JSONObject params = new JSONObject();

        params.put("amount", amount);
        params.put("orderId", orderId);

        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(params.toString());
        os.flush();

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();
        os.close();

        if(jsonObj.get("approvedAt").toString() != null) {
            // 결제방법 카드
            String paymentMethodCd = Constants.PAYMENT_METHOD_CARD;
            String general = Constants.PAYMENT_GENERAL_PERSON; // 결제자구분(개인)
            String paymentDiv = Constants.PAYMENT_DIV_TOSS; // 결제수단 (토스)
            String state = Constants.PAYMENT_STATE_SUCCESS;

            PaymentHistoryDto dto = PaymentHistoryDto.builder()
                                        .paymentMethodCd(paymentMethodCd)
                                        .paymentUserId(loginEntity.getId())
                                        .paymentGeneral(general)
                                        .state(state)
                                        .paymentDiv(paymentDiv)
                                        .regularPaymentYn((paymentEntity.getPaymentType().equals("R"))? "Y" : null)
                                        .tossPaymentKey(paymentKey)
                                        .build();
            paymentHistoryService.modifyPaymentHistory(paymentEntity.getId(), dto);

            return "success";
        } else {
            throw new Exception("결제 오류가 발생했습니다.");
        }
    }

    // 가상계좌 결제승인 API
    public PaymentVirtualAccountEntity moneyApprovalPayment(String paymentKey, String orderId, Integer amount) throws Exception {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

        @SuppressWarnings("unchecked")
        Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
        UserEntity loginEntity = (UserEntity) token.get("user");

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentKey);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + tossPaymentKey);
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistoryByOrderId(orderId);
        UserEntity userEntity = userService.getUser(paymentEntity.getRequestLecturerId());

        if(paymentEntity.getAmount() != amount) {

        }

        JSONObject params = new JSONObject();

        params.put("amount", amount);
        params.put("orderId", orderId);

        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(params.toString());
        os.flush();

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();
        os.close();

        System.out.println(jsonObj);

        if(jsonObj.get("virtualAccount").toString() != null) {

            // 결제방법 현금
            String paymentMethodCd = Constants.PAYMENT_METHOD_MONEY;
            String general = Constants.PAYMENT_GENERAL_PERSON; // 결제자구분(개인)
            String paymentDiv = Constants.PAYMENT_DIV_TOSS; // 결제수단 (토스)

            PaymentHistoryDto dto = PaymentHistoryDto.builder()
                                        .paymentMethodCd(paymentMethodCd)
                                        .paymentUserId(loginEntity.getId())
                                        .paymentGeneral(general)
                                        .paymentDiv(paymentDiv)
                                        .regularPaymentYn((paymentEntity.getPaymentType().equals("R"))? "Y" : null)
                                        .tossPaymentKey(paymentKey)
                                        .build();
            paymentHistoryService.modifyPaymentHistory(paymentEntity.getId(), dto);


            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonObj.get("virtualAccount").toString());
            jsonObj = (JSONObject) obj;

            String accountNumber = jsonObj.get("accountNumber").toString();
            String dueDateStr = jsonObj.get("dueDate").toString();
            LocalDateTime dueDate = LocalDateTime.parse(dueDateStr.substring(0, dueDateStr.lastIndexOf("+")), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            String bank = jsonObj.get("bank").toString();

            PaymentVirtualAccountDto accountDto = PaymentVirtualAccountDto.builder()
                    .paymentHistoryId(paymentEntity.getId())
                    .bank(bank)
                    .accountNumber(accountNumber)
                    .dueDate(dueDate)
                    .build();

            PaymentVirtualAccountEntity paymentVirtualAccountEntity = paymentVirtualAccountService.createPaymentVirtualAccount(accountDto);

            return paymentVirtualAccountEntity;
        } else {
            throw new Exception("결제 오류가 발생했습니다.");
        }
    }

    // 카드결제취소 API
    public String cancelCardPayment(Long paymentId) throws IOException {

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistory(paymentId);

        URL url = new URL("https://api.tosspayments.com/v1/payments/" + paymentEntity.getTossPaymentKey() + "/cancel");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic " + tossPaymentKey);
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        JSONObject params = new JSONObject();

        params.put("cancelReason", paymentEntity.getCancelReason());

        OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
        os.write(params.toString());
        os.flush();

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        String status = jsonObj.get("status").toString();
        String orderId = jsonObj.get("orderId").toString();

        String result = "success";

        if(status != null && status.equals("CANCELED")) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();

            @SuppressWarnings("unchecked")
            Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
            UserEntity loginEntity = (UserEntity) token.get("user");

            PaymentHistoryEntity historyEntity = paymentHistoryService.getPaymentHistoryByOrderId(orderId);

            PaymentHistoryDto historyDto = PaymentHistoryDto.builder()
                    .state(Constants.PAYMENT_STATE_REFUND)
                    .refundProcessId(loginEntity.getId())
                    .build();

            con.disconnect();
            in.close();
            os.close();

            paymentHistoryService.modifyPaymentHistory(historyEntity.getId(), historyDto);
        } else {
            result = jsonObj.get("message").toString();
        }

        return result;
    }

    public void modifyPaymentVirtualAccount(WeakHashMap<String, Object> param) throws Exception {
        String status = param.get("status").toString();
        String orderId = param.get("orderId").toString();

        if(status.equals("DONE")) {

            PaymentHistoryEntity historyEntity = paymentHistoryService.getPaymentHistoryByOrderId(orderId);

            PaymentVirtualAccountDto virtualAccountDto = PaymentVirtualAccountDto.builder()
                    .paymentHistoryId(historyEntity.getId())
                    .depositYn("Y")
                    .build();

            paymentVirtualAccountService.modifyPaymentVirtualAccountByHistoryId(virtualAccountDto);

            PaymentHistoryDto historyDto = PaymentHistoryDto.builder()
                    .state("S")
                    .build();

            paymentHistoryService.modifyPaymentHistory(historyEntity.getId(), historyDto);
        } else {
            throw new Exception("결제 오류가 발생했습니다.");
        }
    }
}