package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.CodeParamDto;
import kr.supporti.api.common.entity.CodeEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface CodeMapper {

    public List<CodeEntity> selectCodeList(@Param(value = "codeParamDto") CodeParamDto codeParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectCodeListCount(@Param(value = "codeParamDto") CodeParamDto codeParamDto);

    public CodeEntity selectCode(@Param(value = "id") Long id);

    public CodeEntity selectCodeByParentNameAndChildNameForIfExists(@Param(value = "parentName") String parentName,
            @Param(value = "childName") String childName);

    public CodeEntity selectCodeByParentName(@Param(value = "parentName") String parentName);
}
