package kr.supporti.api.common.service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.entity.ProfileFileEntity;
import kr.supporti.api.common.entity.CategoryEntity;
import kr.supporti.api.common.mapper.ProfileFileMapper;
import kr.supporti.api.common.repository.ProfileFileRepository;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ProfileFileService {

    @Autowired
    private ProfileFileRepository profilefileRepository;

    @Autowired
    private ProfileFileMapper profilefileMapper;

    public ProfileFileEntity createProfileFile(ProfileFileEntity profileFileEntity) {
        return profilefileRepository.save(profileFileEntity);
    }

    public ProfileFileEntity modifyProfileFile(ProfileFileEntity profileFileEntity) {
        return profilefileRepository.save(profileFileEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeProfileFile(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        profilefileRepository.delete(ProfileFileEntity.builder().id(id).build());
    }

}