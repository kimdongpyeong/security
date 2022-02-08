package kr.supporti.common.util.code.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.code.dto.TreeCodeDto;
import kr.supporti.common.util.code.dto.TreeCodeParamDto;

@Repository
@Mapper
public interface ApiUtilCodeMapper {

    public List<TreeCodeDto> selectTreeCodeList(@Param(value = "treeCodeParamDto") TreeCodeParamDto treeCodeParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectTreeCodeListCount(@Param(value = "treeCodeParamDto") TreeCodeParamDto treeCodeParamDto);

}