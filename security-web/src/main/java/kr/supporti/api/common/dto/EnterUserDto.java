package kr.supporti.api.common.dto;

import java.time.LocalDateTime;

import javax.persistence.Transient;

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
public class EnterUserDto {

    private Long id;
    
    private Long classroomId;

    private String type;
    
    private String name;

    private String grade;
    
    private String email;
    
    private String phoneNum;
    
    private String guestCode;
    
    private String compare;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
}