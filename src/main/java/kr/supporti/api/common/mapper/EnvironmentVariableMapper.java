package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.EnvironmentVariableParamDto;
import kr.supporti.api.common.entity.EnvironmentVariableEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface EnvironmentVariableMapper {

    public List<EnvironmentVariableEntity> selectEnvironmentVariableList(@Param(value = "environmentVariableParamDto") EnvironmentVariableParamDto environmentVariableParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectEnvironmentVariableListCount(@Param(value = "environmentVariableParamDto") EnvironmentVariableParamDto environmentVariableParamDto);

    public EnvironmentVariableEntity selectEnvironmentVariable(@Param(value = "id") Long id);
    
    public EnvironmentVariableEntity selectEnvironmentVariableKey(@Param(value = "key") String key);

}
