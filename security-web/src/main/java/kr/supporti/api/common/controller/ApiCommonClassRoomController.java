package kr.supporti.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.ClassRoomDto;
import kr.supporti.api.common.dto.ClassRoomParamDto;
import kr.supporti.api.common.entity.ClassRoomEntity;
import kr.supporti.api.common.service.ClassRoomService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/classroom")
public class ApiCommonClassRoomController {

    @Autowired
    private ClassRoomService classRoomService;

    @GetMapping(path = "")
    public PageResponse<ClassRoomEntity> getClassRoomList(@ModelAttribute ClassRoomParamDto classRoomParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return classRoomService.getClassRoomList(classRoomParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public ClassRoomEntity getClassRoom(@PathVariable(name = "id") Long id) {
        return classRoomService.getClassRoom(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public ClassRoomEntity createClassRoom(@ModelAttribute ClassRoomDto classRoomDto) {
        return classRoomService.createClassRoom(classRoomDto);
    }

    @PutMapping(path = "")
    public void modifyClassRoom(@ModelAttribute ClassRoomDto classRoomDto) {
        classRoomService.modifyClassRoom(classRoomDto);
    }

    @DeleteMapping(path = "")
    public void removeClassRoom(@RequestBody ClassRoomDto classRoomDto) {
        classRoomService.removeClassRoom(classRoomDto);
    }
}