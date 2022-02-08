package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.PersonalRoomDto;
import kr.supporti.api.common.dto.PersonalRoomParamDto;
import kr.supporti.api.common.entity.PersonalRoomEntity;
import kr.supporti.api.common.mapper.PersonalRoomMapper;
import kr.supporti.api.common.repository.PersonalRoomRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class PersonalRoomService {

    @Autowired
    private PersonalRoomRepository personalRoomRepository;

    @Autowired
    private PersonalRoomMapper personalRoomMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<PersonalRoomEntity> getPersonalRoomList(@Valid PersonalRoomParamDto personalRoomParamDto,
            PageRequest pageRequest) {
        Integer personalRoomListCount = personalRoomMapper.selectPersonalRoomListCount(personalRoomParamDto);
        List<PersonalRoomEntity> personalRoomList = personalRoomMapper.selectPersonalRoomList(personalRoomParamDto,
                pageRequest);
        PageResponse<PersonalRoomEntity> pageResponse = new PageResponse<>(pageRequest, personalRoomListCount);
        pageResponse.setItems(personalRoomList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PersonalRoomEntity getPersonalRoom(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return personalRoomMapper.selectPersonalRoom(id);
    }

    @Transactional
    public PersonalRoomEntity createPersonalRoom(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) PersonalRoomEntity personalRoomEntity) {
        return personalRoomRepository.save(personalRoomEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public PersonalRoomEntity modifyPersonalRoom(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) PersonalRoomEntity personalRoomEntity) {
        return personalRoomRepository.save(personalRoomEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer updatePersonalRoom(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) PersonalRoomParamDto personalRoomParamDto) {
        return personalRoomMapper.updatePersonalRoom(personalRoomParamDto);
    }

}