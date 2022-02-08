package kr.supporti.api.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

import kr.supporti.api.common.dto.NoticeDto;
import kr.supporti.api.common.entity.BoardFileEntity;
import kr.supporti.api.common.entity.NoticeEntity;
import kr.supporti.api.common.service.NoticeService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/boardTest")
public class ApiCommonNoticeController {

    @Autowired
    private NoticeService noticeService;
    
    @Value("${file.upload.path}")
    private String filePath;

    @Value("${file.upload.profile.path}")
    private String profilePath;

    @Value("${file.upload.liveCat.path}")
    private String liveCatPath;

    @Value("${file.upload.room-profile.path}")
    private String roomProfilePath;

    @Value("${file.upload.classRoomProfile.path}")
    private String classRoomProfilePath;

    @Value("${file.upload.classRoomLive.path}")
    private String classRoomLivePath;
    
    @Value("${file.upload.board.path}")
    private String boardPath;

    @GetMapping(path = "")
    public PageResponse<NoticeEntity> getNoticetList(@ModelAttribute NoticeDto noticeDto,
            @ModelAttribute PageRequest pageRequest) {
        return noticeService.getNoticeList(noticeDto, pageRequest);
    }
    
    @GetMapping(path = "/detail")
    public NoticeEntity getClient(@ModelAttribute NoticeDto noticeDto) {
        return noticeService.getNotice(noticeDto);
    }

    @PostMapping(path = "/save", params = { "!bulk" })
    public NoticeEntity createPost(@ModelAttribute NoticeDto noticeDto) {
        return noticeService.createPost(noticeDto);
    }

    @PutMapping(path = "/modify", params = { "!bulk" })
    public NoticeEntity modifyPost(@ModelAttribute NoticeDto noticeDto) {
        return noticeService.modifyPost(noticeDto);
    }
    
    @PostMapping(path = "/file", params = { "!bulk" })
    public Map<String, String> createPostImg(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String fileLink;
        
        try {
            BoardFileEntity boardFileEntity = noticeService.uploadFile(file);
            
            Thread.sleep(1000);
            
            fileLink = "/api/app/data-uploads/image/board/" + boardFileEntity.getSaveFileNm();
            
            // {"link" : "/image/201905/e98ff4f7-93a3-4aeb-813b-12f20a03db96.jpg"}
            return ImmutableMap.of("link", fileLink);

        } catch (Exception ex) {
            ex.printStackTrace();

            return ImmutableMap.of("error", ex.getMessage());
        }
    }
    
}