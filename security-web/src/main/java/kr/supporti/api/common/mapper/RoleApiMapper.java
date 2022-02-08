package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.RoleApiParamDto;
import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface RoleApiMapper {

    public List<RoleApiEntity> selectRoleApiList(@Param(value = "roleApiParamDto") RoleApiParamDto roleApiParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectRoleApiListCount(@Param(value = "roleApiParamDto") RoleApiParamDto roleApiParamDto);

    public RoleApiEntity selectRoleApi(@Param(value = "roleId") Long roleId, @Param(value = "apiId") Long apiId);

}