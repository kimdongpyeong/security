package kr.supporti.api.common.controller;

import java.io.IOException;
import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.ClassRoomLiveTimeDto;
import kr.supporti.api.common.entity.ClassRoomLiveTimeEntity;
import kr.supporti.api.common.service.ClassRoomLiveTimeService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/liveTime")
public class ApiCommonClassRoomLiveTimeController {

    @Autowired
    private ClassRoomLiveTimeService liveTimeService;


    @GetMapping(path = "")
    public PageResponse<ClassRoomLiveTimeEntity> getClassRoomLiveTimeList(@ModelAttribute ClassRoomLiveTimeDto liveTimeDto, PageRequest pageRequest) {
        return liveTimeService.getClassRoomLiveTimeList(liveTimeDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public ClassRoomLiveTimeEntity getClassRoomLiveTime(@PathVariable(name = "id") Long id) {
        return liveTimeService.getClassRoomLiveTime(id);
    }
    
    @GetMapping(path = "/classroom/{id}")
    public ClassRoomLiveTimeEntity getClassRoomIdLiveTime(@PathVariable(name = "id") Long id) {
        return liveTimeService.getClassRoomIdLiveTime(id);
    }

    @GetMapping(path = "createZoomLink")
    public void createZoomLink(@RequestParam(name = "liveTimeId") Long id, @RequestParam(name = "code") String code, HttpServletRequest request, HttpServletResponse response) throws IOException {
        liveTimeService.createZoomLink(id, code, response);
    }

    @SuppressWarnings("unchecked")
    @PostMapping(path = "")
    public ClassRoomLiveTimeEntity createLiveTime(@ModelAttribute ClassRoomLiveTimeDto classRoomLiveTimeDto) throws MalformedURLException {
        return liveTimeService.createLiveTime(classRoomLiveTimeDto);
    }

    @PutMapping(path = "")
    public void modifyLiveTime(@ModelAttribute ClassRoomLiveTimeDto classRoomLiveTimeDto) {
        liveTimeService.modifyLiveTime(classRoomLiveTimeDto);
    }
    
    @DeleteMapping(path = "{id}")
    public void removeLiveTime(@PathVariable(name = "id") Long id) {
        liveTimeService.removeLiveTime(id);
    }
}