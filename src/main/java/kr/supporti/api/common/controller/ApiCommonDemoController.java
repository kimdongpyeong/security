package kr.supporti.api.common.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.common.dto.DemoDto;
import kr.supporti.api.common.dto.DemoParamDto;
import kr.supporti.api.common.entity.DemoEntity;
import kr.supporti.api.common.repository.DemoRepository;
import kr.supporti.api.common.service.DemoService;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;

@RestController
@RequestMapping(path = "api/common/demo")
public class ApiCommonDemoController {

    @Autowired
    private DemoService demoService;

    @Autowired
    private DemoRepository demoRepository;

    @GetMapping(path = "")
    public PageResponse<DemoEntity> getDemoList(
            @ModelAttribute DemoParamDto demoParamDto, @ModelAttribute PageRequest pageRequest) {
        return demoService.getDemoList(demoParamDto, pageRequest);
    }

    @GetMapping(path = "{id}")
    public DemoEntity getDemo(@PathVariable(name = "id") Long id) {
        return demoService.getDemo(id);
    }

    @PostMapping(path = "", params = { "!bulk" })
    public DemoEntity createDemo(@ModelAttribute DemoDto demoDto) {
        return demoService.createDemo(demoDto);
    }

    @PutMapping(path = "", params = { "!bulk" })
    public DemoEntity modifyDemo(@ModelAttribute DemoDto demoDto) {
        return demoService.modifyDemo(demoDto);
    }

    @DeleteMapping(path = "{id}", params = { "!bulk" })
    public void removeDemo(@PathVariable(name = "id") Long id) {
        demoRepository.deleteById(id);
    }
}