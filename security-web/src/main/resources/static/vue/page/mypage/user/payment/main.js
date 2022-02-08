var MypageUserPaymentPage;
MypageUserPaymentPage = Vue.component("mypage-user-payment-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/payment/main.html")).data,
        "data": function() {
            return {
                "data": {
//                    "address": "http://meta-soft.iptime.org:9090",
                    "address": "http://localhost:9090",
                    "orderId": null,
                    "payment": {},
                    "paymentHistory": {},
                    "user": {},
                    "toss": {
                        "clientKey": "test_ck_5GePWvyJnrKaWe2q5ee3gLzN97Eo"
                    },
                },
                "select": {
                    "paymentDiv": [
                        {},
                    ]
                },
                "visible": {
                    "regularPaymentDialog": false,
                }
            }
        },
        "watch": {
        },
        "methods": {
            "init": async function() {
                let data = (await meta.api.common.paymentHistory.getPaymentHistoryByOrderId(this.data.orderId)).data;

                this.data.paymentHistory = data;
            },
            "requestKakaoPayment": async function() {
                let data = (await axios({
                    url: "/api/app/kakao/request/" + this.data.paymentHistory.id,
                    method: "get",
                })).data;

                var width = '500';
                var height = '650';

                var popupX = (window.screen.width / 2) - (width / 2);
                var popupY= (window.screen.height / 2) - (height / 2);

                window.open(data, "카카오페이 결제", 'width='+ width +', height='+ height +', left=' + popupX + ', top='+ popupY + "status=no, menubar=no, toolbar=no, resizable=no");
            },
            // 토스 정기 결제 모달
            "regularPaymentDialog": function() {
                this.visible.regularPaymentDialog = true;
                this.data.payment = {};
            },
            // 토스 카드 등록 후 정기 결제
            "createBillingKey": async function() {

                this.data.payment.customerId = this.data.user.id;
                this.data.payment.orderId = this.data.orderId;

                let data = (await axios({
                    url: "/api/app/toss/billing-key",
                    method: "post",
                    params: this.data.payment
                })).data;
            },
            // 토스 등록된 카드 정기 결제
            "regularPayment": async function() {

                let data = (await axios({
                    url: "/api/app/toss/regular-payment/"+ this.data.user.id,
                    method: "post",
                    data: {
                        "orderId": this.data.orderId
                    }
                })).data;
            },
            // 토스 카드결제
            "cardPayment": function() {
                var tossPayments = TossPayments(this.data.toss.clientKey);

                tossPayments.requestPayment('카드', {
                    amount: this.data.paymentHistory.amount,
                    orderId: this.data.paymentHistory.orderId,
                    orderName:  this.data.paymentHistory.requestLecturerNm + " 강사님이 요청하신 " +
                                    ((this.data.paymentHistory.paymentType == "S")? "단건" : "정기") +"결제",
                    customerName: this.data.user.name,
                    successUrl: this.data.address +'/mypage/users/payment/card-success',
                    failUrl: this.data.address +'/mypage/users/payment/card-fail?orderId=' + this.data.paymentHistory.orderId,
                });
            },
            // 토스 가상계좌 결제
            "moneyPayment": function() {
                var tossPayments = TossPayments(this.data.toss.clientKey);

                try {
                    tossPayments.requestPayment('가상계좌', {
                        amount: this.data.paymentHistory.amount,
                        orderId: this.data.paymentHistory.orderId,
                        orderName:  this.data.paymentHistory.requestLecturerNm + " 강사님이 요청하신 " +
                                        ((this.data.paymentHistory.paymentType == "S")? "단건" : "정기") +"결제",
                        customerName: this.data.user.name,
                        successUrl: this.data.address +'/mypage/users/payment/money-success',
                        failUrl: this.data.address +'/mypage/users/payment/money-fail?orderId=' + this.data.paymentHistory.orderId,
                        validHours: 24, // 유효시간
                        cashReceipt: { // 현금영수증 발급정보
                            type: '소득공제'
                        },
                        virtualAccountCallbackUrl: this.data.address +'/api/app/toss/virtual-account/success',
                    });
                } catch(e) {
                }
            },
            // 토스 카드 결제 취소
            "cancelCardPayment": async function() {
                let data = (await axios({
                    url: "/api/app/toss/card/cancel",
                    method: "get",
                    params: {
                        "paymentKey": "Xy0E4Dv5qpMGjLJoQ1aVZRmwPZpYdVw6KYe2RNgOWznZb7Bm",
                        "cancelReason": "취소"
                    }
                })).data;
            },
            "searchOrder": async function () {
                let data = (await axios({
                    url: "/api/app/toss/search",
                    method: "get",
                    params: {
                        "orderId": "pkUXrmO49yyuAbjhMLeli1YKDkMpEf",
                    }
                })).data;
            },
            // 토스 가상계좌 결제 취소
            "cancelMoneyPayment": async function() {

            }
        },
        "mounted": async function() {
            let orderId = this.$route.query.orderId;

            if(orderId != null && orderId != "") {
                this.data.orderId = orderId;
                this.init();
            } else {
                await meta.alert("잘못된 접근입니다.");
                location.href = "/";
            }

            this.data.user = store.state.app.user;
        },
        "created": async function() {
        },
    });
});