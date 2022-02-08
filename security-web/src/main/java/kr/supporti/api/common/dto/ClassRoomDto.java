package kr.supporti.api.common.dto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonFormat;

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
public class ClassRoomDto {

    private Long id;

    private String type;
    
    private String title;

    private Long userId;

    private int memberNum;

    private int categoryId;

    private String etc;

    private String openType;

    private String enterCode;

    private String fileFlag;

    private MultipartFile thumbnail;

    private String originFileNm;

    private String saveFileNm;

    private String fileSize;

    private String subjectCd;

    private String deleteYn;

    private Long createdBy;

    private String createdByNm;

    private List<Long> deleteClass;

    private ClassRoomLiveTimeDto classTime;
    
    private String endHour;

    private String endMin;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime classEndTime;

    private List<ClassRoomLiveTimeDto> liveTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

}
