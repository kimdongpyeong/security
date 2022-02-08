package kr.supporti.api.common.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

@Getter@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDayDto {

    private Long id;

    private Long lecturerStudentId;

    private String[] day;
    
    private String dayNum;
    
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate startDateSearch;
    
    @JsonFormat (pattern = "yyyy-MM-dd")
    @DateTimeFormat (pattern = "yyyy-MM-dd")
    private LocalDate endDateSearch;
}