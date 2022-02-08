package kr.supporti.api.common.controller;

import java.util.List;

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

import kr.supporti.api.common.dto.TermsDto;
import kr.supporti.api.common.dto.TermsParamDto;
import kr.supporti.api.common.entity.TermsEntity;
import kr.supporti.api.common.service.TermsService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/terms")
public class ApiCommonTermsController {

    @Autowired
    private TermsService termsService;

    @GetMapping(path = "")
    public PageResponse<TermsEntity> getTermsList(@ModelAttribute TermsParamDto termsParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return termsService.getTermsList(termsParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public TermsEntity getTerms(@PathVariable(name = "id") Long id) {
        return termsService.getTerms(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public TermsEntity createTerms(@RequestBody TermsDto termsDto) {
        TermsEntity termsEntity = TermsEntity.builder().title(termsDto.getTitle()).contents(termsDto.getContents())
                .publishYn(termsDto.getPublishYn()).requiredYn(termsDto.getRequiredYn()).build();
        return termsService.createTerms(termsEntity);
    }

    @PutMapping(path = "{id}")
    public TermsEntity modifyTerms(@PathVariable(name = "id") Long id, @RequestBody TermsDto termsDto) {
        TermsEntity termsEntity = TermsEntity.builder().id(id).title(termsDto.getTitle())
                .contents(termsDto.getContents()).publishYn(termsDto.getPublishYn())
                .requiredYn(termsDto.getRequiredYn()).build();
        return termsService.modifyTerms(termsEntity);
    }

    @DeleteMapping(path = "")
    public void removeTermsList(@RequestBody List<Long> idList) {
        termsService.removeTermsList(idList);
    }

}