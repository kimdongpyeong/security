package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.dto.UserPeristalsisDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserPeristalsisEntity;
import kr.supporti.api.common.service.UserPeristalsisService;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/usersPeristalsis")
public class ApiCommonUserPeristalsisController {

    @Autowired
    private UserPeristalsisService userPeristalsisService;

    @GetMapping(path = "/{userId}")
    public UserPeristalsisEntity getUser(@PathVariable(name = "userId") String userId) {
        return userPeristalsisService.getUser(userId);
    }
    
    @PostMapping(path = "", params = { "!bulk" })
    public UserPeristalsisEntity createUser(@RequestBody UserPeristalsisDto userPeristalsisDto) {
        UserPeristalsisEntity userPeristalsisEntity = UserPeristalsisEntity.builder()
                .userId(userPeristalsisDto.getUserId())
                .signUpWay(userPeristalsisDto.getSignUpWay())
                .build();
        return userPeristalsisService.createUser(userPeristalsisEntity);
    }

}