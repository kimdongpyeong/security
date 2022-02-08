package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerDataParamDto;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerDataMapper {

    public LecturerDataEntity selectLecturerData(@Param(value = "id")  Long id);

    public List<LecturerDataEntity> selectLecturerDataList(@Param(value = "lecturerDataDto") LecturerDataDto LecturerDataDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectLecturerDataListCount(@Param(value = "lecturerDataDto") LecturerDataDto lecturerDataDto);

    public List<LecturerDataFileEntity> selectLecturerDataFileList(@Param(value = "lecturerDataDto") LecturerDataFileDto lecturerDataFileDto,
            PageRequest pageRequest);

    public Integer selectLecturerDataListFileCount(@Param(value = "lecturerDataDto") LecturerDataFileDto lecturerDataFileDto);

    public void modifyLecturerData(@Param(value = "lecturerDataEntity") LecturerDataParamDto lecturerDataParamDto);

}