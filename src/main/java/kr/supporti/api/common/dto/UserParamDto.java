package kr.supporti.api.common.dto;

import java.util.List;

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
public class UserParamDto {

    private Long id;

    private Long userId;

    private Long roleId;

    private String username;

    private String name;

    private String status;

    private String password;

    private String phoneNum;

    private String signUpWay;

    private String lecturerApprovalYn;

    private String nmdid;

    List<UserParamDto> userList;
}