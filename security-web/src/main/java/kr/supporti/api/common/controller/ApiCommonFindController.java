package kr.supporti.api.common.controller;

import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.FindService;

@RestController
@RequestMapping(path = "api/common/find")
public class ApiCommonFindController {

    @Autowired
    private FindService findService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(path = "{username}")
    public UserEntity findUser(@PathVariable(name = "username") String username) {
        return findService.findUser(username);
    }

    @PutMapping(path = "{id}/password")
    public void modifyPassword(@PathVariable(name = "id") Long id, @RequestBody AccountDto accountDto)
            throws ServletException {
        findService.modifyPassword(id, accountDto.getNewPassword());

    }
}