package kr.supporti.api.common.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.ProfileFileEntity;

@Repository
@Mapper
public interface ProfileFileMapper {

    public ProfileFileEntity selectProfileFile(@Param(value = "Id") Long Id);

}