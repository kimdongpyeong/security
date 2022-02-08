package kr.supporti.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.PersonalChatNoticeDto;
import kr.supporti.api.common.dto.PersonalChatNoticeParamDto;
import kr.supporti.api.common.entity.PersonalChatNoticeEntity;
import kr.supporti.api.common.service.PersonalChatNoticeService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/personal-chat-notice")
public class ApiCommonPersonalChatNoticeController {

    @Autowired
    private PersonalChatNoticeService personalChatNoticeService;

    @GetMapping(path = "")
    public PageResponse<PersonalChatNoticeEntity> getChatNoticeList(
            @ModelAttribute PersonalChatNoticeParamDto personalChatNoticeParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return personalChatNoticeService.getChatNoticeList(personalChatNoticeParamDto, pageRequest);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public PersonalChatNoticeEntity createChatNotice(@RequestBody PersonalChatNoticeDto personalChatNoticeDto) {
        PersonalChatNoticeEntity personalChatNoticeEntity = PersonalChatNoticeEntity.builder()
                .roomId(personalChatNoticeDto.getRoomId()).messageId(personalChatNoticeDto.getMessageId()).build();
        return personalChatNoticeService.createChatNotice(personalChatNoticeEntity);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public PersonalChatNoticeEntity modifyChatNotice(@RequestBody PersonalChatNoticeDto personalChatNoticeDto) {
        PersonalChatNoticeEntity personalChatNoticeEntity = PersonalChatNoticeEntity.builder()
                .roomId(personalChatNoticeDto.getRoomId()).messageId(personalChatNoticeDto.getMessageId()).build();
        return personalChatNoticeService.modifyChatNotice(personalChatNoticeEntity);
    }

    @DeleteMapping(path = "")
    public void removeChatNotice(@RequestBody PersonalChatNoticeDto personalChatNoticeDto) {
        PersonalChatNoticeEntity personalChatNoticeEntity = PersonalChatNoticeEntity.builder()
                .roomId(personalChatNoticeDto.getRoomId()).messageId(personalChatNoticeDto.getMessageId()).build();
        personalChatNoticeService.removeChatNotice(personalChatNoticeEntity);
    }

}