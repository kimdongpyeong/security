package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserLoginHistoryDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserLoginHistoryEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface UserLoginHistoryMapper {

    public List<UserLoginHistoryEntity> selectUserIpList(@Param(value = "userLoginHistoryDto") UserLoginHistoryDto userLoginHistoryDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectUserIpListCount(@Param(value = "userLoginHistoryDto") UserLoginHistoryDto userLoginHistoryDto);

    public UserLoginHistoryEntity selectUserIp(@Param(value = "id") Long id);
    
    public Integer createUserIp(@Param(value = "userLoginHistoryEntity") UserLoginHistoryEntity userLoginHistoryEntity);

}