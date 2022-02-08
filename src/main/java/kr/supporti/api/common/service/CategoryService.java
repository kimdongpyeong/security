package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.CategoryParamDto;
import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.api.common.entity.CategoryEntity;
import kr.supporti.api.common.mapper.CategoryMapper;
import kr.supporti.api.common.repository.CategoryRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CategoryMapper categoryMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<CategoryEntity> getCategoryList(@Valid CategoryParamDto categoryParamDto,
            PageRequest pageRequest) {
        Integer categoryListCount = categoryMapper.selectCategoryListCount(categoryParamDto);
        List<CategoryEntity> categoryList = categoryMapper.selectCategoryList(categoryParamDto, pageRequest);
        PageResponse<CategoryEntity> pageResponse = new PageResponse<>(pageRequest, categoryListCount);
        pageResponse.setItems(categoryList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public CategoryEntity getCategory(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return categoryMapper.selectCategory(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public CategoryEntity createCategory(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public CategoryEntity modifyCategory(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) CategoryEntity categoryEntity) {
        return categoryRepository.save(categoryEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeCategory(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        categoryRepository.delete(CategoryEntity.builder().id(id).build());
    }

}