var DashboardPaymentCancelPage;
DashboardPaymentCancelPage = Vue.component("dashboard-payment-cancel-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/payment/cancel/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "paymentList": [],
                    "startDateSearch": null,
                    "endDateSearch": null,
                },
                "headers": [
//                    { text: '일자', value: 'paymentDateDay', width: '15%',    align: 'center'},
                    { text: '대상 월', value: 'cancelRequestMonth', width: '10%',    align: 'center'},
                    { text: '취소요청일자', value: 'cancelRequestDate', width: '15%',    align: 'center'},
                    { text: '결제유형', value: 'paymentMethodNm', width: '10%',    align: 'center'},
                    { text: '금액', value: 'amount',width: '10%',    align: 'center'},
                    { text: '취소 사유 입력', value: 'cancelReason', width: '30%' },
                    { text: '상태', value: 'state',width: '10%',    align: 'center'},
                ],
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            // 결제 리스트 불러오기 (취소 및 환불 리스트)
            "loadPaymentList": async function() {
                this.data.paymentList = [];

                let data = (await meta.api.common.paymentHistory.getPaymentHistoryList({
                    "searchStartDay": this.data.startDateSearch,
                    "searchEndDay": this.data.endDateSearch,
                    "page": 1,
                    "rowSize": 100000000,
                    "multiState": ["C","CY","R"],
                    "requestLecturerId": this.data.user.id,
                    "sort": ["cancelRequestDate,asc"]
                })).data.items;

                data.forEach(x => {
                    if(x.state == "C") {
                        x.state = "취소요청";
                    } else if(x.state == "CY") {
                        x.state = "취소승인";
                    } else if(x.state == "R") {
                        x.state = "환불완료";
                    }
                    
                    if(x.cancelReason == null || x.cancelReason == ""){
                        x.cancelReason = "(사유 미기재)"
                    }
                    
                    this.$set(x, "cancelRequestDate", x.cancelRequestDate.substring(0,10));
                    if(x.cancelRequestDate.substring(5,6) == "0"){
                        this.$set(x, "cancelRequestMonth", x.cancelRequestDate.substring(6,7) + "월");
                    } else {
                        this.$set(x, "cancelRequestMonth", x.cancelRequestDate.substring(5,7) + "월");
                    }
                    
                    this.data.paymentList.push(x);
                });
            },
            // 날짜 검색
            "searchDate": async function() {
                var startDate= new Date(this.data.startDateSearch),
                    endDate = new Date(this.data.endDateSearch);
                    
                if (endDate < startDate || (this.data.startDateSearch == null || this.data.endDateSearch == null)) {
                    await meta.alert("검색조건이 맞지 않습니다");
                    return;
                }
                
                await this.loadPaymentList();
            },
            // 검색 초기화
            "initDate": async function() {
                this.data.startDateSearch = null;
                this.data.endDateSearch = null;
                
                await this.loadPaymentList();
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
                
            await this.loadPaymentList();
        },
        "created": function () {
        },
    });
});