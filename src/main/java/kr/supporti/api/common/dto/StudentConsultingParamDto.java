package kr.supporti.api.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class StudentConsultingParamDto {

    private Long id;
    
    private Long lecturerId;
    
    private Long studentId;
    
    private String studentNm;
    
    private String startDate;
    
    private String endDate;

}
