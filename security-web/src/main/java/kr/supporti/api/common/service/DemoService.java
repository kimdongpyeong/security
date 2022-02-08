package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.DemoDto;
import kr.supporti.api.common.dto.DemoParamDto;
import kr.supporti.api.common.entity.DemoEntity;
import kr.supporti.api.common.mapper.DemoMapper;
import kr.supporti.api.common.repository.DemoRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class DemoService {

    @Autowired
    private DemoRepository demoRepository;

    @Autowired
    private DemoMapper demoMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<DemoEntity> getDemoList(@Valid DemoParamDto demoParamDto,
            PageRequest pageRequest) {
        Integer demoListCount = demoMapper.selectDemoListCount(demoParamDto);
        List<DemoEntity> demoList = demoMapper.selectDemoList(demoParamDto,pageRequest);
        PageResponse<DemoEntity> pageResponse = new PageResponse<>(pageRequest, demoListCount);
        pageResponse.setItems(demoList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public DemoEntity getDemo(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return demoMapper.selectDemo(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public DemoEntity createDemo(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) DemoDto demoDto) {

        if (demoDto.getExposureYn().equals("Y")) {
            demoMapper.updateDemoExpoureN();
        }

        DemoEntity demoEntity = DemoEntity.builder()
                .title(demoDto.getTitle())
                .youtubeLink(demoDto.getYoutubeLink())
                .exposureYn(demoDto.getExposureYn())
                .lastModifiedDate(demoDto.getLastModifiedDate())
                .build();

        return demoRepository.save(demoEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public DemoEntity modifyDemo(
            @Valid @NotNull(groups = { ModifyValidationGroup.class }) DemoDto demoDto) {

        if (demoDto.getExposureYn().equals("Y")) {
            demoMapper.updateDemoExpoureN();
        }

        DemoEntity demoEntity = DemoEntity.builder()
                .id(demoDto.getId())
                .title(demoDto.getTitle())
                .youtubeLink(demoDto.getYoutubeLink())
                .exposureYn(demoDto.getExposureYn())
                .lastModifiedDate(demoDto.getLastModifiedDate())
                .build();

        return demoRepository.save(demoEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeDemo(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        demoRepository.delete(DemoEntity.builder().id(id).build());
    }

}