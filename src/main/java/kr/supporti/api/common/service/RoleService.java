package kr.supporti.api.common.service;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.RoleApiParamDto;
import kr.supporti.api.common.dto.RoleMenuParamDto;
import kr.supporti.api.common.dto.RoleParamDto;
import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.api.common.mapper.RoleApiMapper;
import kr.supporti.api.common.mapper.RoleMapper;
import kr.supporti.api.common.mapper.RoleMenuMapper;
import kr.supporti.api.common.repository.RoleApiRepository;
import kr.supporti.api.common.repository.RoleMenuRepository;
import kr.supporti.api.common.repository.RoleRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private RoleApiRepository roleApiRepository;

    @Autowired
    private RoleApiMapper roleApiMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<RoleEntity> getRoleList(@Valid RoleParamDto roleParamDto, PageRequest pageRequest) {
        Integer roleListCount = roleMapper.selectRoleListCount(roleParamDto);
        List<RoleEntity> roleList = roleMapper.selectRoleList(roleParamDto, pageRequest);
        PageResponse<RoleEntity> pageResponse = new PageResponse<>(pageRequest, roleListCount);
        pageResponse.setItems(roleList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public RoleEntity getRole(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return roleMapper.selectRole(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<RoleEntity> createRoleList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) RoleEntity> roleList) {
        return roleRepository.saveAll(roleList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public RoleEntity createRole(@Valid @NotNull(groups = { CreateValidationGroup.class }) RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<RoleEntity> modifyRoleList(
            @Valid @NotEmpty(groups = { ModifyValidationGroup.class }) List<@NotNull(groups = {
                    ModifyValidationGroup.class }) RoleEntity> roleList) {
        return roleRepository.saveAll(roleList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public RoleEntity modifyRole(@Valid @NotNull(groups = { ModifyValidationGroup.class }) RoleEntity roleEntity) {
        return roleRepository.save(roleEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        List<RoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            roleList.add(RoleEntity.builder().id(id).build());
        }
        roleRepository.deleteAll(roleList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRole(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        roleRepository.delete(RoleEntity.builder().id(id).build());
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleAllDependencyList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        List<RoleEntity> roleList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(1);
            pageRequest.setRowSize(1000000);
            RoleMenuParamDto roleMenuParamDto = new RoleMenuParamDto();
            roleMenuParamDto.setRoleId(idList.get(i).toString());
            List<RoleMenuEntity> roleMenuList = roleMenuMapper.selectRoleMenuList(roleMenuParamDto, pageRequest);
            if (!roleMenuList.isEmpty())
                roleMenuRepository.deleteAll(roleMenuList);
            RoleApiParamDto roleApiParamDto = new RoleApiParamDto();
            roleApiParamDto.setRoleId(idList.get(i));
            List<RoleApiEntity> roleApiList = roleApiMapper.selectRoleApiList(roleApiParamDto, pageRequest);
            if (!roleApiList.isEmpty())
                roleApiRepository.deleteAll(roleApiList);
            Long id = idList.get(i);
            roleList.add(RoleEntity.builder().id(id).build());
        }
        roleRepository.deleteAll(roleList);
    }
}