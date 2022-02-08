package kr.supporti.api.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.common.util.FileUtil;

@RestController
@RequestMapping(path = "api/app/images")
public class ApiAppLoadImgController {

    @Autowired
    private FileUtil fileUtil;

//    @GetMapping(path = "{subPath}/{fileName}")
//    public void profileImage(
//            @PathVariable(name="subPath") String subPath,
//            @PathVariable(name="fileName") String fileName) throws IOException {
//        fileUtil.loadImg(subPath, fileName);
//    }

    @GetMapping(path = "")
    public ResponseEntity<Resource> loadImg(@RequestParam(name = "subpath") String subpath,
            @RequestParam(name = "filename") String filename) throws IOException {

        return fileUtil.loadImg(subpath, filename);
    }
}
