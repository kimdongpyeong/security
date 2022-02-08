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

import kr.supporti.api.common.dto.ClassRoomChatNoticeDto;
import kr.supporti.api.common.dto.ClassRoomChatNoticeParamDto;
import kr.supporti.api.common.entity.ClassRoomChatNoticeEntity;
import kr.supporti.api.common.service.ClassRoomChatNoticeService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/classroom-chat-notice")
public class ApiCommonClassRoomChatNoticeController {

    @Autowired
    private ClassRoomChatNoticeService classRoomChatNoticeService;

    @GetMapping(path = "")
    public PageResponse<ClassRoomChatNoticeEntity> getClassRoomChatNoticeList(
            @ModelAttribute ClassRoomChatNoticeParamDto classRoomChatNoticeParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return classRoomChatNoticeService.getClassRoomChatNoticeList(classRoomChatNoticeParamDto, pageRequest);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public ClassRoomChatNoticeEntity createClassRoomChatNotice(
            @RequestBody ClassRoomChatNoticeDto classRoomChatNoticeDto) {
        ClassRoomChatNoticeEntity classRoomChatNoticeEntity = ClassRoomChatNoticeEntity.builder()
                .classroomId(classRoomChatNoticeDto.getClassroomId()).messageId(classRoomChatNoticeDto.getMessageId())
                .build();
        return classRoomChatNoticeService.createClassRoomChatNotice(classRoomChatNoticeEntity);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public ClassRoomChatNoticeEntity modifyClassRoomChatNotice(
            @RequestBody ClassRoomChatNoticeDto classRoomChatNoticeDto) {
        ClassRoomChatNoticeEntity classRoomChatNoticeEntity = ClassRoomChatNoticeEntity.builder()
                .classroomId(classRoomChatNoticeDto.getClassroomId()).messageId(classRoomChatNoticeDto.getMessageId())
                .build();
        return classRoomChatNoticeService.modifyClassRoomChatNotice(classRoomChatNoticeEntity);
    }

    @DeleteMapping(path = "")
    public void removeClassRoomChatNotice(@RequestBody ClassRoomChatNoticeDto classRoomChatNoticeDto) {
        ClassRoomChatNoticeEntity classRoomChatNoticeEntity = ClassRoomChatNoticeEntity.builder()
                .classroomId(classRoomChatNoticeDto.getClassroomId()).messageId(classRoomChatNoticeDto.getMessageId())
                .build();
        classRoomChatNoticeService.removeClassRoomChatNotice(classRoomChatNoticeEntity);
    }

}