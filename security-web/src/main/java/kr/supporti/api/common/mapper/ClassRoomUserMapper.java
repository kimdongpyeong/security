package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ClassRoomUserParamDto;
import kr.supporti.api.common.entity.ClassRoomUserEntity;
import kr.supporti.api.common.entity.CodeEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ClassRoomUserMapper {

    public List<ClassRoomUserEntity> selectClassRoomUserList(
            @Param(value = "classRoomUserParamDto") ClassRoomUserParamDto classRoomUserParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectClassRoomUserListCount(
            @Param(value = "classRoomUserParamDto") ClassRoomUserParamDto classRoomUserParamDto);

    public ClassRoomUserEntity selectClassRoomUser(
            @Param(value = "classRoomUserParamDto") ClassRoomUserParamDto classRoomUserParamDto);
}