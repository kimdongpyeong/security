package kr.supporti.common.util.category.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.category.dto.TreeCategoryDto;
import kr.supporti.common.util.category.dto.TreeCategoryParamDto;

@Repository
@Mapper
public interface ApiUtilCategoryMapper {

    public List<TreeCategoryDto> selectTreeCategoryList(
            @Param(value = "treeCategoryParamDto") TreeCategoryParamDto treeCategoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectTreeCategoryListCount(
            @Param(value = "treeCategoryParamDto") TreeCategoryParamDto treeCategoryParamDto);

}