package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.StudentDayDto;
import kr.supporti.api.common.dto.StudentDto;
import kr.supporti.api.common.dto.StudentGradeDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.LecturerUploadEntity;
import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.StudentGradeEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.StudentService;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;

@RestController
@RequestMapping(path = "api/common/student")
public class ApiCommonStudentController {

    @Autowired
    private StudentService studentService;
    
    @PostMapping(path = "", params = { "!bulk" })
    public StudentEntity createStudent(@RequestBody StudentDto studentDto) {
        StudentEntity studentEntity = StudentEntity.builder()
                .name(studentDto.getName())
                .phoneNum(studentDto.getPhoneNum())
                .email(studentDto.getEmail())
                .gender(studentDto.getGender())
                .educationCd(studentDto.getEducationCd())
                .lectureType(studentDto.getLectureType())
                .lectureName(studentDto.getLectureName())
                .startDate(studentDto.getStartDate())
                .endDate(studentDto.getEndDate())
                .totalLectureNum(studentDto.getTotalLectureNum())
                .createdBy(studentDto.getCreatedBy())
                .build();
        return studentService.createStudent(studentEntity);
    }
    
    @PostMapping(path = "/detail")
    public StudentGradeEntity createStudentGrade(@RequestBody StudentGradeDto studentGradeDto) {
        StudentGradeEntity studentGradeEntity = StudentGradeEntity.builder()
                .lecturerStudentId(studentGradeDto.getLecturerStudentId())
                .classificationCd(studentGradeDto.getClassificationCd())
                .subjectCd(studentGradeDto.getSubjectCd())
                .gradeInputDate(studentGradeDto.getGradeInputDate())
                .score(studentGradeDto.getScore())
                .specificity(studentGradeDto.getSpecificity())
                .build();
        return studentService.createStudentGrade(studentGradeEntity);
    }
    
    @PostMapping(path = "/")
    public List<StudentDayEntity> createStudentDay(@RequestBody StudentDayDto studentDayDto) {
        return studentService.createStudentDay(studentDayDto);
    }
    
    /*
     * @PostMapping(path = "/inst") public StudentEntity insertStudent(@RequestBody
     * StudentDto studentDto) { StudentEntity studentEntity =
     * StudentEntity.builder() .name(studentDto.getName())
     * .studentId(studentDto.getStudentId()) .phoneNum(studentDto.getPhoneNum())
     * .email(studentDto.getEmail()) .lectureName(studentDto.getLectureName())
     * .createdBy(studentDto.getCreatedBy()) .build(); System.out.println("qweqwe" +
     * studentEntity); return studentService.insertStudent(studentEntity); }
     */
    
    @GetMapping(path = "")
    public PageResponse<InviteStudentEntity> getLecturer(@ModelAttribute InviteStudentParamDto inviteStudentParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return studentService.getInviteStudentList(inviteStudentParamDto, pageRequest);
    }
    
    @GetMapping(path = "/List")
    public PageResponse<StudentEntity> getStudentList(@ModelAttribute StudentDto studentDto,
            @ModelAttribute PageRequest pageRequest) {
        return studentService.getStudentList(studentDto, pageRequest);
    }
    
    @GetMapping(path = "/detail/grade")
    public PageResponse<StudentGradeEntity> getStudentGrade(@ModelAttribute StudentGradeDto studentGradeDto,
            @ModelAttribute PageRequest pageRequest) {
        return studentService.getStudentGrade(studentGradeDto, pageRequest);
    }
    
    @GetMapping(path = "{id}")
    public StudentEntity getStudent(@PathVariable(name = "id") Long id) {
        return studentService.getStudent(id);
    }
    
    @GetMapping(path = "/detail/{id}")
    public List<StudentDayEntity> getStudentWeek(@PathVariable(name = "id") Long id) {
        return studentService.getStudentWeek(id);
    }
    
    @PutMapping(path = "{id}")
    public Integer modifyLinkYn(@PathVariable(name = "id") Long id, @RequestBody InviteStudentParamDto inviteStudentParamDto) {
        return studentService.modifyStudentLink(id, inviteStudentParamDto);
    }
    
    @PostMapping(path = "/loadStudent")
    public Integer loadStudent(@RequestBody InviteStudentParamDto inviteStudentParamDto,
            @Valid @NotNull(groups = { CreateValidationGroup.class }) StudentDto studentDto ,@ModelAttribute PageRequest pageRequest) {
        return studentService.loadStudent(inviteStudentParamDto, studentDto, pageRequest);
    }
    
    @PutMapping(path = "/detail/modify/{id}")
    public Integer modifyStudent(@PathVariable(name = "id") Long id, @RequestBody StudentDto studentDto) {
        return studentService.modifyStudent(id, studentDto);
    }
    
    @PutMapping(path = "/detail/modify-schedule")
    public void modifyStudentSchedule(@RequestBody StudentDto studentDto) {
        studentService.modifyStudentSchedule(studentDto);
    }
    
    @PutMapping(path = "/detail/modify-week")
    public void modifyStudentWeek(@RequestBody StudentDayDto studentDayDto) {
        studentService.modifyStudentWeek(studentDayDto);
    }
}