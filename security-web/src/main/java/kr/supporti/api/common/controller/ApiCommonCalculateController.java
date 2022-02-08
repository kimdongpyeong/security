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

import kr.supporti.api.common.dto.CalculateDto;
import kr.supporti.api.common.dto.CalculateParamDto;
import kr.supporti.api.common.dto.SalesTotalDto;
import kr.supporti.api.common.service.CalculateService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/calculate")
public class ApiCommonCalculateController {

    @Autowired
    private CalculateService calculateService;

    @GetMapping(path = "")
    public PageResponse<CalculateDto> getSalesTermTotalList(@ModelAttribute CalculateParamDto calculateParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return calculateService.getCalculateList(calculateParamDto, pageRequest);
    }
}