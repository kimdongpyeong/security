package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.EnterUserDto;
import kr.supporti.api.common.dto.EnterUserParamDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.EnterUserEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.EnterUserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/enter-users")
public class ApiCommonEnterUserController {

    @Autowired
    private EnterUserService enterUserService;

    @GetMapping(path = "")
    public PageResponse<EnterUserEntity> getEnterUserList(@ModelAttribute EnterUserParamDto enterUserParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return enterUserService.getEnterUserList(enterUserParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public EnterUserEntity getEnterUser(HttpServletRequest request, @PathVariable(name = "id") Long id) {
        return enterUserService.getEnterUser(id, request);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public EnterUserEntity createEnterUser(HttpServletRequest request, @RequestBody EnterUserDto enterUserDto) {
        EnterUserEntity enterUserEntity = EnterUserEntity.builder()
                .classroomId(enterUserDto.getClassroomId())
                .type(enterUserDto.getType())
                .name(enterUserDto.getName())
                .grade(enterUserDto.getGrade())
                .email(enterUserDto.getEmail())
                .phoneNum(enterUserDto.getPhoneNum())
                .build();
        
        return enterUserService.createEnterUser(request, enterUserEntity);
    }

    @PutMapping(path = "{id}")
    public EnterUserEntity modifyEnterUser(@PathVariable(name = "id") Long id, @RequestBody EnterUserDto enterUserDto) {
        EnterUserEntity enterUserEntity = EnterUserEntity.builder()
                                            .id(id)
                                            .classroomId(enterUserDto.getClassroomId())
                                            .type(enterUserDto.getType())
                                            .name(enterUserDto.getName())
                                            .grade(enterUserDto.getGrade())
                                            .email(enterUserDto.getEmail())
                                            .phoneNum(enterUserDto.getPhoneNum())
                                            .build();
        return enterUserService.modifyEnterUser(enterUserEntity);
    }
    
    @GetMapping (path = "session")
    public Map<String, Object> getSession() {
        return enterUserService.getSession();
    }
    
    @PostMapping (path = "/id-exists")
    public Integer guestIDExists(@RequestBody EnterUserParamDto enterUserParamDto) {
        return enterUserService.getGuestExists(enterUserParamDto);
    }
}