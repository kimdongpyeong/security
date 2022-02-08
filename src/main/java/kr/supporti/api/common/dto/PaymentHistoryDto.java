package kr.supporti.api.common.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class PaymentHistoryDto {

    private Long id;

    private Long requestLecturerId;

    private Long cancelUserId;

    private Long refundProcessId;

    private String name;

    private String paymentTargetCd;

    private String tossPaymentKey;

    private Integer amount;

    private String paymentType;

    private String paymentMethodCd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime paymentDate;

    private String paymentGeneral;

    private String phoneNum;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime requestDate;

    private String state;

    private String multiState;

    private String cancelReason;

    private String orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    private String regularPaymentYn;

    private String regularPaymentFailDesc;

    private String paymentDiv;

    private Long paymentUserId;

    private String kakaoTid;

    private String kakaoAid;

    private String kakaoSid;

}
