package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.CategoryParamDto;
import kr.supporti.api.common.entity.CategoryEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface CategoryMapper {

    public List<CategoryEntity> selectCategoryList(@Param(value = "categoryParamDto") CategoryParamDto categoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectCategoryListCount(@Param(value = "categoryParamDto") CategoryParamDto categoryParamDto);

    public CategoryEntity selectCategory(@Param(value = "id") Long id);

}