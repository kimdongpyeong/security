package kr.supporti.api.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.repository.LecturerUploadFileRepository;
import kr.supporti.api.common.repository.LecturerUploadRepository;
import kr.supporti.api.common.service.LecturerUploadService;
import kr.supporti.api.common.service.MypageService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "/api/common/student")
public class ApiCommonLecturerUploadController {

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
    private LecturerUploadService lecturerUploadService;
    
    @Autowired
    private LecturerUploadFileRepository lecturerUploadFileRepository;
    
    @GetMapping(path = "/detail/upload/{id}")
    public LecturerUploadEntity getLecturerUpload(@PathVariable(name = "id") Long id) {
        return lecturerUploadService.getLecturerUpload(id);
    }
    
    @GetMapping(path = "/detail/upload")
    public PageResponse<LecturerUploadEntity> getLecturerUploadList(@ModelAttribute LecturerUploadDto lecturerUploadDto,
            @ModelAttribute PageRequest pageRequest) {
        return lecturerUploadService.getLecturerUploadList(lecturerUploadDto, pageRequest);
    }
    
    @GetMapping(path = "/detail/upload-data-file")
    public PageResponse<LecturerUploadFileEntity> getLecturerUploadFileList(@ModelAttribute LecturerUploadFileDto lecturerUploadFileDto,
            @ModelAttribute PageRequest pageRequest) {
        return lecturerUploadService.getLecturerUploadFileList(lecturerUploadFileDto, pageRequest);
    }

    @PostMapping(path = "/detail/upload-data-sort")
    public void sortLecturerUpload(@ModelAttribute LecturerUploadParamDto lecturerUploadParamDto) {
        List<LecturerUploadParamDto> params = lecturerUploadParamDto.getParams();
        for(int i = 0; i < params.size(); i++) {
            Long dataId = params.get(i).getId();

            if(dataId == null) {
                lecturerUploadParamDto.setLecturerStudentId(lecturerUploadParamDto.getLecturerStudentId());
                lecturerUploadParamDto.setTitle(params.get(i).getTitle());
                lecturerUploadParamDto.setDesc(params.get(i).getDesc());
                lecturerUploadParamDto.setFiles(params.get(i).getFiles());
                lecturerUploadParamDto.setDeleteYn(params.get(i).getDeleteYn());
                createLecturerUpload(lecturerUploadParamDto);
            } else {
                lecturerUploadParamDto.setId(params.get(i).getId());;
                lecturerUploadParamDto.setTitle(params.get(i).getTitle());
                lecturerUploadParamDto.setDesc(params.get(i).getDesc());
                lecturerUploadParamDto.setFiles(params.get(i).getFiles());
                lecturerUploadParamDto.setDeleteYn(params.get(i).getDeleteYn());
                modifyLecturerUpload(lecturerUploadParamDto);
            }
        }
    }

    @PostMapping(path = "/detail/upload-data")
    public LecturerUploadEntity createLecturerUpload(@ModelAttribute LecturerUploadParamDto lecturerUploadParamDto) {
        return lecturerUploadService.createLecturerUpload(lecturerUploadParamDto);
    }
    
    @PutMapping(path = "/detail/upload-data")
    public void modifyLecturerUpload(@ModelAttribute LecturerUploadParamDto lecturerUploadParamDto) {
        lecturerUploadService.modifyLecturerUpload(lecturerUploadParamDto);
    }
    
    @PutMapping(path = "/detail/{id}")
    public Integer modifyLecturerUploadYn(@PathVariable(name = "id") Long id, @RequestBody LecturerUploadParamDto lecturerUploadParamDto) {
        return lecturerUploadService.modifyStudentUploadYn(id, lecturerUploadParamDto);
    }
    
    @DeleteMapping(path = "/detail/{id}")
    public void removeLecturerUpload(@PathVariable(name = "id") Long id) {
        lecturerUploadFileRepository.deleteById(id);
    }
    
    @GetMapping(path ="/detail/download")
    public void download(HttpServletResponse response, @ModelAttribute LecturerUploadFileDto lecturerUploadFileDto) throws Exception {
        try {
            /* String lectUploadOrgFilename = lecturerUploadFileDto.getOriginFileNm(); */
            String path = "C:\\Users\\kbg93\\OneDrive\\바탕 화면\\faq_Magnifier.png";
            System.out.println(path);
            
            File file = new File(path);
            response.setHeader("Content-Disposition", "attachment;filename=" + file.getName()); // 다운로드 되거나 로컬에 저장되는 용도로 쓰이는지를 알려주는 헤더
            
            FileInputStream fileInputStream = new FileInputStream(path); // 파일 읽어오기 
            OutputStream out = response.getOutputStream();
            int read = 0;
                byte[] buffer = new byte[1024];
                while ((read = fileInputStream.read(buffer)) != -1) { // 1024바이트씩 계속 읽으면서 outputStream에 저장, -1이 나오면 더이상 읽을 파일이 없음
                    out.write(buffer, 0, read);
                }
                
        } catch (Exception e) {
            throw new Exception("download error");
        }
//      String lectUploadOrgFilename = lecturerUploadFileDto.getOriginFileNm();
//      
//     byte[] fileByte = FileUtils.readFileToByteArray(new File("C:\\Users\\kbg93\\OneDrive\\바탕 화면\\"+lectUploadOrgFilename));
//     
//     //파일유형설정
//     response.setContentType("application/octet-stream");
//     //파일길이설정
//     response.setContentLength(fileByte.length);
//     //데이터형식/성향설정 (attachment: 첨부파일)
//     response.setHeader("Content-Disposition", "attachment; fileName=\"" + URLEncoder.encode(lectUploadOrgFilename,"UTF-8")+"\";");
//     //내용물 인코딩방식설정
//     response.setHeader("Content-Transfer-Encoding", "binary");
//     //버퍼의 출력스트림을 출력
//     response.getOutputStream().write(fileByte);
//     
//     System.out.println(fileByte);
//     System.out.println(response);
//     
//     //버퍼에 남아있는 출력스트림을 출력
//     response.getOutputStream().flush();
//     //출력스트림을 닫는다
//     response.getOutputStream().close();
    }
}
