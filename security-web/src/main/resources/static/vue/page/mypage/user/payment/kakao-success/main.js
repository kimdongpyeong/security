var MypageUserPaymentKakaoSuccessPage;
MypageUserPaymentKakaoSuccessPage = Vue.component("mypage-user-payment-kakao-success-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/payment/kakao-success/main.html")).data,
        "data": function() {
            return {
                "data": {
                },
            }
        },
        "watch": {
        },
        "methods": {
        },
        "mounted": async function() {

            if(opener == null) {
                await meta.alert("잘못된 접근입니다.");
                location.href = "/";
            } else {
                self.close();

                opener.meta.alert("결제가 완료되었습니다.");
                opener.location.href = "/main";
            }
        },
        "created": async function() {
        },
    });
});