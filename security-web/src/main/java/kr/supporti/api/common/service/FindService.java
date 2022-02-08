package kr.supporti.api.common.service;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.FindMapper;
import kr.supporti.api.common.repository.FindRepository;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class FindService {

    @Autowired
    private FindMapper findMapper;

    @Autowired
    private FindRepository findRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserEntity findUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) String username) {
        return findMapper.selectUser(username);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyPassword(Long id, String newPassword) throws ServletException {
        UserEntity userEntity = findMapper.selectFindUser(id);
        userEntity.setPassword(passwordEncoder.encode(newPassword));
        findRepository.save(userEntity);
    }

}