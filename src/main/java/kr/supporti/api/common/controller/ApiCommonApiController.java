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

import kr.supporti.api.common.dto.ApiDto;
import kr.supporti.api.common.dto.ApiParamDto;
import kr.supporti.api.common.entity.ApiEntity;
import kr.supporti.api.common.service.ApiService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/apis")
public class ApiCommonApiController {

    @Autowired
    private ApiService apiService;

    @GetMapping(path = "")
    public PageResponse<ApiEntity> getApiList(@ModelAttribute ApiParamDto apiParamDto,
            @ModelAttribute PageRequest pageRequest) {
        return apiService.getApiList(apiParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public ApiEntity getApi(@PathVariable(name = "id") Long id) {
        return apiService.getApi(id);
    }

    @PostMapping(path = "", params = { "bulk" })
    public List<ApiEntity> createApiList(@RequestBody List<ApiDto> apiDtoList) {
        List<ApiEntity> apiList = new ArrayList<>();
        for (int i = 0; i < apiDtoList.size(); i++) {
            ApiDto apiDto = apiDtoList.get(i);
            ApiEntity apiEntity = ApiEntity.builder().url(apiDto.getUrl()).method(apiDto.getMethod())
                    .name(apiDto.getName()).description(apiDto.getDescription()).build();
            apiList.add(apiEntity);
        }
        return apiService.createApiList(apiList);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public ApiEntity createApi(@RequestBody ApiDto apiDto) {
        ApiEntity apiEntity = ApiEntity.builder().url(apiDto.getUrl()).method(apiDto.getMethod()).name(apiDto.getName())
                .description(apiDto.getDescription()).build();
        return apiService.createApi(apiEntity);
    }

    @PutMapping(path = "")
    public List<ApiEntity> modifyApiList(@RequestBody List<ApiDto> apiDtoList) {
        List<ApiEntity> apiList = new ArrayList<>();
        for (int i = 0; i < apiDtoList.size(); i++) {
            ApiDto apiDto = apiDtoList.get(i);
            ApiEntity apiEntity = ApiEntity.builder().id(apiDto.getId()).url(apiDto.getUrl()).method(apiDto.getMethod())
                    .name(apiDto.getName()).description(apiDto.getDescription()).build();
            apiList.add(apiEntity);
        }
        return apiService.modifyApiList(apiList);
    }

    @PutMapping(path = "{id}")
    public ApiEntity modifyApi(@PathVariable(name = "id") Long id, @RequestBody ApiDto apiDto) {
        ApiEntity apiEntity = ApiEntity.builder().id(id).url(apiDto.getUrl()).method(apiDto.getMethod())
                .name(apiDto.getName()).description(apiDto.getDescription()).build();
        return apiService.modifyApi(apiEntity);
    }

    @DeleteMapping(path = "")
    public void removeApiList(@RequestBody List<Long> idList) {
        apiService.removeApiList(idList);
    }

    @DeleteMapping(path = "{id}")
    public void removeApi(@PathVariable(name = "id") Long id) {
        apiService.removeApi(id);
    }
}