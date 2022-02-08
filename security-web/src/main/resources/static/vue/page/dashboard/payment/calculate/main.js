var DashboardPaymentCalculatePage;
DashboardPaymentCalculatePage = Vue.component("dashboard-payment-calculate-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/payment/calculate/main.html")).data,
        "data": function () {
            return {
            	"month": moment().format("MM"),
                "data":{
                    "user":{}
                },
                "dateOption":{
                    "disabledDate": this.disabledDate
                },
                "salesList":[],
                "monthTotalList":[],
                "select":{
                    "monthTerm": null,
                },
                "summary":{
                    "payDate": null,
                    "standardDate": null,
                    "account": null,
                    "payMoney": null
                },
                "calculateList": [],
                "lastPayDate": null,
                "sortTable":[
                	{
                		prop: 'month', 
                		order: 'descending'
                	},
                	{
                		prop: 'payDate', 
                		order: 'descending'
                	},
                	{
                		prop: 'payMoney', 
                		order: 'descending'
                	},
                	{
                		prop: 'cashTotal', 
                		order: 'descending'
                	},
                	{
                		prop: 'cardTotal', 
                		order: 'descending'
                	},
                	{
                		prop: 'levyMoney', 
                		order: 'descending'
                	}
                ],
                "download":{
                	"createdBy": null,
                	"startMonth": null,
                	"endMonth": null
                }
            };
        },
        "watch": {
            "select.monthTerm": function(date){
            	//기간 선택 시 해당하는 달의 정산 내역 불러오기
                this.loadMonthCalculate(date);
            },
        },
        "methods": {
            //콤마붙이기
            "comma": function(str) {
                str = String(str);
                return str.replace(/(\d)(?=(?:\d{3})+(?!\d))/g, '$1,');
            },
            //콤마떼기
            "uncomma": function(str) {
                str = String(str);
                return str.replace(/[^\d]+/g, '');
            },
            //이번달보다 미래 달은 선택 불가
            "disabledDate": function(date){
                date = moment(String(date)).format('yyyy년 MM월');

                return date > moment().format("yyyy년 MM월")
            },
            "loadSummary": async function(){
            	let user, date = new Date(),
            		startDate, endDate, calculate = [], payMoney;
            	
            	//정산 지급 예정일 지정
            	this.summary.payDate = moment(new Date(date.getFullYear(), date.getMonth() + 1, 15)).format("yyyy년 MM월 DD일");
                
            	//정산 기준일 지정
            	startDate = moment(date).format("yyyy년 MM월 01일");
                endDate = moment(new Date(date.getFullYear(), date.getMonth() + 1, 0)).format("yyyy년 MM월 DD일");
                this.summary.standardDate = startDate + " ~ " + endDate;
                
                //유저 정보 불러오기
                user = (await meta.api.common.user.getUser(this.data.user.id)).data;
                
                this.data.user = _.cloneDeep(user);
                
                //은행 코드 및 계좌가 있으면 넣어주기
                if(this.data.user.bankCd && this.data.user.accountNum){
                	this.summary.account = this.data.user.bankName +" "+ this.data.user.accountNum;
                }
                
                //해당 달의 카드, 현금 금액 불러오기
                calculate = (await meta.api.common.calculate.getCalculateList({
            		"page": 1,
            		"rowSize": 100000,
            		"createdBy": this.data.user.id,
            		"startMonth": moment(date).format("yyyy-MM"),
            		"endMonth": moment(date).format("yyyy-MM")
            	})).data.items;

                if(this.data.user.paymentGeneral){
            		//개인일 경우
            		if(this.data.user.paymentGeneral === 'P'){
            			payMoney = (calculate[0].cashTotal * 0.967) + (calculate[0].cardTotal * 0.96);
            		} else{
            			//사업자일 경우
            			payMoney = (calculate[0].cashTotal * 0.89) + (calculate[0].cardTotal * 0.96);
            		}
            		//정산 예상 금액
            		this.summary.payMoney = this.comma(Math.round(payMoney)) + "원";
            	}
            },
            "loadMonthCalculate": async function(month){
                let now = new Date(), date = [],
                	calculateList = [];

                //날짜 선택 없을 경우 현재기준 두달 전 ~ 현재 달
                if(!month){
                    date[0] = moment(new Date(now.setMonth(now.getMonth() - 1))).format("yyyy-MM");
                    date[1] = moment().format("yyyy-MM");

                    month = date;
                }

                //다운로드 시 조건에 맞는 값을 불러오기 위해 값넣어두기
                this.download.startMonth = month[0];
                this.download.endMonth = month[1];
                
                //해당 달의 카드, 현금 금액 불러오기
                calculateList = (await meta.api.common.calculate.getCalculateList({
            		"page": 1,
            		"rowSize": 100000,
            		"createdBy": this.data.user.id,
                    "startMonth": month[0],
                    "endMonth": month[1]
            	})).data.items;
                
                calculateList.forEach(e => {
                	//정산하는 달 년/월
                	e.gYear = e.month.substr(0, 4);
                	e.gMonth = e.month.substr(5,2);
                	
                	//정산 예정일
                	e.payDate = moment(new Date(e.gYear, e.gMonth, 15)).format("yyyy년 MM월 DD일");

                	e.month = moment(e.month).format("yyyy년 MM월");
                	
                	if(this.data.user.paymentGeneral){
                		//개인일 경우
                		if(this.data.user.paymentGeneral === 'P'){
                			e.payMoney = (e.cashTotal * 0.967) + (e.cardTotal * 0.96);
                		} else {
                			//사업자일 경우
                			e.payMoney = (e.cashTotal * 0.89) + (e.cardTotal * 0.96);
                		}
                		e.levyMoney = this.comma(Math.round(e.total - e.payMoney)) + "원";
                		//정산 예상 금액
                		e.payMoney = this.comma(Math.round(e.payMoney)) + "원";
                	} else{
                		e.levyMoney = "개인/사업자 선택 필요";
                		e.payMoney = "개인/사업자 선택 필요";
                	}
                	
                	e.cashTotal = this.comma(e.cashTotal) + "원";
                	e.cardTotal = this.comma(e.cardTotal) + "원";
                });

                this.lastPayDate = calculateList[calculateList.length - 1].payDate;
                this.calculateList = calculateList;
            },
            "downloadExcel": async function() {
            	let today = moment().format("yyyy년 MM월 DD일");
            	this.download.createdBy = this.data.user.id;

            	if(today >= this.lastPayDate){
            		await (meta.api.common.calculate.downloadCalculateList(this.download));
            	} else{
            		meta.alert("마지막 선택 달의 정산 예정일이 지나야 다운로드가 가능합니다.");
            	}
            }
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);

            this.$set(this.data.user, "id", user.id);
            this.loadSummary();
            this.loadMonthCalculate();
        },
        "created": function () {
        },
    });
});