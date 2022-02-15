package kr.supporti.api.common.service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.common.dto.LecturerBoardDto;
import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerDataParamDto;
import kr.supporti.api.common.entity.LecturerBoardEntity;
import kr.supporti.api.common.entity.LecturerBoardFileEntity;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.ProfileFileEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.LecturerBoardFileMapper;
import kr.supporti.api.common.mapper.LecturerBoardMapper;
import kr.supporti.api.common.mapper.LecturerDataFileMapper;
import kr.supporti.api.common.mapper.LecturerDataMapper;
import kr.supporti.api.common.mapper.MypageMapper;
import kr.supporti.api.common.repository.LecturerBoardFileRepository;
import kr.supporti.api.common.repository.LecturerBoardRepository;
import kr.supporti.api.common.repository.LecturerDataFileRepository;
import kr.supporti.api.common.repository.LecturerDataRepository;
import kr.supporti.api.common.repository.MypageRepository;
import kr.supporti.api.common.repository.ProfileFileRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Service
public class MypageService {

    @Autowired
    private LecturerBoardMapper lecturerBoardMapper;

    @Autowired
    private LecturerBoardRepository lecturerBoardRepository;

    @Autowired
    private LecturerBoardFileMapper lecturerBoardFileMapper;

    @Autowired
    private LecturerBoardFileRepository lecturerBoardFileRepository;

    @Autowired
    private MypageMapper mypageMapper;

    @Autowired
    private LecturerDataMapper lecturerDataMapper;

    @Autowired
    private LecturerDataFileMapper lecturerDataFileMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MypageRepository mypageRepository;

    @Autowired
    ProfileFileRepository profileFileRepository;

    @Autowired
    private LecturerDataRepository lecturerDataRepository;

