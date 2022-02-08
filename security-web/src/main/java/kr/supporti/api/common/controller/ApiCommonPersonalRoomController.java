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

import kr.supporti.api.common.dto.PersonalRoomDto;
import kr.supporti.api.common.dto.PersonalRoomParamDto;
import kr.supporti.api.common.entity.PersonalRoomEntity;
import kr.supporti.api.common.service.PersonalRoomService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/personal-room")
public class ApiCommonPersonalRoomController {

    @Autowired
    private PersonalRoomService personalRoomService;

    @GetMapping(path = "")
    public PageResponse<PersonalRoomEntity> getPersonalRoomList(
            @ModelAttribute PersonalRoomParamDto personalRoomParamDto, @ModelAttribute PageRequest pageRequest) {
        return personalRoomService.getPersonalRoomList(personalRoomParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public PersonalRoomEntity getPersonalRoom(@PathVariable(name = "id") Long id) {
        return personalRoomService.getPersonalRoom(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public PersonalRoomEntity createPersonalRoom(@RequestBody PersonalRoomDto personalRoomDto) {
        PersonalRoomEntity personalRoomEntity = PersonalRoomEntity.builder()
                .createdUser(personalRoomDto.getCreatedUser()).invitedUser(personalRoomDto.getInvitedUser())
                .createdOutYn(personalRoomDto.getCreatedOutYn()).invitedOutYn(personalRoomDto.getInvitedOutYn())
                .createdEnterDate(LocalDateTime.now()).invitedEnterDate(LocalDateTime.now()).build();
        return personalRoomService.createPersonalRoom(personalRoomEntity);
    }

    @PutMapping(path = "{id}")
    public PersonalRoomEntity modifyPersonalRoom(@PathVariable(name = "id") Long id,
            @RequestBody PersonalRoomDto personalRoomDto) {
        PersonalRoomEntity personalRoomEntity = PersonalRoomEntity.builder().id(id)
                .createdUser(personalRoomDto.getCreatedUser()).invitedUser(personalRoomDto.getInvitedUser())
                .createdOutYn(personalRoomDto.getCreatedOutYn()).invitedOutYn(personalRoomDto.getInvitedOutYn())
                .createdEnterDate(personalRoomDto.getCreatedEnterDate())
                .invitedEnterDate(personalRoomDto.getInvitedEnterDate()).build();
        return personalRoomService.modifyPersonalRoom(personalRoomEntity);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public Integer updatePersonalRoom(@RequestBody PersonalRoomParamDto personalRoomParamDto) {
        return personalRoomService.updatePersonalRoom(personalRoomParamDto);
    }

}