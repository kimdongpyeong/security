package kr.supporti.common.util.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.category.dto.TreeCategoryDto;
import kr.supporti.common.util.category.dto.TreeCategoryParamDto;
import kr.supporti.common.util.category.service.ApiUtilCategoryService;

@RestController
@RequestMapping(path = "api/util/categories")
public class ApiUtilCategoryController {

    @Autowired
    private ApiUtilCategoryService apiUtilCategoryService;

    @GetMapping(path = "tree-categories")
    public PageResponse<TreeCategoryDto> getTreeCategoryList(@ModelAttribute TreeCategoryParamDto treeCategoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return apiUtilCategoryService.getTreeCategoryList(treeCategoryParamDto, pageRequest);
    }

}