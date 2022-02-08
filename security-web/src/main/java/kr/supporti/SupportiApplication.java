package kr.supporti;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.service.ApiAppAccountService;
import kr.supporti.api.common.dto.CodeParamDto;
import kr.supporti.api.common.entity.CodeEntity;
import kr.supporti.api.common.service.CodeService;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.util.menu.dto.TreeMenuDto;
import kr.supporti.common.util.menu.dto.TreeMenuParamDto;
import kr.supporti.common.util.menu.service.ApiUtilMenuService;

@SpringBootApplication
@EnableAsync
@EnableScheduling
public class SupportiApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(SupportiApplication.class, args);
    }
}

@RestController
@RequestMapping(path = "api")
class ApiController {

    @Autowired
    ApiAppAccountService accountService;

    @Autowired
    UserService userService;

    @Autowired
    CodeService codeService;

    @Autowired
    ApiUtilMenuService apiUtilMenuService;

    @GetMapping(path = "")
    public ResponseEntity<Object> main() {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "menu")
    public PageResponse<TreeMenuDto> getMainMenuList() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setRowSize(10000);
        List<String> sort = new ArrayList<>();
        sort.add("ranking");
        pageRequest.setSort(sort);
        return apiUtilMenuService.getTreeMenuList(TreeMenuParamDto.builder().isDefault(true).build(), pageRequest);
    }

    @GetMapping(path = "codes/{type}")
    public PageResponse<CodeEntity> getCodeList(@PathVariable(name = "type") String type) {
        Long parentId = null;
        Long parentIdInParentId = null;
        switch (type) {
        case "industry1":
            parentId = Long.valueOf(136);
            break;
        case "industry2":
            parentIdInParentId = Long.valueOf(136);
            break;
        case "technology":
            parentId = Long.valueOf(0);
            break;
        case "product-stage":
            parentId = Long.valueOf(0);
            break;
        case "growth-stage":
            parentId = Long.valueOf(13);
            break;
        default:
            parentId = Long.valueOf(0);
        }
        CodeParamDto codeParamDto = CodeParamDto.builder().status("T").parentId(parentId)
                .parentIdInParentId(parentIdInParentId).build();
        PageRequest pageRequest = new PageRequest();
        List<String> sort = new ArrayList<>();
        sort.add("id,asc");
        pageRequest.setSort(sort);
        pageRequest.setRowSize(10000000);
        return codeService.getCodeList(codeParamDto, pageRequest);
    }
}
