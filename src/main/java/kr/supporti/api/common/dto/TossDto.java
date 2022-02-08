package kr.supporti.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossDto {

    private String orderId;

    private String cardNumber;

    private String cardExpirationYear;

    private String cardExpirationMonth;

    private String cardPassword;

    private String customerBirthday;

    private String customerKey;

    private Long customerId;

}
