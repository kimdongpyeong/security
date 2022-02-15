package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.PaymentHistoryDto;
import kr.supporti.api.common.dto.PaymentHistoryParamDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface PaymentHistoryMapper {

    public List<PaymentHistoryEntity> selectPaymentHistoryList(
            @Param(value = "paymentHistoryParamDto") PaymentHistoryParamDto paymentHistoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectPaymentHistoryListCount(@Param(value = "paymentHistoryParamDto") PaymentHistoryParamDto paymentHistoryParamDto);

    public PaymentHistoryEntity selectPaymentHistory(@Param(value = "id") Long id);

    public PaymentHistoryEntity selectPaymentHistoryByOrderId(@Param(value = "orderId") String orderId);

    public List<PaymentHistoryEntity> selectNoRegularPaymentHistory(@Param(value = "paymentHistoryParamDto") PaymentHistoryParamDto paymentHistoryParamDto);

    public Integer updatePaymentHistory(@Param(value = "id") Long id, @Param(value = "paymentHistoryDto") PaymentHistoryDto paymentHistoryDto);

}