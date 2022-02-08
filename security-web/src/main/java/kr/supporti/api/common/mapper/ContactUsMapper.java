package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ContactUsDto;
import kr.supporti.api.common.dto.ContactUsParamDto;
import kr.supporti.api.common.entity.ContactUsEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ContactUsMapper {

    public List<ContactUsEntity> selectContactUsList(
            @Param(value = "contactUsParamDto") ContactUsParamDto contactUsParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectContactUsListCount(
            @Param(value = "contactUsParamDto") ContactUsParamDto contactUsParamDto);

    public ContactUsEntity selectContactUs(@Param(value = "id") Long id);

    public Integer completedContactUs(@Param(value = "contactUsParamDto") ContactUsParamDto contactUsParamDto);
    
    public Integer lastUpdated(@Param(value = "contactUsParamDto") ContactUsParamDto contactUsParamDto);
}