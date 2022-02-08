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

import kr.supporti.api.common.dto.RoleApiDto;
import kr.supporti.api.common.dto.RoleApiParamDto;
import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.api.common.entity.id.RoleApiEntityId;
import kr.supporti.api.common.service.RoleApiService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/role-apis")
public class ApiCommonRoleApiController {

    @Autowired
    private RoleApiService roleApiService;

    @GetMapping(path = "")
    public PageResponse<RoleApiEntity> getRoleApiList(@ModelAttribute RoleApiParamDto roleApiParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return roleApiService.getRoleApiList(roleApiParamDto, pageRequest);
    }

    @GetMapping(path = "{roleId},{apiId}")
    public RoleApiEntity getRoleApi(@PathVariable(name = "roleId") Long roleId,
            @PathVariable(name = "apiId") Long apiId) {
        return roleApiService.getRoleApi(roleId, apiId);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<RoleApiEntity> createRoleApiList(@RequestBody List<RoleApiDto> roleApiDtoList) {
        List<RoleApiEntity> roleApiList = new ArrayList<>();
        for (int i = 0; i < roleApiDtoList.size(); i++) {
            RoleApiDto roleApiDto = roleApiDtoList.get(i);
            RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(roleApiDto.getRoleId())
                    .apiId(roleApiDto.getApiId()).build();
            roleApiList.add(roleApiEntity);
        }
        return roleApiService.createRoleApiList(roleApiList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public RoleApiEntity createRoleApi(@RequestBody RoleApiDto roleApiDto) {
        RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(roleApiDto.getRoleId())
                .apiId(roleApiDto.getApiId()).build();
        return roleApiService.createRoleApi(roleApiEntity);
    }

    @PutMapping(path = "")
    public List<RoleApiEntity> modifyRoleApiList(@RequestBody List<RoleApiDto> roleApiDtoList) {
        List<RoleApiEntity> roleApiList = new ArrayList<>();
        for (int i = 0; i < roleApiDtoList.size(); i++) {
            RoleApiDto roleApiDto = roleApiDtoList.get(i);
            RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(roleApiDto.getRoleId())
                    .apiId(roleApiDto.getApiId()).build();
            roleApiList.add(roleApiEntity);
        }
        return roleApiService.modifyRoleApiList(roleApiList);
    }

    @PutMapping(path = "{roleId},{apiId}")
    public RoleApiEntity modifyRoleApi(@PathVariable(name = "roleId") Long roleId,
            @PathVariable(name = "apiId") Long apiId, @RequestBody RoleApiDto roleApiDto) {
        RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(roleId).apiId(apiId).build();
        return roleApiService.modifyRoleApi(roleApiEntity);
    }

    @DeleteMapping(path = "")
    public void removeRoleApiList(@RequestBody List<RoleApiEntityId> roleApiEntityIdList) {
        roleApiService.removeRoleApiList(roleApiEntityIdList);
    }

    @DeleteMapping(path = "{roleId},{apiId}")
    public void removeRoleApi(@PathVariable(name = "roleId") Long roleId, @PathVariable(name = "apiId") Long apiId) {
        roleApiService.removeRoleApi(roleId, apiId);
    }

}