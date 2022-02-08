package kr.supporti.common.util.code.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.code.dto.TreeCodeDto;
import kr.supporti.common.util.code.dto.TreeCodeParamDto;
import kr.supporti.common.util.code.service.ApiUtilCodeService;

@RestController
@RequestMapping(path = "api/util/codes")
public class ApiUtilCodeController {

    @Autowired
    private ApiUtilCodeService apiUtilCodeService;

    @GetMapping(path = "tree-codes")
    public PageResponse<TreeCodeDto> getTreeCodeList(@ModelAttribute TreeCodeParamDto treeCodeParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return apiUtilCodeService.getTreeCodeList(treeCodeParamDto, pageRequest);
    }

}