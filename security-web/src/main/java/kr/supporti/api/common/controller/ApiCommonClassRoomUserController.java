package kr.supporti.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.ClassRoomUserDto;
import kr.supporti.api.common.dto.ClassRoomUserParamDto;
import kr.supporti.api.common.dto.EnterUserParamDto;
import kr.supporti.api.common.entity.ClassRoomUserEntity;
import kr.supporti.api.common.entity.CodeEntity;
import kr.supporti.api.common.entity.EnterUserEntity;
import kr.supporti.api.common.service.ClassRoomUserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/classroom-user")
public class ApiCommonClassRoomUserController {

    @Autowired
    private ClassRoomUserService classRoomUserService;

    @GetMapping(path = "", params = { "!bulk" })
    public PageResponse<ClassRoomUserEntity> getClassRoomUserList(
            @ModelAttribute ClassRoomUserParamDto classRoomUserParamDto, @ModelAttribute PageRequest pageRequest) {
        return classRoomUserService.getClassRoomUserList(classRoomUserParamDto, pageRequest);
    }
    
    @GetMapping(path = "")
    public ClassRoomUserEntity getClassRoomUser(@ModelAttribute ClassRoomUserParamDto classRoomUserParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return classRoomUserService.getClassRoomUser(classRoomUserParamDto, pageRequest);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public ClassRoomUserEntity createClassRoomUser(@RequestBody ClassRoomUserDto classRoomUserDto) {
        ClassRoomUserEntity classRoomUserEntity = ClassRoomUserEntity.builder()
                .classroomId(classRoomUserDto.getClassroomId()).userId(classRoomUserDto.getUserId()).build();
        return classRoomUserService.createClassRoomUser(classRoomUserEntity);
    }

    @DeleteMapping(path = "")
    public void removeClassRoomUser(@RequestBody ClassRoomUserDto classRoomUserDto) {
        ClassRoomUserEntity classRoomUserEntity = ClassRoomUserEntity.builder()
                .classroomId(classRoomUserDto.getClassroomId()).userId(classRoomUserDto.getUserId()).build();
        classRoomUserService.removeClassRoomUser(classRoomUserEntity);
    }

}