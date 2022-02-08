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

import kr.supporti.api.common.dto.RoleUserDto;
import kr.supporti.api.common.dto.RoleUserParamDto;
import kr.supporti.api.common.entity.RoleUserEntity;
import kr.supporti.api.common.entity.id.RoleUserEntityId;
import kr.supporti.api.common.service.RoleUserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/role-users")
public class ApiCommonRoleUserController {

    @Autowired
    private RoleUserService roleUserService;

    @GetMapping(path = "")
    public PageResponse<RoleUserEntity> getRoleUserList(@ModelAttribute RoleUserParamDto roleUserParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return roleUserService.getRoleUserList(roleUserParamDto, pageRequest);
    }

    @GetMapping(path = "{roleId},{userId}")
    public RoleUserEntity getRoleUser(@PathVariable(name = "userId") Long userId) {
        return roleUserService.getRoleUser(userId);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<RoleUserEntity> createRoleUserList(@RequestBody List<RoleUserDto> roleUserDtoList) {
        List<RoleUserEntity> roleUserList = new ArrayList<>();
        for (int i = 0; i < roleUserDtoList.size(); i++) {
            RoleUserDto roleUserDto = roleUserDtoList.get(i);
            RoleUserEntity roleUserEntity = RoleUserEntity.builder().roleId(roleUserDto.getRoleId()).build();
            roleUserList.add(roleUserEntity);
        }
        return roleUserService.createRoleUserList(roleUserList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public RoleUserEntity createRoleUser(@RequestBody RoleUserDto roleUserDto) {
        RoleUserEntity roleUserEntity = RoleUserEntity.builder().roleId(roleUserDto.getRoleId())
                .userId(roleUserDto.getUserId()).build();
        return roleUserService.createRoleUser(roleUserEntity);
    }

    @PutMapping(path = "")
    public List<RoleUserEntity> modifyRoleUserList(@RequestBody List<RoleUserDto> roleUserDtoList) {
        List<RoleUserEntity> roleUserList = new ArrayList<>();
        for (int i = 0; i < roleUserDtoList.size(); i++) {
            RoleUserDto roleUserDto = roleUserDtoList.get(i);
            RoleUserEntity roleUserEntity = RoleUserEntity.builder().roleId(roleUserDto.getRoleId())
                    .userId(roleUserDto.getUserId()).build();
            roleUserList.add(roleUserEntity);
        }
        return roleUserService.modifyRoleUserList(roleUserList);
    }

    @PutMapping(path = "{roleId},{userId}")
    public RoleUserEntity modifyRoleUser(@PathVariable(name = "roleId") Long roleId,
            @PathVariable(name = "userId") Long userId, @RequestBody RoleUserDto roleUserDto) {
        RoleUserEntity roleUserEntity = RoleUserEntity.builder().roleId(roleId).userId(userId).build();
        return roleUserService.modifyRoleUser(roleUserEntity);
    }

    @DeleteMapping(path = "")
    public void removeRoleUserList(@RequestBody List<RoleUserEntityId> roleUserEntityIdList) {
        roleUserService.removeRoleUserList(roleUserEntityIdList);
    }

    @DeleteMapping(path = "{roleId},{userId}")
    public void removeRoleUser(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "userId") Long userId) {
        roleUserService.removeRoleUser(roleId, userId);
    }
}