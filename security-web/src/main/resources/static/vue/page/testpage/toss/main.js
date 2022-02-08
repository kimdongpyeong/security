var TossTestPage;
TossTestPage = Vue.component("toss-test-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/toss/main.html")).data,
        "data": function () {
            return {
                data: {
                    cardNum: "",
                    year: "",
                    month: "",
                    password: "",
                    birthDay: ""
                }
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            "openPayments": async function() {
                // * 테스트용 클라이언트 키로 시작하세요
                var clientKey = 'test_ck_5mBZ1gQ4YVX5NqNG7Q2rl2KPoqNb'
                var tossPayments = TossPayments(clientKey);

                // * 계좌이체 결제창 열기
                tossPayments.requestPayment('카드', {
                  amount: 1,
                  orderId: 'b_YIdAx3zArIj_8LlXVWl',
                  orderName: '토스 티셔츠 외 2건',
                  customerName: '박토스',
                  successUrl: 'http://localhost:9090/success',
                  failUrl: 'http://localhost:9090/fail'
                });

                // * 카드 자동결제 등록창 열기
//                tossPayments.requestBillingAuth('카드', {
//                    customerKey: 'CBDp_aujonktRLXO8uBRP',
//                    successUrl: 'http://localhost:9090/api/app/toss/success?amount=4600&orderName="구독"',
//                    failUrl: 'http://localhost:9090/api/app/toss/fail'
//                });

//                let data = (await axios({
//                    url: "/api/app/toss/test",
//                    method: "get",
//                    params: this.data
//                }));

                console.log(data);
            },
            "orderSearch": async function() {
                let data = (await axios({
                    url: "/api/app/toss/order",
                    method: "get",
                    params: this.data
                }));

                console.log(data);
            }
        },
        "mounted": async function () {
             var orderId = new Date().getTime();
             console.log(orderId);
        },
        "created": async function () {
        },
    });
});