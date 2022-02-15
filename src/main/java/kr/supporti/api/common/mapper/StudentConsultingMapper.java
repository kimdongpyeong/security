package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.StudentConsultingDto;
import kr.supporti.api.common.dto.StudentConsultingParamDto;
import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.entity.StudentConsultingEntity;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.validation.group.CreateValidationGroup;

@Repository
@Mapper
public interface StudentConsultingMapper {

    public List<StudentConsultingEntity> selectConsultingList(
            @Param(value = "studentConsultingParamDto") StudentConsultingParamDto studentConsultingParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectConsultingListCount(@Param(value = "studentConsultingParamDto") StudentConsultingParamDto studentConsultingParamDto);

    public StudentConsultingEntity selectConsulting(@Param(value = "id") Long id);

    public StudentConsultingEntity selectStudentConsulting(@Param(value = "id") Long id);
    
}