package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ClassRoomChatNoticeParamDto;
import kr.supporti.api.common.entity.ClassRoomChatNoticeEntity;
import kr.supporti.api.common.mapper.ClassRoomChatNoticeMapper;
import kr.supporti.api.common.repository.ClassRoomChatNoticeRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ClassRoomChatNoticeService {

    @Autowired
    private ClassRoomChatNoticeRepository classRoomChatNoticeRepository;

    @Autowired
    private ClassRoomChatNoticeMapper classRoomChatNoticeMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ClassRoomChatNoticeEntity> getClassRoomChatNoticeList(
            @Valid ClassRoomChatNoticeParamDto classRoomChatNoticeParamDto, PageRequest pageRequest) {
        Integer classRoomChatNoticeListCount = classRoomChatNoticeMapper
                .selectClassRoomChatNoticeListCount(classRoomChatNoticeParamDto);
        List<ClassRoomChatNoticeEntity> classRoomChatNoticeList = classRoomChatNoticeMapper
                .selectClassRoomChatNoticeList(classRoomChatNoticeParamDto, pageRequest);
        PageResponse<ClassRoomChatNoticeEntity> pageResponse = new PageResponse<>(pageRequest,
                classRoomChatNoticeListCount);
        pageResponse.setItems(classRoomChatNoticeList);
        return pageResponse;
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public ClassRoomChatNoticeEntity createClassRoomChatNotice(@Valid @NotNull(groups = {
            CreateValidationGroup.class }) ClassRoomChatNoticeEntity classRoomChatNoticeEntity) {
        return classRoomChatNoticeRepository.save(classRoomChatNoticeEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public ClassRoomChatNoticeEntity modifyClassRoomChatNotice(@Valid @NotNull(groups = {
            ModifyValidationGroup.class }) ClassRoomChatNoticeEntity classRoomChatNoticeEntity) {
        return classRoomChatNoticeRepository.save(classRoomChatNoticeEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeClassRoomChatNotice(@Valid @NotNull(groups = {
            RemoveValidationGroup.class }) ClassRoomChatNoticeEntity classRoomChatNoticeEntity) {
        classRoomChatNoticeRepository.delete(classRoomChatNoticeEntity);
    }

}