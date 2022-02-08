package kr.supporti.api.common.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import kr.supporti.api.common.dto.ClassRoomDto;
import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.entity.BoardFileEntity;
import kr.supporti.api.common.entity.NoticeEntity;
import kr.supporti.api.common.mapper.BoardFileMapper;
import kr.supporti.api.common.mapper.NoticeMapper;
import kr.supporti.api.common.repository.BoardFileRepository;
import kr.supporti.api.common.repository.NoticeRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class NoticeService {

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private BoardFileRepository boardFileRepository;
    
    @Autowired
    private NoticeMapper noticeMapper;

    @Autowired
    private BoardFileMapper boardFileMapper;
    
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.board.path}")
    private String boardPath;

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public PageResponse<NoticeEntity> getNoticeList(
           @Valid NoticeDto noticeDto,
           PageRequest pageRequest) {
        Integer noticeListCount = noticeMapper.selectNoticeListCount(noticeDto);
        List<NoticeEntity> noticeList = noticeMapper.selectNoticeList(noticeDto, pageRequest);
        PageResponse<NoticeEntity> pageResponse = new PageResponse<>(pageRequest, noticeListCount);
        pageResponse.setItems(noticeList);
        return pageResponse;
   }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public NoticeEntity getNotice(@Valid NoticeDto noticeDto) {
        return noticeMapper.selectNotice(noticeDto);
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public NoticeEntity createPost(@Valid @NotNull(groups = { CreateValidationGroup.class }) NoticeDto noticeDto) {

        NoticeEntity noticeEntity = NoticeEntity.builder()
                .title(noticeDto.getTitle())
                .desc(noticeDto.getDesc())
                .kindsCd(noticeDto.getKindsCd())
                .importantYn(noticeDto.getImportantYn())
                .noticeYn(noticeDto.getNoticeYn())
                .deleteYn(noticeDto.getDeleteYn())
                .createdBy(noticeDto.getCreatedBy())
                .build();
        
        noticeEntity = noticeRepository.save(noticeEntity);
        Long boardId = noticeEntity.getId();
        noticeDto.setId(boardId);
        
        if(noticeDto.getFileList().length > 0) {
            boardFileMapper.updateBoardFile(noticeDto);
        }
        
        //지운파일리스트가 있다면 boardFile테이블에서 db삭제 및 파일 삭제
        if(noticeDto.getRemoveFileList().length > 0) {
            boardFileMapper.deleteBoardFile(noticeDto);
            
            String[] removeList = noticeDto.getRemoveFileList();
            
            for(int i = 0; i < removeList.length; i++) {
                String boardFileFullPath = filePath + File.separator + boardPath + File.separator + removeList[i];
                
                File file = new File(boardFileFullPath);
                
                if (file.exists()) {
                    file.delete();
                } else {
                }
            }

        }
        
        return noticeEntity;
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public BoardFileEntity uploadFile(@Valid @NotNull(groups = { CreateValidationGroup.class }) MultipartFile file) {
        
        BoardFileEntity boardFileEntity = new BoardFileEntity();
        
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

                boardFileEntity = BoardFileEntity.builder()
                        .originFileNm(boardFileOrgFilename)
                        .saveFileNm(boardFileSaveFilename)
                        .fileSize(String.valueOf(file.getSize()))
                        .build();
                
            } catch (Exception e) {
            }
        }
        
        return boardFileRepository.save(boardFileEntity);
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public NoticeEntity modifyPost(@Valid @NotNull(groups = { ModifyValidationGroup.class }) NoticeDto noticeDto) {
        
        NoticeEntity noticeEntity = NoticeEntity.builder()
                .id(noticeDto.getId())
                .title(noticeDto.getTitle())
                .desc(noticeDto.getContent())
                .kindsCd(noticeDto.getKindsCd())
                .importantYn(noticeDto.getImportantYn())
                .noticeYn(noticeDto.getNoticeYn())
                .deleteYn(noticeDto.getDeleteYn())
                .createdBy(noticeDto.getCreatedBy())
                .build();
        noticeEntity = noticeRepository.save(noticeEntity);

        //추가파일있다면 boardFile테이블 게시글id 업데이트
        if(noticeDto.getFileList().length > 0) {
            boardFileMapper.updateBoardFile(noticeDto);
        }
        
        //지운파일리스트가 있다면 boardFile테이블에서 db삭제 및 파일 삭제
        if(noticeDto.getRemoveFileList().length > 0) {
            boardFileMapper.deleteBoardFile(noticeDto);
            
            String[] removeList = noticeDto.getRemoveFileList();
            
            for(int i = 0; i < removeList.length; i++) {
                String boardFileFullPath = filePath + File.separator + boardPath + File.separator + removeList[i];
                
                File file = new File(boardFileFullPath);
                
                if (file.exists()) {
                    file.delete();
                } else {
                }
            }

        }
        
        return noticeEntity;
    }
}