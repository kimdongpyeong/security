package kr.supporti.api.common.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ClassRoomUserParamDto;
import kr.supporti.api.common.entity.ClassRoomUserEntity;
import kr.supporti.api.common.entity.CodeEntity;
import kr.supporti.api.common.mapper.ClassRoomUserMapper;
import kr.supporti.api.common.repository.ClassRoomUserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ClassRoomUserService {

    @Autowired
    private ClassRoomUserRepository classRoomUserRepository;

    @Autowired
    private ClassRoomUserMapper classRoomUserMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ClassRoomUserEntity> getClassRoomUserList(@Valid ClassRoomUserParamDto classRoomUserParamDto,
            PageRequest pageRequest) {
        Integer classRoomUserListCount = classRoomUserMapper.selectClassRoomUserListCount(classRoomUserParamDto);
        List<ClassRoomUserEntity> classRoomUserList = classRoomUserMapper.selectClassRoomUserList(classRoomUserParamDto,
                pageRequest);
        PageResponse<ClassRoomUserEntity> pageResponse = new PageResponse<>(pageRequest, classRoomUserListCount);
        pageResponse.setItems(classRoomUserList);
        return pageResponse;
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ClassRoomUserEntity getClassRoomUser(@Valid ClassRoomUserParamDto classRoomUserParamDto,
            PageRequest pageRequest) {
        return classRoomUserMapper.selectClassRoomUser(classRoomUserParamDto);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public ClassRoomUserEntity createClassRoomUser(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) ClassRoomUserEntity classRoomUserEntity) {
        return classRoomUserRepository.save(classRoomUserEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeClassRoomUser(
            @Valid @NotNull(groups = { RemoveValidationGroup.class }) ClassRoomUserEntity classRoomUserEntity) {
        classRoomUserRepository.delete(classRoomUserEntity);
    }

}