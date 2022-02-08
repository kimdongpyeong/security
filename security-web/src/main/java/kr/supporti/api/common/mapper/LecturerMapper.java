package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerMapper {

//	UserEntity selectLecturerUser();

    List<UserEntity> selectLecturerList(@Param(value = "userParamDto") UserParamDto userParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    Integer selectLecturerListCount(@Param(value = "userParamDto") UserParamDto userParamDto);

}
