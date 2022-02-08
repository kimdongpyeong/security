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

import kr.supporti.api.common.dto.CategoryDto;
import kr.supporti.api.common.dto.CategoryParamDto;
import kr.supporti.api.common.entity.CategoryEntity;
import kr.supporti.api.common.service.CategoryService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/category")
public class ApiCommonCategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping(path = "")
    public PageResponse<CategoryEntity> getCategoryList(@ModelAttribute CategoryParamDto categoryParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return categoryService.getCategoryList(categoryParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public CategoryEntity getCategory(@PathVariable(name = "id") Long id) {
        return categoryService.getCategory(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public CategoryEntity createCategory(@RequestBody CategoryDto categoryDto) {
        CategoryEntity categoryEntity = CategoryEntity.builder()
                .title(categoryDto.getTitle()).desc(categoryDto.getDesc()).publishYn(categoryDto.getPublishYn())
                .orderNo(categoryDto.getOrderNo()).build();
        return categoryService.createCategory(categoryEntity);
    }

    @PutMapping(path = "{id}")
    public CategoryEntity modifyCategory(@PathVariable(name = "id") Long id, @RequestBody CategoryDto categoryDto) {
        CategoryEntity categoryEntity = CategoryEntity.builder().id(id)
                .title(categoryDto.getTitle()).desc(categoryDto.getDesc()).publishYn(categoryDto.getPublishYn())
                .orderNo(categoryDto.getOrderNo()).build();
        return categoryService.modifyCategory(categoryEntity);
    }

    @DeleteMapping(path = "{id}")
    public void removeCategory(@PathVariable(name = "id") Long id) {
        categoryService.removeCategory(id);
    }
}