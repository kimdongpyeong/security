package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.CalculateDto;
import kr.supporti.api.common.dto.CalculateParamDto;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface CalculateMapper {

    //월별 정산내역
    public List<CalculateDto> selectCalculateList(
            @Param(value = "calculateParamDto") CalculateParamDto calculateParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectCalculateListCount(@Param(value = "calculateParamDto") CalculateParamDto calculateParamDto);
}