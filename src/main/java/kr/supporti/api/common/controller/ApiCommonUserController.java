package kr.supporti.api.common.controller;

import java.net.InetAddress;
import java.time.LocalDateTime;
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
import kr.supporti.api.common.dto.UserLoginHistoryDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserLoginHistoryEntity;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/users")
public class ApiCommonUserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "")
    public PageResponse<UserEntity> getUserList(@ModelAttribute UserParamDto userParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return userService.getUserList(userParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public UserEntity getUser(@PathVariable(name = "id") Long id) {
        return userService.getUser(id);
    }

    @GetMapping(path = "/find/{username}")
    public UserEntity getReferralId(@PathVariable(name = "username") String username) {
        return userService.getUserByUsername(username);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<UserEntity> createUserList(@RequestBody List<UserDto> userDtoList) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < userDtoList.size(); i++) {
            UserDto userDto = userDtoList.get(i);
            UserEntity userEntity = UserEntity.builder()
                    .name(userDto.getName())
                    .username(userDto.getUsername())
                    .password(userDto.getPassword())
                    .status(userDto.getStatus())
                    .build();
            userList.add(userEntity);
        }
        return userService.createUserList(userList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public UserEntity createUser(@RequestBody UserDto userDto) {
        UserEntity userEntity = UserEntity.builder()
                .name(userDto.getName())
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .status(userDto.getStatus())
                .build();
        return userService.createUser(userEntity);
    }

    @PutMapping(path = "")
    public void modifyUserList(@ModelAttribute UserParamDto userParamDto) {
        userService.modifyUserList(userParamDto);       // 관리자페이지 -강사 등록 승인 사용중
    }

    @PutMapping(path = "{id}")
    public Integer modifyUser(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto) {
        return userService.modifyUser(id, userDto);
    }

    @DeleteMapping(path = "")
    public void removeUserList(@RequestBody List<Long> idList) {
        userService.removeUserList(idList);
    }

    @DeleteMapping(path = "{id}")
    public void removeUser(@PathVariable(name = "id") Long id) {
        userService.removeUser(id);
    }

    @PutMapping(path = "{id}/password")
    public Map<String, Object> modifyPassword(@PathVariable(name = "id") Long id, @RequestBody UserDto userDto)
            throws ServletException {
        return userService.modifyPassword(id, userDto.getOldPassword(), userDto.getNewPassword());

    }

    @PostMapping(path = "/id-exists")
    public Integer userIDExsts(@RequestBody UserParamDto userParamDto) {
        return userService.getUserExists(userParamDto);
    }

    @PostMapping(path = "/sms-id-exists")
    public Integer userSmsIDExsts(@RequestBody UserParamDto userParamDto) {
        return userService.getSmsUserExists(userParamDto);
    }
    
    @GetMapping(path = "/ipList")
    public PageResponse<UserLoginHistoryEntity> getUserIpList(@ModelAttribute UserLoginHistoryDto userLoginHistoryDto,
            @ModelAttribute PageRequest pageRequest) {
        return userService.getUserIpList(userLoginHistoryDto, pageRequest);
    }

    @GetMapping(path = "/ip/{id}")
    public UserEntity getUserIp(@PathVariable(name = "id") Long id) {
        return userService.getUserIp(id);
    }

}