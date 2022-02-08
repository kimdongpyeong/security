package kr.supporti.api.common.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PaymentVirtualAccountDto;
import kr.supporti.api.common.entity.PaymentVirtualAccountEntity;
import kr.supporti.api.common.mapper.PaymentVirtualAccountMapper;
import kr.supporti.api.common.repository.PaymentVirtualAccountRepository;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;

@Validated
@Service
public class PaymentVirtualAccountService {

    @Autowired
    private PaymentVirtualAccountRepository paymentVirtualAccountRepository;

    @Autowired
    private PaymentVirtualAccountMapper paymentVirtualAccountMapper;

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public PaymentVirtualAccountEntity createPaymentVirtualAccount (
            @Valid @NotNull(groups = { CreateValidationGroup.class }) PaymentVirtualAccountDto paymentVirtualAccountDto) {

        PaymentVirtualAccountEntity paymentVirtualAccountEntity = PaymentVirtualAccountEntity.builder()
                .paymentHistoryId(paymentVirtualAccountDto.getPaymentHistoryId())
                .bank(paymentVirtualAccountDto.getBank())
                .accountNumber(paymentVirtualAccountDto.getAccountNumber())
                .dueDate(paymentVirtualAccountDto.getDueDate())
                .expiredYn("N")
                .depositYn("N")
                .build();

        paymentVirtualAccountEntity = paymentVirtualAccountRepository.save(paymentVirtualAccountEntity);

        return paymentVirtualAccountEntity;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyPaymentVirtualAccountByHistoryId (
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) PaymentVirtualAccountDto paymentVirtualAccountDto) {
        return paymentVirtualAccountMapper.updatePaymentVirtualAccountByHistoryId(paymentVirtualAccountDto);
    }

}