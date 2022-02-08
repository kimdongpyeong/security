package kr.supporti.api.common.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.supporti.api.common.dto.MenuParamDto;
import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.api.common.mapper.MenuMapper;
import kr.supporti.api.common.repository.MenuRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.menu.dto.TreeMenuDto;
import kr.supporti.common.util.menu.dto.TreeMenuParamDto;
import kr.supporti.common.util.menu.service.ApiUtilMenuService;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    @Autowired
    private ApiUtilMenuService apiUtilMenuService;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<MenuEntity> getMenuList(@Valid MenuParamDto menuParamDto, PageRequest pageRequest) {
        Integer menuListCount = menuMapper.selectMenuListCount(menuParamDto);
        List<MenuEntity> menuList = menuMapper.selectMenuList(menuParamDto, pageRequest);
        PageResponse<MenuEntity> pageResponse = new PageResponse<>(pageRequest, menuListCount);
        pageResponse.setItems(menuList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public MenuEntity getMenu(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return menuMapper.selectMenu(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<MenuEntity> createMenuList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) MenuEntity> menuList) {
        return menuRepository.saveAll(menuList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public MenuEntity createMenu(@Valid @NotNull(groups = { CreateValidationGroup.class }) MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<MenuEntity> modifyMenuList(
            @Valid @NotEmpty(groups = { ModifyValidationGroup.class }) List<@NotNull(groups = {
                    ModifyValidationGroup.class }) MenuEntity> menuList) {
        return menuRepository.saveAll(menuList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public MenuEntity modifyMenu(@Valid @NotNull(groups = { ModifyValidationGroup.class }) MenuEntity menuEntity) {
        return menuRepository.save(menuEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeMenuList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        List<MenuEntity> menuList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            menuList.add(MenuEntity.builder().id(id).build());
        }
        menuRepository.deleteAll(menuList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeMenu(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        menuRepository.delete(MenuEntity.builder().id(id).build());
    }

    public Map<String, Object> getSession(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id,
            HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> map = new WeakHashMap<>();

        if (session.getAttribute("MENU_LIST") == null) {
            ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(1);
            pageRequest.setRowSize(100000000);
            PageRequest treeMenuDtoListPageRequest = new PageRequest();
            treeMenuDtoListPageRequest.setPage(1);
            treeMenuDtoListPageRequest.setRowSize(100000000);
            treeMenuDtoListPageRequest.setSort(Arrays.asList("rankingPath,asc"));
            List<MenuEntity> menuEntityList = getMenuList(MenuParamDto.builder().userId(id).build(), pageRequest)
                    .getItems();
            List<TreeMenuDto> treeMenuDtoList = apiUtilMenuService
                    .getTreeMenuList(TreeMenuParamDto.builder().userId(id).build(), treeMenuDtoListPageRequest)
                    .getItems();
            List<Map<String, Object>> menuList = objectMapper.convertValue(menuEntityList,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> treeMenuList = objectMapper.convertValue(treeMenuDtoList,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            session.setAttribute("MENU_LIST", menuList);
            session.setAttribute("TREE_MENU_LIST", treeMenuList);
            session.setMaxInactiveInterval(-1);
        }
        map.put("menuList", session.getAttribute("MENU_LIST"));
        map.put("treeMenuList", session.getAttribute("TREE_MENU_LIST"));
        return map;
    }

}