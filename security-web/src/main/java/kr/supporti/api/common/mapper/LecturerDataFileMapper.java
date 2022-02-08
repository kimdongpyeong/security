package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface LecturerDataFileMapper {

    public List<LecturerDataFileEntity> selectLecturerDataFileList(@Param(value = "lecturerDataFileDto") LecturerDataFileDto lecturerDataFileDto,
            PageRequest pageRequest);

    public Integer selectLecturerDataFileListCount(@Param(value = "lecturerDataFileDto") LecturerDataFileDto lecturerDataFileDto);

}