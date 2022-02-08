package kr.supporti.api.common.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomLiveParamDto {

    private Long id;

    private Long classroomId;

    private String title;

    private String deleteYn;

    private MultipartFile thumbnail;

    private String originFileNm;

    private String saveFileNm;

    private String fileSize;

    private Long createdBy;

    private List<ClassRoomLiveParamDto> liveTime;
    
    private Long classroomLiveId;

    private String startDate;

    private String startTime;

    private String endHour;

    private String endMin;
}