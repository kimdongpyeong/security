package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.MenuDto;
import kr.supporti.api.common.dto.MenuParamDto;
import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.api.common.service.MenuService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/menus")
public class ApiCommonMenuController {

    @Autowired
    private MenuService menuService;

    @GetMapping(path = "")
    public PageResponse<MenuEntity> getMenuList(@ModelAttribute MenuParamDto menuParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return menuService.getMenuList(menuParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public MenuEntity getMenu(@PathVariable(name = "id") Long id) {
        return menuService.getMenu(id);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<MenuEntity> createMenuList(@RequestBody List<MenuDto> menuDtoList) {
        List<MenuEntity> menuList = new ArrayList<>();
        for (int i = 0; i < menuDtoList.size(); i++) {
            MenuDto menuDto = menuDtoList.get(i);
            MenuEntity menuEntity = MenuEntity.builder().parentId(menuDto.getParentId()).name(menuDto.getName())
                    .description(menuDto.getDescription()).path(menuDto.getPath()).ranking(menuDto.getRanking())
                    .show(menuDto.getShow()).sideShow(menuDto.getSideShow()).publicyStatus(menuDto.getPublicyStatus())
                    .icon(menuDto.getIcon()).build();
            menuList.add(menuEntity);
        }
        return menuService.createMenuList(menuList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public MenuEntity createMenu(@RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = MenuEntity.builder().parentId(menuDto.getParentId()).name(menuDto.getName())
                .description(menuDto.getDescription()).path(menuDto.getPath()).ranking(menuDto.getRanking())
                .show(menuDto.getShow()).sideShow(menuDto.getSideShow()).publicyStatus(menuDto.getPublicyStatus())
                .icon(menuDto.getIcon()).build();
        return menuService.createMenu(menuEntity);
    }

    @PutMapping(path = "")
    public List<MenuEntity> modifyMenuList(@RequestBody List<MenuDto> menuDtoList) {
        List<MenuEntity> menuList = new ArrayList<>();
        for (int i = 0; i < menuDtoList.size(); i++) {
            MenuDto menuDto = menuDtoList.get(i);
            MenuEntity menuEntity = MenuEntity.builder().id(menuDto.getId()).parentId(menuDto.getParentId())
                    .name(menuDto.getName()).description(menuDto.getDescription()).path(menuDto.getPath())
                    .ranking(menuDto.getRanking()).show(menuDto.getShow()).sideShow(menuDto.getSideShow())
                    .publicyStatus(menuDto.getPublicyStatus()).icon(menuDto.getIcon()).build();
            menuList.add(menuEntity);
        }
        return menuService.modifyMenuList(menuList);
    }

    @PutMapping(path = "{id}")
    public MenuEntity modifyMenu(@PathVariable(name = "id") Long id, @RequestBody MenuDto menuDto) {
        MenuEntity menuEntity = MenuEntity.builder().id(id).parentId(menuDto.getParentId()).name(menuDto.getName())
                .description(menuDto.getDescription()).path(menuDto.getPath()).ranking(menuDto.getRanking())
                .show(menuDto.getShow()).sideShow(menuDto.getSideShow()).publicyStatus(menuDto.getPublicyStatus())
                .icon(menuDto.getIcon()).build();
        return menuService.modifyMenu(menuEntity);
    }

    @DeleteMapping(path = "")
    public void removeMenuList(@RequestBody List<Long> idList) {
        menuService.removeMenuList(idList);
    }

    @DeleteMapping(path = "{id}")
    public void removeMenu(@PathVariable(name = "id") Long id) {
        menuService.removeMenu(id);
    }

    @GetMapping(path = "session/{id}")
    public Map<String, Object> getSession(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        return menuService.getSession(id, request);
    }

}