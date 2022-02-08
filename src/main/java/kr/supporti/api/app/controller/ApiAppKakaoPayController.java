package kr.supporti.api.app.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.service.KakaoPayService;

@RestController
@RequestMapping(path = "api/app/kakao")
public class ApiAppKakaoPayController {

    @Autowired
    private KakaoPayService kakaoPayService;

    @GetMapping("/request/{paymentId}")
    public String request(@PathVariable(name = "paymentId") Long id) throws IOException {
        return kakaoPayService.requestPayment(id);
    }

    @GetMapping("/success")
    public void success(@RequestParam("paymentId") Long paymentId, @RequestParam("pg_token") String pgToken, HttpServletResponse response) throws IOException, InterruptedException, ParseException {
        kakaoPayService.successPayment(paymentId, pgToken, response);
    }

    @GetMapping("/fail")
    public void fail(@RequestParam("paymentId") Long paymentId) {
        kakaoPayService.failPayment(paymentId);
    }

    @GetMapping("/cancel/{paymentId}")
    public String cancel(@PathVariable(name = "paymentId") Long paymentId) throws IOException, ParseException {
        return kakaoPayService.cancelPayment(paymentId);
    }

}
