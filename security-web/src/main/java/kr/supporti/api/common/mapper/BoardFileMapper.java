package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.BoardFileDto;
import kr.supporti.api.common.dto.BoardFileParamDto;
import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.entity.BoardFileEntity;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.validation.group.CreateValidationGroup;

@Repository
@Mapper
public interface BoardFileMapper {

    public List<BoardFileEntity> selectBoardFileList(
            @Param(value = "boardFileParamDto") BoardFileParamDto boardFileParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectBoardFileListCount(@Param(value = "boardFileParamDto") BoardFileParamDto boardFileParamDto);

    public BoardFileEntity selectBoardFile(@Param(value = "id") Long id);

    public BoardFileEntity selectFileOriginNm(@Param(value = "filename") String filename);
    
    void updateBoardFile(@Param(value = "noticeDto") NoticeDto noticeDto);
    
    void deleteBoardFile(@Param(value = "noticeDto") NoticeDto noticeDto);

}