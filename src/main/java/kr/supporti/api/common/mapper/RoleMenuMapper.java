package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.RoleMenuParamDto;
import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface RoleMenuMapper {

    public List<RoleMenuEntity> selectRoleMenuList(@Param(value = "roleMenuParamDto") RoleMenuParamDto roleMenuParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectRoleMenuListCount(@Param(value = "roleMenuParamDto") RoleMenuParamDto roleMenuParamDto);

    public RoleMenuEntity selectRoleMenu(@Param(value = "roleId") Long roleId, @Param(value = "menuId") Long menuId);

}