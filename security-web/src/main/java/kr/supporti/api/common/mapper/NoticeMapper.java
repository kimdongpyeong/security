package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.dto.UserAuthParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.NoticeEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface NoticeMapper {
    public List<NoticeEntity> selectNoticeList(@Param(value = "noticeDto") NoticeDto noticeDto,
            @Param(value = "pageRequest") PageRequest pageRequest);
    
    public Integer selectNoticeListCount(@Param(value = "noticeDto") NoticeDto noticeDto);
    
    public NoticeEntity selectNotice(@Param(value = "noticeDto") NoticeDto noticeDto);
}