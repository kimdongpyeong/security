package kr.supporti.common;

public class Constants {

    /*
     * 결제상태 (진행중, 결제완료, 취소요청, 환불완료)
     */
    public static final String PAYMENT_STATE_ING = "I";
    public static final String PAYMENT_STATE_SUCCESS = "S";
    public static final String PAYMENT_STATE_REFUND = "R";
    public static final String PAYMENT_STATE_REFUND_REJECT = "RR";
    public static final String PAYMENT_STATE_CANCEL_REQUEST = "C";
    public static final String PAYMENT_STATE_CANCEL_YES = "CY";

    /*
     * 결제유형 (정기, 단건)
     */
    public static final String PAYMENT_TYPE_REGULAR = "R";
    public static final String PAYMENT_TYPE_SINGLE = "S";

    /*
     * 결제방법 (카드, 현금)
     */
    public static final String PAYMENT_METHOD_CARD = "PM01";
    public static final String PAYMENT_METHOD_MONEY = "PM02";

    /*
     * 결제자 구분(개인, 사업자)
     */
    public static final String PAYMENT_GENERAL_PERSON = "P";
    public static final String PAYMENT_GENERAL_BUSINESS = "B";

    /*
     * 결제수단 (카카오, 토스)
     */
    public static final String PAYMENT_DIV_KAKAO = "K";
    public static final String PAYMENT_DIV_TOSS = "T";

    /*
     * 카카오페이 CID (단건, 정기)
     */
    public static final String SINGLE_CID = "TC0ONETIME";
    public static final String REGULAR_CID = "TCSUBSCRIP";
}
