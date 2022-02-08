package kr.supporti.api.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.RegularPaymentHistoryMapper;
import kr.supporti.common.Constants;

@Validated
@Service
public class KakaoPayService {

    @Value("${supporti.domain}")
    private String serverDomain;

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @Autowired
    private RegularPaymentHistoryMapper regularPaymentHistoryMapper;

    @Autowired
    private UserService userService;

    // 결제요청
    public String requestPayment(Long paymentId) throws IOException {
        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistory(paymentId);
        return singlePayment(paymentId);
    }

    // 단건결제
    public String singlePayment(Long paymentId) throws IOException {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        @SuppressWarnings("unchecked")
        Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
        UserEntity loginEntity = (UserEntity) token.get("user");

        URL url = new URL("https://kapi.kakao.com/v1/payment/ready");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        con.setRequestProperty("Authorization", "KakaoAK 2d4744d0053b08d6159c35221d3008a6");
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistory(paymentId);
        UserEntity userEntity = userService.getUser(paymentEntity.getRequestLecturerId());
        String type = (paymentEntity.getPaymentType().equals("S"))? "단건결제" : "정기결제";

        String cid = (paymentEntity.getPaymentType().equals("S"))? Constants.SINGLE_CID : Constants.REGULAR_CID;

        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", paymentEntity.getOrderId());
        params.put("partner_user_id", loginEntity.getId());
        params.put("item_name", userEntity.getName() + " 강사님이 요청한 " + type);
        params.put("quantity", "1");
        params.put("total_amount", paymentEntity.getAmount());
        params.put("tax_free_amount", "100");
        params.put("approval_url", serverDomain + "/api/app/kakao/success?paymentId="+ paymentId);
        params.put("cancel_url", serverDomain + "/api/app/kakao/cancel?paymentId="+ paymentId);
        params.put("fail_url", serverDomain + "/api/app/kakao/fail?paymentId="+ paymentId);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        con.getOutputStream().write(postDataBytes);

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();

        paymentHistoryService.modifyPaymentHistory(
                paymentId,
                PaymentHistoryDto.builder()
                    .kakaoTid(jsonObj.get("tid").toString())
                    .paymentUserId(loginEntity.getId()).build());

        return jsonObj.get("next_redirect_pc_url").toString();
    }

