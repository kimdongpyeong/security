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

import kr.supporti.api.common.dto.SalesHistoryDto;
import kr.supporti.api.common.dto.SalesHistoryParamDto;
import kr.supporti.api.common.dto.SalesTotalDto;
import kr.supporti.api.common.entity.SalesHistoryEntity;
import kr.supporti.api.common.service.SalesHistoryService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/sales")
public class ApiCommonSalesHistoryController {

    @Autowired
    private SalesHistoryService salesHistoryService;

    @GetMapping(path = "")
    public PageResponse<SalesHistoryEntity> getSalesHistoryList(@ModelAttribute SalesHistoryParamDto salesHistoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return salesHistoryService.getSalesHistoryList(salesHistoryParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public SalesHistoryEntity getSalesHistory(@PathVariable(name = "id") Long id) {
        return salesHistoryService.getSalesHistory(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public SalesHistoryEntity createSalesHistory(@RequestBody SalesHistoryDto salesHistoryDto) {
        return salesHistoryService.createSalesHistory(salesHistoryDto);
    }

    @GetMapping(path = "/total")
    public PageResponse<SalesTotalDto> getSalesTotalList(@ModelAttribute SalesHistoryParamDto salesHistoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return salesHistoryService.getSalesTotalList(salesHistoryParamDto, pageRequest);
    }
    
    @GetMapping(path = "/term-total")
    public PageResponse<SalesTotalDto> getSalesTermTotalList(@ModelAttribute SalesHistoryParamDto salesHistoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return salesHistoryService.getSalesTermTotalList(salesHistoryParamDto, pageRequest);
    }
}