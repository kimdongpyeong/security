package kr.supporti.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.dto.PaymentHistoryParamDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.api.common.service.PaymentHistoryService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/payment")
public class ApiCommonPaymentHistoryController {

    @Autowired
    private PaymentHistoryService paymentHistoryService;

    @GetMapping(path = "")
    public PageResponse<PaymentHistoryEntity> getPaymentHistoryList(@ModelAttribute PaymentHistoryParamDto paymentHistoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return paymentHistoryService.getPaymentHistoryList(paymentHistoryParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public PaymentHistoryEntity getPaymentHistory(@PathVariable(name = "id") Long id) {
        return paymentHistoryService.getPaymentHistory(id);
    }

    @GetMapping(path = "/order/{orderId}")
    public PaymentHistoryEntity getPaymentHistoryByOrderId(@PathVariable(name = "orderId") String orderId) {
        return paymentHistoryService.getPaymentHistoryByOrderId(orderId);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public PaymentHistoryEntity createPaymentHistory(@RequestBody PaymentHistoryDto paymentHistoryDto) {
        return paymentHistoryService.createPaymentHistory(paymentHistoryDto);
    }
    @PutMapping(path = "{id}")
    public Integer modifyPayment(@PathVariable(name = "id") Long id, @RequestBody PaymentHistoryDto paymentHistoryDto) {
        return paymentHistoryService.modifyPaymentHistory(id, paymentHistoryDto);
    }
}