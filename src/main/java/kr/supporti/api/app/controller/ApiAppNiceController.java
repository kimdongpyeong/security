package kr.supporti.api.app.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.dto.NiceParamDto;
import kr.supporti.api.app.service.ApiAppNiceService;

@RestController
@RequestMapping(path = "api/app/nices")
public class ApiAppNiceController {

    @Autowired
    ApiAppNiceService niceService;

    @PostMapping(path = "sign-up/check-plus")
    public Map<String, Object> createSignUpCheckplus() {
        return niceService.createSignUpCheckplus("/sign-up/checkplus-success", "/sign-up/checkplus-fail");
    }

    @GetMapping(path = "/sign-up/checkplus-success")
    public Map<String, Object> signUpCheckplusSuccessGet(@RequestBody NiceParamDto checkPlusDto) {
        return niceService.signUpCheckplusSuccess(checkPlusDto);
    }

    @PostMapping(path = "/sign-up/checkplus-success")
    public Map<String, Object> signUpCheckplusSuccessPost(@RequestBody NiceParamDto checkPlusDto) {
        return niceService.signUpCheckplusSuccess(checkPlusDto);
    }

    @GetMapping(path = "sign-up/checkplus-fail")
    public void signUpCheckplusFail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("TEST");
    }

    @PostMapping(path = "find-account/check-plus")
    public Map<String, Object> createFindAccountCheckplus() {
        return niceService.createFindAccountCheckplus("/find-account/checkplus-success",
                "/find-account/checkplus-fail");
    }

    @GetMapping(path = "find-account/checkplus-success")
    public Map<String, Object> findAccountCheckplusSuccessGet(@RequestBody NiceParamDto checkPlusDto) {
        return niceService.findAccountCheckplusSuccess(checkPlusDto);
    }

    @PostMapping(path = "find-account/checkplus-success")
    public Map<String, Object> findAccountCheckplusSuccessPost(@RequestBody NiceParamDto checkPlusDto) {
        return niceService.findAccountCheckplusSuccess(checkPlusDto);
    }

    @GetMapping(path = "find-account/checkplus-fail")
    public void findAccountCheckplusFail(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("TEST");
    }

    @GetMapping(path = "session")
    public Map<String, Object> getSession() {
        return niceService.getSession();
    }

    @DeleteMapping(path = "session")
    public void removeSession() {
        niceService.removeSession();
    }
}
