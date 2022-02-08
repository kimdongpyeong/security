package kr.supporti.api.app.dto;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Transient;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.supporti.api.common.dto.UserDto;
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
public class AccountDto {

    private Long userId;

    private Long roleId;

    private Long referralId;

    private Long lecturerId;
    
    private Long classroomId;
    
    private String inviteFlag;
    
    private UserDto userDto;

    private MultipartFile profileFile;

    private List<Long> termsList;

    private Long id;

    private String name;

    private String username;

    private String password;

    private String fileFlag;

    private String status;

    private String phoneNum;

    private String signUpWay;

    private Long profileId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @Transient
    private String oldPassword;

    @Transient
    private String newPassword;
}
