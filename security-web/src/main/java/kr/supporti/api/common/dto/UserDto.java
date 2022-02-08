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
public class UserDto {

    private Long id;

    private String name;

    private String username;

    private String password;

    private String phoneNum;

    private String bankCd;

    private String accountNum;
    
    private String paymentGeneral;

    private String status;

    private Long profileId;

    private String signUpWay;

    private String lecturerApprovalYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Transient
    private String oldPassword;

    @Transient
    private String newPassword;
}