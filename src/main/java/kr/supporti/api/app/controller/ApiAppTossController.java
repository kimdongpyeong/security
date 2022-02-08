package kr.supporti.api.app.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.WeakHashMap;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.dto.TossDto;
import kr.supporti.api.common.entity.PaymentVirtualAccountEntity;
import kr.supporti.api.common.service.PaymentVirtualAccountService;
import kr.supporti.api.common.service.TossService;

@RestController
@RequestMapping(path = "api/app/toss")
public class ApiAppTossController {

    @Autowired
    private TossService tossService;

    @Autowired
    private PaymentVirtualAccountService paymentVirtualAccountService;

    @PostMapping("/billing-key")
    public String createBillingKey(@ModelAttribute TossDto tossDto) throws Exception{
        return tossService.createBillingKey(tossDto);
    }

    @PostMapping("/regular-payment/{userId}")
    public String regularPayment(@PathVariable(name="userId") Long userId, @RequestBody PaymentHistoryDto paymentHistoryDto) throws Exception{
        return tossService.regularPayment(userId, paymentHistoryDto.getOrderId());
    }

    @GetMapping("/card/success")
    public String cardPaymentSuccess(
                @RequestParam(name="paymentKey") String paymentKey,
                @RequestParam(name="orderId") String orderId,
                @RequestParam(name="amount") Integer amount) throws Exception{

        return tossService.cardApprovalPayment(paymentKey, orderId, amount);
    }

    @GetMapping("/card/fail")
    public void cardPaymentFail() {

    }

    @GetMapping("/money/success")
    public PaymentVirtualAccountEntity moneyPaymentSuccess(
                @RequestParam(name="paymentKey") String paymentKey,
                @RequestParam(name="orderId") String orderId,
                @RequestParam(name="amount") Integer amount) throws Exception{

        return tossService.moneyApprovalPayment(paymentKey, orderId, amount);
    }

    @GetMapping("/money/fail")
    public void moneyPaymentFail() {

    }

    @PostMapping("/virtual-account/success")
    public void virtualAccountSuccess(@RequestBody WeakHashMap<String, Object> param) throws Exception{
        tossService.modifyPaymentVirtualAccount(param);
    }

    @GetMapping("/card/cancel/{paymentId}")
    public String cancelCardPayment(@PathVariable(name = "paymentId") Long paymentId) throws IOException {
        return tossService.cancelCardPayment(paymentId);
    }

    @GetMapping("/search")
    public void searchOrder(
            @RequestParam(name="orderId") String orderId) throws IOException {
        URL url = new URL("https://api.tosspayments.com/v1/payments/orders/" + orderId);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "Basic dGVzdF9za19scFAyWXhKNEs4N1pvNG1XNHE5clJHWndYTE9iOg==");
        con.setRequestMethod("GET");
        con.setConnectTimeout(5000);
        con.setReadTimeout(5000);
        con.setDoInput(true);
        con.setDoOutput(true);

        // 응답
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
        JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

        con.disconnect();
        in.close();

        System.out.println(jsonObj);

    }
}
