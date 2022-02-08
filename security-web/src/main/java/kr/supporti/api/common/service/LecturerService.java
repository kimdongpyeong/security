package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.LecturerMapper;
import kr.supporti.api.common.repository.LecturerRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Service
public class LecturerService {

    @Autowired
    private LecturerMapper lecturerMapper;

    @Autowired
    private LecturerRepository lecturerRepository;

    @Autowired
    ProfileFileService profileFileService;

//    @Validated (value = {ReadValidationGroup.class})
//    @Transactional (readOnly = true)
//	public UserEntity getLectuereUser() {
//	    return lecturerMapper.selectLecturerUser();
//	}

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<UserEntity> getLecturerList(@Valid UserParamDto userParamDto, PageRequest pageRequest) {
        Integer lecturerListCount = lecturerMapper.selectLecturerListCount(userParamDto);
        List<UserEntity> lecturerList = lecturerMapper.selectLecturerList(userParamDto, pageRequest);
        PageResponse<UserEntity> pageResponse = new PageResponse<>(pageRequest, lecturerListCount);
        pageResponse.setItems(lecturerList);
        return pageResponse;
    }

}
