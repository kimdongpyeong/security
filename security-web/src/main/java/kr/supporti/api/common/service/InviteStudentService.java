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

import kr.supporti.api.common.dto.InviteStudentDto;
import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.mapper.InviteStudentMapper;
import kr.supporti.api.common.repository.InviteStudentRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class InviteStudentService {

    @Autowired
    private InviteStudentRepository inviteStudentRepository;

    @Autowired
    private InviteStudentMapper inviteStudentMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    public InviteStudentEntity getInviteStudent(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return inviteStudentMapper.selectInviteStudent(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public InviteStudentEntity createInviteStudent(@Valid @NotNull(groups = { CreateValidationGroup.class }) InviteStudentEntity inviteStudentEntity) {
        return inviteStudentRepository.save(inviteStudentEntity);
    }
}