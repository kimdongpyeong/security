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

import kr.supporti.api.common.dto.RoleUserParamDto;
import kr.supporti.api.common.entity.RoleUserEntity;
import kr.supporti.api.common.entity.id.RoleUserEntityId;
import kr.supporti.api.common.mapper.RoleUserMapper;
import kr.supporti.api.common.repository.RoleUserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class RoleUserService {

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private RoleUserMapper roleUserMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<RoleUserEntity> getRoleUserList(@Valid RoleUserParamDto roleUserParamDto,
            PageRequest pageRequest) {
        Integer roleUserListCount = roleUserMapper.selectRoleUserListCount(roleUserParamDto);
        List<RoleUserEntity> roleUserList = roleUserMapper.selectRoleUserList(roleUserParamDto, pageRequest);
        PageResponse<RoleUserEntity> pageResponse = new PageResponse<>(pageRequest, roleUserListCount);
        pageResponse.setItems(roleUserList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public RoleUserEntity getRoleUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long userId) {
        return roleUserMapper.selectRoleUser(userId);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<RoleUserEntity> createRoleUserList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) RoleUserEntity> roleUserList) {
        return roleUserRepository.saveAll(roleUserList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public RoleUserEntity createRoleUser(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) RoleUserEntity roleUserEntity) {
        return roleUserRepository.save(roleUserEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<RoleUserEntity> modifyRoleUserList(
            @Valid @NotEmpty(groups = { ModifyValidationGroup.class }) List<@NotNull(groups = {
                    ModifyValidationGroup.class }) RoleUserEntity> roleUserList) {
        return roleUserRepository.saveAll(roleUserList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public RoleUserEntity modifyRoleUser(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) RoleUserEntity roleUserEntity) {
        return roleUserRepository.save(roleUserEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleUserList(@Valid @NotEmpty(groups = { RemoveValidationGroup.class }) List<@NotNull(groups = {
            RemoveValidationGroup.class }) RoleUserEntityId> roleUserEntityIdList) {
        List<RoleUserEntity> roleUserList = new ArrayList<>();
        for (int i = 0; i < roleUserEntityIdList.size(); i++) {
            RoleUserEntityId roleUserEntityId = roleUserEntityIdList.get(i);
            Long roleId = roleUserEntityId.getRoleId();
            Long userId = roleUserEntityId.getUserId();
            roleUserList.add(RoleUserEntity.builder().roleId(roleId).userId(userId).build());
        }
        roleUserRepository.deleteAll(roleUserList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleUser(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long roleId,
            @Valid @NotNull(groups = { RemoveValidationGroup.class }) Long userId) {
        roleUserRepository.delete(RoleUserEntity.builder().roleId(roleId).userId(userId).build());
    }
}