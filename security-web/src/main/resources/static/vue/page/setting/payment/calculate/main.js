var SettingPaymentCalculatePage;
SettingPaymentCalculatePage = Vue.component("setting-payment-calculate-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/payment/calculate/main.html")).data,
        "data": function () {
            return {
            	"month": moment().format("MM"),
                "data":{
                    "user":{},
		    		"userList": [],
		    		"clickUser":{
		    			"id": "",
		    			"name":""
		    		}
                },
                "search": {
                	"username": "",
                	"name":""
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
                    "account": null,
                    "payMoney": null
                },
                "payDay":{},
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
                },
                "visible":{
                	"payDayVisible": false
                }
            };
        },
        "watch": {
            "select.monthTerm": function(date){
            	//기간 선택 시 해당하는 달의 정산 내역 불러오기
                this.loadMonthCalculate(date);
            },
            "search.username": function(){
            	this.loadUserList();
            },
            "search.name": function(){
            	this.loadUserList();
            },
            "payDay.value": function(e){
            	console.log(this.payDay)
            }
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
            //강사리스트 불러오기
        	"loadUserList": async function(){
                let userList = (await meta.api.common.user.getUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roleId": 1,
                    "username": this.search.username,
                    "name": this.search.name
                })).data.items;
                
                this.data.userList = userList;
        	},
        	"clickUser": async function(id, name){
        		this.data.clickUser.id = id;
        		this.data.clickUser.name = name;
        		
                this.loadSummary();
                this.loadMonthCalculate();
        	},
            "loadSummary": async function(){
            	let user, date = new Date(),
            		startDate, endDate, calculate = [], payMoney;
            	
            	//선택 강사 변경 시, 초기화
            	this.summary.account = "";

            	//유저 정보 불러오기
                user = (await meta.api.common.user.getUser(this.data.clickUser.id)).data;
                
                this.data.clickUser = _.cloneDeep(user);
                
                //은행 코드 및 계좌가 있으면 넣어주기
                if(this.data.clickUser.bankCd && this.data.clickUser.accountNum){
                	this.summary.account = this.data.clickUser.bankName +" "+ this.data.clickUser.accountNum;
                }
            },
            "loadMonthCalculate": async function(month){
                let now = new Date(), date = [],
                	calculateList = [];

            	//선택 강사 변경 시, 초기화
                this.calculateList = [];
                
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
            		"createdBy": this.data.clickUser.id,
                    "startMonth": month[0],
                    "endMonth": month[1]
            	})).data.items;

                calculateList.forEach(e => {
                	//정산하는 달 년/월
                	e.gYear = e.month.substr(0, 4);
                	e.gMonth = e.month.substr(5,2);
                	
                	//정산 예정일
                	e.payDate = moment(new Date(e.gYear, e.gMonth, Number(this.payDay.value))).format("yyyy년 MM월 DD일");

                	e.month = moment(e.month).format("yyyy년 MM월");
                	
                	if(this.data.clickUser.paymentGeneral){
                		//개인일 경우
                		if(this.data.clickUser.paymentGeneral === 'P'){
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
                
                if(calculateList.length > 0){
                	this.lastPayDate = calculateList[calculateList.length - 1].payDate;
                	this.calculateList = calculateList;
                }
            },
            "downloadExcel": async function() {
            	let today = moment().format("yyyy년 MM월 DD일");
            	this.download.createdBy = this.data.clickUser.id;
            	
            	//선택한 기간의 데이터가 있는 경우만 다운로드 가능
            	if(this.calculateList.length > 0){
            		await (meta.api.common.calculate.downloadCalculateList(this.download));
            	} else{
            		meta.alert(this.download.startMonth + "~" + this.download.endMonth + " 기간의 데이터가 없습니다.");
            	}
//            	if(today >= this.lastPayDate){
//            		await (meta.api.common.calculate.downloadCalculateList(this.download));
//            	} else{
//            		meta.alert("마지막 선택 달의 정산 예정일이 지나야 다운로드가 가능합니다.");
//            	}
            },
            //정산 예정 지급일자 불러오기
            "loadPayDay": async function(){
            	let payDay = (await meta.api.common.environment.getEnvironmentVariableKey("PAYMENT_DUE_DATE")).data;

            	this.payDay = payDay;
            },
            //정산 예정 지급일자 수정하기
            "modifyPayDay": async function(){
            	let payDay = Number(this.payDay.value);

                var regNum = /^[0-9]*$/;
            	if(this.payDay.value !== null && this.payDay.value !== "" && this.payDay.value !== undefined){
            		if(!regNum.test(this.payDay.value) || this.payDay.value.search(/\s/) !== -1){
            			meta.alert("숫자만 입력해주세요(공백불가)");
            		} else{
            			if(payDay <= 0 || payDay > 30 ){
            				meta.alert("1~30 사이의 숫자만 가능합니다");
            			} else{
            				meta.alert("수정완료");
            			}
            		}
            	} else{
            		meta.alert("일자를 입력해주세요.")
            	}
            }
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);

            this.$set(this.data.user, "id", user.id);
            this.loadUserList();
            this.loadPayDay()
        },
        "created": function () {
        },
    });
});