package kr.supporti.api.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoleMenuParamDto {

    private String roleId;

    private String menuId;
}