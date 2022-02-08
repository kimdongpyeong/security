var MypageUserPaymentCardFailPage;
MypageUserPaymentCardFailPage = Vue.component("mypage-user-payment-card-fail-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/payment/card-fail/main.html")).data,
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
            await meta.alert("결제 중에 오류가 발생했습니다.");
            this.$router.push({
                "path": "/mypage/users/payment",
                "query": {
                    "orderId": this.$route.query.orderId
                }
            });
        },
        "created": async function() {
        },
    });
});