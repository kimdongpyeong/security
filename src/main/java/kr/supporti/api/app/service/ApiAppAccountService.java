package kr.supporti.api.app.service;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.common.dto.RoleUserParamDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.ProfileFileEntity;
import kr.supporti.api.common.entity.RoleUserEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserTermsEntity;
import kr.supporti.api.common.repository.InviteStudentRepository;
import kr.supporti.api.common.repository.RoleUserRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.api.common.repository.UserTermsRepository;
import kr.supporti.api.common.service.ProfileFileService;
import kr.supporti.api.common.service.RoleUserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Service
public class ApiAppAccountService {

    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.profile.path}")
    private String profilePath;

    @Autowired
    RoleUserService roleUserService;

    @Autowired
    private RoleUserRepository roleUserRepository;

    @Autowired
    private UserTermsRepository userTermsRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InviteStudentRepository inviteStudentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ProfileFileService profileFileService;

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public AccountDto createAccount(@Valid @NotNull(groups = { CreateValidationGroup.class }) AccountDto accountDto) {
        AccountDto result = new AccountDto();

        UserEntity userEntity = null;

        if (accountDto.getUserDto() != null) {
            userEntity = UserEntity.builder()
                    .name(accountDto.getUserDto().getName())
                    .username(accountDto.getUserDto().getUsername())
                    .password("12345")
                    .lastModifiedPasswordDate(LocalDate.now().plusMonths(3L))
                    .phoneNum(accountDto.getUserDto().getPhoneNum())
                    .signUpWay("N")
                    .status("T")
                    .build();
        } else {
            return null;
        }

        userEntity.setPassword(passwordEncoder.encode((userEntity.getPassword())));
        Long userId = userRepository.save(userEntity).getId();
        result.setUserId(userId);
        result.setRoleId(accountDto.getRoleId());
        RoleUserEntity roleUserEntity = new RoleUserEntity(userId, accountDto.getRoleId(), null, null);
        roleUserRepository.save(roleUserEntity);
        return result;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyAccount(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {

        UserEntity userEntity = UserEntity.builder()
                .id(accountDto.getUserDto().getId())
                .name(accountDto.getUserDto().getName())
                .username((accountDto.getUserDto().getStatus().equals("D"))
                        ? accountDto.getUserDto().getUsername() + "_leave" + accountDto.getUserDto().getId()
                        : accountDto.getUserDto().getUsername())
                .password(accountDto.getUserDto().getPassword())
                .status(accountDto.getUserDto().getStatus())
                .profileId(accountDto.getUserDto().getProfileId())
                .phoneNum(accountDto.getUserDto().getPhoneNum())
                .signUpWay(accountDto.getUserDto().getSignUpWay()).build();

        Long userId = userRepository.save(userEntity).getId();

        RoleUserParamDto roleUserParamDto = new RoleUserParamDto();
        roleUserParamDto.setUserId(userId);
        roleUserParamDto.setRoleId(null);

        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(1);
        pageRequest.setRowSize(1000000);

        PageResponse<RoleUserEntity> pageResponse = roleUserService.getRoleUserList(roleUserParamDto, pageRequest);
        List<RoleUserEntity> resultList = pageResponse.getItems();
        roleUserRepository.deleteByUserId(resultList.get(0).getUserId());
        RoleUserEntity roleUserEntity = new RoleUserEntity(userId, accountDto.getRoleId(), null, null);
        roleUserRepository.save(roleUserEntity);

    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public void userSignUp(@Valid @NotNull(groups = { CreateValidationGroup.class }) AccountDto accountDto) {

        Long fileId = null;

        if (accountDto.getProfileFile() != null) {
            File profileDir = new File(filePath + File.separator + profilePath);
            if (!profileDir.exists()) {
                profileDir.mkdirs();
            }

            String profileOrgFilename = accountDto.getProfileFile().getOriginalFilename();
            String profileExtension = StringUtils.getFilenameExtension(profileOrgFilename);
            String profileSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "." + profileExtension;
            String profileFileFullPath = filePath + File.separator + profilePath + File.separator + profileSaveFilename;

            try {
                accountDto.getProfileFile().transferTo(new File(profileFileFullPath));

                ProfileFileEntity profileFileEntity = ProfileFileEntity.builder()
                        .originFileNm(profileOrgFilename)
                        .saveFileNm(profileSaveFilename)
                        .fileSize(String.valueOf(accountDto.getProfileFile().getSize()))
                        .build();
                profileFileEntity = profileFileService.createProfileFile(profileFileEntity);
                fileId = profileFileEntity.getId();
            } catch (Exception e) {

            }
        }

        UserEntity userEntity = UserEntity.builder()
                .name(accountDto.getName())
                .username(accountDto.getUsername())
                .password(accountDto.getPassword())
                .phoneNum((accountDto.getPhoneNum() != null)? accountDto.getPhoneNum().replaceAll("-", "") : null)
                .createdDate(accountDto.getCreatedDate())
                .status(accountDto.getStatus())
                .signUpWay(accountDto.getSignUpWay())
                .profileId((accountDto.getProfileFile() != null) ? fileId : null)
                .lecturerApprovalYn((accountDto.getRoleId().longValue() == Long.valueOf(2)) ? 
                                    "N" : null)
                .build();

        if (accountDto.getPassword() != null) {
            userEntity.setPassword(passwordEncoder.encode((userEntity.getPassword())));
        }
        Long userId = userRepository.save(userEntity).getId();

        RoleUserEntity roleUserEntity = new RoleUserEntity();

        roleUserEntity.setUserId(userId);
//        roleUserEntity.setRoleId(accountDto.getRoleId().longValue() == Long.valueOf(1) ? 3L : accountDto.getRoleId());
        roleUserEntity.setRoleId(accountDto.getRoleId().longValue());

        roleUserRepository.save(roleUserEntity);

        if(accountDto.getTermsList() != null && accountDto.getTermsList().size() > 0) {
            List<UserTermsEntity> userTermsEntityList = new ArrayList<>();

            accountDto.getTermsList().forEach(e -> {
                UserTermsEntity userTermsEntity = UserTermsEntity.builder()
                        .userId(userId)
                        .termsId(e)
                        .build();

                userTermsEntityList.add(userTermsEntity);
            });

            userTermsRepository.saveAll(userTermsEntityList);
        }

        if(accountDto.getInviteFlag() != null && accountDto.getInviteFlag().equals("true")) {
            InviteStudentEntity inviteStudentEntity = InviteStudentEntity.builder()
                    .lecturerId(accountDto.getLecturerId())
                    .studentId(userId)
                    .classroomId(accountDto.getClassroomId())
                    .linkYn("N")
                    .build();

            inviteStudentRepository.save(inviteStudentEntity);
        }

    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeAccount(@Valid @NotNull(groups = { RemoveValidationGroup.class }) AccountDto accountDto) {
        userRepository.delete(UserEntity.builder().id(accountDto.getUserId()).build());
        roleUserRepository.delete(RoleUserEntity.builder().userId(accountDto.getUserId()).build());
    }

}
