package kr.supporti.api.common.dto;

import java.util.Date;

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
public class CalculateDto {
    //매출 달
    private String month;

    //매출 달
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;
    
    //현금 총금액
    private Integer cashTotal;

    //카드 총금액
    private Integer cardTotal;
    
    //현금 + 카드  = 총 매출액(세금떼기전)
    private Integer total;
    
}
