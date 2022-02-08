package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.MenuParamDto;
import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface MenuMapper {

    public List<MenuEntity> selectMenuList(@Param(value = "menuParamDto") MenuParamDto menuParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectMenuListCount(@Param(value = "menuParamDto") MenuParamDto menuParamDto);

    public MenuEntity selectMenu(@Param(value = "id") Long id);

}