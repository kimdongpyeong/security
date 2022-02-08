package kr.supporti.api.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.UserPaymentInfoEntity;

@Repository
@Mapper
public interface UserPaymentInfoMapper {

    public UserPaymentInfoEntity selectUserPaymentInfoByUserId(@Param(value = "userId") Long userId);

}