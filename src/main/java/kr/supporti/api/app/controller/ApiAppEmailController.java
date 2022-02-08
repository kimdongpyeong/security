package kr.supporti.api.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.service.ApiAppEmailService;

@RestController
@RequestMapping(path = "api/app/email")
public class ApiAppEmailController {

    @Autowired
    private ApiAppEmailService apiAppEmailService;

//    @PostMapping("/email-Auth")
//    public Object emailCode(HttpServletRequest request, @RequestBody EmailParamDto emailParamDto) throws Exception {
//        return apiAppEmailService.emailAutn(request, emailParamDto);
//    }
//
//    @PostMapping("/email-comparison")
//    public Object userAuth(@RequestBody EmailParamDto emailParamDto) {
//        return apiAppEmailService.emailAutnComparison(emailParamDto);
//    }

}