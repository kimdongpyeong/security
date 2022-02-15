package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.StudentConsultingDto;
import kr.supporti.api.common.dto.StudentConsultingParamDto;
import kr.supporti.api.common.entity.StudentConsultingEntity;
import kr.supporti.api.common.mapper.StudentConsultingMapper;
import kr.supporti.api.common.repository.StudentConsultingRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class StudentConsultingService {

    @Autowired
    private StudentConsultingRepository studentConsultingRepository;

    @Autowired
    private StudentConsultingMapper studentConsultingMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<StudentConsultingEntity> getConsultingList(@Valid StudentConsultingParamDto studentConsultingParamDto,
            PageRequest pageRequest) {
        Integer consultingListCount = studentConsultingMapper.selectConsultingListCount(studentConsultingParamDto);
        List<StudentConsultingEntity> consultingList = studentConsultingMapper.selectConsultingList(studentConsultingParamDto,
                pageRequest);
        PageResponse<StudentConsultingEntity> pageResponse = new PageResponse<>(pageRequest, consultingListCount);
        pageResponse.setItems(consultingList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public StudentConsultingEntity getConsulting(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return studentConsultingMapper.selectConsulting(id);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public StudentConsultingEntity getStudentConsulting(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return studentConsultingMapper.selectStudentConsulting(id);
    }

    @Transactional
    public StudentConsultingEntity createConsulting(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) StudentConsultingEntity studentConsultingEntity) {
        return studentConsultingRepository.save(studentConsultingEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyConsulting(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) StudentConsultingDto studentConsultingDto) {
        StudentConsultingEntity studentConsultingEntity = StudentConsultingEntity.builder()
                .id(studentConsultingDto.getId())
                .lecturerId(studentConsultingDto.getLecturerId())
                .studentId(studentConsultingDto.getStudentId())
                .consultingDate(studentConsultingDto.getConsultingDate())
                .title(studentConsultingDto.getTitle())
                .contents(studentConsultingDto.getContents())
                .build();
        studentConsultingRepository.save(studentConsultingEntity);
    }

}