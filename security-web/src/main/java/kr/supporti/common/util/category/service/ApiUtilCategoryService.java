package kr.supporti.common.util.category.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.category.dto.TreeCategoryDto;
import kr.supporti.common.util.category.dto.TreeCategoryParamDto;
import kr.supporti.common.util.category.mapper.ApiUtilCategoryMapper;

@Service
public class ApiUtilCategoryService {

    @Autowired
    private ApiUtilCategoryMapper apiUtilCategoryMapper;

    @Transactional(readOnly = true)
    public PageResponse<TreeCategoryDto> getTreeCategoryList(TreeCategoryParamDto treeCategoryParamDto,
            PageRequest pageRequest) {
        Integer treeCategoryListCount = apiUtilCategoryMapper.selectTreeCategoryListCount(treeCategoryParamDto);
        List<TreeCategoryDto> treeCategoryList = apiUtilCategoryMapper.selectTreeCategoryList(treeCategoryParamDto,
                pageRequest);
        PageResponse<TreeCategoryDto> pageResponse = new PageResponse<>(pageRequest, treeCategoryListCount);
        pageResponse.setItems(treeCategoryList);
        return pageResponse;
    }

}