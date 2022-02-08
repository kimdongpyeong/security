package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ClassRoomDto;
import kr.supporti.api.common.dto.ClassRoomParamDto;
import kr.supporti.api.common.entity.ClassRoomEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ClassRoomMapper {

    public List<ClassRoomEntity> selectClassRoomList(
            @Param(value = "classRoomParamDto") ClassRoomParamDto classRoomParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectClassRoomListCount(@Param(value = "classRoomParamDto") ClassRoomParamDto classRoomParamDto);

    public ClassRoomEntity selectClassRoom(@Param(value = "id") Long id);

    public void modifyClassRoom(@Param(value = "classRoomDto") ClassRoomDto classRoomDto);

    public void removeClassRoom(@Param(value = "classRoomDto") ClassRoomDto classRoomDto);

}