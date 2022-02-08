package kr.supporti.api.app.service;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.supporti.api.app.dto.NiceParamDto;
import kr.supporti.api.common.dto.UserParamDto;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.util.PageRequest;

@Service
public class ApiAppNiceService {

    @Value("${supporti.domain}")
    private String domain;

    @Value("${homepage.nice.auth.phone.id}")
    private String phoneId;

    @Value("${homepage.nice.auth.phone.pw}")
    private String phonePw;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public Map<String, Object> createSignUpCheckplus(String success, String fail) {
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

        String sSiteCode = phoneId; // NICE로부터 부여받은 사이트 코드
        String sSitePassword = phonePw; // NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = "REQ0000000001"; // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
                                                 // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가
        session.setAttribute("SIGN_UP_REQ_SEQ", sRequestNumber); // 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = ""; // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun = "N"; // Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize = ""; // 없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; // 없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        // 리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url :
        // http://www.~
        String sReturnUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + success; // 성공시
                                                                                                               // 이동될
                                                                                                               // URL
        String sErrorUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + fail; // 실패시 이동될
                                                                                                           // URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber + "8:SITECODE"
                + sSiteCode.getBytes().length + ":" + sSiteCode + "9:AUTH_TYPE" + sAuthType.getBytes().length + ":"
                + sAuthType + "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl + "7:ERR_URL"
                + sErrorUrl.getBytes().length + ":" + sErrorUrl + "11:POPUP_GUBUN" + popgubun.getBytes().length + ":"
                + popgubun + "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + "6:GENDER"
                + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if (iReturn == 0) {
            sEncData = niceCheck.getCipherData();
        } else if (iReturn == -1) {
            sMessage = "암호화 시스템 에러입니다.";
        } else if (iReturn == -2) {
            sMessage = "암호화 처리오류입니다.";
        } else if (iReturn == -3) {
            sMessage = "암호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        Map<String, Object> map = new WeakHashMap<>();
        map.put("sEncData", sEncData);
        map.put("code", iReturn);
        map.put("message", sMessage);
        return map;
    }

    public Map<String, Object> signUpCheckplusSuccess(NiceParamDto checkplusParamDto) {
        // 인증 후 결과값이 null로 나오는 부분은 관리담당자에게 문의 바랍니다.
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가

        Map<String, Object> map = new WeakHashMap<>();

        String sEncodeData = requestReplace(checkplusParamDto.getEncodeData(), "encodeData");

        String sSiteCode = phoneId; // NICE로부터 부여받은 사이트 코드
        String sSitePassword = phonePw; // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = ""; // 복호화한 시간
        String sRequestNumber = ""; // 요청 번호
        String sResponseNumber = ""; // 인증 고유번호
        String sAuthType = ""; // 인증 수단
        String sName = ""; // 성명
        String sDupInfo = ""; // 중복가입 확인값 (DI_64 byte)
        String sConnInfo = ""; // 연계정보 확인값 (CI_88 byte)
        String sBirthDate = ""; // 생년월일(YYYYMMDD)
        String sGender = ""; // 성별
        String sNationalInfo = ""; // 내/외국인정보 (개발가이드 참조)
        String sMobileNo = ""; // 휴대폰번호
        String sMobileCo = ""; // 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        if (iReturn == 0) {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);

            sRequestNumber = (String) mapresult.get("REQ_SEQ");
            sResponseNumber = (String) mapresult.get("RES_SEQ");
            sAuthType = (String) mapresult.get("AUTH_TYPE");
            sName = (String) mapresult.get("NAME");
            // sName = (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate = (String) mapresult.get("BIRTHDATE");
            sGender = (String) mapresult.get("GENDER");
            sNationalInfo = (String) mapresult.get("NATIONALINFO");
            sDupInfo = (String) mapresult.get("DI");
            sConnInfo = (String) mapresult.get("CI");
            sMobileNo = (String) mapresult.get("MOBILE_NO");
            sMobileCo = (String) mapresult.get("MOBILE_CO");

            String session_sRequestNumber = (String) session.getAttribute("SIGN_UP_REQ_SEQ");
            if (!sRequestNumber.equals(session_sRequestNumber)) {
                sMessage = "세션값 불일치 오류입니다. 새로고침 후 다시 시도해주세요.";
                iReturn = 1;
            } else {
                UserParamDto userParamDto = UserParamDto.builder().phoneNum(sMobileNo).status("T").build();

                List<UserEntity> userList = userService.getUserList(userParamDto, new PageRequest()).getItems();

                if (userList.isEmpty()) {
                    map.put("phoneNum", sMobileNo);
                    sMessage = "인증되었습니다.";
                } else {
                    sMessage = "이미 회원가입이 된 회원입니다.";
                    iReturn = 1;
                }
            }
        } else if (iReturn == -1) {
            sMessage = "복호화 시스템 오류입니다.";
        } else if (iReturn == -4) {
            sMessage = "복호화 처리 오류입니다.";
        } else if (iReturn == -5) {
            sMessage = "복호화 해쉬 오류입니다.";
        } else if (iReturn == -6) {
            sMessage = "복호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else if (iReturn == -12) {
            sMessage = "사이트 패스워드 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        map.put("code", iReturn);
        map.put("message", sMessage);
        return map;
    }

    public Map<String, Object> createFindAccountCheckplus(String success, String fail) {
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

        String sSiteCode = phoneId; // NICE로부터 부여받은 사이트 코드
        String sSitePassword = phonePw; // NICE로부터 부여받은 사이트 패스워드

        String sRequestNumber = "REQ0000000003"; // 요청 번호, 이는 성공/실패후에 같은 값으로 되돌려주게 되므로
                                                 // 업체에서 적절하게 변경하여 쓰거나, 아래와 같이 생성한다.
        sRequestNumber = niceCheck.getRequestNO(sSiteCode);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가
        session.setAttribute("ACCOUNT_REQ_SEQ", sRequestNumber); // 해킹등의 방지를 위하여 세션을 쓴다면, 세션에 요청번호를 넣는다.

        String sAuthType = ""; // 없으면 기본 선택화면, M: 핸드폰, C: 신용카드, X: 공인인증서

        String popgubun = "N"; // Y : 취소버튼 있음 / N : 취소버튼 없음
        String customize = ""; // 없으면 기본 웹페이지 / Mobile : 모바일페이지

        String sGender = ""; // 없으면 기본 선택 값, 0 : 여자, 1 : 남자

        // CheckPlus(본인인증) 처리 후, 결과 데이타를 리턴 받기위해 다음예제와 같이 http부터 입력합니다.
        // 리턴url은 인증 전 인증페이지를 호출하기 전 url과 동일해야 합니다. ex) 인증 전 url : http://www.~ 리턴 url :
        // http://www.~
        String sReturnUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + success; // 성공시
                                                                                                               // 이동될
                                                                                                               // URL
        String sErrorUrl = request.getRequestURL().toString().replace(request.getRequestURI(), "") + fail; // 실패시 이동될
                                                                                                           // URL

        // 입력될 plain 데이타를 만든다.
        String sPlainData = "7:REQ_SEQ" + sRequestNumber.getBytes().length + ":" + sRequestNumber + "8:SITECODE"
                + sSiteCode.getBytes().length + ":" + sSiteCode + "9:AUTH_TYPE" + sAuthType.getBytes().length + ":"
                + sAuthType + "7:RTN_URL" + sReturnUrl.getBytes().length + ":" + sReturnUrl + "7:ERR_URL"
                + sErrorUrl.getBytes().length + ":" + sErrorUrl + "11:POPUP_GUBUN" + popgubun.getBytes().length + ":"
                + popgubun + "9:CUSTOMIZE" + customize.getBytes().length + ":" + customize + "6:GENDER"
                + sGender.getBytes().length + ":" + sGender;

        String sMessage = "";
        String sEncData = "";

        int iReturn = niceCheck.fnEncode(sSiteCode, sSitePassword, sPlainData);
        if (iReturn == 0) {
            sEncData = niceCheck.getCipherData();
        } else if (iReturn == -1) {
            sMessage = "암호화 시스템 에러입니다.";
        } else if (iReturn == -2) {
            sMessage = "암호화 처리오류입니다.";
        } else if (iReturn == -3) {
            sMessage = "암호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        Map<String, Object> map = new WeakHashMap<>();
        map.put("sEncData", sEncData);
        map.put("code", iReturn);
        map.put("message", sMessage);
        return map;
    }

    public Map<String, Object> findAccountCheckplusSuccess(NiceParamDto checkplusParamDto) {
        // 인증 후 결과값이 null로 나오는 부분은 관리담당자에게 문의 바랍니다.
        NiceID.Check.CPClient niceCheck = new NiceID.Check.CPClient();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가

        Map<String, Object> map = new WeakHashMap<>();

        String sEncodeData = requestReplace(checkplusParamDto.getEncodeData(), "encodeData");

        String sSiteCode = phoneId; // NICE로부터 부여받은 사이트 코드
        String sSitePassword = phonePw; // NICE로부터 부여받은 사이트 패스워드

        String sCipherTime = ""; // 복호화한 시간
        String sRequestNumber = ""; // 요청 번호
        String sResponseNumber = ""; // 인증 고유번호
        String sAuthType = ""; // 인증 수단
        String sName = ""; // 성명
        String sDupInfo = ""; // 중복가입 확인값 (DI_64 byte)
        String sConnInfo = ""; // 연계정보 확인값 (CI_88 byte)
        String sBirthDate = ""; // 생년월일(YYYYMMDD)
        String sGender = ""; // 성별
        String sNationalInfo = ""; // 내/외국인정보 (개발가이드 참조)
        String sMobileNo = ""; // 휴대폰번호
        String sMobileCo = ""; // 통신사
        String sMessage = "";
        String sPlainData = "";

        int iReturn = niceCheck.fnDecode(sSiteCode, sSitePassword, sEncodeData);
        if (iReturn == 0) {
            sPlainData = niceCheck.getPlainData();
            sCipherTime = niceCheck.getCipherDateTime();

            // 데이타를 추출합니다.
            java.util.HashMap mapresult = niceCheck.fnParse(sPlainData);

            sRequestNumber = (String) mapresult.get("REQ_SEQ");
            sResponseNumber = (String) mapresult.get("RES_SEQ");
            sAuthType = (String) mapresult.get("AUTH_TYPE");
            sName = (String) mapresult.get("NAME");
            // sName = (String)mapresult.get("UTF8_NAME"); //charset utf8 사용시 주석 해제 후 사용
            sBirthDate = (String) mapresult.get("BIRTHDATE");
            sGender = (String) mapresult.get("GENDER");
            sNationalInfo = (String) mapresult.get("NATIONALINFO");
            sDupInfo = (String) mapresult.get("DI");
            sConnInfo = (String) mapresult.get("CI");
            sMobileNo = (String) mapresult.get("MOBILE_NO");
            sMobileCo = (String) mapresult.get("MOBILE_CO");

            String session_sRequestNumber = (String) session.getAttribute("ACCOUNT_REQ_SEQ");
            if (!sRequestNumber.equals(session_sRequestNumber)) {
                sMessage = "세션값 불일치 오류입니다. 새로고침 후 다시 시도해주세요.";
                iReturn = 1;
            } else {
                UserParamDto userParamDto = UserParamDto.builder().username(checkplusParamDto.getUsername())
                        .name(checkplusParamDto.getName()).status("T").phoneNum(sMobileNo).build();
                List<UserEntity> userList = userService.getUserList(userParamDto, new PageRequest()).getItems();

                if (!userList.isEmpty()) {
                    sMessage = "인증되었습니다. 계속 진행해주세요.";
                    map.put("username", userList.get(0).getUsername());
                    map.put("name", userList.get(0).getName());
                    map.put("phoneNum", sMobileNo);
                } else {
                    sMessage = "존재하지 않는 정보입니다.";
                    iReturn = 1;
                }
            }
        } else if (iReturn == -1) {
            sMessage = "복호화 시스템 오류입니다.";
        } else if (iReturn == -4) {
            sMessage = "복호화 처리 오류입니다.";
        } else if (iReturn == -5) {
            sMessage = "복호화 해쉬 오류입니다.";
        } else if (iReturn == -6) {
            sMessage = "복호화 데이터 오류입니다.";
        } else if (iReturn == -9) {
            sMessage = "입력 데이터 오류입니다.";
        } else if (iReturn == -12) {
            sMessage = "사이트 패스워드 오류입니다.";
        } else {
            sMessage = "알수 없는 에러 입니다. iReturn : " + iReturn;
        }

        map.put("code", iReturn);
        map.put("message", sMessage);
        return map;
    }

    public Map<String, Object> getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가
        Map<String, Object> map = new WeakHashMap<>();
        if (session.getAttribute("NMCHK_BIRTH") == null) {
            if (session.getAttribute("NMCHK_USERNAME") == null) {
                return null;
            } else {
                map.put("name", session.getAttribute("NMCHK_NAME"));
                map.put("username", session.getAttribute("NMCHK_USERNAME"));
                map.put("password", session.getAttribute("NMCHK_PASSWORD"));
            }
        } else {
            map.put("name", session.getAttribute("NMCHK_NAME"));
            map.put("nmdid", session.getAttribute("NMCHK_DID"));
            map.put("nmcid", session.getAttribute("NMCHK_CID"));
            map.put("birth", session.getAttribute("NMCHK_BIRTH"));
            map.put("sex", session.getAttribute("NMCHK_SEX").equals("1") ? "M" : "W");
        }
        session.removeAttribute("NMCHK_NAME");
        session.removeAttribute("NMCHK_DID");
        session.removeAttribute("NMCHK_CID");
        session.removeAttribute("NMCHK_BIRTH");
        session.removeAttribute("NMCHK_SEX");
        session.removeAttribute("NMCHK_JID");
        session.removeAttribute("NMCHK_USERNAME");
        session.removeAttribute("NMCHK_PASSWORD");
        return map;
    }

    public void removeSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest(); // 추가
        HttpSession session = request.getSession(); // 추가
        session.removeAttribute("NMCHK_NAME");
        session.removeAttribute("NMCHK_DID");
        session.removeAttribute("NMCHK_CID");
        session.removeAttribute("NMCHK_BIRTH");
        session.removeAttribute("NMCHK_SEX");
        session.removeAttribute("NMCHK_JID");
    }

