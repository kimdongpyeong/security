package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.RoleParamDto;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface RoleMapper {

    public List<RoleEntity> selectRoleList(@Param(value = "roleParamDto") RoleParamDto roleParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectRoleListCount(@Param(value = "roleParamDto") RoleParamDto roleParamDto);

    public RoleEntity selectRole(@Param(value = "id") Long id);

}