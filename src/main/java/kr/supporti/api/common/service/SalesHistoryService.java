package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.SalesHistoryDto;
import kr.supporti.api.common.dto.SalesHistoryParamDto;
import kr.supporti.api.common.dto.SalesTotalDto;
import kr.supporti.api.common.entity.SalesHistoryEntity;
import kr.supporti.api.common.mapper.SalesHistoryMapper;
import kr.supporti.api.common.repository.SalesHistoryRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class SalesHistoryService {

    @Autowired
    private SalesHistoryRepository salesHistoryRepository;

    @Autowired
    private SalesHistoryMapper salesHistoryMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<SalesHistoryEntity> getSalesHistoryList(@Valid SalesHistoryParamDto salesHistoryParamDto,
            PageRequest pageRequest) {
        Integer salesHistoryListCount = salesHistoryMapper.selectSalesHistoryListCount(salesHistoryParamDto);
        List<SalesHistoryEntity> salesHistoryList = salesHistoryMapper.selectSalesHistoryList(salesHistoryParamDto, pageRequest);
        PageResponse<SalesHistoryEntity> pageResponse = new PageResponse<>(pageRequest, salesHistoryListCount);
        pageResponse.setItems(salesHistoryList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<SalesTotalDto> getSalesTotalList(@Valid SalesHistoryParamDto salesHistoryParamDto,
            PageRequest pageRequest) {
        Integer salesTotalListCount = salesHistoryMapper.selectSalesTotalListCount(salesHistoryParamDto);
        List<SalesTotalDto> salesTotalList = salesHistoryMapper.selectSalesTotalList(salesHistoryParamDto, pageRequest);
        PageResponse<SalesTotalDto> pageResponse = new PageResponse<>(pageRequest, salesTotalListCount);
        pageResponse.setItems(salesTotalList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<SalesTotalDto> getSalesTermTotalList(@Valid SalesHistoryParamDto salesHistoryParamDto,
            PageRequest pageRequest) {
        Integer salesTermTotalListCount = salesHistoryMapper.selectSalesTermTotalListCount(salesHistoryParamDto);
        List<SalesTotalDto> salesTermTotalList = salesHistoryMapper.selectSalesTermTotalList(salesHistoryParamDto, pageRequest);
        PageResponse<SalesTotalDto> pageResponse = new PageResponse<>(pageRequest, salesTermTotalListCount);
        pageResponse.setItems(salesTermTotalList);
        return pageResponse;
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public SalesHistoryEntity getSalesHistory(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return salesHistoryMapper.selectSalesHistory(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public SalesHistoryEntity createSalesHistory(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) SalesHistoryDto salesHistoryDto) {


        SalesHistoryEntity salesHistoryEntity = SalesHistoryEntity.builder()
                .salesStartDate(salesHistoryDto.getSalesStartDate())
                .salesEndDate(salesHistoryDto.getSalesEndDate())
                .count(salesHistoryDto.getCount())
                .totalSales(salesHistoryDto.getTotalSales())
                .paymentMethodCd(salesHistoryDto.getPaymentMethodCd())
                .createdBy(salesHistoryDto.getCreatedBy())
                .build();
        salesHistoryEntity = salesHistoryRepository.save(salesHistoryEntity);
        
        return salesHistoryEntity;
    }

}