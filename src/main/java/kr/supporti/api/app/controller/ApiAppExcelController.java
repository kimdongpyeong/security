package kr.supporti.api.app.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.supporti.api.app.service.ApiAppDownloadExcelService;
import kr.supporti.api.common.dto.CalculateParamDto;
import kr.supporti.api.common.dto.SalesHistoryParamDto;
import kr.supporti.api.common.dto.StudentConsultingParamDto;

@RestController
@RequestMapping(path = "api/app/excels")
public class ApiAppExcelController {

    @Autowired
    ApiAppDownloadExcelService downloadExcelService;

    @GetMapping(path = "userList-download.xlsx", produces = {
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
    public byte[] downloadUserList() throws IOException {
        return downloadExcelService.downloadUserList();
    }
    
    @GetMapping(path = "salesList-download.xlsx", produces = {
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
    public byte[] downloadSalesList(
            @ModelAttribute SalesHistoryParamDto salesHistoryParamDto) throws IOException {
        return downloadExcelService.downloadSalesList(salesHistoryParamDto);
    }

    @GetMapping(path = "calculateList-download.xlsx", produces = {
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
    public byte[] downloadCalculateList(
            @ModelAttribute CalculateParamDto calculateParamDto) throws IOException {
        return downloadExcelService.downloadCalculateList(calculateParamDto);
    }

    @GetMapping(path = "consultingList-download.xlsx", produces = {
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" })
    public byte[] downloadConsultingList(
            @ModelAttribute StudentConsultingParamDto studentConsultingParamDto) throws IOException {
        return downloadExcelService.downloadConsultingList(studentConsultingParamDto);
    }
}
