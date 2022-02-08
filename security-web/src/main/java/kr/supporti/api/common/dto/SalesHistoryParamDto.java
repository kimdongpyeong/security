package kr.supporti.api.common.dto;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SalesHistoryParamDto {

    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate salesStartDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate salesEndDate;
    
    private Integer count;
    
    private Integer totalSales;
    
    private Long createdBy;

    private String paymentMethodCd;
    
    //한달 매출 내역 달 선택(첫날~마지막날)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    //월별 매출보기 달 선택 선택 첫달~선택 마지막달
    private String startMonth;
    
    private String endMonth;
    
    //매출통계 년 선택
    private String searchYear;

    //선택한 달
    private String selectMonth;
    
//  @JsonFormat(pattern = "yyyy-MM")
//  @DateTimeFormat(pattern = "yyyy-MM")
//  private LocalDate startMonth;
//  
//  @JsonFormat(pattern = "yyyy-MM")
//  @DateTimeFormat(pattern = "yyyy-MM")
//  private LocalDate endMonth;
}
