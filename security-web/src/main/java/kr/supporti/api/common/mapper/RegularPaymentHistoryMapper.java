package kr.supporti.api.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.RegularPaymentHistoryParamDto;

@Repository
@Mapper
public interface RegularPaymentHistoryMapper {

    public Integer selectRegularPaymentHistoryListCount(
            @Param(value = "regularPaymentHistoryParamDto") RegularPaymentHistoryParamDto paramDto);

    public void insertRegularPaymentByKakao(
            @Param(value = "paymentHistoryId") Long paymentHistoryId,
            @Param(value = "kakaoTid") String kakaoTid,
            @Param(value = "kakaoSid") String kakaoSid);

    public void insertRegularPaymentByToss(
            @Param(value = "paymentHistoryId") Long paymentHistoryId, @Param(value = "paymentKey") String paymentKey);

    public Integer selectMaxNumberByPaymentHistoryId(
            @Param(value = "paymentHistoryId") Long paymentHistoryId);

}