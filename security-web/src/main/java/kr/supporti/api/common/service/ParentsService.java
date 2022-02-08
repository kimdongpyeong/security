package kr.supporti.api.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ParentsDto;
import kr.supporti.api.common.dto.StudentDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.ParentsEntity;
import kr.supporti.api.common.entity.StudentDayEntity;
import kr.supporti.api.common.entity.StudentEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.ParentsMapper;
import kr.supporti.api.common.mapper.StudentMapper;
import kr.supporti.api.common.mapper.UserMapper;
import kr.supporti.api.common.repository.ParentsRepository;
import kr.supporti.api.common.repository.StudentDayRepository;
import kr.supporti.api.common.repository.StudentRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ParentsService {

    @Autowired
    private ParentsRepository parentsRepository;

    @Autowired
    private ParentsMapper parentsMapper;
    
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
    public ParentsEntity createParents(@Valid @NotNull(groups = { CreateValidationGroup.class }) ParentsEntity parentsEntity) {
        return parentsRepository.save(parentsEntity);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ParentsEntity> getParentsList(@Valid ParentsDto parentsDto, PageRequest pageRequest) {
        Integer inviteParentsListCount = parentsMapper.selectParentsListCount(parentsDto);
        List<ParentsEntity> inviteParentsList = parentsMapper.selectParentsList(parentsDto, pageRequest);
        PageResponse<ParentsEntity> pageResponse = new PageResponse<>(pageRequest, inviteParentsListCount);
        pageResponse.setItems(inviteParentsList);
        return pageResponse;
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ParentsEntity getParents(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return parentsMapper.selectParents(id);
    }
}