package kr.supporti.api.app.controller;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.service.ApiAppDataUploadService;

@RestController
@RequestMapping(path = "api/app/data-uploads")
public class ApiAppDataUploadController {

    @Autowired
    private ApiAppDataUploadService apiAppDataUploadService;

    @GetMapping(path = "/image/{subPath}/{filename}")
    public void getImage(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "subPath") String subPath, @PathVariable(name = "filename") String filename)
            throws IOException {
        apiAppDataUploadService.getImage(request, response, subPath, filename);
    }

    @GetMapping(path = "/file/{subPath}/{filename}")
    public byte[] getFile(@PathVariable(name = "subPath") String subPath,
            @PathVariable(name = "filename") String filename) throws IOException {
        return apiAppDataUploadService.getFile(subPath, filename);
    }

    @GetMapping(path = "/file/class/{subPath}/{filename}")
    public byte[] getFileClass(@PathVariable(name = "subPath") String subPath,
            @PathVariable(name = "filename") String filename) throws IOException {
        return apiAppDataUploadService.getFile("class" + File.separator + subPath, filename);
    }

    @GetMapping(path = "/zip/class/{subPath}/{classId}")
    public void getFileZip(HttpServletRequest request, HttpServletResponse response,
            @PathVariable(name = "subPath") String subPath, @PathVariable(name = "classId") Long classId,
            @RequestParam(name = "zipname") String zipname, @RequestParam(name = "fileList") List<String> fileList,
            @RequestParam(name = "originFileList") List<String> originFileList) throws IOException {
        apiAppDataUploadService.CompressZIP(request, response, "class" + File.separator + subPath, zipname, fileList,
                originFileList);
    }
}
