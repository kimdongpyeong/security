package kr.supporti.api.common.controller;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.StudentConsultingDto;
import kr.supporti.api.common.dto.StudentConsultingParamDto;
import kr.supporti.api.common.entity.StudentConsultingEntity;
import kr.supporti.api.common.service.StudentConsultingService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/student-consulting")
public class ApiCommonStudentConsultingController {

    @Autowired
    private StudentConsultingService studentConsultingService;

    @GetMapping(path = "")
    public PageResponse<StudentConsultingEntity> getConsultingList(
            @ModelAttribute StudentConsultingParamDto studentConsultingParamDto, @ModelAttribute PageRequest pageRequest) {
        return studentConsultingService.getConsultingList(studentConsultingParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public StudentConsultingEntity getConsulting(@PathVariable(name = "id") Long id) {
        return studentConsultingService.getConsulting(id);
    }

    @GetMapping(path = "/student/{id}")
    public StudentConsultingEntity getStudentConsulting(@PathVariable(name = "id") Long id) {
        return studentConsultingService.getStudentConsulting(id);
    }
    
    @PostMapping(path = "", params = { "!bulk" })
    public StudentConsultingEntity createConsulting(@RequestBody StudentConsultingDto studentConsultingDto) {
        StudentConsultingEntity studentConsultingEntity = StudentConsultingEntity.builder()
                .lecturerId(studentConsultingDto.getLecturerId())
                .studentId(studentConsultingDto.getStudentId())
                .consultingDate(studentConsultingDto.getConsultingDate())
                .title(studentConsultingDto.getTitle())
                .contents(studentConsultingDto.getContents())
                .build();
        return studentConsultingService.createConsulting(studentConsultingEntity);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public void modifyConsulting(@RequestBody StudentConsultingDto studentConsultingDto) {
        studentConsultingService.modifyConsulting(studentConsultingDto);
    }

}