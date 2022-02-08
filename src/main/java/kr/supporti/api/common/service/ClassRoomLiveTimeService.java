package kr.supporti.api.common.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import kr.supporti.api.common.dto.ClassRoomLiveTimeDto;
import kr.supporti.api.common.entity.ClassRoomLiveTimeEntity;
import kr.supporti.api.common.mapper.ClassRoomLiveTimeMapper;
import kr.supporti.api.common.repository.ClassRoomLiveTimeRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class ClassRoomLiveTimeService {

    @Autowired
    ClassRoomLiveTimeRepository classRoomLiveTimeRepository;

    @Autowired
    private ClassRoomLiveTimeMapper classRoomLiveTimeMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<ClassRoomLiveTimeEntity> getClassRoomLiveTimeList(@Valid ClassRoomLiveTimeDto liveTimeDto, PageRequest pageRequest) {
        Integer classRoomLiveTimeListCount = classRoomLiveTimeMapper.selectClassRoomLiveTimeListCount(liveTimeDto);
        List<ClassRoomLiveTimeEntity> classRoomLiveTimeList = classRoomLiveTimeMapper.selectClassRoomLiveTimeList(liveTimeDto, pageRequest);
        PageResponse<ClassRoomLiveTimeEntity> pageResponse = new PageResponse<>(pageRequest, classRoomLiveTimeListCount);
        pageResponse.setItems(classRoomLiveTimeList);
        return pageResponse;
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ClassRoomLiveTimeEntity getClassRoomLiveTime(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return classRoomLiveTimeMapper.selectClassRoomLiveTime(id);
    }

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public ClassRoomLiveTimeEntity getClassRoomIdLiveTime(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id) {
        return classRoomLiveTimeMapper.selectClassRoomIdLiveTime(id);
    }
    
    @Validated(value = { ReadValidationGroup.class })
    public void createZoomLink(Long liveTimeId, String code, HttpServletResponse response) throws MalformedURLException {
        String accessToken = null;
        WeakHashMap<String, Object> map = new WeakHashMap<>();

        URL url = new URL("https://zoom.us/oauth/token");

        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            // Request Header 정의
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8;");
            con.setRequestProperty("Authorization",
                    "Basic bUE0dnByMk1SZ2lZMzBENDB3T0NzZzpBZFpnYnFoNk4wcGRQWm1IOGxPdzdKeEZ4NGpveWxyRg==");
//            con.setRequestProperty("Authorization", "Basic UVgzWXJsblhRRnFEc1pDNzI1T19ZZzpDN2dzV0hXRFFIWGNReXpkTTBBMGFUNFNOcTRkRUZxTw==");
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);

            WeakHashMap<String, Object> params = new WeakHashMap<>();
            params.put("code", code);
            params.put("grant_type", "authorization_code");
//            params.put("redirect_uri", "http://localhost:9090/api/common/liveTime/createZoomLink?liveTimeId="+liveTimeId);
            params.put("redirect_uri", "http://meta-soft.iptime.org:9090/api/common/liveTime/createZoomLink?liveTimeId="+liveTimeId);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0)
                    postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            con.getOutputStream().write(postDataBytes);

            // 응답
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            JSONObject jsonObj = (JSONObject) JSONValue.parse(in.readLine());

            accessToken = jsonObj.get("access_token").toString();

            con.disconnect();
            in.close();

            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(1);
            pageRequest.setRowSize(10000);

            ClassRoomLiveTimeEntity liveTimeEntity = classRoomLiveTimeMapper.selectClassRoomLiveTime(liveTimeId);

            url = new URL("https://api.zoom.us/v2/users/me/meetings");

            LocalDateTime startTime = liveTimeEntity.getStartTime();
            LocalDateTime endTime = liveTimeEntity.getEndTime();

            Integer year = startTime.getYear();
            Integer month = startTime.getMonthValue();
            Integer day = startTime.getDayOfMonth();

            Integer hour = startTime.getHour();
            Integer minute = startTime.getMinute();

            Duration duration = Duration.between(startTime, endTime);
            Long endMinute = duration.getSeconds() / 60;

            con = (HttpURLConnection) url.openConnection();
            // Request Header 정의
            con.setRequestProperty("Content-Type", "application/json;");
            con.setRequestProperty("User-Agent", "Zoom-Jwt-Request");
            con.setRequestProperty("Authorization", "Bearer " + accessToken);
            con.setRequestProperty("Accept", "application/json");
            con.setRequestMethod("POST");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            con.setDoInput(true);
            con.setDoOutput(true);

            JSONObject reqParams = new JSONObject();
            reqParams.put("topic", liveTimeEntity.getTitle());
            reqParams.put("type", 2);
//            reqParams.put("start_time", "2019-08-30T22:00:00Z");
            reqParams.put("timezone", "Asia/Seoul");
            reqParams.put("duration", endMinute);

            JSONObject tempMap = new JSONObject();
            tempMap.put("host_video", true);
            tempMap.put("participant_video", true);
            tempMap.put("approval_type", 2);
//            tempMap.put("mute_upon_entry", false);
//            tempMap.put("watermark", false);
//            tempMap.put("use_pmi", true);
//            tempMap.put("registration_type", 1);
//            tempMap.put("audio", "both");
//            tempMap.put("auto_recording", "none");
//            tempMap.put("registrants_email_notification", false);
//            tempMap.put("waiting_room", false);
            tempMap.put("meeting_authentication", false);
            reqParams.put("settings", tempMap);
//            WeakHashMap<String, Object> breakMap = new WeakHashMap<>();
//            breakMap.put("enable", true);
//            breakMap.put("rooms", [{{"name": "회의실1", "participants": []}}]);
//            tempMap.put("breakout_room", false);

            OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
            os.write(reqParams.toString());
            os.flush();

            // 응답
            in = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
            jsonObj = (JSONObject) JSONValue.parse(in.readLine());

            con.disconnect();
            os.close();
            in.close();

            classRoomLiveTimeRepository.updateZoomLinkById(liveTimeId, jsonObj.get("join_url").toString());
            response.sendRedirect(jsonObj.get("join_url").toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public ClassRoomLiveTimeEntity createLiveTime(ClassRoomLiveTimeDto classRoomLiveTimeDto) throws MalformedURLException {

//        Integer endTime = Integer.parseInt(classRoomLiveTimeDto.getEndHour()) * 60 + Integer.parseInt(classRoomLiveParamDto.getEndMin());
//
//        String startDate = (String)liveTime.get(i).getStartDate();
//        String startTime = (String)liveTime.get(i).getStartTime();
//
//        Integer year = Integer.parseInt(startDate.substring(0, 4));
//        Integer month = Integer.parseInt(startDate.substring(4, 6));
//        Integer day = Integer.parseInt(startDate.substring(6, 8));
//
//        Integer hour = Integer.parseInt(startTime.substring(0, 2));
//        Integer minute = Integer.parseInt(startTime.substring(3));

        ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
//                .title(year + "년 " + month + "월 " + day + "일 라이브 미팅")
//                .startTime(LocalDateTime.of(year, month, day, hour, minute))
//                .endTime(LocalDateTime.of(year, month, day, hour, minute).plusMinutes(endTime))
                .build();
        classRoomLiveTimeEntity = classRoomLiveTimeRepository.save(classRoomLiveTimeEntity);

        return classRoomLiveTimeEntity;
    }

    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public void modifyLiveTime(@Valid @NotNull(groups = { ModifyValidationGroup.class }) ClassRoomLiveTimeDto classRoomLiveTimeDto) {

        Long id = classRoomLiveTimeDto.getId();
//        Integer endTime = Integer.parseInt(classRoomLiveParamDto.getEndHour()) * 60 + Integer.parseInt(classRoomLiveParamDto.getEndMin());

//        String startDate = (String)liveTime.get(i).getStartDate();
//        String startTime = (String)liveTime.get(i).getStartTime();

//        Integer year = Integer.parseInt(startDate.substring(0, 4));
//        Integer month = Integer.parseInt(startDate.substring(5, 7));
//        Integer day = Integer.parseInt(startDate.substring(8, 10));
//
//        Integer hour = Integer.parseInt(startTime.substring(0, 2));
//        Integer minute = Integer.parseInt(startTime.substring(3));

        ClassRoomLiveTimeEntity classRoomLiveTimeEntity = ClassRoomLiveTimeEntity.builder()
                .id(id)
                .title(classRoomLiveTimeDto.getTitle())
//                .startTime(LocalDateTime.of(year, month, day, hour, minute))
//                .endTime(LocalDateTime.of(year, month, day, hour, minute).plusMinutes(endTime))
                .build();
        classRoomLiveTimeMapper.modifyClassRoomLiveTime(classRoomLiveTimeEntity);
    }

    @Validated(value = { RemoveValidationGroup.class })
    @Transactional
    public void removeLiveTime(@Valid @NotNull(groups = { RemoveValidationGroup.class }) Long id) {
        classRoomLiveTimeRepository.delete(ClassRoomLiveTimeEntity.builder().id(id).build());
    }
}