package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.CalculateDto;
import kr.supporti.api.common.dto.CalculateParamDto;
import kr.supporti.api.common.mapper.CalculateMapper;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class CalculateService {

    @Autowired
    private CalculateMapper calculateMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<CalculateDto> getCalculateList(@Valid CalculateParamDto calculateParamDto,
            PageRequest pageRequest) {
        Integer calculateListCount = calculateMapper.selectCalculateListCount(calculateParamDto);
        List<CalculateDto> calculateList = calculateMapper.selectCalculateList(calculateParamDto, pageRequest);
        PageResponse<CalculateDto> pageResponse = new PageResponse<>(pageRequest, calculateListCount);
        pageResponse.setItems(calculateList);
        return pageResponse;
    }


}