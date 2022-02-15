package kr.supporti.api.app.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.CalculateDto;
import kr.supporti.api.common.dto.CalculateParamDto;
import kr.supporti.api.common.dto.SalesHistoryParamDto;
import kr.supporti.api.common.dto.SalesTotalDto;
import kr.supporti.api.common.dto.StudentConsultingParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.EnvironmentVariableEntity;
import kr.supporti.api.common.entity.StudentConsultingEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.mapper.CalculateMapper;
import kr.supporti.api.common.mapper.EnvironmentVariableMapper;
import kr.supporti.api.common.mapper.SalesHistoryMapper;
import kr.supporti.api.common.mapper.StudentConsultingMapper;
import kr.supporti.api.common.mapper.UserMapper;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.validation.group.ReadValidationGroup;

@Validated
@Service
public class ApiAppDownloadExcelService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private SalesHistoryMapper salesHistoryMapper;
    
    @Autowired
    private CalculateMapper calculateMapper;

    @Autowired
    private StudentConsultingMapper studentConsultingMapper;
    
    @Autowired
    private EnvironmentVariableMapper environmentVariableMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public byte[] downloadUserList() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            XSSFCellStyle headerCellStyle = null;
            XSSFCellStyle bodyCellStyle = null;
            XSSFFont headerFont = null;
            XSSFFont bodyFont = null;
            headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontHeightInPoints((short) 11);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setFont(headerFont);
            bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setFont(bodyFont);

            sheet = workbook.createSheet("사용자");
            row = sheet.createRow(0);
            cell = row.createCell(0);
            cell.setCellValue("아이디");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(1);
            cell.setCellValue("이름");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("핸드폰번호");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellValue("역할");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(4);
            cell.setCellValue("상태");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(5);
            cell.setCellValue("가입일");
            cell.setCellStyle(headerCellStyle);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setRowSize(500000);
            UserParamDto userParamDto = UserParamDto.builder().build();
            List<UserEntity> userList = userMapper.selectUserList(userParamDto, pageRequest);

            Integer rowNum = 1;
            for (int i = 0; i < userList.size(); i++) {
                row = sheet.createRow(rowNum++);
                cell = row.createCell(0);
                cell.setCellValue(userList.get(i).getUsername() != null ? userList.get(i).getUsername() : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(1);
                cell.setCellValue(userList.get(i).getName() != null ? userList.get(i).getName() : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(userList.get(i).getPhoneNum() != null ? userList.get(i).getPhoneNum() : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(3);
                cell.setCellValue(userList.get(i).getRoleName() != null ? userList.get(i).getRoleName() : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(4);
                String status = (userList.get(i).getStatus() != null) ? userList.get(i).getStatus() : "";
                if (!status.equals("")) {
                    status = (status.equals("T")) ? "사용" : "삭제";
                }
                cell.setCellValue(status);
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(5);
                cell.setCellValue(userList.get(i).getCreatedDate() != null
                        ? userList.get(i).getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                        : "");
                cell.setCellStyle(bodyCellStyle);
            }

            sheet.setColumnWidth(0, 6000);
            sheet.setColumnWidth(1, 3000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 4000);
            sheet.setColumnWidth(5, 4000);
            sheet.setColumnWidth(6, 4000);
            sheet.setColumnWidth(7, 4000);
            sheet.setColumnWidth(8, 4000);
            sheet.setColumnWidth(9, 5000);

            workbook.write(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public byte[] downloadSalesList(SalesHistoryParamDto salesHistoryParamDto) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            XSSFCellStyle headerCellStyle = null;
            XSSFCellStyle bodyCellStyle = null;
            XSSFFont headerFont = null;
            XSSFFont bodyFont = null;
            headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontHeightInPoints((short) 12);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setFont(headerFont);
            bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setFont(bodyFont);

            sheet = workbook.createSheet("매출 내역");
            //해당하는 달의 매출내역 헤더
            row = sheet.createRow(1);
            cell = row.createCell(1);
            cell.setCellValue(salesHistoryParamDto.getSelectMonth()+" 매출 내역");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 4));
            row = sheet.createRow(2);
            cell = row.createCell(1);
            cell.setCellValue("날짜");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("생성");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellValue("결제");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(4);
            cell.setCellValue("날짜별 합계");
            cell.setCellStyle(headerCellStyle);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setRowSize(500000);
            List<SalesTotalDto> salesList = salesHistoryMapper.selectSalesTotalList(salesHistoryParamDto, pageRequest);
            List<SalesTotalDto> monthSalesList = salesHistoryMapper.selectSalesTermTotalList(salesHistoryParamDto, pageRequest);

            Integer rowNum = 3, salesSum = 0, paymentSum = 0, daySum = 0;
            DecimalFormat formatter = new DecimalFormat("###,###");

            //해당하는 달의 매출내역 body
            for (int i = 0; i < salesList.size(); i++) {
                SalesTotalDto salesDto = salesList.get(i);
                String salesDate = salesDto.getSalesDate();
                Integer salesTotal = salesDto.getSalesTotal();
                Integer paymentTotal = salesDto.getPaymentTotal();
                Integer dayTotal = salesDto.getDayTotal();
                
                salesSum += salesTotal;
                paymentSum += paymentTotal;
                daySum += dayTotal;
                
                row = sheet.createRow(rowNum++);
                cell = row.createCell(1);
                cell.setCellValue(salesDate != null ? salesDate : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(salesTotal != null ? formatter.format(salesTotal) : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(3);
                cell.setCellValue(paymentTotal != null ? formatter.format(paymentTotal) : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(4);
                cell.setCellValue(dayTotal != null ? formatter.format(dayTotal) : "");
                cell.setCellStyle(bodyCellStyle);
            }
            //해당하는 달의 매출내역 하단 total
            row = sheet.createRow(rowNum);
            cell = row.createCell(1);
            cell.setCellValue("Total");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue(salesSum != null ? formatter.format(salesSum) : "");
            cell.setCellStyle(bodyCellStyle);
            cell = row.createCell(3);
            cell.setCellValue(paymentSum != null ? formatter.format(paymentSum) : "");
            cell.setCellStyle(bodyCellStyle);
            cell = row.createCell(4);
            cell.setCellValue(daySum != null ? formatter.format(daySum) : "");
            cell.setCellStyle(headerCellStyle);

            //선택한 기간의 월별 총 매출
            row = sheet.createRow(rowNum + 3);
            cell = row.createCell(1);
            cell.setCellValue("월별 총 매출 내역 ( " + salesHistoryParamDto.getStartMonth() + " ~ " + salesHistoryParamDto.getEndMonth() + " )");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowNum + 3, rowNum + 3, 1, 2));
            row = sheet.createRow(rowNum + 4);
            cell = row.createCell(1);
            cell.setCellValue("날짜");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("총 매출");
            cell.setCellStyle(headerCellStyle);

            rowNum = rowNum + 5;
            //선택한 기간의 월별 총 매출 body
            for (int i = 0; i < monthSalesList.size(); i++) {
                SalesTotalDto monthSalesDto = monthSalesList.get(i);
                String month = monthSalesDto.getMonth();
                Integer monthTotal = monthSalesDto.getMonthTotal();
                
                row = sheet.createRow(rowNum++);
                cell = row.createCell(1);
                cell.setCellValue(month != null ? month : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(monthTotal != null ? formatter.format(monthTotal) : "");
                cell.setCellStyle(bodyCellStyle);
            }
            
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 6000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 4000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(8, 6000);
            sheet.setColumnWidth(9, 5000);

            workbook.write(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public byte[] downloadCalculateList(CalculateParamDto calculateParamDto) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            XSSFCellStyle headerCellStyle = null;
            XSSFCellStyle bodyCellStyle = null;
            XSSFFont headerFont = null;
            XSSFFont bodyFont = null;
            headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontHeightInPoints((short) 12);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setFont(headerFont);
            bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setFont(bodyFont);

            sheet = workbook.createSheet("매출 내역");
            //해당하는 달의 매출내역 헤더
            row = sheet.createRow(1);
            cell = row.createCell(1);
            cell.setCellValue("기간별 정산내역 ( " + calculateParamDto.getStartMonth() + " ~ " + calculateParamDto.getEndMonth() + " )");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(4);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(5);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(6);
            cell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 6));
            row = sheet.createRow(2);
            cell = row.createCell(1);
            cell.setCellValue("정산 대상월");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("정산 예상일");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellValue("정산 금액");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(4);
            cell.setCellValue("현금 입금액");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(5);
            cell.setCellValue("카드 결제액");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(6);
            cell.setCellValue("부과세");
            cell.setCellStyle(headerCellStyle);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setRowSize(500000);
            List<CalculateDto> calculateList = calculateMapper.selectCalculateList(calculateParamDto, pageRequest);
            UserEntity user = userMapper.selectUser(calculateParamDto.getCreatedBy());
            EnvironmentVariableEntity payDay = environmentVariableMapper.selectEnvironmentVariableKey("PAYMENT_DUE_DATE");
            
            Integer rowNum = 3;
            DecimalFormat formatter = new DecimalFormat("###,###");
            SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

            //해당하는 달의 매출내역 body
            for (int i = 0; i < calculateList.size(); i++) {
                CalculateDto calculateDto = calculateList.get(i);
                String month = calculateDto.getMonth();
                Date date = calculateDto.getDate();
                Integer cashTotal = calculateDto.getCashTotal();
                Integer cardTotal = calculateDto.getCardTotal();
                Integer total = calculateDto.getTotal();

                //정산 지급 예정일 
                Calendar cal1 = Calendar.getInstance();
                cal1.setTime(date);
                cal1.set(Calendar.MONTH, cal1.get(Calendar.MONTH) + 1);
                cal1.set(Calendar.DATE, Integer.parseInt(payDay.getValue()));
                String payDate = transFormat.format(cal1.getTime());
                
                double payMoney = 0;
                double levyMoney = 0;                
                //개인 또는 사업자에 따라 정산 금액 계산 다름
                if(user.getPaymentGeneral() != null) {
                    //개일일 때
                    if(user.getPaymentGeneral().equals("P")) {
                        payMoney = (cashTotal * 0.967) + (cardTotal * 0.96);
                    } else {
                        //사업자일 때
                        payMoney = (cashTotal * 0.89) + (cardTotal * 0.96);
                    }
                    levyMoney = total - payMoney;
                }
                
                row = sheet.createRow(rowNum++);
                cell = row.createCell(1);
                cell.setCellValue(month != null ? month : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(payDate != null ? payDate : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(3);
                cell.setCellValue(payMoney != 0 ? formatter.format(payMoney) : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(4);
                cell.setCellValue(cashTotal != null ? formatter.format(cashTotal) : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(5);
                cell.setCellValue(cardTotal != null ? formatter.format(cardTotal) : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(6);
                cell.setCellValue(levyMoney != 0 ? formatter.format(levyMoney) : "");
                cell.setCellStyle(bodyCellStyle);
            }
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 4000);
            sheet.setColumnWidth(3, 5000);
            sheet.setColumnWidth(4, 5000);
            sheet.setColumnWidth(5, 5000);
            sheet.setColumnWidth(6, 4000);

            workbook.write(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public byte[] downloadConsultingList(StudentConsultingParamDto studentConsultingParamDto) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = null;
            XSSFRow row = null;
            XSSFCell cell = null;
            XSSFCellStyle headerCellStyle = null;
            XSSFCellStyle bodyCellStyle = null;
            XSSFCellStyle consultingCellStyle = null;
            XSSFFont headerFont = null;
            XSSFFont bodyFont = null;
            headerFont = workbook.createFont();
            headerFont.setFontHeightInPoints((short) 12);
            headerFont.setBold(true);
            bodyFont = workbook.createFont();
            bodyFont.setFontHeightInPoints((short) 12);
            headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setAlignment(HorizontalAlignment.CENTER);
            headerCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            headerCellStyle.setBorderTop(BorderStyle.THIN);
            headerCellStyle.setBorderRight(BorderStyle.THIN);
            headerCellStyle.setBorderBottom(BorderStyle.THIN);
            headerCellStyle.setBorderLeft(BorderStyle.THIN);
            headerCellStyle.setFont(headerFont);
            bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.CENTER);
            bodyCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            bodyCellStyle.setWrapText(true);
            bodyCellStyle.setBorderTop(BorderStyle.THIN);
            bodyCellStyle.setBorderRight(BorderStyle.THIN);
            bodyCellStyle.setBorderBottom(BorderStyle.THIN);
            bodyCellStyle.setBorderLeft(BorderStyle.THIN);
            bodyCellStyle.setFont(bodyFont);
            consultingCellStyle = workbook.createCellStyle();
            consultingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            consultingCellStyle.setWrapText(true);
            consultingCellStyle.setBorderTop(BorderStyle.THIN);
            consultingCellStyle.setBorderRight(BorderStyle.THIN);
            consultingCellStyle.setBorderBottom(BorderStyle.THIN);
            consultingCellStyle.setBorderLeft(BorderStyle.THIN);
            consultingCellStyle.setFont(bodyFont);

            sheet = workbook.createSheet("매출 내역");
            //해당하는 달의 매출내역 헤더
            row = sheet.createRow(1);
            cell = row.createCell(1);
            cell.setCellValue(studentConsultingParamDto.getStudentNm() + "학생의 상담내역 ( " + studentConsultingParamDto.getStartDate() + " ~ " + studentConsultingParamDto.getEndDate() + " )");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellStyle(headerCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(1, 1, 1, 3));
            row = sheet.createRow(2);
            cell = row.createCell(1);
            cell.setCellValue("상담 날짜");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(2);
            cell.setCellValue("제목");
            cell.setCellStyle(headerCellStyle);
            cell = row.createCell(3);
            cell.setCellValue("상담 내용");
            cell.setCellStyle(headerCellStyle);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setRowSize(500000);
            List<StudentConsultingEntity> consultingList = studentConsultingMapper.selectConsultingList(studentConsultingParamDto, pageRequest);
            
            Integer rowNum = 3;
            //해당하는 달의 상담내역
            for (int i = 0; i < consultingList.size(); i++) {
                StudentConsultingEntity consultingEntity = consultingList.get(i);
                String date = consultingEntity.getConsultingDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String title = consultingEntity.getTitle();
                String contents = consultingEntity.getContents();

                row = sheet.createRow(rowNum++);
                cell = row.createCell(1);
                cell.setCellValue(date != null ? date : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(2);
                cell.setCellValue(title != null ? title : "");
                cell.setCellStyle(bodyCellStyle);
                cell = row.createCell(3);
                cell.setCellValue(contents != null ? contents : "");
                cell.setCellStyle(consultingCellStyle);
                
            }
            sheet.setColumnWidth(0, 2000);
            sheet.setColumnWidth(1, 4000);
            sheet.setColumnWidth(2, 6000);
            sheet.setColumnWidth(3, 15000);

            workbook.write(baos);
        } catch (IOException e) {
        }
        return baos.toByteArray();
    }

}