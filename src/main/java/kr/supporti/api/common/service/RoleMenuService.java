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

import kr.supporti.api.common.dto.RoleMenuParamDto;
import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.api.common.entity.id.RoleMenuEntityId;
import kr.supporti.api.common.mapper.RoleMenuMapper;
import kr.supporti.api.common.repository.RoleMenuRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class RoleMenuService {

    @Autowired
    private RoleMenuRepository roleMenuRepository;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<RoleMenuEntity> getRoleMenuList(@Valid RoleMenuParamDto roleMenuParamDto,
            PageRequest pageRequest) {
        Integer roleMenuListCount = roleMenuMapper.selectRoleMenuListCount(roleMenuParamDto);
        List<RoleMenuEntity> roleMenuList = roleMenuMapper.selectRoleMenuList(roleMenuParamDto, pageRequest);
        PageResponse<RoleMenuEntity> pageResponse = new PageResponse<>(pageRequest, roleMenuListCount);
        pageResponse.setItems(roleMenuList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public RoleMenuEntity getRoleMenu(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long roleId,
            @Valid @NotNull(groups = { ReadValidationGroup.class }) Long menuId) {
        return roleMenuMapper.selectRoleMenu(roleId, menuId);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<RoleMenuEntity> createRoleMenuList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) RoleMenuEntity> roleMenuList) {
        return roleMenuRepository.saveAll(roleMenuList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public RoleMenuEntity createRoleMenu(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) RoleMenuEntity roleMenuEntity) {
        return roleMenuRepository.save(roleMenuEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<RoleMenuEntity> modifyRoleMenuList(
            @Valid @NotEmpty(groups = { ModifyValidationGroup.class }) List<@NotNull(groups = {
                    ModifyValidationGroup.class }) RoleMenuEntity> roleMenuList) {
        return roleMenuRepository.saveAll(roleMenuList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public RoleMenuEntity modifyRoleMenu(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) RoleMenuEntity roleMenuEntity) {
        return roleMenuRepository.save(roleMenuEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleMenuList(@Valid @NotEmpty(groups = { RemoveValidationGroup.class }) List<@NotNull(groups = {
            RemoveValidationGroup.class }) RoleMenuEntityId> roleMenuEntityIdList) {
        List<RoleMenuEntity> roleMenuList = new ArrayList<>();
        for (int i = 0; i < roleMenuEntityIdList.size(); i++) {
            RoleMenuEntityId roleMenuEntityId = roleMenuEntityIdList.get(i);
            Long roleId = roleMenuEntityId.getRoleId();
            Long menuId = roleMenuEntityId.getMenuId();
            roleMenuList.add(RoleMenuEntity.builder().roleId(roleId).menuId(menuId).build());
        }
        roleMenuRepository.deleteAll(roleMenuList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleMenu(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long roleId,
            @Valid @NotNull(groups = { RemoveValidationGroup.class }) Long menuId) {
        roleMenuRepository.delete(RoleMenuEntity.builder().roleId(roleId).menuId(menuId).build());
    }

}