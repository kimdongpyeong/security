package kr.supporti.common.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class FileUtil {

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

    // 이미지 로드 공통
//    public void loadImg(String subPath, String fileName) throws IOException {
//
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpServletResponse res = attr.getResponse();
//        OutputStream os = res.getOutputStream();
//
//        String ext = "jpg";
//        String path = "";
//        String basicImg = "";
//
//        if (subPath.equals("profile")) {
//            path = profilePath;
//            basicImg = "static/resources/img/profile.png";
//        } else if (subPath.equals("liveCat")) {
//            path = liveCatPath;
//            basicImg = "static/resources/img/banner_sample.png";
//        } else if (subPath.equals("roomprofile")) {
//            path = roomProfilePath;
//            basicImg = "static/resources/img/profile.png";
//        } else if (subPath.equals("classRoomProfile")) {
//            path = classRoomProfilePath;
//            basicImg = "static/resources/img/profile.png";
//        }
//
//        try {
//
//            int idx = fileName.lastIndexOf(".") + 1;
//            ext = fileName.substring(idx, fileName.length());
//
//            if (!ext.equalsIgnoreCase("gif")) {
//                BufferedImage originalImg = null;
//                try {
//                    originalImg = ImageIO.read(new File(filePath + File.separator + path + File.separator + fileName));
//                } catch (IOException e) {
//                    ClassPathResource classPathResource = new ClassPathResource(basicImg);
//                    originalImg = ImageIO.read(classPathResource.getInputStream());
//                    ext = "png";
//                }
//
//                res.setContentType("image/" + ext);
//                ImageIO.write(originalImg, ext, os);
//
//                os.flush();
//                os.close();
//            } else {
//                try {
//                    InputStream in = new FileInputStream(
//                            new File(filePath + File.separator + path + File.separator + fileName));
//                    this.copy(in, os);
//                } catch (Exception e) {
//
//                    BufferedImage originalImg = null;
//                    ClassPathResource classPathResource = new ClassPathResource(basicImg);
//                    originalImg = ImageIO.read(classPathResource.getInputStream());
//                    ext = "png";
//
//                    res.setContentType("image/" + ext);
//                    ImageIO.write(originalImg, ext, os);
//
//                    os.flush();
//                    os.close();
//                }
//            }
//
//        } catch (Exception e) {
//            BufferedImage originalImg = null;
//            ClassPathResource classPathResource = new ClassPathResource(basicImg);
//            originalImg = ImageIO.read(classPathResource.getInputStream());
//            ext = "png";
//
//            res.setContentType("image/" + ext);
//            ImageIO.write(originalImg, ext, os);
//
//            os.flush();
//            os.close();
//        }
//    }
//
//    public void copy(final InputStream in, final OutputStream out) throws IOException {
//        byte[] buffer = new byte[1024];
//        int count;
//
//        while ((count = in.read(buffer)) != -1) {
//            out.write(buffer, 0, count);
//        }
//        out.flush();
//    }

    public ResponseEntity<Resource> loadImg(String subPath, String fileName) throws IOException {

        String ext = "jpg";
        String path = "";
        String basicImg = "";

        if (subPath.equals("profile")) {
            path = profilePath;
            basicImg = "static/resources/img/profile.png";
        } else if (subPath.equals("classRoomProfile")) {
            path = classRoomProfilePath;
            basicImg = "static/resources/img/base.jpg";
        } else if (subPath.equals("classRoomLive")) {
            path = classRoomLivePath;
            basicImg = "static/resources/img/live_temp.png";
        }

        String imageRoot = filePath + File.separator + path;
        imageRoot = imageRoot + File.separator + fileName;

        Resource resource = new FileSystemResource(imageRoot);

        HttpHeaders header = new HttpHeaders();
        Path filePath = null;

        if (!resource.exists()) {
            try {
                ClassPathResource classPathResource = new ClassPathResource(basicImg);
                filePath = Paths.get(classPathResource.getURI());
                header.add("Content-Type", Files.probeContentType(filePath));
                resource = new FileSystemResource(filePath);
            } catch (Exception e) {
            }
            return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
        }

        try {
            filePath = Paths.get(imageRoot);
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (Exception e) {
        }
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}
