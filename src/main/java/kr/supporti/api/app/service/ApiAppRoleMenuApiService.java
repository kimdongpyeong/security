package kr.supporti.api.app.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.RoleApiParamDto;
import kr.supporti.api.common.dto.RoleDto;
import kr.supporti.api.common.dto.RoleMenuParamDto;
import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.api.common.mapper.RoleApiMapper;
import kr.supporti.api.common.mapper.RoleMenuMapper;
import kr.supporti.api.common.repository.RoleApiRepository;
import kr.supporti.api.common.repository.RoleMenuRepository;
import kr.supporti.api.common.repository.RoleRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.validation.group.CreateValidationGroup;

@Service
public class ApiAppRoleMenuApiService {

    @Autowired
    RoleApiMapper roleApiMapper;

    @Autowired
    RoleApiRepository roleApiRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleMenuRepository roleMenuRepository;

    @Autowired
    RoleMenuMapper roleMenuMapper;

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public String createRoleMenuApi(RoleDto roleDto) {
        RoleEntity roleEntity = RoleEntity.builder().name(roleDto.getName()).description(roleDto.getDescription())
                .value(roleDto.getValue()).build();
        RoleEntity resultRoleEntity = roleRepository.save(roleEntity);
        if (roleDto.getRoleMenuListIdString() != null && !roleDto.getRoleMenuListIdString().equals("")) {
            String[] roleMenuListIdString = roleDto.getRoleMenuListIdString().split(",");
            for (Integer i = 0; i < roleMenuListIdString.length; i++) {
                RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(resultRoleEntity.getId())
                        .menuId(Long.valueOf(roleMenuListIdString[i])).build();
                roleMenuRepository.save(roleMenuEntity);
            }
        }
        if (roleDto.getRoleApiListIdString() != null && !roleDto.getRoleApiListIdString().equals("")) {
            String[] roleApiListIdString = roleDto.getRoleApiListIdString().split(",");
            for (Integer i = 0; i < roleApiListIdString.length; i++) {
                RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(resultRoleEntity.getId())
                        .apiId(Long.valueOf(roleApiListIdString[i])).build();
                roleApiRepository.save(roleApiEntity);
            }
        }
        return "success";
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public String updateRoleMenuApi(RoleDto roleDto) {
        RoleEntity resultRoleEntity = roleRepository.getOne(roleDto.getId());
        resultRoleEntity.setName(roleDto.getName());
        resultRoleEntity.setDescription(roleDto.getDescription());
        resultRoleEntity.setValue(roleDto.getValue());
        roleRepository.save(resultRoleEntity);
        deleteAfterUpdateRoleMenuList(roleDto, resultRoleEntity);
        deleteAfterUpdateRoleApiList(roleDto, resultRoleEntity);
        return "success";
    }

    public void deleteAfterUpdateRoleMenuList(RoleDto roleDto, RoleEntity resultRoleEntity) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setRowSize(1000000);
        RoleMenuParamDto roleMenuParamDto = new RoleMenuParamDto();
        roleMenuParamDto.setRoleId(roleDto.getId().toString());
        String[] roleMenuListIdString = roleDto.getRoleMenuListIdString().split(",");
        List<String> roleMenuListIdList = new ArrayList<>();
        if (roleDto.getRoleMenuListIdString() != null && !roleDto.getRoleMenuListIdString().equals(""))
            Collections.addAll(roleMenuListIdList, roleMenuListIdString);
        List<RoleMenuEntity> roleMenuList = roleMenuMapper.selectRoleMenuList(roleMenuParamDto, pageRequest);
        List<RoleMenuEntity> roleMenuDeleteList = new ArrayList<>();
        for (RoleMenuEntity roleMenu : roleMenuList) {
            if (!roleMenuListIdList.contains(roleMenu.getMenuId().toString())) {
                roleMenuDeleteList.add(roleMenu);
            }
            if (roleMenuListIdList.contains(roleMenu.getMenuId().toString())) {
                roleMenuListIdList.remove(roleMenuListIdList.indexOf(roleMenu.getMenuId().toString()));
            }
        }
        if (!roleMenuDeleteList.isEmpty())
            roleMenuRepository.deleteAll(roleMenuDeleteList);
        for (Integer i = 0; i < roleMenuListIdList.size(); i++) {
            RoleMenuEntity roleMenuEntity = RoleMenuEntity.builder().roleId(resultRoleEntity.getId())
                    .menuId(Long.valueOf(roleMenuListIdList.get(i))).build();
            roleMenuRepository.save(roleMenuEntity);
        }
    }

    public void deleteAfterUpdateRoleApiList(RoleDto roleDto, RoleEntity resultRoleEntity) {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setRowSize(1000000);
        String[] roleApiListIdString = roleDto.getRoleApiListIdString().split(",");
        RoleApiParamDto roleApiParamDto = new RoleApiParamDto();
        roleApiParamDto.setRoleId(roleDto.getId());
        List<String> roleApiListIdList = new ArrayList<>();
        if (roleDto.getRoleApiListIdString() != null && !roleDto.getRoleApiListIdString().equals(""))
            Collections.addAll(roleApiListIdList, roleApiListIdString);
        List<RoleApiEntity> roleApiList = roleApiMapper.selectRoleApiList(roleApiParamDto, pageRequest);
        List<RoleApiEntity> roleApiDeleteList = new ArrayList<>();
        for (RoleApiEntity roleApi : roleApiList) {
            if (!roleApiListIdList.contains(roleApi.getApiId().toString())) {
                roleApiDeleteList.add(roleApi);
            }
            if (roleApiListIdList.contains(roleApi.getApiId().toString())) {
                roleApiListIdList.remove(roleApiListIdList.indexOf(roleApi.getApiId().toString()));
            }
        }
        if (!roleApiDeleteList.isEmpty())
            roleApiRepository.deleteAll(roleApiDeleteList);
        for (Integer i = 0; i < roleApiListIdList.size(); i++) {
            RoleApiEntity roleApiEntity = RoleApiEntity.builder().roleId(resultRoleEntity.getId())
                    .apiId(Long.valueOf(roleApiListIdList.get(i))).build();
            roleApiRepository.save(roleApiEntity);
        }
    }
}
