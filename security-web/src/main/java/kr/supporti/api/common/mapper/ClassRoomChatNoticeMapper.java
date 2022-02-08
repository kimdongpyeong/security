package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ClassRoomChatNoticeParamDto;
import kr.supporti.api.common.entity.ClassRoomChatNoticeEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ClassRoomChatNoticeMapper {

    public List<ClassRoomChatNoticeEntity> selectClassRoomChatNoticeList(
            @Param(value = "classRoomChatNoticeParamDto") ClassRoomChatNoticeParamDto classRoomChatNoticeParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectClassRoomChatNoticeListCount(
            @Param(value = "classRoomChatNoticeParamDto") ClassRoomChatNoticeParamDto classRoomChatNoticeParamDto);

}