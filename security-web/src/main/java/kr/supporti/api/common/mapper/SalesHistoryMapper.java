package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.SalesHistoryParamDto;
import kr.supporti.api.common.dto.SalesTotalDto;
import kr.supporti.api.common.entity.SalesHistoryEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface SalesHistoryMapper {

    public List<SalesHistoryEntity> selectSalesHistoryList(
            @Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectSalesHistoryListCount(@Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto);

    public SalesHistoryEntity selectSalesHistory(@Param(value = "id") Long id);

    //등록한 매출과 결제 내역 한꺼번에 가져오기
    public List<SalesTotalDto> selectSalesTotalList(
            @Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectSalesTotalListCount(@Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto);
    
    //월별 총매출..
    public List<SalesTotalDto> selectSalesTermTotalList(
            @Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectSalesTermTotalListCount(@Param(value = "salesHistoryParamDto") SalesHistoryParamDto salesHistoryParamDto);
}