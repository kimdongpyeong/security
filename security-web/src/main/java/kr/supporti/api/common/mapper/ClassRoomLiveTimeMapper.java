package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ClassRoomLiveParamDto;
import kr.supporti.api.common.dto.ClassRoomLiveTimeDto;
import kr.supporti.api.common.entity.ClassRoomLiveTimeEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ClassRoomLiveTimeMapper {

    public List<ClassRoomLiveTimeEntity> selectClassRoomLiveTimeList(@Param(value = "classRoomLiveTimeDto") ClassRoomLiveTimeDto liveTimeDto,
            @Param(value = "pageRequest")  PageRequest pageRequest);

    public Integer selectClassRoomLiveTimeListCount(@Param(value = "classRoomLiveTimeDto") ClassRoomLiveTimeDto liveTimeDto);

    public ClassRoomLiveTimeEntity selectClassRoomLiveTime(@Param(value = "id") Long id);

    public ClassRoomLiveTimeEntity selectClassRoomIdLiveTime(@Param(value = "id") Long id);
    
//    public void modifyClassRoomLiveTime(@Param(value = "classRoomLiveTimeDto") ClassRoomLiveTimeDto classRoomLiveTimeDto);

    public void modifyClassRoomLiveTime(@Param(value = "classRoomLiveTimeEntity") ClassRoomLiveTimeEntity classRoomLiveTimeEntity);

}