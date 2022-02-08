package kr.supporti.api.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.app.service.ApiAppAccountService;

@RestController
@RequestMapping(path = "api/app/accounts")
public class ApiAppAccountController {

    @Autowired
    ApiAppAccountService accountService;

    @PostMapping(path = "")
    public AccountDto createAccount(@RequestBody AccountDto accountDto) {
        return accountService.createAccount(accountDto);
    }

    @PutMapping(path = "")
    public void modifyAccount(@RequestBody AccountDto accountDto) {
        accountService.modifyAccount(accountDto);
    }

    @DeleteMapping(path = "")
    public void removeAccount(@RequestBody AccountDto accountDto) {
        accountService.removeAccount(accountDto);
    }

    @PostMapping(path = "/sign-up")
    public void userSignUp(@ModelAttribute AccountDto accountDto) {
        accountService.userSignUp(accountDto);
    }
}
