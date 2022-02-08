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
public class LecturerDataParamDto {
    
    private List<LecturerDataParamDto> params;

    private Long id;

    private Long lecturerId;

    private String title;

    private String subtitle;
    
    private List<MultipartFile> files;
    
    private Long lecturerDataId;
 
    private MultipartFile file;

//    private String originFileNm;
//
//    private String saveFileNm;
//
//    private String fileExtension;
//
//    private String fileSize;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

}