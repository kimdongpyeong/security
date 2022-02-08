package kr.supporti.common.scheduler;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import kr.supporti.api.common.dto.PaymentHistoryParamDto;
import kr.supporti.api.common.dto.RegularPaymentHistoryParamDto;
import kr.supporti.api.common.entity.PaymentHistoryEntity;
import kr.supporti.api.common.mapper.PaymentHistoryMapper;
import kr.supporti.api.common.mapper.RegularPaymentHistoryMapper;
import kr.supporti.api.common.service.KakaoPayService;
import kr.supporti.common.Constants;
import kr.supporti.common.util.PageRequest;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class KakaoPayScheduler {

    @Autowired
    private KakaoPayService kakaoPayService;

    @Autowired
    private PaymentHistoryMapper paymentHistoryMapper;

    @Autowired
    private RegularPaymentHistoryMapper regularPaymentHistoryMapper;

    // 정기결제
    @Scheduled (cron = "0 0 6 * * *")
    @Async
    public void regularPaymentScheduler () {

        PaymentHistoryParamDto paymentHistoryParamDto = new PaymentHistoryParamDto();

        LocalDate nowDate = LocalDate.now();
        LocalDate lastDate = nowDate.withDayOfMonth(nowDate.lengthOfMonth());

        paymentHistoryParamDto.setState(Constants.PAYMENT_STATE_SUCCESS);
        paymentHistoryParamDto.setPaymentType(Constants.PAYMENT_TYPE_REGULAR);
        paymentHistoryParamDto.setPaymentDiv(Constants.PAYMENT_DIV_KAKAO);

        // 월의 말일인 경우
        if(nowDate.equals(lastDate)) {
            paymentHistoryParamDto.setSearchLastDay(lastDate.getDayOfMonth());
        } else {
            paymentHistoryParamDto.setSearchDay(nowDate.getDayOfMonth());
        }

        PageRequest pageRequest = new PageRequest();
        pageRequest.setRowSize(100000);

        List<PaymentHistoryEntity> paymentHistoryList = paymentHistoryMapper.selectPaymentHistoryList(paymentHistoryParamDto, pageRequest);

        paymentHistoryList.forEach(x -> {
            RegularPaymentHistoryParamDto paramDto = RegularPaymentHistoryParamDto.builder()
                    .paymentHistoryId(x.getId())
                    .paymentDateMonth(LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM")))
                    .build();

            Integer cnt = regularPaymentHistoryMapper.selectRegularPaymentHistoryListCount(paramDto);

            if(cnt == 0) {
                try {
                    kakaoPayService.regularPayment(x.getId());
                } catch (IOException e) {
                } catch (ParseException e) {
                }
            }
        });
    }
}
