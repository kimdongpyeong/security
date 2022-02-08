package kr.supporti.api.common.mapper;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.EnterUserParamDto;
import kr.supporti.api.common.entity.EnterUserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface EnterUserMapper {

    public List<EnterUserEntity> selectEnterUserList(@Param(value = "enterUserParamDto") EnterUserParamDto enterUserParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectEnterUserListCount(@Param(value = "enterUserParamDto") EnterUserParamDto enterUserParamDto);

    public EnterUserEntity selectEnterUser(@Param(value = "id") Long id, HttpServletRequest request);

    public Integer selectGuestExists(
            @Param (value = "enterUserParamDto") EnterUserParamDto enterUserParamDto);
}