package kr.supporti.api.common.controller;

import java.util.ArrayList;
import java.util.List;

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

import kr.supporti.api.common.dto.RoleMenuDto;
import kr.supporti.api.common.dto.RoleMenuParamDto;
import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.api.common.entity.id.RoleMenuEntityId;
import kr.supporti.api.common.service.RoleMenuService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/role-menus")
public class ApiCommonRoleMenuController {

    @Autowired
    private RoleMenuService roleMenuService;

    @GetMapping(path = "")
    public PageResponse<RoleMenuEntity> getRoleMenuList(@ModelAttribute RoleMenuParamDto roleMenuParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return roleMenuService.getRoleMenuList(roleMenuParamDto, pageRequest);
    }

    @GetMapping(path = "{roleId},{menuId}")
    public RoleMenuEntity getRoleMenu(@PathVariable(name = "roleId") Long roleId,
            @PathVariable(name = "menuId") Long menuId) {
        return roleMenuService.getRoleMenu(roleId, menuId);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<RoleMenuEntity> createRoleMenuList(@RequestBody List<RoleMenuDto> roleMenuDtoList) {
        List<RoleMenuEntity> roleMenuList = new ArrayList<>();
        for (int i = 0; i < roleMenuDtoList.size(); i++) {
            RoleMenuDto roleMenuDto = roleMenuDtoList.get(i);
            RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(roleMenuDto.getRoleId())
                    .menuId(roleMenuDto.getMenuId()).build();
            roleMenuList.add(roleMenuEntity);
        }
        return roleMenuService.createRoleMenuList(roleMenuList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public RoleMenuEntity createRoleMenu(@RequestBody RoleMenuDto roleMenuDto) {
        RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(roleMenuDto.getRoleId())
                .menuId(roleMenuDto.getMenuId()).build();
        return roleMenuService.createRoleMenu(roleMenuEntity);
    }

    @PutMapping(path = "")
    public List<RoleMenuEntity> modifyRoleMenuList(@RequestBody List<RoleMenuDto> roleMenuDtoList) {
        List<RoleMenuEntity> roleMenuList = new ArrayList<>();
        for (int i = 0; i < roleMenuDtoList.size(); i++) {
            RoleMenuDto roleMenuDto = roleMenuDtoList.get(i);
            RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(roleMenuDto.getRoleId())
                    .menuId(roleMenuDto.getMenuId()).build();
            roleMenuList.add(roleMenuEntity);
        }
        return roleMenuService.modifyRoleMenuList(roleMenuList);
    }

    @PutMapping(path = "{roleId},{menuId}")
    public RoleMenuEntity modifyRoleMenu(@PathVariable(name = "roleId") Long roleId,
            @PathVariable(name = "menuId") Long menuId, @RequestBody RoleMenuDto roleMenuDto) {
        RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(roleId).menuId(menuId).build();
        return roleMenuService.modifyRoleMenu(roleMenuEntity);
    }

    @DeleteMapping(path = "")
    public void removeRoleMenuList(@RequestBody List<RoleMenuEntityId> roleMenuEntityIdList) {
        roleMenuService.removeRoleMenuList(roleMenuEntityIdList);
    }

    @DeleteMapping(path = "{roleId},{menuId}")
    public void removeRoleMenu(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "menuId") Long menuId) {
        roleMenuService.removeRoleMenu(roleId, menuId);
    }

}