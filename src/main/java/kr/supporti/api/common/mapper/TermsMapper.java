package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ApiParamDto;
import kr.supporti.api.common.dto.TermsDto;
import kr.supporti.api.common.dto.TermsParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.ApiEntity;
import kr.supporti.api.common.entity.TermsEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface TermsMapper {

    public List<TermsEntity> selectTermsList(@Param(value = "termsParamDto") TermsParamDto termsParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectTermsListCount(@Param(value = "termsParamDto") TermsParamDto termsParamDto);

    public TermsEntity selectTerms(@Param(value = "id") Long id);

}