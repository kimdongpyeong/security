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
import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.api.common.entity.id.RoleApiEntityId;
import kr.supporti.api.common.mapper.RoleApiMapper;
import kr.supporti.api.common.repository.RoleApiRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class RoleApiService {

    @Autowired
    private RoleApiRepository roleApiRepository;

    @Autowired
    private RoleApiMapper roleApiMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<RoleApiEntity> getRoleApiList(@Valid RoleApiParamDto roleApiParamDto, PageRequest pageRequest) {
        Integer roleApiListCount = roleApiMapper.selectRoleApiListCount(roleApiParamDto);
        List<RoleApiEntity> roleApiList = roleApiMapper.selectRoleApiList(roleApiParamDto, pageRequest);
        PageResponse<RoleApiEntity> pageResponse = new PageResponse<>(pageRequest, roleApiListCount);
        pageResponse.setItems(roleApiList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public RoleApiEntity getRoleApi(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long roleId,
            @Valid @NotNull(groups = { ReadValidationGroup.class }) Long apiId) {
        return roleApiMapper.selectRoleApi(roleId, apiId);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<RoleApiEntity> createRoleApiList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) RoleApiEntity> roleApiList) {
        return roleApiRepository.saveAll(roleApiList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public RoleApiEntity createRoleApi(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) RoleApiEntity roleApiEntity) {
        return roleApiRepository.save(roleApiEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<RoleApiEntity> modifyRoleApiList(
            @Valid @NotEmpty(groups = { ModifyValidationGroup.class }) List<@NotNull(groups = {
                    ModifyValidationGroup.class }) RoleApiEntity> roleApiList) {
        return roleApiRepository.saveAll(roleApiList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public RoleApiEntity modifyRoleApi(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) RoleApiEntity roleApiEntity) {
        return roleApiRepository.save(roleApiEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleApiList(@Valid @NotEmpty(groups = { RemoveValidationGroup.class }) List<@NotNull(groups = {
            RemoveValidationGroup.class }) RoleApiEntityId> roleApiEntityIdList) {
        List<RoleApiEntity> roleApiList = new ArrayList<>();
        for (int i = 0; i < roleApiEntityIdList.size(); i++) {
            RoleApiEntityId roleApiEntityId = roleApiEntityIdList.get(i);
            Long roleId = roleApiEntityId.getRoleId();
            Long apiId = roleApiEntityId.getApiId();
            roleApiList.add(RoleApiEntity.builder().roleId(roleId).apiId(apiId).build());
        }
        roleApiRepository.deleteAll(roleApiList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeRoleApi(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long roleId,
            @Valid @NotNull(groups = { RemoveValidationGroup.class }) Long apiId) {
        roleApiRepository.delete(RoleApiEntity.builder().roleId(roleId).apiId(apiId).build());
    }

}