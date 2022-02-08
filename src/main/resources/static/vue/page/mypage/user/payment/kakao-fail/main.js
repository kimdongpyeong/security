var MypageUserPaymentKakaoFailPage;
MypageUserPaymentKakaoFailPage = Vue.component("mypage-user-payment-kakao-fail-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/payment/kakao-fail/main.html")).data,
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
                let result = this.$route.query.result;

                self.close();
                opener.meta.alert(result);
                opener.location.href = "/main";
            }
        },
        "created": async function() {
        },
    });
});