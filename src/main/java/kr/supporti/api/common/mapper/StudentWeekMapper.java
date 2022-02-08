package kr.supporti.api.common.mapper;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.StudentDayDto;
import kr.supporti.api.common.dto.StudentDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Repository
@Mapper
public interface StudentWeekMapper {
    public List<StudentDayEntity> selectStudentWeek(@Param(value = "id") Long id);
    
    public void updateStudentWeekInfo(@Param(value = "studentDayDto") StudentDayDto studentDayDto);
}