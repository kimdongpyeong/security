package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ContactUsDto;
import kr.supporti.api.common.dto.ContactUsParamDto;
import kr.supporti.api.common.entity.ContactUsEntity;
import kr.supporti.api.common.mapper.ContactUsMapper;
import kr.supporti.api.common.repository.ContactUsRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    @Autowired
    private ContactUsMapper contactUsMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ContactUsEntity> getContactUsList(@Valid ContactUsParamDto contactUsParamDto,
            PageRequest pageRequest) {
        Integer contactUsListCount = contactUsMapper.selectContactUsListCount(contactUsParamDto);
        List<ContactUsEntity> contactUsList = contactUsMapper.selectContactUsList(contactUsParamDto,
                pageRequest);
        PageResponse<ContactUsEntity> pageResponse = new PageResponse<>(pageRequest, contactUsListCount);
        pageResponse.setItems(contactUsList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ContactUsEntity getContactUs(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return contactUsMapper.selectContactUs(id);
    }

    @Transactional
    public ContactUsEntity createContactUs(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) ContactUsEntity contactUsEntity) {
        return contactUsRepository.save(contactUsEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyContactUs(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) ContactUsParamDto contactUsParamDto) {
        contactUsMapper.completedContactUs(contactUsParamDto);
    }
    
    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void lastUpdated(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) ContactUsParamDto contactUsParamDto) {
        contactUsMapper.lastUpdated(contactUsParamDto);
    }

}