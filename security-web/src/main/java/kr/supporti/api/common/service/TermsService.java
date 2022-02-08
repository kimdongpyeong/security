package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.TermsParamDto;
import kr.supporti.api.common.entity.TermsEntity;
import kr.supporti.api.common.mapper.TermsMapper;
import kr.supporti.api.common.repository.TermsRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class TermsService {

    @Autowired
    private TermsRepository termsRepository;

    @Autowired
    private TermsMapper termsMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<TermsEntity> getTermsList(@Valid TermsParamDto termsParamDto, PageRequest pageRequest) {
        Integer termsListCount = termsMapper.selectTermsListCount(termsParamDto);
        List<TermsEntity> termsList = termsMapper.selectTermsList(termsParamDto, pageRequest);
        PageResponse<TermsEntity> pageResponse = new PageResponse<>(pageRequest, termsListCount);
        pageResponse.setItems(termsList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public TermsEntity getTerms(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return termsMapper.selectTerms(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public TermsEntity createTerms(@Valid @NotNull(groups = { CreateValidationGroup.class }) TermsEntity termsEntity) {
        return termsRepository.save(termsEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public TermsEntity modifyTerms(@Valid @NotNull(groups = { ModifyValidationGroup.class }) TermsEntity termsEntity) {
        return termsRepository.save(termsEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeTermsList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        for (int i = 0; i < idList.size(); i++) {
            termsRepository.deleteById(idList.get(i));
        }
    }
}