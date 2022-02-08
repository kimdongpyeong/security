package kr.supporti.api.common.repository;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.StudentGradeEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.validation.group.CreateValidationGroup;

@Repository
public interface StudentGradeRepository extends JpaRepository<StudentGradeEntity, Long> {

}