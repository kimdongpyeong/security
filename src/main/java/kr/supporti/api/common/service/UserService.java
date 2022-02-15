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

import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.dto.UserLoginHistoryDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserLoginHistoryEntity;
import kr.supporti.api.common.mapper.UserLoginHistoryMapper;
import kr.supporti.api.common.mapper.UserMapper;
import kr.supporti.api.common.repository.UserLoginHistoryRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private UserLoginHistoryRepository userLoginHistoryRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    PasswordEncoder passwordEncoder;
    
    @Autowired
    UserLoginHistoryMapper userLoginHistoryMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<UserEntity> getUserList(@Valid UserParamDto userParamDto, PageRequest pageRequest) {
        Integer userListCount = userMapper.selectUserListCount(userParamDto);
        List<UserEntity> userList = userMapper.selectUserList(userParamDto, pageRequest);
        PageResponse<UserEntity> pageResponse = new PageResponse<>(pageRequest, userListCount);
        pageResponse.setItems(userList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserEntity getUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return userMapper.selectUser(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public List<UserEntity> createUserList(
            @Valid @NotEmpty(groups = { CreateValidationGroup.class }) List<@NotNull(groups = {
                    CreateValidationGroup.class }) UserEntity> userList) {
        return userRepository.saveAll(userList);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public UserEntity createUser(@Valid @NotNull(groups = { CreateValidationGroup.class }) UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyUserList(@Valid @NotNull(groups = { ModifyValidationGroup.class }) UserParamDto userParamDto) {
        List<UserParamDto> userList = userParamDto.getUserList();
        
        if(userParamDto.getLecturerApprovalYn().equals("Y")) {
            for (int i = 0; i < userList.size(); i++) {
                    System.out.println(userList.get(i));
                    UserEntity userEntity = UserEntity.builder()
                            .id(userList.get(i).getId())
                            .lecturerApprovalYn(userParamDto.getLecturerApprovalYn())
                            .build();
                    userMapper.approvalLecturer(userEntity);
            }
        } else if(userParamDto.getLecturerApprovalYn().equals("R")) {
            for (int i = 0; i < userList.size(); i++) {
                System.out.println(userList.get(i));
                UserEntity userEntity = UserEntity.builder()
                        .id(userList.get(i).getId())
                        .username(userList.get(i).getUsername() + "_leave" + userList.get(i).getId())
                        .status("D")
                        .lecturerApprovalYn(userParamDto.getLecturerApprovalYn())
                        .build();
                userMapper.refuseLecturer(userEntity);
            }
        }
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyUser(@Valid @NotNull(groups = { ModifyValidationGroup.class }) Long id, @Valid @NotNull(groups = { ModifyValidationGroup.class }) UserDto userDto) {
        return userMapper.updateUserInfo(id, userDto);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeUserList(@Valid @NotEmpty(groups = {
            RemoveValidationGroup.class }) List<@NotNull(groups = { RemoveValidationGroup.class }) Long> idList) {
        List<UserEntity> userList = new ArrayList<>();
        for (int i = 0; i < idList.size(); i++) {
            Long id = idList.get(i);
            userList.add(UserEntity.builder().id(id).build());
        }
        userRepository.deleteAll(userList);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeUser(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        userRepository.delete(UserEntity.builder().id(id).build());
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserEntity getUserByUsername(@Valid @NotNull(groups = { ReadValidationGroup.class }) String username) {
        return userMapper.selectUserByUsername(username);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Map<String, Object> modifyPassword(Long id, String oldPassword, String newPassword) throws ServletException {
        UserEntity userEntity = userMapper.selectUser(id);
        String orignPassword = userEntity.getPassword();
        Map<String, Object> map = new WeakHashMap<>();

        if (passwordEncoder.matches(oldPassword, orignPassword)) {
            if (oldPassword.equals(newPassword)) {
                map.put("result", false);
                map.put("resultMsg", "같은 비밀번호로는 변경이 불가능합니다.");
            } else {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(userEntity);
                map.put("result", true);
                map.put("resultMsg", "변경되었습니다");
            }
        } else {
            map.put("result", false);
            map.put("resultMsg", "현재 비밀번호가 일치하지않습니다.");
        }
        return map;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public Integer getUserExists(@Valid UserParamDto userParamDto) {
        return userMapper.selectUserExistsCount(userParamDto);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public Integer getSmsUserExists(@Valid UserParamDto userParamDto) {
        return userMapper.selectSmsUserListCount(userParamDto);
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public void createUserIp(@Valid @NotNull(groups = { CreateValidationGroup.class }) UserLoginHistoryEntity userLoginHistoryEntity) {
        userLoginHistoryMapper.createUserIp(userLoginHistoryEntity);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<UserLoginHistoryEntity> getUserIpList(@Valid UserLoginHistoryDto userLoginHistoryDto, PageRequest pageRequest) {
        Integer userIpListCount = userLoginHistoryMapper.selectUserIpListCount(userLoginHistoryDto);
        List<UserLoginHistoryEntity> userIpList = userLoginHistoryMapper.selectUserIpList(userLoginHistoryDto, pageRequest);
        PageResponse<UserLoginHistoryEntity> pageResponse = new PageResponse<>(pageRequest, userIpListCount);
        pageResponse.setItems(userIpList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserEntity getUserIp(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return userMapper.selectUser(id);
    }
}