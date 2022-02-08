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

import kr.supporti.api.common.dto.ApiParamDto;
import kr.supporti.api.common.entity.ApiEntity;
import kr.supporti.api.common.mapper.ApiMapper;
import kr.supporti.api.common.repository.ApiRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ApiService {

    @Autowired
    private ApiRepository apiRepository;

    @Autowired
    private ApiMapper apiMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ApiEntity> getApiList(@Valid ApiParamDto apiParamDto, PageRequest pageRequest) {
        Integer apiListCount = apiMapper.selectApiListCount(apiParamDto);
        List<ApiEntity> apiList = apiMapper.selectApiList(apiParamDto, pageRequest);
        PageResponse<ApiEntity> pageResponse = new PageResponse<>(pageRequest, apiListCount);
        pageResponse.setItems(apiList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ApiEntity getApi(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return apiMapper.selectApi(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<ApiEntity> createApiList(@Valid @NotEmpty(groups = {
            CreateValidationGroup.class }) List<@NotNull(groups = { CreateValidationGroup.class }) ApiEntity> apiList) {
        return apiRepository.saveAll(apiList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public ApiEntity createApi(@Valid @NotNull(groups = { CreateValidationGroup.class }) ApiEntity apiEntity) {
        return apiRepository.save(apiEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public List<ApiEntity> modifyApiList(@Valid @NotEmpty(groups = {
            ModifyValidationGroup.class }) List<@NotNull(groups = { ModifyValidationGroup.class }) ApiEntity> apiList) {
        return apiRepository.saveAll(apiList);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public ApiEntity modifyApi(@Valid @NotNull(groups = { ModifyValidationGroup.class }) ApiEntity apiEntity) {
        return apiRepository.save(apiEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeApiList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        List<ApiEntity> apiList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            apiList.add(ApiEntity.builder().id(id).build());
        }
        apiRepository.deleteAll(apiList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeApi(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        apiRepository.delete(ApiEntity.builder().id(id).build());
    }

}