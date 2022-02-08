var SettingPaymentPaymentPage;
SettingPaymentPaymentPage = Vue.component("setting-payment-payment-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/payment/payment/main.html")).data,
        "data": function () {
            return {
                "tab": null,
                "data": {
                    "payment": {},
                    "cancelPayment": {},
                    "refundPayment": {},
                },
                "payment": {
                    "dataTable": {
                        "table": {
                            "headers": [
                                {"text": "강사명", "sortable": false, "value": "requestLecturerNm"},
                                {"text": "사용자명", "sortable": false, "value": "paymentUserNm"},
                                {"text": "결제일", "sortable": false, "value": "lastPaymentDate"},
                                {"text": "결제유형", "sortable": false, "value": "paymentTypeNm"},
                                {"text": "결제방법", "sortable": false, "value": "paymentMethodNm"},
                                {"text": "결제수단", "sortable": false, "value": "paymentDivNm"},
                                {"text": "결제금액", "sortable": false, "value": "amount"},
                                {"text": "상태", "sortable": false, "value": "state"},
                            ],
                            "items": [],
                            "search": false,
                            "loading": false,
                            "page":1,
                            "itemsPerPage":10,
                            "totalRows": 0,
                        }
                    }
                },
                "cancelPayment": {
                    "dataTable": {
                        "table": {
                            "headers": [
                                {"text": "강사명", "sortable": false, "value": "requestLecturerNm"},
                                {"text": "사용자명", "sortable": false, "value": "paymentUserNm"},
                                {"text": "결제일", "sortable": false, "value": "lastPaymentDate"},
                                {"text": "결제유형", "sortable": false, "value": "paymentTypeNm"},
                                {"text": "결제방법", "sortable": false, "value": "paymentMethodNm"},
                                {"text": "결제수단", "sortable": false, "value": "paymentDivNm"},
                                {"text": "결제금액", "sortable": false, "value": "amount"},
                                {"text": "취소요청일자", "sortable": false, "value": "cancelRequestDate"},
                                {"text": "취소승인일자", "sortable": false, "value": "cancelApprovalDate"},
                            ],
                            "items": [],
                            "search": false,
                            "loading": false,
                            "page":1,
                            "itemsPerPage":10,
                            "totalRows": 0,
                        }
                    }
                },
                "refundPayment": {
                    "dataTable": {
                        "table": {
                            "headers": [
                                {"text": "강사명", "sortable": false, "value": "requestLecturerNm"},
                                {"text": "사용자명", "sortable": false, "value": "paymentUserNm"},
                                {"text": "결제일", "sortable": false, "value": "lastPaymentDate"},
                                {"text": "결제유형", "sortable": false, "value": "paymentTypeNm"},
                                {"text": "결제방법", "sortable": false, "value": "paymentMethodNm"},
                                {"text": "결제수단", "sortable": false, "value": "paymentDivNm"},
                                {"text": "결제금액", "sortable": false, "value": "amount"},
                                {"text": "환불일자", "sortable": false, "value": "refundDate"},
                            ],
                            "items": [],
                            "search": false,
                            "loading": false,
                            "page":1,
                            "itemsPerPage":10,
                            "totalRows": 0,
                        }
                    }
                }
            };
        },
        "watch": {
        },
        "methods": {
            // 결제내역
            "paymentList": async function(options) {
                const data = (await meta.api.common.paymentHistory.getPaymentHistoryList({
                    "page": options.page,
                    "rowSize": options.itemsPerPage,
                    "sort": ["id"],
                })).data;

                data.items.forEach(e=> {
                    e.paymentTypeNm = (e.paymentType == 'S')? '단건' : '정기';
                    e.paymentDivNm = (e.paymentDiv == 'K')? '카카오' : '토스';
                    e.amount = numeral(e.amount).format('0,0') + '원';
                    if(e.state == "I") {
                        e.state = "결제진행중";
                    } else if(e.state == "S") {
                        e.state = "결제완료";
                    } else if(e.state == "C") {
                        e.state = "취소요청";
                    } else if(e.state == "CY") {
                        e.state = "취소승인";
                    } else if(e.state == "R") {
                        e.state = "환불완료";
                    } else if(e.state == "RR") {
                        e.state = "환불거절";
                    }
                })
                this.payment.dataTable.table.items = data.items;
                this.payment.dataTable.table.totalRows = data.totalRows;
            },
            // 취소승인내역
            "cancelPaymentList": async function(options) {
                const data = (await meta.api.common.paymentHistory.getPaymentHistoryList({
                    "page": options.page,
                    "rowSize": options.itemsPerPage,
                    "sort": ["id"],
                    "state": "CY",
                })).data;

                data.items.forEach(e=> {
                    e.paymentTypeNm = (e.paymentType == 'S')? '단건' : '정기';
                    e.paymentDivNm = (e.paymentDiv == 'K')? '카카오' : '토스';
                    e.amount = numeral(e.amount).format('0,0') + '원';
                })
                this.cancelPayment.dataTable.table.items = data.items;
                this.cancelPayment.dataTable.table.totalRows = data.totalRows;
            },
            // 환불내역
            "refundPaymentList": async function(options) {
                const data = (await meta.api.common.paymentHistory.getPaymentHistoryList({
                    "page": options.page,
                    "rowSize": options.itemsPerPage,
                    "sort": ["id"],
                    "state": "R",
                })).data;

                data.items.forEach(e=> {
                    e.paymentTypeNm = (e.paymentType == 'S')? '단건' : '정기';
                    e.paymentDivNm = (e.paymentDiv == 'K')? '카카오' : '토스';
                    e.amount = numeral(e.amount).format('0,0') + '원';
                })
                this.refundPayment.dataTable.table.items = data.items;
                this.refundPayment.dataTable.table.totalRows = data.totalRows;
            },
            "clickPayment": async function (e) {
                this.data.payment = (await meta.api.common.paymentHistory.getPaymentHistory(e.id)).data;
            },
            "clickCancelPayment": async function (e) {
                this.data.cancelPayment = (await meta.api.common.paymentHistory.getPaymentHistory(e.id)).data;
            },
            "clickRefundPayment": async function (e) {
                this.data.refundPayment = (await meta.api.common.paymentHistory.getPaymentHistory(e.id)).data;
            },
            "cancelPayment": async function() {
                let data = this.data.cancelPayment;

                // 단건결제
                if(data.paymentType == "S") {
                    if(data.paymentDiv == "K") {
                        // 카카오 환불
                        let result = (await axios({
                            url: "/api/app/kakao/cancel/" + data.id,
                            method: "get",
                        })).data;

                        if(result == "success") {
                            await meta.alert("환불이 완료되었습니다.");
                        } else {
                            await meta.alert(result + "\n관리자에게 문의해주세요.");
                        }
                        this.$router.go();
                    } else {
                        // 토스 환불
                        if(data.paymentMethodCd == "PM01") {
                            // 카드결제
                            let result = (await axios({
                                url: "/api/app/toss/card/cancel/" + data.id,
                                method: "get",
                            })).data;

                            if(result == "success") {
                                await meta.alert("환불이 완료되었습니다.");
                            } else {
                                await meta.alert(result + "\n관리자에게 문의해주세요.");
                            }
                            this.$router.go();
                        } else {
                            // 무통장결제

                        }
                    }
                }
            }
        },
        "mounted": async function () {
        },
        "created": function () {
        },
    });
});