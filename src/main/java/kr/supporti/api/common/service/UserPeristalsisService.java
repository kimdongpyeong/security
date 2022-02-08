package kr.supporti.api.common.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserPeristalsisEntity;
import kr.supporti.api.common.mapper.UserMapper;
import kr.supporti.api.common.mapper.UserPeristalsisMapper;
import kr.supporti.api.common.repository.UserPeristalsisRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class UserPeristalsisService {

    @Autowired
    private UserPeristalsisRepository userPeristalsisRepository;

    @Autowired
    private UserPeristalsisMapper userPeristalsisMapper;
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserPeristalsisEntity getUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) String userId) {
        return userPeristalsisMapper.selectUser(userId);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public UserPeristalsisEntity createUser(@Valid @NotNull(groups = { CreateValidationGroup.class }) UserPeristalsisEntity userPeristalsisEntity) {
        return userPeristalsisRepository.save(userPeristalsisEntity);
    }

}