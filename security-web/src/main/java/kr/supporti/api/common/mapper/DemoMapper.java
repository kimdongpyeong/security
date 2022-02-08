package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.DemoParamDto;
import kr.supporti.api.common.entity.DemoEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface DemoMapper {

    public List<DemoEntity> selectDemoList(
            @Param(value = "demoParamDto") DemoParamDto demoParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectDemoListCount(
            @Param(value = "demoParamDto") DemoParamDto demoParamDto);

    public DemoEntity selectDemo(@Param(value = "id") Long id);

    public Integer updateDemoExpoureN();

}