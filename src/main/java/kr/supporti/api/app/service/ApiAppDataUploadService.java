package kr.supporti.api.app.service;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.supporti.api.common.entity.BoardFileEntity;
import kr.supporti.api.common.entity.LecturerBoardFileEntity;
import kr.supporti.api.common.mapper.BoardFileMapper;
import kr.supporti.api.common.mapper.LecturerBoardFileMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ApiAppDataUploadService {

    @Value("${file.upload.path}")
    private String path;

    @Value("${file.upload.class.attch-data.path}")
    private String classAttchPath;
    
    @Autowired
    private BoardFileMapper boardFileMapper;

    @Autowired
    private LecturerBoardFileMapper lecturerBoardFileMapper;

    public void getImage(HttpServletRequest request, HttpServletResponse response, String subPath, String filename)
            throws IOException {

        String fileDownloadPath = "";
        String fileNm = "";

        // 파일 저장 경로 및 확장자 정보 가져오기

        if (filename != null && !filename.equals("")) {
            fileNm = filename;
            fileDownloadPath = path + File.separator + subPath;
        }

        File file = null;
        try {
            file = new File(fileDownloadPath, fileNm);
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw ex;
        }

        try (InputStream in = new FileInputStream(file);
                BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());) {
            if (file.exists()) {
                if (!fileNm.equals("")) {
                    String fileType = fileNm.substring(fileNm.lastIndexOf(".") + 1);
                    
                    if (fileType.equals("hwp")){
                        response.setContentType("application/x-hwp");
                    } else if (fileType.equals("pdf")){
                        response.setContentType("application/pdf");
                    } else if (fileType.equals("ppt") || fileType.equals("pptx")){
                        response.setContentType("application/vnd.ms-powerpoint");
                    } else if (fileType.equals("doc") || fileType.equals("docx")){
                        response.setContentType("application/msword");
                    } else if (fileType.equals("xls") || fileType.equals("xlsx")){
                        response.setContentType("application/vnd.ms-excel");
                    } else {
                        response.setContentType("image/" + fileNm.substring(fileNm.lastIndexOf(".") + 1));
                    }
                } else {
                    response.setContentType("image/gif");
                }
                if(subPath.equals("board")) {
                    try{
                        BoardFileEntity boardFileEntity = boardFileMapper.selectFileOriginNm(filename);
                        fileNm = boardFileEntity.getOriginFileNm();
                    } catch(Exception ie) {
                        LecturerBoardFileEntity lecturerBoardFileEntity = lecturerBoardFileMapper.selectFileOriginNm(filename);
                        fileNm = lecturerBoardFileEntity.getOriginFileNm();
                    }
                }
                response.setHeader("Content-Disposition", "inline;filename=" + new String(fileNm.getBytes("UTF-8"), "ISO-8859-1"));

                int len;
                byte[] buf = new byte[1024];
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

            }
        } catch (IOException ie) {
            throw ie;
        } catch (Exception e) {
            throw e;
        }
    }

    public byte[] getFile(String subPath, String filename) throws IOException {

        return Files.readAllBytes(Paths.get(path + File.separator + subPath + File.separator + filename));
    }

    public void CompressZIP(HttpServletRequest request, HttpServletResponse response, String subPath, String zipname,
            List<String> fileList, List<String> originFileList) throws IOException {

        List<String> files = fileList;
        List<String> originFiles = originFileList;

        String zipDownloadPath = "";
        ZipOutputStream zout = null;
        String zipName = ""; // ZIP 압축 파일명

        if (files.size() > 0) {
            try {
                zipDownloadPath = path + File.separator + subPath + File.separator;
                zipName = zipname + ".zip";

                // ZIP파일 압축 START
                zout = new ZipOutputStream(new FileOutputStream(zipDownloadPath + zipName));
                byte[] buffer = new byte[1024];
                FileInputStream in = null;

                for (int k = 0; k < files.size(); k++) {
                    in = new FileInputStream(zipDownloadPath + files.get(k)); // 압축 대상 파일
                    zout.putNextEntry(new ZipEntry(originFiles.get(k))); // 압축파일에 저장될 파일명

                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zout.write(buffer, 0, len); // 읽은 파일을 ZipOutputStream에 Write
                    }
                    zout.closeEntry();
                    in.close();
                }

                zout.close(); // ZIP파일 압축 END

                // 파일다운로드 START
                response.setContentType("application/zip");
                response.addHeader("Content-Disposition", "attachment;filename=" + zipName);

                FileInputStream fis = new FileInputStream(zipDownloadPath + zipName);
                BufferedInputStream bis = new BufferedInputStream(fis);
                ServletOutputStream so = response.getOutputStream();
                BufferedOutputStream bos = new BufferedOutputStream(so);

                int n = 0;
                while ((n = bis.read(buffer)) > 0) {
                    bos.write(buffer, 0, n);
                    bos.flush();
                }
                if (bos != null)
                    bos.close();
                if (bis != null)
                    bis.close();
                if (so != null)
                    so.close();
                if (fis != null)
                    fis.close();
                // 파일다운로드 END
            } catch (IOException e) {
                throw e;
            } finally {
                if (zout != null)
                    zout = null;
            }
        }
    }

}
