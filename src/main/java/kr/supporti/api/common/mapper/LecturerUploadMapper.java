package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerDataParamDto;
import kr.supporti.api.common.dto.LecturerUploadDto;
import kr.supporti.api.common.dto.LecturerUploadFileDto;
import kr.supporti.api.common.dto.LecturerUploadParamDto;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.LecturerUploadEntity;
import kr.supporti.api.common.entity.LecturerUploadFileEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerUploadMapper {

    public LecturerUploadEntity selectLecturerUpload(@Param(value = "id")  Long id);

    public List<LecturerUploadEntity> selectLecturerUploadList(@Param(value = "lecturerUploadDto") LecturerUploadDto LecturerUploadDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectLecturerUploadListCount(@Param(value = "lecturerUploadDto") LecturerUploadDto lecturerUploadDto);

    public List<LecturerUploadFileEntity> selectLecturerUploadFileList(@Param(value = "lecturerUploadDto") LecturerUploadFileDto lecturerUploadFileDto,
            PageRequest pageRequest);

    public Integer selectLecturerUploadListFileCount(@Param(value = "lecturerUploadDto") LecturerUploadFileDto lecturerUploadFileDto);

    public void modifyLecturerUpload(@Param(value = "lecturerUploadEntity") LecturerUploadParamDto lecturerUploadParamDto);
    
    public Integer modifyStudentUploadYn(@Param(value = "id") Long id, @Param(value = "lecturerUploadParamDto") LecturerUploadParamDto lecturerUploadParamDto);

}