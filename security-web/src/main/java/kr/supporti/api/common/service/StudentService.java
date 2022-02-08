package kr.supporti.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.StudentDayDto;
import kr.supporti.api.common.dto.StudentDto;
import kr.supporti.api.common.dto.StudentGradeDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.StudentGradeEntity;
import kr.supporti.api.common.mapper.InviteStudentMapper;
import kr.supporti.api.common.mapper.StudentGradeMapper;
import kr.supporti.api.common.mapper.StudentMapper;
import kr.supporti.api.common.mapper.StudentWeekMapper;
import kr.supporti.api.common.repository.InviteStudentRepository;
import kr.supporti.api.common.repository.StudentDayRepository;
import kr.supporti.api.common.repository.StudentGradeRepository;
import kr.supporti.api.common.repository.StudentRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentDayRepository studentDayRepository;
    
    @Autowired
    private InviteStudentRepository inviteStudentRepository;
    
    @Autowired
    private StudentGradeRepository studentGradeRepository;
    
    @Autowired
    private StudentMapper studentMapper;
    
    @Autowired
    private StudentWeekMapper studentWeekMapper;
    
    @Autowired
    private StudentGradeMapper studentGradeMapper;
    
    @Autowired
    private InviteStudentMapper inviteStudentMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    /*
     * @Validated(value = { ReadValidationGroup.class })
     * 
     * @Transactional(readOnly = true) public UserEntity
     * getUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
     * return studentMapper.selectUser(id); }
     */

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public StudentEntity createStudent(@Valid @NotNull(groups = { CreateValidationGroup.class }) StudentEntity studentEntity) {
        return studentRepository.save(studentEntity);
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public StudentGradeEntity createStudentGrade(@Valid @NotNull(groups = { CreateValidationGroup.class }) StudentGradeEntity studentGradeEntity) {
        return studentGradeRepository.save(studentGradeEntity);
    }
    

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<StudentDayEntity> createStudentDay(@Valid @NotNull(groups = { CreateValidationGroup.class }) StudentDayDto studentDayDto) {
        
        List<StudentDayEntity> list = new ArrayList<>();
        
        
         for (int i = 0; i < studentDayDto.getDay().length; i++) { StudentDayEntity
         studentDayEntity = StudentDayEntity.builder() .day(studentDayDto.getDay()[i])
         .lecturerStudentId(studentDayDto.getLecturerStudentId()) .build();
         
         list.add(studentDayEntity); }
         
        
        return studentDayRepository.saveAll(list);
    }
    
    /*
     * @Validated(value = { CreateValidationGroup.class })
     * 
     * @Transactional public StudentEntity insertStudent(@Valid @NotNull(groups = {
     * CreateValidationGroup.class }) StudentEntity studentEntity) { return
     * studentMapper.insertStudent(studentEntity); }
     */
    
    
//    @Validated(value = { CreateValidationGroup.class })
//    @Transactional
//    public List<StudentEntity> insertStudent(@Valid @NotNull(groups = { CreateValidationGroup.class }) StudentDto studentDto) {
//        List<StudentEntity> list = new ArrayList<>();
//        
//        for (int i = 0; i < studentDto.)
//        return studentMapper.insertStudent(studentDto);
//    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<InviteStudentEntity> getInviteStudentList(@Valid InviteStudentParamDto inviteStudentParamDto, PageRequest pageRequest) {
        Integer inviteStudentListCount = inviteStudentMapper.selectInviteStudentListCount(inviteStudentParamDto);
        List<InviteStudentEntity> inviteStudentList = inviteStudentMapper.selectInviteStudentList(inviteStudentParamDto, pageRequest);
        PageResponse<InviteStudentEntity> pageResponse = new PageResponse<>(pageRequest, inviteStudentListCount);
        pageResponse.setItems(inviteStudentList);
        return pageResponse;
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<StudentEntity> getStudentList(@Valid StudentDto studentDto, PageRequest pageRequest) {
        Integer inviteStudentListCount = studentMapper.selectStudentListCount(studentDto);
        List<StudentEntity> inviteStudentList = studentMapper.selectStudentList(studentDto, pageRequest);
        PageResponse<StudentEntity> pageResponse = new PageResponse<>(pageRequest, inviteStudentListCount);
        pageResponse.setItems(inviteStudentList);
        return pageResponse;
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public StudentEntity getStudent(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return studentMapper.selectStudent(id);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public List<StudentDayEntity> getStudentWeek(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return studentWeekMapper.selectStudentWeek(id);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<StudentGradeEntity> getStudentGrade(@Valid StudentGradeDto studentGradeDto, PageRequest pageRequest) {
        Integer studentGradeListCount = studentGradeMapper.selectStudentGradeListCount(studentGradeDto);
        List<StudentGradeEntity> studentGradeList = studentGradeMapper.selectStudentGradeList(studentGradeDto, pageRequest);
        PageResponse<StudentGradeEntity> pageResponse = new PageResponse<>(pageRequest, studentGradeListCount);
        pageResponse.setItems(studentGradeList);
        return pageResponse;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyStudentLink(@Valid @NotNull(groups = { ModifyValidationGroup.class }) Long id, @Valid @NotNull(groups = { ModifyValidationGroup.class }) InviteStudentParamDto inviteStudentParamDto) {
        return inviteStudentMapper.updateStudentInfo(id, inviteStudentParamDto);
    }
    
    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyStudent(@Valid @NotNull(groups = { ModifyValidationGroup.class }) Long id, @Valid @NotNull(groups = { ModifyValidationGroup.class }) StudentDto studentDto) {
        return studentMapper.updateStudentInfo(id, studentDto);
    }
    
    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyStudentWeek(@Valid @NotNull(groups = { ModifyValidationGroup.class }) StudentDayDto studentDayDto) {
        
        studentMapper.updateStudentWeekInfo(studentDayDto);
        studentWeekMapper.updateStudentWeekInfo(studentDayDto);
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public Integer loadStudent(@Valid @NotNull(groups = { CreateValidationGroup.class }) InviteStudentParamDto inviteStudentParamDto, 
            @Valid @NotNull(groups = { CreateValidationGroup.class }) StudentDto studentDto ,PageRequest pageRequest) {

        List<InviteStudentEntity> inviteStudentList = inviteStudentMapper.selectInviteStudentList(inviteStudentParamDto, pageRequest);
        
        int returnVal = 0;
        for(int i = 0; i < inviteStudentList.size(); i++) {
            String link = "N";
            System.out.println("qweqwe" + inviteStudentList.get(i).getLinkYn().getClass().getName());
            System.out.println("zxczxc" + link.getClass().getName());
//            if(inviteStudentList.get(i).getLinkYn() == link) {
                studentDto.setStudentId(inviteStudentList.get(i).getStudentId());
                studentDto.setName(inviteStudentList.get(i).getUserName());
                studentDto.setPhoneNum(inviteStudentList.get(i).getUserPhone());
                studentDto.setEmail(inviteStudentList.get(i).getUserEmail());
                studentDto.setLectureName(inviteStudentList.get(i).getClassTitle());
                studentDto.setCreatedBy(inviteStudentList.get(i).getLecturerId());
                
                Long id = inviteStudentList.get(i).getStudentId();
                inviteStudentParamDto.setLinkYn("Y");
                
                inviteStudentMapper.updateStudentInfo(id, inviteStudentParamDto);
                
                returnVal += 1; 
                studentMapper.insertStudent(studentDto);
//            }
        }
        return returnVal;
    }
}