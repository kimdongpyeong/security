var DashboardPaymentPaymentPage;
DashboardPaymentPaymentPage = Vue.component("dashboard-payment-payment-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/payment/payment/main.html")).data,
        "data": function () {
            var checkAge = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('필수 입력 사항입니다.'));
                } else if (!Number.isInteger(Number(value))) {
                    return callback(new Error('숫자만 입력해주세요.'));
                } else {
                    callback();
                }
            };
            var checkPhone = (rule, value, callback) => {
                if (!value) {
                    return callback(new Error('필수 입력 사항입니다.'));
                } else if (!Number.isInteger(Number(value))) {
                    return callback(new Error('숫자만 입력해주세요.'));
                } else {
                    callback();
                }
            };
            var checkInfoYn = (rule, value, callback) => {
                if (!value && value != "Y") {
                    return callback(new Error('개인정보 이용에 동의해주세요.'));
                } else {
                    callback();
                }
            };
            var checkPaymentYn = (rule, value, callback) => {
                if (!value && value != "Y") {
                    return callback(new Error('결제 진행에 동의해주세요.'));
                } else {
                    callback();
                }
            };
            var checkPayingYn = (rule, value, callback) => {
                if (!value && value != "Y") {
                    return callback(new Error('결제 진행에 동의해주세요.'));
                } else {
                    callback();
                }
            };
            return {
                "data": {
                    "targetList": [],
                    "typeList": [
                        {"text": "단건결제", "value": "S"},
                        {"text": "정기결제", "value": "R"},
                    ],
                    "paymentHistoryList": [],
                    "payment": {
                        "name": "",
                        "paymentTargetCd": "",
                        "paymentType": "",
                        "amount": "",
                        "phoneNum": "",
                    },
                    "step02Title": "",
                    "step02Desc": "",
                    "startDateSearch": null,
                    "endDateSearch": null,
                },
                "visible": {
                    "paymentStep01Dialog": false,
                    "paymentStep02Dialog": false,
                    "paymentStep03Dialog": false,
                    "resultDialog": false,
                },
                "flag": {
                    "paymentType": "",
                },
                "rules": {
                    name: [
                        { required: true, message: '필수 입력 사항입니다.', trigger: 'blur' },
                    ],
                    paymentTargetCd: [
                        { required: true, message: '필수 선택 사항입니다.', trigger: 'change' }
                    ],
                    amount: [
                        { validator: checkAge, required: true, trigger: 'blur' }
                    ],
                    paymentType: [
                        { required: true, trigger: 'change' },
                    ],
                    phoneNum: [
                        { validator: checkPhone, required: true, trigger: 'blur' }
                    ],
                    infoYn: [
                        { validator: checkInfoYn, required: true, message: '개인정보 이용에 동의해주세요.', trigger: 'change' }
                    ],
                    paymentYn: [
                        { validator: checkPaymentYn, required: true, message: '결제 진행에 동의해주세요.', trigger: 'change' }
                    ],
                    payingYn: [
                        { validator: checkPayingYn, required: true, message: '결제 진행에 동의해주세요.', trigger: 'change' }
                    ],
                },
            };
        },
        "computed": {
        },
        "watch": {
            "data.payment.amount": function(val) {
                this.data.payment.amount = val.replace(/[^0-9]/g,'');
            },
            "data.payment.phoneNum": function(val) {
                this.data.payment.phoneNum = val.replace(/[^0-9]/g,'');
            },
            "data.payment.paymentType": function() {
                this.flag.paymentType = "";
            }
        },
        "methods": {
            "lodaPaymentHistory": async function() {
                this.data.paymentHistoryList = [];

                let data = (await meta.api.common.paymentHistory.getPaymentHistoryList({
                    "searchStartDay": this.data.startDateSearch,
                    "searchEndDay": this.data.endDateSearch,
                    "page": 1,
                    "rowSize": 100000000,
                    "requestLecturerId": store.state.app.user.id
                })).data.items;

                data.forEach(x => {
                    if(x.state == "I") {
                        x.state = "진행중";
                    } else if(x.state == "S") {
                        x.state = "결제완료";
                    } else if(x.state == "N") {
                        x.state = "취소진행중";
                    } else if(x.state == "C") {
                        x.state = "취소완료";
                    }

                    this.data.paymentHistoryList.push(x);
                });
            },
            "openPaymentStep01Dialog": async function() {
                this.data.targetList = [];

                let codeList = (await meta.api.common.code.getCodeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "parentId": 107
                })).data.items;

                codeList.forEach(e => {
                    this.data.targetList.push({"text": e.name, "value": e.value});
                });

                this.data.payment = {
                    "name": "",
                    "paymentTargetCd": "",
                    "paymentType": this.data.typeList[0].value,
                    "amount": "",
                    "phoneNum": "",
                };

                this.visible.paymentStep01Dialog = true;
            },
            "openPaymentStep02Dialog": async function(ruleForm) {

                try {
                    await this.$refs[ruleForm].validate();

                    if(this.flag.paymentType != true) {
                        return;
                    }

                    this.visible.paymentStep01Dialog = false;

                    if(this.data.payment.paymentType == "S") {
                        this.data.step02Title = "단건 결제 확정";
                        this.data.step02Desc = "단건 결제 내용입니다. 해당 금액이 한 번만 결제 됩니다.";
                    } else {
                        this.data.step02Title = "월별 결제 확정";
                        this.data.step02Desc = `월별 정기 결제입니다. 해당 금액을 매달 정기적으로 결제됩니다.<br>
                                                고객이 카드 정보 등록 시 자동으로 결제가 진행되며 취소를<br>
                                                원할 시 결제 취소 페이지에서 해지가 가능합니다.`;
                    }

                    this.visible.paymentStep02Dialog = true;

                } catch(e) {
                    await meta.alert("필수 사항을 입력해주세요.");
                    return;
                }
            },
            "openPaymentStep03Dialog": async function(ruleForm) {
                try {
                    await this.$refs[ruleForm].validate();

                    this.visible.paymentStep02Dialog = false;

                    this.$set(this.data.payment, "targetText", this.data.targetList.find(x => x.value == this.data.payment.paymentTargetCd).text);
                    this.$set(this.data.payment, "paymentTypeText", this.data.typeList.find(x => x.value == this.data.payment.paymentType).text);

                    this.visible.paymentStep03Dialog = true;
                } catch(e) {
                    await meta.alert("결제 진행에 동의해주세요.");
                    return;
                }
            },
            "requestPayment": async function(ruleForm) {
                try {
                    await this.$refs[ruleForm].validate();
                    let data = _.cloneDeep(this.data.payment);
                    await meta.api.common.paymentHistory.createPayment(data);
                    this.visible.paymentStep03Dialog = false;
                    this.visible.resultDialog = true;
                } catch(e) {
                    await meta.alert("결제 진행에 동의해주세요.");
                    return;
                }
            },
            "dateSearch": async function() {
                console.log(this.data.startDateSearch);
                console.log(this.data.endDateSearch);
            }
        },
        "mounted": async function () {
            this.lodaPaymentHistory();
        },
        "created": function () {
        },
    });
});