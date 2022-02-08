package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.PersonalChatNoticeParamDto;
import kr.supporti.api.common.entity.PersonalChatNoticeEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface PersonalChatNoticeMapper {

    public List<PersonalChatNoticeEntity> selectChatNoticeList(
            @Param(value = "personalChatNoticeParamDto") PersonalChatNoticeParamDto personalChatNoticeParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectChatNoticeListCount(
            @Param(value = "personalChatNoticeParamDto") PersonalChatNoticeParamDto personalChatNoticeParamDto);

}