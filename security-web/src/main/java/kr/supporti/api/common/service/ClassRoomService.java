package kr.supporti.api.common.service;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ClassRoomDto;
import kr.supporti.api.common.dto.ClassRoomLiveTimeDto;
import kr.supporti.api.common.dto.ClassRoomParamDto;
import kr.supporti.api.common.entity.ClassRoomEntity;
import kr.supporti.api.common.entity.ClassRoomLiveTimeEntity;
import kr.supporti.api.common.entity.ClassRoomUserEntity;
import kr.supporti.api.common.mapper.ClassRoomLiveTimeMapper;
import kr.supporti.api.common.mapper.ClassRoomMapper;
import kr.supporti.api.common.repository.ClassRoomLiveTimeRepository;
import kr.supporti.api.common.repository.ClassRoomRepository;
import kr.supporti.api.common.repository.ClassRoomUserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ClassRoomService {

    @Autowired
    private ClassRoomRepository classRoomRepository;

    @Autowired
    private ClassRoomUserRepository classRoomUserRepository;

    @Autowired
    private ClassRoomLiveTimeRepository classRoomLiveTimeRepository;

    @Autowired
    private ClassRoomMapper classRoomMapper;

    @Autowired
    private ClassRoomLiveTimeMapper classRoomLiveTimeMapper;

    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.classRoomProfile.path}")
    private String classRoomProfilePath;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ClassRoomEntity> getClassRoomList(@Valid ClassRoomParamDto classRoomParamDto,
            PageRequest pageRequest) {
        Integer classRoomListCount = classRoomMapper.selectClassRoomListCount(classRoomParamDto);
        List<ClassRoomEntity> classRoomList = classRoomMapper.selectClassRoomList(classRoomParamDto, pageRequest);
        PageResponse<ClassRoomEntity> pageResponse = new PageResponse<>(pageRequest, classRoomListCount);
        pageResponse.setItems(classRoomList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ClassRoomEntity getClassRoom(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return classRoomMapper.selectClassRoom(id);
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public ClassRoomEntity createClassRoom(
            @Valid @NotNull(groups = { CreateValidationGroup.class }) ClassRoomDto classRoomDto) {

        String originFileNm = null, saveFileNm = null, fileSize = null;
        ClassRoomEntity classRoomEntity = new ClassRoomEntity();

        if (classRoomDto.getThumbnail() != null) {
            File classRoomDir = new File(filePath + File.separator + classRoomProfilePath);
            if (!classRoomDir.exists()) {
                classRoomDir.mkdirs();
            }

            String classRoomOrgFilename = classRoomDto.getThumbnail().getOriginalFilename();
            String classRoomExtension = StringUtils.getFilenameExtension(classRoomOrgFilename);
            String classRoomSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "." + classRoomExtension;
            String classRoomFileFullPath = filePath + File.separator + classRoomProfilePath + File.separator
                    + classRoomSaveFilename;

            try {
                classRoomDto.getThumbnail().transferTo(new File(classRoomFileFullPath));

                originFileNm = classRoomOrgFilename;
                saveFileNm = classRoomSaveFilename;
                fileSize = String.valueOf(classRoomDto.getThumbnail().getSize());

            } catch (Exception e) {
            }
        }

        //강의실 생성
        classRoomEntity = ClassRoomEntity.builder()
              .type(classRoomDto.getType())
              .title(classRoomDto.getTitle())
              .categoryId(classRoomDto.getCategoryId())
              .etc(classRoomDto.getEtc() != null && !classRoomDto.getEtc().equals("") ? classRoomDto.getEtc() : null)
              .memberNum(classRoomDto.getMemberNum())
              .openType(classRoomDto.getOpenType())
              .enterCode(classRoomDto.getOpenType().equals("C") ? classRoomDto.getEnterCode() : null)
              .originFileNm(originFileNm)
              .saveFileNm(saveFileNm)
              .fileSize(fileSize)
              .deleteYn(classRoomDto.getDeleteYn())
              .createdBy(classRoomDto.getCreatedBy()).build();
        classRoomEntity = classRoomRepository.save(classRoomEntity);
        
        Long classroomId = classRoomEntity.getId();
        
        //강의실 생성 후 생성자 입장
        ClassRoomUserEntity classRoomUserEntity = ClassRoomUserEntity.builder()
                .classroomId(classroomId)
                .userId(classRoomDto.getCreatedBy())
                .build();

        classRoomUserRepository.save(classRoomUserEntity);
        
        //강의실 유형이 실시간인 경우, 예상 날짜 및 시간 저장
        if(classRoomDto.getType().equals("R")) {
            Integer year, month, day, startHour, startMin, endHour, endMin;
            ClassRoomLiveTimeDto classTime = classRoomDto.getClassTime();
            //날짜
            year = Integer.parseInt(classTime.getDate().substring(0, 4));
            month = Integer.parseInt(classTime.getDate().substring(4, 6));
            day = Integer.parseInt(classTime.getDate().substring(6, 8));
            //시작 시간
            startHour = Integer.parseInt(classTime.getStartTime().substring(0, 2));
            startMin = Integer.parseInt(classTime.getStartTime().substring(3));
            //종료 시간
            endHour = Integer.parseInt(classTime.getEndTime().substring(0, 2));
            endMin = Integer.parseInt(classTime.getEndTime().substring(3));
            
            ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
                      .classroomId(classroomId)
                      .title(year + "년 " + month + "월 " + day + "일 실시간 강의")
                      .startTime(LocalDateTime.of(year, month, day, startHour, startMin))
                      .endTime(LocalDateTime.of(year, month, day, endHour, endMin))
                      .build();
            
            classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);
        }
//        List<ClassRoomLiveTimeDto> liveTime = classRoomDto.getLiveTime();
//
//        Integer endHour = Integer.parseInt(classRoomDto.getEndHour());
//        Integer endMin = Integer.parseInt(classRoomDto.getEndMin());
//
//        for(int i = 0; i < liveTime.size(); i++) {
//            String startDate = (String)liveTime.get(i).getStartDate();
//            String startTime = (String)liveTime.get(i).getStartTime();
//
//            Integer year = Integer.parseInt(startDate.substring(0, 4));
//            Integer month = Integer.parseInt(startDate.substring(4, 6));
//            Integer day = Integer.parseInt(startDate.substring(6, 8));
//
//            Integer hour = Integer.parseInt(startTime.substring(0, 2));
//            Integer minute = Integer.parseInt(startTime.substring(3));
//
//            ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
//                    .classroomId(classroomId)
//                    .title(year + "년 " + month + "월 " + day + "일 라이브 미팅")
//                    .startTime(LocalDateTime.of(year, month, day, hour, minute))
//                    .endTime(LocalDateTime.of(year, month, day, endHour, endMin))
//                    .build();
//            classRoomLiveTimeEntity = classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);
//        }


        return classRoomEntity;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyClassRoom(@Valid @NotNull(groups = { ModifyValidationGroup.class }) ClassRoomDto classRoomDto) {

        String originFileNm = null, saveFileNm = null, fileSize = null;
        Long classroomId = classRoomDto.getId();
        ClassRoomEntity classRoomEntity = new ClassRoomEntity();
        Optional<ClassRoomEntity> entity = classRoomRepository.findById(classroomId);
        
        //썸네일을 지정해줬으면
        if (classRoomDto.getThumbnail() != null) {
            File classRoomDir = new File(filePath + File.separator + classRoomProfilePath);
            if (!classRoomDir.exists()) {
                classRoomDir.mkdirs();
            }

            String classRoomOrgFilename = classRoomDto.getThumbnail().getOriginalFilename();
            String classRoomExtension = StringUtils.getFilenameExtension(classRoomOrgFilename);
            String classRoomSaveFilename = UUID.randomUUID().toString().replaceAll("-", "") + "." + classRoomExtension;
            String classRoomFileFullPath = filePath + File.separator + classRoomProfilePath + File.separator
                    + classRoomSaveFilename;

            try {
                classRoomDto.getThumbnail().transferTo(new File(classRoomFileFullPath));

                originFileNm = classRoomOrgFilename;
                saveFileNm = classRoomSaveFilename;
                fileSize = String.valueOf(classRoomDto.getThumbnail().getSize());

                classRoomDto.setOriginFileNm(originFileNm);
                classRoomDto.setSaveFileNm(saveFileNm);
                classRoomDto.setFileSize(fileSize);
            } catch (Exception e) {
            }
        } else {
            if(classRoomDto.getFileFlag() == null || classRoomDto.getFileFlag().equals("remove")) {
                classRoomDto.setOriginFileNm(entity.get().getOriginFileNm());
                classRoomDto.setSaveFileNm(entity.get().getSaveFileNm());
                classRoomDto.setFileSize(entity.get().getFileSize());
            }
        }

        if (classRoomDto.getId() != null && classRoomDto.getFileFlag() != null ) {
            String fileNm = entity.get().getSaveFileNm();
            String thumbFileFullPath = filePath + File.separator + classRoomProfilePath + File.separator + fileNm;

            File file = new File(thumbFileFullPath);

            if (file.exists()) {
                file.delete();
            } else {
            }
        }
        
        //ClassRoomLiveTime테이블에 데이터가 있다면 우선 삭제
        ClassRoomLiveTimeEntity classRoomTime = classRoomLiveTimeMapper.selectClassRoomIdLiveTime(classroomId);
        if(classRoomTime != null) {
            classRoomLiveTimeRepository.deleteById(classRoomTime.getId());
        }

        //강의실 유형이 실시간인 경우, 예상 날짜 및 시간 저장
        if(classRoomDto.getType().equals("R")) {
            Integer year, month, day, startHour, startMin, endHour, endMin;
            ClassRoomLiveTimeDto classTime = classRoomDto.getClassTime();
            //날짜
            year = Integer.parseInt(classTime.getDate().substring(0, 4));
            month = Integer.parseInt(classTime.getDate().substring(4, 6));
            day = Integer.parseInt(classTime.getDate().substring(6, 8));
            //시작 시간
            startHour = Integer.parseInt(classTime.getStartTime().substring(0, 2));
            startMin = Integer.parseInt(classTime.getStartTime().substring(3));
            //종료 시간
            endHour = Integer.parseInt(classTime.getEndTime().substring(0, 2));
            endMin = Integer.parseInt(classTime.getEndTime().substring(3));
            
            ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
                      .classroomId(classroomId)
                      .title(year + "년 " + month + "월 " + day + "일 실시간 강의")
                      .startTime(LocalDateTime.of(year, month, day, startHour, startMin))
                      .endTime(LocalDateTime.of(year, month, day, endHour, endMin))
                      .build();
            
            classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);
        }
        
        //강의실 수정
        classRoomEntity = ClassRoomEntity.builder()
                .id(classroomId)
                .type(classRoomDto.getType())
                .title(classRoomDto.getTitle())
                .categoryId(classRoomDto.getCategoryId())
                .etc(classRoomDto.getEtc() != null && !classRoomDto.getEtc().equals("") ? classRoomDto.getEtc() : null)
                .memberNum(classRoomDto.getMemberNum())
                .openType(classRoomDto.getOpenType())
                .enterCode(classRoomDto.getOpenType().equals("C") ? classRoomDto.getEnterCode() : null)
                .originFileNm(classRoomDto.getOriginFileNm())
                .saveFileNm(classRoomDto.getSaveFileNm())
                .fileSize(classRoomDto.getFileSize())
                .deleteYn(classRoomDto.getDeleteYn())
                .createdBy(classRoomDto.getCreatedBy()).build();
        
        classRoomRepository.save(classRoomEntity);
        

//        Integer endTime = Integer.parseInt(classRoomDto.getEndHour()) * 60 + Integer.parseInt(classRoomDto.getEndMin());
//        List<ClassRoomLiveTimeDto> liveTime = classRoomDto.getLiveTime();
//
//        for(int i = 0; i < liveTime.size(); i++) {
//            Long liveTimeId = liveTime.get(i).getId();
//
//            String startDate = (String)liveTime.get(i).getStartDate();
//            String startTime = (String)liveTime.get(i).getStartTime();
//
//            Integer year = Integer.parseInt(startDate.substring(0, 4));
//            Integer month = Integer.parseInt(startDate.substring(4, 6));
//            Integer day = Integer.parseInt(startDate.substring(6, 8));
//
//            Integer hour = Integer.parseInt(startTime.substring(0, 2));
//            Integer minute = Integer.parseInt(startTime.substring(3));
//
//            if(liveTimeId == null) {
//                ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
//                        .classroomId(classRoomDto.getId())
//                        .title(year + "년 " + month + "월 " + day + "일 라이브 미팅")
//                        .startTime(LocalDateTime.of(year, month, day, hour, minute))
//                        .endTime(LocalDateTime.of(year, month, day, hour, minute).plusMinutes(endTime))
//                        .build();
//                classRoomLiveTimeEntity = classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);
//            } else {
//                ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
//                        .id(liveTimeId)
//                        .classroomId(classRoomDto.getId())
//                        .title(year + "년 " + month + "월 " + day + "일 라이브 미팅")
//                        .startTime(LocalDateTime.of(year, month, day, hour, minute))
//                        .endTime(LocalDateTime.of(year, month, day, hour, minute).plusMinutes(endTime))
//                        .build();
//                classRoomLiveTimeEntity = classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);
////                classRoomLiveTimeMapper.modifyClassRoomLiveTime(classRoomLiveTimeEntity);
//            }
//        }

    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeClassRoom(@Valid @NotNull(groups = { RemoveValidationGroup.class }) ClassRoomDto classRoomDto) {
        classRoomMapper.removeClassRoom(classRoomDto);
    }

}