    // 정기결제
    public void regularPayment(Long paymentId) throws IOException, ParseException {

        URL url = new URL("https://kapi.kakao.com/v1/payment/subscription");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        con.setRequestProperty("Authorization", "KakaoAK 2d4744d0053b08d6159c35221d3008a6");
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistory(paymentId);
        UserEntity userEntity = userService.getUser(paymentEntity.getRequestLecturerId());

        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("cid", Constants.REGULAR_CID);
        params.put("sid", paymentEntity.getKakaoSid());
        params.put("partner_order_id", paymentEntity.getOrderId());
        params.put("partner_user_id", paymentEntity.getPaymentUserId());
        params.put("item_name", userEntity.getName() + " 강사님이 요청한 정기결제");
        params.put("quantity", "1");
        params.put("total_amount", paymentEntity.getAmount());
        params.put("tax_free_amount", "100");
        params.put("approval_url", serverDomain + "/api/app/kakao/success?paymentId="+ paymentId);
        params.put("cancel_url", serverDomain + "/api/app/kakao/cancel?paymentId="+ paymentId);
        params.put("fail_url", serverDomain + "/api/app/kakao/fail?paymentId="+ paymentId);

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        con.getOutputStream().write(postDataBytes);

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();

        PaymentHistoryDto dto = new PaymentHistoryDto();

        if(jsonObj.get("approved_at").toString() != null) {
            // 결제방법 (카드, 현금)
            dto.setRegularPaymentYn("Y");
            dto.setRegularPaymentFailDesc("결제성공");
            dto.setState(Constants.PAYMENT_STATE_SUCCESS);

            regularPaymentHistoryMapper.insertRegularPaymentByKakao(paymentId, jsonObj.get("tid").toString(), jsonObj.get("sid").toString());
        } else {
            dto.setRegularPaymentYn("N");

            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonObj.get("extras").toString());
            jsonObj = (JSONObject) obj;
            dto.setRegularPaymentFailDesc(jsonObj.get("method_result_message").toString());
        }
        paymentHistoryService.modifyPaymentHistory(paymentId, dto);
    }

    public void successPayment(Long paymentId, String pgToken, HttpServletResponse response) throws IOException, ParseException{
        URL url = new URL("https://kapi.kakao.com/v1/payment/approve");

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        con.setRequestProperty("Authorization", "KakaoAK 2d4744d0053b08d6159c35221d3008a6");
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        PaymentHistoryEntity paymentEntity = paymentHistoryService.getPaymentHistory(paymentId);
        String cid = (paymentEntity.getPaymentType().equals("S"))? Constants.SINGLE_CID : Constants.REGULAR_CID;

        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("cid", cid);
        params.put("partner_order_id", paymentEntity.getOrderId());
        params.put("partner_user_id", paymentEntity.getPaymentUserId());
        params.put("tid", paymentEntity.getKakaoTid());
        params.put("pg_token", pgToken);
        params.put("total_amount", paymentEntity.getAmount());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        con.getOutputStream().write(postDataBytes);

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();

        if(jsonObj.get("approved_at").toString() != null) {
            // 결제방법 (카드, 현금)
            String paymentMethodCd =
                        (jsonObj.get("payment_method_type").toString().equals("CARD"))? Constants.PAYMENT_METHOD_CARD : Constants.PAYMENT_METHOD_MONEY;
            String tid = jsonObj.get("tid").toString(); // 결제고유번호
            String aid = jsonObj.get("aid").toString(); // 요청고유번호
            String sid = (jsonObj.get("sid") != null)? jsonObj.get("sid").toString() : null; // 결제 완료 후 고유번호
            String general = Constants.PAYMENT_GENERAL_PERSON; // 결제자구분(개인)
            String paymentDiv = Constants.PAYMENT_DIV_KAKAO; // 결제수단 (카카오)
            String state = Constants.PAYMENT_STATE_SUCCESS;

            PaymentHistoryDto dto = PaymentHistoryDto.builder()
                                        .kakaoTid(tid)
                                        .kakaoAid(aid)
                                        .kakaoSid(sid)
                                        .paymentMethodCd(paymentMethodCd)
                                        .paymentGeneral(general)
                                        .state(state)
                                        .paymentDiv(paymentDiv)
                                        .regularPaymentYn((paymentEntity.getPaymentType().equals("R"))? "Y" : null)
                                        .build();
            paymentHistoryService.modifyPaymentHistory(paymentId, dto);
            response.sendRedirect(serverDomain + "/mypage/users/payment/kakao-success");
        } else {
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(jsonObj.get("extras").toString());
            jsonObj = (JSONObject) obj;
            response.sendRedirect(serverDomain + "/mypage/users/payment/kakao-fail?result=" + jsonObj.get("method_result_message").toString());
        }
    }

    // 결제취소
    public String cancelPayment(Long paymentId) throws IOException, ParseException {

        PaymentHistoryEntity entity = paymentHistoryService.getPaymentHistory(paymentId);
        String cid = (entity.getPaymentType().equals("S"))? Constants.SINGLE_CID : Constants.REGULAR_CID;

        URL orderUrl = new URL("https://kapi.kakao.com/v1/payment/order");

        HttpURLConnection con = (HttpURLConnection) orderUrl.openConnection();

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        con.setRequestProperty("Authorization", "KakaoAK 2d4744d0053b08d6159c35221d3008a6");
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        WeakHashMap<String, Object> params = new WeakHashMap<>();
        params.put("cid", cid);
        params.put("tid", entity.getKakaoTid());

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        byte[] postDataBytes = postData.toString().getBytes("UTF-8");

        con.getOutputStream().write(postDataBytes);

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        JSONParser parser = new JSONParser();
        Object obj = parser.parse(jsonObj.get("amount").toString());
        jsonObj = (JSONObject) obj;

        Integer amount = Integer.parseInt(jsonObj.get("total").toString());
        Integer taxFree = Integer.parseInt(jsonObj.get("tax_free").toString());
        Integer vat = Integer.parseInt(jsonObj.get("vat").toString());

        URL cancelUrl = new URL("https://kapi.kakao.com/v1/payment/cancel");

        con = (HttpURLConnection) cancelUrl.openConnection();

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
        con.setRequestProperty("Authorization", "KakaoAK 2d4744d0053b08d6159c35221d3008a6");
        con.setRequestMethod("POST");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        WeakHashMap<String, Object> params2 = new WeakHashMap<>();
        params2.put("cid", cid);
        params2.put("tid", entity.getKakaoTid());
        params2.put("cancel_amount", amount);
        params2.put("cancel_tax_free_amount", taxFree);
        params.put("cancel_vat_amount", vat);

        postData = new StringBuilder();
        for (Map.Entry<String, Object> param : params2.entrySet()) {
            if (postData.length() != 0)
                postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }
        postDataBytes = postData.toString().getBytes("UTF-8");

        con.getOutputStream().write(postDataBytes);

        // 응답
        in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();

        PaymentHistoryDto historyDto = new PaymentHistoryDto();

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        @SuppressWarnings("unchecked")
        Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
        UserEntity loginEntity = (UserEntity) token.get("user");
        String result = "success";

        if(jsonObj.get("status") != null && jsonObj.get("status").toString().equals("CANCEL_PAYMENT")) {
            historyDto = PaymentHistoryDto.builder()
                    .state(Constants.PAYMENT_STATE_REFUND)
                    .refundProcessId(loginEntity.getId())
                    .build();
            paymentHistoryService.modifyPaymentHistory(paymentId, historyDto);
        } else {
            parser = new JSONParser();
            obj = parser.parse(jsonObj.get("extras").toString());
            jsonObj = (JSONObject) obj;
            result = jsonObj.get("method_result_message").toString();
        }

        return result;
    }

    // 결제실패
    public void failPayment(Long paymentId) {
    }
}