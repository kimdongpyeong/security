package kr.supporti.api.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.common.entity.UserEntity;

@Repository
@Mapper
public interface MypageMapper {

    UserEntity selectMypageUser(@Param(value = "id") Long id);

    void modifyUserInfo(@Param(value = "accountDto") AccountDto accountDto);

    void modifyUserIntroduce(@Param(value = "accountDto") AccountDto accountDto);

    void modifyProfile(@Param(value = "accountDto") AccountDto accountDto);

    void removeUser(@Param(value = "accountDto") AccountDto accountDto);

}
