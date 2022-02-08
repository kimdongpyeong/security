package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerUploadFileDto;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.LecturerUploadFileEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerUploadFileMapper {

    public List<LecturerUploadFileEntity> selectLecturerUploadFileList(@Param(value = "lecturerUploadFileDto") LecturerUploadFileDto lecturerUploadFileDto,
            PageRequest pageRequest);

    public Integer selectLecturerUploadFileListCount(@Param(value = "lecturerUploadFileDto") LecturerUploadFileDto lecturerUploadFileDto);

}