package kr.supporti.api.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.PaymentVirtualAccountDto;

@Repository
@Mapper
public interface PaymentVirtualAccountMapper {

    public Integer updatePaymentVirtualAccount(@Param(value = "id") Long id, @Param(value = "paymentVirtualAccountDto") PaymentVirtualAccountDto paymentVirtualAccountDto);

    public Integer updatePaymentVirtualAccountByHistoryId( @Param(value = "paymentVirtualAccountDto") PaymentVirtualAccountDto paymentVirtualAccountDto);

}