var MypageUserPaymentCardSuccessPage;
MypageUserPaymentCardSuccessPage = Vue.component("mypage-user-payment-card-success-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/payment/card-success/main.html")).data,
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
            let paymentKey = this.$route.query.paymentKey,
                orderId = this.$route.query.orderId,
                amount = this.$route.query.amount;

            if(paymentKey == null || paymentKey == '' ||
                 orderId == null || orderId == '' ||
                 amount == null || amount == '') {
                await meta.alert("잘못된 접근입니다.");
                this.$router.push("/");
                return;
            } else {
                let data = (await axios({
                    url: "/api/app/toss/card/success",
                    method: "get",
                    params: {
                        "paymentKey": paymentKey,
                        "orderId": orderId,
                        "amount": amount,
                    }
                })).data;

                if(data == "success") {
                    await meta.alert("결제가 완료되었습니다.");
                    this.$router.push("/mypage/users");
                } else {
                    this.$router.push("/mypage/users/payment/card-fail");
                }
            }
        },
        "created": async function() {
        },
    });
});