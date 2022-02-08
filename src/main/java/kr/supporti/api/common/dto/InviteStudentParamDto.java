package kr.supporti.api.common.dto;

import javax.persistence.Transient;

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
public class InviteStudentParamDto {

    private Long id;
    
    private Long lecturerId;
    
    private Long studentId;
    
    private Long classroomId;
    
    private String linkYn;
    
    private String userName;
    
    private String userPhone;
    
    private String userEmail;
    
    private String classTitle;

}