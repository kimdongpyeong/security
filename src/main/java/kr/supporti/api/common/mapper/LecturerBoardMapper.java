package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.LecturerBoardDto;
import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.dto.UserAuthParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.LecturerBoardEntity;
import kr.supporti.api.common.entity.NoticeEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerBoardMapper {
    public List<LecturerBoardEntity> selectLecturerBoardList(@Param(value = "lecturerBoardDto") LecturerBoardDto lecturerBoardDto,
            @Param(value = "pageRequest") PageRequest pageRequest);
    
    public Integer selectLecturerBoardListCount(@Param(value = "lecturerBoardDto") LecturerBoardDto lecturerBoardDto);
    
    public LecturerBoardEntity selectLecturerBoard(@Param(value = "lecturerBoardDto") LecturerBoardDto lecturerBoardDto);
}