package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.PersonalRoomParamDto;
import kr.supporti.api.common.entity.PersonalRoomEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface PersonalRoomMapper {

    public List<PersonalRoomEntity> selectPersonalRoomList(
            @Param(value = "personalRoomParamDto") PersonalRoomParamDto personalRoomParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectPersonalRoomListCount(
            @Param(value = "personalRoomParamDto") PersonalRoomParamDto personalRoomParamDto);

    public PersonalRoomEntity selectPersonalRoom(@Param(value = "id") Long id);

    public Integer updatePersonalRoom(@Param(value = "personalRoomParamDto") PersonalRoomParamDto personalRoomParamDto);

}