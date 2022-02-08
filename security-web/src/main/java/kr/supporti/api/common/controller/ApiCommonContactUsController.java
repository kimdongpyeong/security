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

import kr.supporti.api.common.dto.ContactUsDto;
import kr.supporti.api.common.dto.ContactUsParamDto;
import kr.supporti.api.common.entity.ContactUsEntity;
import kr.supporti.api.common.service.ContactUsService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/contact-us")
public class ApiCommonContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @GetMapping(path = "")
    public PageResponse<ContactUsEntity> getContactUsList(
            @ModelAttribute ContactUsParamDto contactUsParamDto, @ModelAttribute PageRequest pageRequest) {
        return contactUsService.getContactUsList(contactUsParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public ContactUsEntity getContactUs(@PathVariable(name = "id") Long id) {
        return contactUsService.getContactUs(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public ContactUsEntity createContactUs(@RequestBody ContactUsDto contactUsDto) {
        ContactUsEntity contactUsEntity = ContactUsEntity.builder()
                .lecturerId(contactUsDto.getLecturerId())
                .userId(contactUsDto.getUserId())
                .completedYn(contactUsDto.getCompletedYn())
                .build();
        return contactUsService.createContactUs(contactUsEntity);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public void modifyContactUs(@RequestBody ContactUsParamDto contactUsParamDto) {
        contactUsService.modifyContactUs(contactUsParamDto);
    }
    
    @PutMapping(path = "/last", params = { "!bulk" })
    public void lastUpdated(@RequestBody ContactUsParamDto contactUsParamDto) {
        contactUsService.lastUpdated(contactUsParamDto);
    }

}