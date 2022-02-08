package kr.supporti.api.common.dto;

import java.time.LocalDateTime;

import org.apache.htrace.shaded.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

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
public class PaymentVirtualAccountDto {

    private Long paymentHistoryId;

    private String accountNumber;

    private String bank;

    private String depositYn;

    private String expiredYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime dueDate;

}
