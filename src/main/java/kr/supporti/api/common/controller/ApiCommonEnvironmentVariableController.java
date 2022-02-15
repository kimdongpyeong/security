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

import kr.supporti.api.common.dto.EnvironmentVariableDto;
import kr.supporti.api.common.dto.EnvironmentVariableParamDto;
import kr.supporti.api.common.entity.EnvironmentVariableEntity;
import kr.supporti.api.common.service.EnvironmentVariableService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/environment")
public class ApiCommonEnvironmentVariableController {

    @Autowired
    private EnvironmentVariableService environmentVariableService;

    @GetMapping(path = "")
    public PageResponse<EnvironmentVariableEntity> getEnvironmentVariableList(@ModelAttribute EnvironmentVariableParamDto environmentVariableParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return environmentVariableService.getEnvironmentVariableList(environmentVariableParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public EnvironmentVariableEntity getEnvironmentVariable(@PathVariable(name = "id") Long id) {
        return environmentVariableService.getEnvironmentVariable(id);
    }

    @GetMapping(path = "/key/{key}")
    public EnvironmentVariableEntity getEnvironmentVariableKey(@PathVariable(name = "key") String key) {
        return environmentVariableService.getEnvironmentVariableKey(key);
    }

    @PutMapping(path = "")
    public EnvironmentVariableEntity modifyEnvironment(@RequestBody EnvironmentVariableDto environmentVariableDto) {
        EnvironmentVariableEntity environmentVariableEntity = EnvironmentVariableEntity.builder()
                .id(environmentVariableDto.getId())
                .key(environmentVariableDto.getKey())
                .desc(environmentVariableDto.getDesc())
                .value(environmentVariableDto.getValue())
                .build();
        return environmentVariableService.modifyEnvironment(environmentVariableEntity);
    }

}
