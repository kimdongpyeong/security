package kr.supporti.common.util.code.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.code.dto.TreeCodeDto;
import kr.supporti.common.util.code.dto.TreeCodeParamDto;
import kr.supporti.common.util.code.mapper.ApiUtilCodeMapper;

@Service
public class ApiUtilCodeService {

    @Autowired
    private ApiUtilCodeMapper apiUtilCodeMapper;

    @Transactional(readOnly = true)
    public PageResponse<TreeCodeDto> getTreeCodeList(TreeCodeParamDto treeCodeParamDto, PageRequest pageRequest) {
        Integer treeCodeListCount = apiUtilCodeMapper.selectTreeCodeListCount(treeCodeParamDto);
        List<TreeCodeDto> treeCodeList = apiUtilCodeMapper.selectTreeCodeList(treeCodeParamDto, pageRequest);
        PageResponse<TreeCodeDto> pageResponse = new PageResponse<>(pageRequest, treeCodeListCount);
        pageResponse.setItems(treeCodeList);
        return pageResponse;
    }

}