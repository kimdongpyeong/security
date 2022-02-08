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
import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.LecturerBoardDto;
import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerDataParamDto;
import kr.supporti.api.common.dto.LecturerUploadDto;
import kr.supporti.api.common.dto.LecturerUploadFileDto;
import kr.supporti.api.common.dto.LecturerUploadParamDto;
import kr.supporti.api.common.entity.LecturerBoardEntity;
import kr.supporti.api.common.entity.LecturerBoardFileEntity;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.LecturerUploadEntity;
import kr.supporti.api.common.entity.LecturerUploadFileEntity;
import kr.supporti.api.common.entity.ProfileFileEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.LecturerBoardFileMapper;
import kr.supporti.api.common.mapper.LecturerBoardMapper;
import kr.supporti.api.common.mapper.LecturerDataFileMapper;
import kr.supporti.api.common.mapper.LecturerDataMapper;
import kr.supporti.api.common.mapper.LecturerUploadFileMapper;
import kr.supporti.api.common.mapper.LecturerUploadMapper;
import kr.supporti.api.common.mapper.MypageMapper;
import kr.supporti.api.common.repository.LecturerBoardFileRepository;
import kr.supporti.api.common.repository.LecturerBoardRepository;
import kr.supporti.api.common.repository.LecturerDataFileRepository;
import kr.supporti.api.common.repository.LecturerDataRepository;
import kr.supporti.api.common.repository.LecturerUploadFileRepository;
import kr.supporti.api.common.repository.LecturerUploadRepository;
import kr.supporti.api.common.repository.MypageRepository;
import kr.supporti.api.common.repository.ProfileFileRepository;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Service
public class LecturerUploadService {

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
    private LecturerUploadMapper lecturerUploadMapper;
    
    @Autowired
    private LecturerDataFileMapper lecturerDataFileMapper;
    
    @Autowired
    private LecturerUploadFileRepository lecturerUploadFileRepository;
    
    @Autowired
    private LecturerUploadRepository lecturerUploadRepository;
    