    @Autowired
    private LecturerDataFileRepository lecturerDatafileRepository;

    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.board.path}")
    private String boardPath;

    @Value("${file.upload.profile.path}")
    private String profilePath;

    @Value("${file.upload.lecturer-data.path}")
    private String lecturerDataPath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ProfileFileService profileFileService;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<LecturerBoardEntity> getLecturerBoardList(
           @Valid LecturerBoardDto lecturerBoardDto,
           PageRequest pageRequest) {
        Integer LecturerBoardListCount = lecturerBoardMapper.selectLecturerBoardListCount(lecturerBoardDto);
        List<LecturerBoardEntity> LecturerBoardList = lecturerBoardMapper.selectLecturerBoardList(lecturerBoardDto, pageRequest);
        PageResponse<LecturerBoardEntity> pageResponse = new PageResponse<>(pageRequest, LecturerBoardListCount);
        pageResponse.setItems(LecturerBoardList);
        return pageResponse;
   }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public LecturerBoardEntity getLecturerBoard(@Valid LecturerBoardDto lecturerBoardDto) {
        return lecturerBoardMapper.selectLecturerBoard(lecturerBoardDto);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public UserEntity getMypageUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return mypageMapper.selectMypageUser(id);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyUserInfo(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {
        mypageMapper.modifyUserInfo(accountDto);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyUserIntroduce(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {
        mypageMapper.modifyUserIntroduce(accountDto);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyProfile(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {

        Long fileId = null;
        Long orgProfileId = accountDto.getProfileId();

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

                ProfileFileEntity profileFileEntity = ProfileFileEntity.builder().originFileNm(profileOrgFilename)
                        .saveFileNm(profileSaveFilename).fileSize(String.valueOf(accountDto.getProfileFile().getSize()))
                        .build();
                profileFileEntity = profileFileService.modifyProfileFile(profileFileEntity);
                fileId = profileFileEntity.getId();
                accountDto.setProfileId(fileId);
            } catch (Exception e) {
            }
        } else {
            if (accountDto.getFileFlag().equals("remove")) {
                accountDto.setProfileId(null);
            }
        }
        mypageMapper.modifyProfile(accountDto);

        if (orgProfileId != null
                && (accountDto.getFileFlag().equals("remove") || accountDto.getFileFlag().equals("change"))) {
            Optional<ProfileFileEntity> entity = profileFileRepository.findById(orgProfileId);
            String fileNm = entity.get().getSaveFileNm();
            String profileFileFullPath = filePath + File.separator + profilePath + File.separator + fileNm;

            File file = new File(profileFileFullPath);

            if (file.exists()) {
                file.delete();
            } else {
            }
            profileFileService.removeProfileFile(orgProfileId);
        }
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Map<String, Object> modifyPassword(Long id, String oldPassword, String newPassword) throws ServletException {
        UserEntity userEntity = mypageMapper.selectMypageUser(id);
        String orignPassword = userEntity.getPassword();
        Map<String, Object> map = new WeakHashMap<>();

        if (passwordEncoder.matches(oldPassword, orignPassword)) {
            if (oldPassword.equals(newPassword)) {
                map.put("result", false);
                map.put("resultMsg", "같은 비밀번호로는 변경이 불가능합니다.");
            } else {
                userEntity.setPassword(passwordEncoder.encode(newPassword));
                mypageRepository.save(userEntity);
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
    @Transactional
    public void removeUser(@Valid @NotNull(groups = { ModifyValidationGroup.class }) AccountDto accountDto) {
        accountDto.setStatus("D");
        mypageMapper.removeUser(accountDto);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public LecturerDataEntity getLecturerData(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return lecturerDataMapper.selectLecturerData(id);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<LecturerDataEntity> getLecturerDataList(@Valid LecturerDataDto lecturerDataDto, PageRequest pageRequest) {
        Integer lecturerDataListCount = lecturerDataMapper.selectLecturerDataListCount(lecturerDataDto);
        List<LecturerDataEntity> lecturerDataList = lecturerDataMapper.selectLecturerDataList(lecturerDataDto, pageRequest);
        PageResponse<LecturerDataEntity> pageResponse = new PageResponse<>(pageRequest, lecturerDataListCount);
        pageResponse.setItems(lecturerDataList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<LecturerDataFileEntity> getLecturerDataFileList(@Valid LecturerDataFileDto lecturerDataFileDto, PageRequest pageRequest) {
        Integer lecturerDataFileListCount = lecturerDataFileMapper.selectLecturerDataFileListCount(lecturerDataFileDto);
        List<LecturerDataFileEntity> lecturerDataFileList = lecturerDataFileMapper.selectLecturerDataFileList(lecturerDataFileDto, pageRequest);
        PageResponse<LecturerDataFileEntity> pageResponse = new PageResponse<>(pageRequest, lecturerDataFileListCount);
        pageResponse.setItems(lecturerDataFileList);
        return pageResponse;
    }
    
    @Validated(value = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Transactional
    public void sortLecturerData(LecturerDataParamDto lecturerDataParamDto) {
        
        List<LecturerDataParamDto> params = lecturerDataParamDto.getParams();
        
        // 전체 삭제
        if(lecturerDataParamDto.getDelDataList() != null && lecturerDataParamDto.getDelDataList().size() > 0) {
            for(int i = 0; i < lecturerDataParamDto.getDelDataList().size(); i++) {
                PageRequest pageRequest = new PageRequest();
                Long dataId = lecturerDataParamDto.getDelDataList().get(i);
                
                pageRequest.setPage(1);
                pageRequest.setRowSize(100000);
                        
                List<LecturerDataFileEntity> dataFile = lecturerDataFileMapper.selectLecturerDataFileList(
                        LecturerDataFileDto.builder().lecturerDataId(dataId).build(), pageRequest);
                
                for(LecturerDataFileEntity fileEntity : dataFile) {
                    Long fileId = fileEntity.getId();
                    String fileNm = fileEntity.getSaveFileNm();

                    String thumbFileFullPath = filePath + File.separator + lecturerDataPath + File.separator + fileNm;

                    File file = new File(thumbFileFullPath);

                    if (file.exists()) {
                        file.delete();
                    } else {
                    }
                }
                lecturerDatafileRepository.deleteAll(dataFile);
                lecturerDataRepository.deleteById(dataId);
            }
        }
        // 파일 삭제
        if(lecturerDataParamDto.getDelFileList() != null && lecturerDataParamDto.getDelFileList().size() > 0) {
            for(int i = 0; i < lecturerDataParamDto.getDelFileList().size(); i++) {
                Long fileId = lecturerDataParamDto.getDelFileList().get(i);
                
                LecturerDataFileEntity fileEntity = lecturerDataFileMapper.selectDataFile(fileId);
                
                String fileNm = fileEntity.getSaveFileNm();

                String thumbFileFullPath = filePath + File.separator + lecturerDataPath + File.separator + fileNm;

                File file = new File(thumbFileFullPath);

                if (file.exists()) {
                    file.delete();
                } else {
                }
                
                lecturerDatafileRepository.deleteById(fileId);
            }
        }
        
        for(int i = 0; i < params.size(); i++) {
            Long dataId = params.get(i).getId();
            
            if(dataId == null) {
                lecturerDataParamDto.setLecturerId(lecturerDataParamDto.getLecturerId());
                lecturerDataParamDto.setTitle(params.get(i).getTitle());
                lecturerDataParamDto.setSubtitle(params.get(i).getSubtitle());
                lecturerDataParamDto.setFiles(params.get(i).getFiles());
                createLecturerData(lecturerDataParamDto);
            } else {
                lecturerDataParamDto.setId(params.get(i).getId());;
                lecturerDataParamDto.setTitle(params.get(i).getTitle());
                lecturerDataParamDto.setSubtitle(params.get(i).getSubtitle());
                lecturerDataParamDto.setFiles(params.get(i).getFiles());
                modifyLecturerData(lecturerDataParamDto);
            }
        }
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public LecturerDataEntity createLecturerData(@Valid @NotNull(groups = { CreateValidationGroup.class }) LecturerDataParamDto lecturerDataParamDto) {

        List<MultipartFile> files = lecturerDataParamDto.getFiles();
        LecturerDataEntity lecturerDataEntity = LecturerDataEntity.builder()
                .lecturerId(lecturerDataParamDto.getLecturerId())
                .title(lecturerDataParamDto.getTitle())
                .subtitle(lecturerDataParamDto.getSubtitle())
                .build();
        lecturerDataEntity = lecturerDataRepository.save(lecturerDataEntity);
        
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            String originFileNm = null, saveFileNm = null, fileExtension = null, fileSize = null;

            if (file != null) {
                File lectDataDir = new File(filePath + File.separator + lecturerDataPath);
                if (!lectDataDir.exists()) {
                    lectDataDir.mkdirs();
                }

                String lectDataOrgFilename = file.getOriginalFilename();
                String lectDataExtension = StringUtils.getFilenameExtension(lectDataOrgFilename);
                String lectDataSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "."
                        + lectDataExtension;
                String lectDataFileFullPath = filePath + File.separator + lecturerDataPath + File.separator
                        + lectDataSaveFilename;

                try {
                    file.transferTo(new File(lectDataFileFullPath));

                    originFileNm = lectDataOrgFilename;
                    saveFileNm = lectDataSaveFilename;
                    fileExtension = lectDataExtension;
                    fileSize = String.valueOf(file.getSize());
                } catch (Exception e) {}

            }

            LecturerDataFileEntity fileEntity = LecturerDataFileEntity.builder()
                    .lecturerDataId(lecturerDataEntity.getId())
                    .originFileNm(originFileNm)
                    .saveFileNm(saveFileNm)
                    .fileExtension(fileExtension)
                    .fileSize(fileSize)
                    .build();
            fileEntity = lecturerDatafileRepository.save(fileEntity);
        }
        
        return new LecturerDataEntity();
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyLecturerData(@Valid @NotNull(groups = { ModifyValidationGroup.class }) LecturerDataParamDto lecturerDataParamDto) {

        lecturerDataMapper.modifyLecturerData(lecturerDataParamDto);
        
        List<MultipartFile> files = lecturerDataParamDto.getFiles();
        
        if(files != null) {
            for(int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                String originFileNm = null, saveFileNm = null, fileExtension = null, fileSize = null;

                if (file != null) {
                    File lectDataDir = new File(filePath + File.separator + lecturerDataPath);
                    if (!lectDataDir.exists()) {
                        lectDataDir.mkdirs();
                    }

                    String lectDataOrgFilename = file.getOriginalFilename();
                    String lectDataExtension = StringUtils.getFilenameExtension(lectDataOrgFilename);
                    String lectDataSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "."
                            + lectDataExtension;
                    String lectDataFileFullPath = filePath + File.separator + lecturerDataPath + File.separator
                            + lectDataSaveFilename;

                    try {
                        file.transferTo(new File(lectDataFileFullPath));

                        originFileNm = lectDataOrgFilename;
                        saveFileNm = lectDataSaveFilename;
                        fileExtension = lectDataExtension;
                        fileSize = String.valueOf(file.getSize());
                    } catch (Exception e) {}

                    LecturerDataFileEntity fileEntity = LecturerDataFileEntity.builder()
                            .lecturerDataId(lecturerDataParamDto.getId())
                            .originFileNm(originFileNm)
                            .saveFileNm(saveFileNm)
                            .fileExtension(fileExtension)
                            .fileSize(fileSize)
                            .build();
                    fileEntity = lecturerDatafileRepository.save(fileEntity);
                }
            }
        }
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public LecturerBoardEntity createPost(@Valid @NotNull(groups = { CreateValidationGroup.class }) LecturerBoardDto lecturerBoardDto) {

        LecturerBoardEntity lecturerBoardEntity = LecturerBoardEntity.builder()
                .lecturerId(lecturerBoardDto.getLecturerId())
                .title(lecturerBoardDto.getTitle())
                .desc(lecturerBoardDto.getDesc())
                .deleteYn(lecturerBoardDto.getDeleteYn())
                .build();

        lecturerBoardEntity = lecturerBoardRepository.save(lecturerBoardEntity);
        Long boardId = lecturerBoardEntity.getId();
        lecturerBoardDto.setId(boardId);

        if(lecturerBoardDto.getFileList().length > 0) {
            lecturerBoardFileMapper.updateBoardFile(lecturerBoardDto);
        }

        //지운파일리스트가 있다면 boardFile테이블에서 db삭제 및 파일 삭제
        if(lecturerBoardDto.getRemoveFileList().length > 0) {
            lecturerBoardFileMapper.deleteBoardFile(lecturerBoardDto);

            String[] removeList = lecturerBoardDto.getRemoveFileList();

            for(int i = 0; i < removeList.length; i++) {
                String boardFileFullPath = filePath + File.separator + boardPath + File.separator + removeList[i];

                File file = new File(boardFileFullPath);

                if (file.exists()) {
                    file.delete();
                } else {
                }
            }

        }

        return lecturerBoardEntity;
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public LecturerBoardFileEntity uploadFile(@Valid @NotNull(groups = { CreateValidationGroup.class }) MultipartFile file) {

        LecturerBoardFileEntity lecturerBoardFileEntity = new LecturerBoardFileEntity();

        if(file != null) {
            File boardFileDir = new File(filePath + File.separator + boardPath);

            if(!boardFileDir.exists()) {
                boardFileDir.mkdirs();
            }

            String boardFileOrgFilename = file.getOriginalFilename();
            String boardFileExtension = StringUtils.getFilenameExtension(boardFileOrgFilename);
            String boardFileSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "." + boardFileExtension;
            String boardFileFullPath = filePath + File.separator + boardPath + File.separator + boardFileSaveFilename;

            try {
                file.transferTo(new File(boardFileFullPath));

                lecturerBoardFileEntity = LecturerBoardFileEntity.builder()
                        .originFileNm(boardFileOrgFilename)
                        .saveFileNm(boardFileSaveFilename)
                        .fileSize(String.valueOf(file.getSize()))
                        .build();

            } catch (Exception e) {
            }
        }

        return lecturerBoardFileRepository.save(lecturerBoardFileEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public LecturerBoardEntity modifyPost(@Valid @NotNull(groups = { ModifyValidationGroup.class }) LecturerBoardDto lecturerBoardDto) {

        LecturerBoardEntity lecturerBoardEntity = LecturerBoardEntity.builder()
                .id(lecturerBoardDto.getId())
                .lecturerId(lecturerBoardDto.getLecturerId())
                .title(lecturerBoardDto.getTitle())
                .desc(lecturerBoardDto.getContent())
                .deleteYn(lecturerBoardDto.getDeleteYn())
                .build();
        lecturerBoardEntity = lecturerBoardRepository.save(lecturerBoardEntity);

        //추가파일있다면 boardFile테이블 게시글id 업데이트
        if(lecturerBoardDto.getFileList().length > 0) {
            lecturerBoardFileMapper.updateBoardFile(lecturerBoardDto);
        }

        //지운파일리스트가 있다면 boardFile테이블에서 db삭제 및 파일 삭제
        if(lecturerBoardDto.getRemoveFileList().length > 0) {
            lecturerBoardFileMapper.deleteBoardFile(lecturerBoardDto);

            String[] removeList = lecturerBoardDto.getRemoveFileList();

            for(int i = 0; i < removeList.length; i++) {
                String boardFileFullPath = filePath + File.separator + boardPath + File.separator + removeList[i];

                File file = new File(boardFileFullPath);

                if (file.exists()) {
                    file.delete();
                } else {
                }
            }

        }

        return lecturerBoardEntity;
    }

}
