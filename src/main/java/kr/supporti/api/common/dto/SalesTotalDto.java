package kr.supporti.api.common.dto;

import java.time.LocalDate;
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
public class SalesTotalDto {
    //매출 날짜
    private String salesDate;
    
    //직접 등록한 매출 총 금액
    private Integer salesTotal;
    
    //결제 총 금액
    private Integer paymentTotal;
    
    //날짜별 직접등록매출+결제매출 총 금액
    private Integer dayTotal;
    
    //월별
    private String month;
    
    //월별 총 매출 금액
    private Integer monthTotal;
    
    //년도
    private String year;
}
