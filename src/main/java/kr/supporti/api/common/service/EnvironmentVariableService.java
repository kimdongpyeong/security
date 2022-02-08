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

import kr.supporti.api.common.dto.EnvironmentVariableParamDto;
import kr.supporti.api.common.entity.EnvironmentVariableEntity;
import kr.supporti.api.common.mapper.EnvironmentVariableMapper;
import kr.supporti.api.common.repository.EnvironmentVariableRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Service
public class EnvironmentVariableService {

    @Autowired
    private EnvironmentVariableMapper environmentVariableMapper;

    @Autowired
    private EnvironmentVariableRepository environmentVariableRepository;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<EnvironmentVariableEntity> getEnvironmentVariableList(@Valid EnvironmentVariableParamDto environmentVariableParamDto, PageRequest pageRequest) {
        Integer environmentVariableListCount = environmentVariableMapper.selectEnvironmentVariableListCount(environmentVariableParamDto);
        List<EnvironmentVariableEntity> environmentVariableList = environmentVariableMapper.selectEnvironmentVariableList(environmentVariableParamDto, pageRequest);
        PageResponse<EnvironmentVariableEntity> pageResponse = new PageResponse<>(pageRequest, environmentVariableListCount);
        pageResponse.setItems(environmentVariableList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public EnvironmentVariableEntity getEnvironmentVariable(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return environmentVariableMapper.selectEnvironmentVariable(id);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public EnvironmentVariableEntity getEnvironmentVariableKey(@Valid @NotNull(groups = { ReadValidationGroup.class }) String key) {
        return environmentVariableMapper.selectEnvironmentVariableKey(key);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public EnvironmentVariableEntity modifyEnvironmentVariable(@Valid @NotNull(groups = { ModifyValidationGroup.class }) EnvironmentVariableEntity environmentVariableEntity) {
        return environmentVariableRepository.save(environmentVariableEntity);
    }

}
