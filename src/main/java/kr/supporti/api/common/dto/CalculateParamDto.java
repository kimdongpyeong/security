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
public class CalculateParamDto {

    private Long id;

    private Long createdBy;
    
    //정산 내역 기간 선택 선택 첫달~선택 마지막달
    private String startMonth;
    
    private String endMonth;
}