    // 문자열 점검 함수
    private String requestReplace(String paramValue, String gubun) {
        String result = "";

        if (paramValue != null) {

            paramValue = paramValue.replaceAll("<", "&lt;").replaceAll(">", "&gt;");

            paramValue = paramValue.replaceAll("\\*", "");
            paramValue = paramValue.replaceAll("\\?", "");
            paramValue = paramValue.replaceAll("\\[", "");
            paramValue = paramValue.replaceAll("\\{", "");
            paramValue = paramValue.replaceAll("\\(", "");
            paramValue = paramValue.replaceAll("\\)", "");
            paramValue = paramValue.replaceAll("\\^", "");
            paramValue = paramValue.replaceAll("\\$", "");
            paramValue = paramValue.replaceAll("'", "");
            paramValue = paramValue.replaceAll("@", "");
            paramValue = paramValue.replaceAll("%", "");
            paramValue = paramValue.replaceAll(";", "");
            paramValue = paramValue.replaceAll(":", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll("#", "");
            paramValue = paramValue.replaceAll("--", "");
            paramValue = paramValue.replaceAll("-", "");
            paramValue = paramValue.replaceAll(",", "");

            if (gubun != "encodeData") {
                paramValue = paramValue.replaceAll("\\+", "");
                paramValue = paramValue.replaceAll("/", "");
                paramValue = paramValue.replaceAll("=", "");
            }
            result = paramValue;

        }
        return result;
    }

}