    @Autowired
    private LecturerUploadFileMapper lecturerUploadFileMapper;

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
    private String lecturerUploadPath;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    ProfileFileService profileFileService;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public LecturerUploadEntity getLecturerUpload(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return lecturerUploadMapper.selectLecturerUpload(id);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<LecturerUploadEntity> getLecturerUploadList(@Valid LecturerUploadDto lecturerUploadDto, PageRequest pageRequest) {
        Integer lecturerUploadListCount = lecturerUploadMapper.selectLecturerUploadListCount(lecturerUploadDto);
        List<LecturerUploadEntity> lecturerUploadList = lecturerUploadMapper.selectLecturerUploadList(lecturerUploadDto, pageRequest);
        PageResponse<LecturerUploadEntity> pageResponse = new PageResponse<>(pageRequest, lecturerUploadListCount);
        pageResponse.setItems(lecturerUploadList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<LecturerUploadFileEntity> getLecturerUploadFileList(@Valid LecturerUploadFileDto lecturerUploadFileDto, PageRequest pageRequest) {
        Integer lecturerUploadFileListCount = lecturerUploadFileMapper.selectLecturerUploadFileListCount(lecturerUploadFileDto);
        List<LecturerUploadFileEntity> lecturerUploadFileList = lecturerUploadFileMapper.selectLecturerUploadFileList(lecturerUploadFileDto, pageRequest);
        PageResponse<LecturerUploadFileEntity> pageResponse = new PageResponse<>(pageRequest, lecturerUploadFileListCount);
        pageResponse.setItems(lecturerUploadFileList);
        return pageResponse;
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public LecturerUploadEntity createLecturerUpload(@Valid @NotNull(groups = { CreateValidationGroup.class }) LecturerUploadParamDto lecturerUploadParamDto) {

        List<MultipartFile> files = lecturerUploadParamDto.getFiles();
        LecturerUploadEntity lecturerUploadEntity = LecturerUploadEntity.builder()
                .lecturerStudentId(lecturerUploadParamDto.getLecturerStudentId())
                .title(lecturerUploadParamDto.getTitle())
                .desc(lecturerUploadParamDto.getDesc())
                .deleteYn(lecturerUploadParamDto.getDeleteYn())
                .build();
        lecturerUploadEntity = lecturerUploadRepository.save(lecturerUploadEntity);

        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);

            String originFileNm = null, saveFileNm = null, fileExtension = null, fileSize = null;

            if (file != null) {
                File lectDataDir = new File(filePath + File.separator + lecturerUploadPath);
                if (!lectDataDir.exists()) {
                    lectDataDir.mkdirs();
                }

                String lectUploadOrgFilename = file.getOriginalFilename();
                String lectUploadExtension = StringUtils.getFilenameExtension(lectUploadOrgFilename);
                String lectUploadSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "."
                        + lectUploadExtension;
                String lectUploadFileFullPath = filePath + File.separator + lecturerUploadPath + File.separator
                        + lectUploadSaveFilename;

                try {
                    file.transferTo(new File(lectUploadFileFullPath));

                    originFileNm = lectUploadOrgFilename;
                    saveFileNm = lectUploadSaveFilename;
                    fileExtension = lectUploadExtension;
                    fileSize = String.format("%.3f",file.getSize() / 1024.0) + "KB";
                    System.out.println("qweqwe"+ fileSize);
                    
                } catch (Exception e) {}

            }

            LecturerUploadFileEntity fileEntity = LecturerUploadFileEntity.builder()
                    .lecturerStudentFileId(lecturerUploadEntity.getId())
                    .originFileNm(originFileNm)
                    .saveFileNm(saveFileNm)
                    .fileExtension(fileExtension)
                    .fileSize(fileSize)
                    .build();
            fileEntity = lecturerUploadFileRepository.save(fileEntity);
        }

        return new LecturerUploadEntity();
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public Integer modifyStudentUploadYn(@Valid @NotNull(groups = { ModifyValidationGroup.class }) Long id, @Valid @NotNull(groups = { ModifyValidationGroup.class }) LecturerUploadParamDto lecturerUploadParamDto) {
        return lecturerUploadMapper.modifyStudentUploadYn(id, lecturerUploadParamDto);
    }
    
    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyLecturerUpload(@Valid @NotNull(groups = { ModifyValidationGroup.class }) LecturerUploadParamDto lecturerUploadParamDto) {

        lecturerUploadMapper.modifyLecturerUpload(lecturerUploadParamDto);

        List<MultipartFile> files = lecturerUploadParamDto.getFiles();
        if(files != null) {
            for(int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);

                String originFileNm = null, saveFileNm = null, fileExtension = null, fileSize = null;

                if (file != null) {
                    File lectUploadDir = new File(filePath + File.separator + lecturerUploadPath);
                    if (!lectUploadDir.exists()) {
                        lectUploadDir.mkdirs();
                    }

                    String lectUploadOrgFilename = file.getOriginalFilename();
                    String lectUploadExtension = StringUtils.getFilenameExtension(lectUploadOrgFilename);
                    String lectUploadSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "."
                            + lectUploadExtension;
                    String lectUploadFileFullPath = filePath + File.separator + lecturerUploadPath + File.separator
                            + lectUploadSaveFilename;

                    try {
                        file.transferTo(new File(lectUploadFileFullPath));

                        originFileNm = lectUploadOrgFilename;
                        saveFileNm = lectUploadSaveFilename;
                        fileExtension = lectUploadExtension;
                        fileSize = String.format("%.3f",file.getSize() / 1024.0) + "KB";
                    } catch (Exception e) {}

                    LecturerUploadFileEntity fileEntity = LecturerUploadFileEntity.builder()
                            .lecturerStudentFileId(lecturerUploadParamDto.getId())
                            .originFileNm(originFileNm)
                            .saveFileNm(saveFileNm)
                            .fileExtension(fileExtension)
                            .fileSize(fileSize)
                            .build();
                    fileEntity = lecturerUploadFileRepository.save(fileEntity);
                }
            }
        }
    }

}
