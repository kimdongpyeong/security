package kr.supporti.api.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
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
public class StudentDto {

    private Long id;
    
    private Long studentId;

    private String name;

    private String phoneNum;

    private String email;

    private String gender;

    private String educationCd;
    
    private String lectureType;
    
    private String lectureName;
    
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`start_date`", columnDefinition = "DATE", nullable = false)
    private LocalDate startDate;
    
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    @Column (name = "`end_date`", columnDefinition = "DATE", nullable = false)
    private LocalDate endDate;
    
    private Integer lectureNum;
    
    private Long createdBy;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    private String linkYn;
    
    private String studentCode;
    
    private String week;
}