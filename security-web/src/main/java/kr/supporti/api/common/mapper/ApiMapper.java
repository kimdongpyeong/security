package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.ApiParamDto;
import kr.supporti.api.common.entity.ApiEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface ApiMapper {

    public List<ApiEntity> selectApiList(@Param(value = "apiParamDto") ApiParamDto apiParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectApiListCount(@Param(value = "apiParamDto") ApiParamDto apiParamDto);

    public ApiEntity selectApi(@Param(value = "id") Long id);

}