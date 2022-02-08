package kr.supporti.api.common.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RegularPaymentHistoryParamDto {

    private Long id;

    private Long paymentHistoryId;

    private String paymentDateMonth;


}
