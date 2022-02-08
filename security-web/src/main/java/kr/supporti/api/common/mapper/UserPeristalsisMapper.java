package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.UserAuthParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserPeristalsisEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface UserPeristalsisMapper {

    public UserPeristalsisEntity selectUser(@Param(value = "userId") String userId);

}