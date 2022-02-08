package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

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

import kr.supporti.api.common.dto.ParentsDto;
import kr.supporti.api.common.dto.StudentDayDto;
import kr.supporti.api.common.dto.StudentDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.ParentsEntity;
import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.ParentsService;
import kr.supporti.api.common.service.StudentService;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/parents")
public class ApiCommonParentsController {

    @Autowired
    private ParentsService parentsService;
    
    @PostMapping(path = "", params = { "!bulk" })
    public ParentsEntity createParents(@RequestBody ParentsDto parentsDto) {
        ParentsEntity parentsEntity = ParentsEntity.builder()
                .name(parentsDto.getName())
                .phoneNum(parentsDto.getPhoneNum())
                .studentName(parentsDto.getStudentName())
                .lectureType(parentsDto.getLectureType())
                .lectureName(parentsDto.getLectureName())
                .createdBy(parentsDto.getCreatedBy())
                .build();
        return parentsService.createParents(parentsEntity);
    }
    
    @GetMapping(path = "/List")
    public PageResponse<ParentsEntity> getParentsList(@ModelAttribute ParentsDto parentsDto,
            @ModelAttribute PageRequest pageRequest) {
        return parentsService.getParentsList(parentsDto, pageRequest);
    }
    
    @GetMapping(path = "{id}")
    public ParentsEntity getStudent(@PathVariable(name = "id") Long id) {
        return parentsService.getParents(id);
    }
}