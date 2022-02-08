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
public class TermsDto {

    private Long id;

    private String title;

    private String contents;

    private String publishYn;

    private String requiredYn;

    private String delYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

//    @JsonFormat (pattern = "yyyy-MM-dd")
//    @DateTimeFormat (pattern = "yyyy-MM-dd")
//    private LocalDateTime lastModifiedPasswordDate;
}