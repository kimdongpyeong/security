package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface UserMapper {

    public List<UserEntity> selectUserList(@Param(value = "userParamDto") UserParamDto userParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectUserListCount(@Param(value = "userParamDto") UserParamDto userParamDto);

    public UserEntity selectUser(@Param(value = "id") Long id);

    public UserEntity selectUserByUsername(@Param(value = "username") String username);

    public Integer selectSmsUserListCount(@Param(value = "userParamDto") UserParamDto userParamDto);

    public Integer selectUserExistsCount(@Param(value = "userParamDto") UserParamDto userParamDto);

    public Integer updateUserInfo(@Param(value = "id") Long id, @Param(value = "userDto") UserDto userDto);
    
    public void approvalLecturer(@Param(value = "userEntity") UserEntity userEntity);
    
    public void refuseLecturer(@Param(value = "userEntity") UserEntity userEntity);

}