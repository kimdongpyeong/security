package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PersonalChatNoticeParamDto;
import kr.supporti.api.common.entity.PersonalChatNoticeEntity;
import kr.supporti.api.common.mapper.PersonalChatNoticeMapper;
import kr.supporti.api.common.repository.PersonalChatNoticeRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class PersonalChatNoticeService {

    @Autowired
    private PersonalChatNoticeRepository personalChatNoticeRepository;

    @Autowired
    private PersonalChatNoticeMapper personalChatNoticeMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<PersonalChatNoticeEntity> getChatNoticeList(
            @Valid PersonalChatNoticeParamDto personalChatNoticeParamDto, PageRequest pageRequest) {
        Integer personalChatNoticeListCount = personalChatNoticeMapper
                .selectChatNoticeListCount(personalChatNoticeParamDto);
        List<PersonalChatNoticeEntity> personalChatNoticeList = personalChatNoticeMapper
                .selectChatNoticeList(personalChatNoticeParamDto, pageRequest);
        PageResponse<PersonalChatNoticeEntity> pageResponse = new PageResponse<>(pageRequest,
                personalChatNoticeListCount);
        pageResponse.setItems(personalChatNoticeList);
        return pageResponse;
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public PersonalChatNoticeEntity createChatNotice(@Valid @NotNull(groups = {
            CreateValidationGroup.class }) PersonalChatNoticeEntity personalChatNoticeEntity) {
        return personalChatNoticeRepository.save(personalChatNoticeEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public PersonalChatNoticeEntity modifyChatNotice(@Valid @NotNull(groups = {
            ModifyValidationGroup.class }) PersonalChatNoticeEntity personalChatNoticeEntity) {
        return personalChatNoticeRepository.save(personalChatNoticeEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeChatNotice(@Valid @NotNull(groups = {
            RemoveValidationGroup.class }) PersonalChatNoticeEntity personalChatNoticeEntity) {
        personalChatNoticeRepository.delete(personalChatNoticeEntity);
    }

}