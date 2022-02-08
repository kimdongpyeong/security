package kr.supporti.api.common.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.collect.ImmutableMap;

import kr.supporti.api.app.dto.AccountDto;
import kr.supporti.api.common.dto.ClassRoomLiveParamDto;
import kr.supporti.api.common.dto.LecturerBoardDto;
import kr.supporti.api.common.dto.LecturerDataDto;
import kr.supporti.api.common.dto.LecturerDataFileDto;
import kr.supporti.api.common.dto.LecturerDataParamDto;
import kr.supporti.api.common.entity.LecturerBoardEntity;
import kr.supporti.api.common.entity.LecturerBoardFileEntity;
import kr.supporti.api.common.entity.LecturerDataEntity;
import kr.supporti.api.common.entity.LecturerDataFileEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.MypageService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/mypage")
public class ApiCommonMypageController {

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
    
    @Autowired
    private MypageService mypageService;
    
    @GetMapping(path = "/lecturer/board/list")
    public PageResponse<LecturerBoardEntity> getLecturerBoardList(@ModelAttribute LecturerBoardDto lecturerBoardDto,
            @ModelAttribute PageRequest pageRequest) {
        return mypageService.getLecturerBoardList(lecturerBoardDto, pageRequest);
    }
    
    @GetMapping(path = "/lecturer/board")
    public LecturerBoardEntity getLecturerBoard(@ModelAttribute LecturerBoardDto lecturerBoardDto) {
        return mypageService.getLecturerBoard(lecturerBoardDto);
    }

    @GetMapping(path = "{id}")
    public UserEntity getMypageUser(@PathVariable(name = "id") Long id) {
        return mypageService.getMypageUser(id);
    }

    @PutMapping(path = "")
    public void modifyUserInfo(@RequestBody AccountDto accountDto) {
        mypageService.modifyUserInfo(accountDto);
    }

    @PutMapping(path = "/introduce")
    public void modifyUserIntroduce(@RequestBody AccountDto accountDto) {
        mypageService.modifyUserIntroduce(accountDto);
    }

    @PutMapping(path = "/profile")
    public void modifyProfile(@ModelAttribute AccountDto accountDto) {
        mypageService.modifyProfile(accountDto);
    }

    @PutMapping(path = "{id}")
    public void removeUser(@PathVariable(name = "id") Long id, @ModelAttribute AccountDto accountDto) {
        mypageService.removeUser(accountDto);
    }

    @PutMapping(path = "{id}/password")
    public Map<String, Object> modifyPassword(@PathVariable(name = "id") Long id, @RequestBody AccountDto accountDto)
            throws ServletException {
        return mypageService.modifyPassword(id, accountDto.getOldPassword(), accountDto.getNewPassword());
    }
    
    @GetMapping(path = "/lecturer-data/{id}")
    public LecturerDataEntity getLecturerData(@PathVariable(name = "id") Long id) {
        return mypageService.getLecturerData(id);
    }
    
    @GetMapping(path = "/lecturer-data")
    public PageResponse<LecturerDataEntity> getLecturerDataList(@ModelAttribute LecturerDataDto lecturerDataDto,
            @ModelAttribute PageRequest pageRequest) {
        return mypageService.getLecturerDataList(lecturerDataDto, pageRequest);
    }
    
    @GetMapping(path = "/lecturer-data-file")
    public PageResponse<LecturerDataFileEntity> getLecturerDataFileList(@ModelAttribute LecturerDataFileDto lecturerDataFileDto,
            @ModelAttribute PageRequest pageRequest) {
        return mypageService.getLecturerDataFileList(lecturerDataFileDto, pageRequest);
    }

    @PostMapping(path = "/lecturer-data-sort")
    public void sortLecturerData(@ModelAttribute LecturerDataParamDto lecturerDataParamDto) {
        List<LecturerDataParamDto> params = lecturerDataParamDto.getParams();
        
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

    @PostMapping(path = "/lecturer-data")
    public LecturerDataEntity createLecturerData(@ModelAttribute LecturerDataParamDto lecturerDataParamDto) {
        return mypageService.createLecturerData(lecturerDataParamDto);
    }
    
    @PutMapping(path = "/lecturer-data")
    public void modifyLecturerData(@ModelAttribute LecturerDataParamDto lecturerDataParamDto) {
        mypageService.modifyLecturerData(lecturerDataParamDto);
    }
    
    @PostMapping(path = "/lecturer/board/save", params = { "!bulk" })
    public LecturerBoardEntity createPost(@ModelAttribute LecturerBoardDto lecturerBoardDto) {
        return mypageService.createPost(lecturerBoardDto);
    }

    @PutMapping(path = "/lecturer/board/modify", params = { "!bulk" })
    public LecturerBoardEntity modifyPost(@ModelAttribute LecturerBoardDto lecturerBoardDto) {
        return mypageService.modifyPost(lecturerBoardDto);
    }
    
    @PostMapping(path = "/lecturer/board/file", params = { "!bulk" })
    public Map<String, String> createPostImg(HttpServletRequest request, @RequestParam("file") MultipartFile file) {
        String fileLink;
        
        try {
            LecturerBoardFileEntity lecturerBoardFileEntity = mypageService.uploadFile(file);
            
            Thread.sleep(1000);
            
            fileLink = "/api/app/data-uploads/image/board/" + lecturerBoardFileEntity.getSaveFileNm();
            
            // {"link" : "/image/201905/e98ff4f7-93a3-4aeb-813b-12f20a03db96.jpg"}
            return ImmutableMap.of("link", fileLink);

        } catch (Exception ex) {
            ex.printStackTrace();

            return ImmutableMap.of("error", ex.getMessage());
        }
    }
    
}
