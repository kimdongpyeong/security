package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.RoleUserParamDto;
import kr.supporti.api.common.entity.RoleUserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface RoleUserMapper {

    public List<RoleUserEntity> selectRoleUserList(@Param(value = "roleUserParamDto") RoleUserParamDto roleUserParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectRoleUserListCount(@Param(value = "roleUserParamDto") RoleUserParamDto roleUserParamDto);

    public RoleUserEntity selectRoleUser(@Param(value = "userId") Long userId);

    public void selectRoleTypeCode(@Param(value = "roleId") Long roleId);

}