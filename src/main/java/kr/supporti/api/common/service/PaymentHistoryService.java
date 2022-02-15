package kr.supporti.api.common.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.dto.PaymentHistoryParamDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.PaymentHistoryMapper;
import kr.supporti.api.common.repository.PaymentHistoryRepository;
import kr.supporti.common.Constants;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class PaymentHistoryService {

    @Autowired
    private PaymentHistoryRepository paymentHistoryRepository;

    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<PaymentHistoryEntity> getPaymentHistoryList(@Valid PaymentHistoryParamDto paymentHistoryParamDto,
            PageRequest pageRequest) {
        Integer paymentHistoryListCount = paymentHistoryMapper.selectPaymentHistoryListCount(paymentHistoryParamDto);
        List<PaymentHistoryEntity> paymentHistoryList = paymentHistoryMapper.selectPaymentHistoryList(paymentHistoryParamDto, pageRequest);
        PageResponse<PaymentHistoryEntity> pageResponse = new PageResponse<>(pageRequest, paymentHistoryListCount);
        pageResponse.setItems(paymentHistoryList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PaymentHistoryEntity getPaymentHistory(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return paymentHistoryMapper.selectPaymentHistory(id);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PaymentHistoryEntity getPaymentHistoryByOrderId(@Valid @NotNull(groups = { ReadValidationGroup.class }) String orderId) {
        return paymentHistoryMapper.selectPaymentHistoryByOrderId(orderId);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public PaymentHistoryEntity createPaymentHistory(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) PaymentHistoryDto paymentHistoryDto) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        @SuppressWarnings("unchecked")
        Map<String, Object> token = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
        UserEntity loginEntity = (UserEntity) token.get("user");

        int leftLimit = 48;
        int rightLimit = 122;
        int targetStringLength = 30;
        Random random = new Random();
        String orderId = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)).limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();

        PaymentHistoryEntity paymentHistoryEntity = PaymentHistoryEntity.builder()
                .name(paymentHistoryDto.getName())
                .lectureName(paymentHistoryDto.getLectureName())
                .lectureNum(paymentHistoryDto.getLectureNum())
                .paymentTargetCd(paymentHistoryDto.getPaymentTargetCd())
                .amount(paymentHistoryDto.getAmount())
                .paymentType(paymentHistoryDto.getPaymentType())
                .phoneNum(paymentHistoryDto.getPhoneNum())
                .requestDate(LocalDateTime.now())
                .state(Constants.PAYMENT_STATE_ING)
                .orderId(orderId)
                .requestLecturerId(loginEntity.getId())
                .build();

        return paymentHistoryRepository.save(paymentHistoryEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyPaymentHistory(
            @Valid Long id, @Valid PaymentHistoryDto paymentHistoryDto) {
        return paymentHistoryMapper.updatePaymentHistory(id, paymentHistoryDto);
    }

}