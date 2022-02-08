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

import kr.supporti.api.common.dto.RoleDto;
import kr.supporti.api.common.dto.RoleParamDto;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.api.common.service.RoleService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/roles")
public class ApiCommonRoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(path = "")
    public PageResponse<RoleEntity> getRoleList(@ModelAttribute RoleParamDto roleParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return roleService.getRoleList(roleParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public RoleEntity getRole(@PathVariable(name = "id") Long id) {
        return roleService.getRole(id);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<RoleEntity> createRoleList(@RequestBody List<RoleDto> roleDtoList) {
        List<RoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < roleDtoList.size(); i++) {
            RoleDto roleDto = roleDtoList.get(i);
            RoleEntity roleEntity = RoleEntity.builder().name(roleDto.getName()).description(roleDto.getDescription())
                    .value(roleDto.getValue()).build();
            roleList.add(roleEntity);
        }
        return roleService.createRoleList(roleList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public RoleEntity createRole(@RequestBody RoleDto roleDto) {
        RoleEntity roleEntity = RoleEntity.builder().name(roleDto.getName()).description(roleDto.getDescription())
                .value(roleDto.getValue()).build();
        return roleService.createRole(roleEntity);
    }

    @PutMapping(path = "")
    public List<RoleEntity> modifyRoleList(@RequestBody List<RoleDto> roleDtoList) {
        List<RoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < roleDtoList.size(); i++) {
            RoleDto roleDto = roleDtoList.get(i);
            RoleEntity roleEntity = RoleEntity.builder().id(roleDto.getId()).name(roleDto.getName())
                    .description(roleDto.getDescription()).value(roleDto.getValue()).build();
            roleList.add(roleEntity);
        }
        return roleService.modifyRoleList(roleList);
    }

    @PutMapping(path = "{id}")
    public RoleEntity modifyRole(@PathVariable(name = "id") Long id, @RequestBody RoleDto roleDto) {
        RoleEntity roleEntity = RoleEntity.builder().id(id).name(roleDto.getName())
                .description(roleDto.getDescription()).value(roleDto.getValue()).build();
        return roleService.modifyRole(roleEntity);
    }

    @DeleteMapping(path = "")
    public void removeRoleList(@RequestBody List<Long> idList) {
        roleService.removeRoleList(idList);
    }

    @DeleteMapping(path = "{id}")
    public void removeRole(@PathVariable(name = "id") Long id) {
        roleService.removeRole(id);
    }

    @DeleteMapping(path = "delete-all-dependency")
    public void removeRoleAllDependencyList(@RequestBody List<Long> idList) {
        idList.remove(Long.valueOf("1"));
        idList.remove(Long.valueOf("2"));
        idList.remove(Long.valueOf("3"));
        roleService.removeRoleAllDependencyList(idList);
    }

}