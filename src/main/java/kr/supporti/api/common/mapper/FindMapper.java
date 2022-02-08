package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface FindMapper {

    public UserEntity selectUser(@Param(value = "username") String username);

    public UserEntity selectFindUser(@Param(value = "id") Long id);

}