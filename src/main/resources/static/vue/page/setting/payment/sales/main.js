var SettingPaymentSalesPage;
SettingPaymentSalesPage = Vue.component("setting-payment-sales-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/payment/sales/main.html")).data,
        "data": function () {
            return {
                "month": moment().format("yyyy년 MM월"),
                "year": moment().format("yyyy"),
                "startDay": moment().format("yyyy-MM-01"),
                "lastDay": "",
            	"data":{
            		"userList": [],
            		"clickUser":{
            			"id": "",
            			"name":""
            		}
            	},
                "input":{
                    "date":[],
                    "count": 1,
                    "money": null,
                    "method": null
                },
                "total":{
                    "sales": null,
                    "payment": null,
                    "days": null,
                },
                "dateOption":{
                    "disabledDate": this.disabledDate
                },
                "salesList":[],
                "monthTotalList":[],
                "gatherSales": null,
                "select":{
                    "month": null,
                    "monthTerm": null,
                    "year": null
                },
                "search": {
                	"username": "",
                	"name":""
                },
            };
        },
        "watch": {
            "input.money": function(e){
                //첫번째 숫자가 0인경우 지우기
                if(e.length === 1 && e === "0"){
                    this.input.money = '';
                }
                this.input.money = this.comma(this.uncomma(e));
            },
            "select.month": function(date){
                //달 선택 시 this.month변경 X클릭 시 이번 달로 변경
                if(date){
                    this.month = moment(date).format("yyyy년 MM월");
                } else{
                    this.month = moment().format("yyyy년 MM월");
                }
                //선택한 달의 매출&결제 내역 가져오기
                this.loadSalesTotal(date);
            },
            "select.monthTerm": function(date){
                this.loadMonthTotal(date);
            },
            "select.year": function(year){
                if(year){
                    this.year = moment(year).format("yyyy");
                } else{
                    this.year = moment().format("yyyy");
                }
                this.loadYearTotal(this.year);
            },
            "search.username": function(){
            	this.loadUserList();
            },
            "search.name": function(){
            	this.loadUserList();
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
                this.loadSalesTotal();
                this.loadMonthTotal();
                this.loadYearTotal();
                this.loadGatherSales();
        	},
            //해당 달의 매출내역불러오기
            "loadSalesTotal": async function(date){
                let startDay, endDay, salesTotalList = [];

                //선택한 달의 첫날과 마지막날 구하기
                if(!date){
                    date = new Date();
                }
                startDay = moment(date).format("yyyy-MM-01");
                endDay = moment(new Date(date.getFullYear(), date.getMonth() + 1, 0)).format("yyyy-MM-DD");
                
                //기존 들어있는 값 초기화
                this.salesList = [];
                this.total.days = null;
                this.total.sales = null,
                this.total.payment = null;

                //매출&결제
                salesTotalList = (await meta.api.common.salesHistory.getSalesTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.clickUser.id,
                    "startDate": startDay,
                    "endDate": endDay
                })).data.items;

                salesTotalList.forEach(x => {
                    x.salesDate = moment(x.salesDate).format("MM월 DD일");
                    x.salesMoney = this.comma(x.salesTotal);
                    x.paymentMoney = this.comma(x.paymentTotal);
                    x.dayTotalMoney = this.comma(x.dayTotal);

                    this.total.sales += x.salesTotal;
                    this.total.payment += x.paymentTotal;
                    this.total.days += x.dayTotal;

                    this.salesList.push(x);
                });

                this.total.sales = this.comma(this.total.sales);
                this.total.payment = this.comma(this.total.payment);
                this.total.days = this.comma(this.total.days);
            },
            //월별 총매출 불러오기
            "loadMonthTotal": async function(month){
                let now = new Date(), date = [],
                    totalList = [];

                //날짜 선택 없을 경우 현재기준 두달 전 ~ 현재 달
                if(!month){
                    date[0] = moment(new Date(now.setMonth(now.getMonth() - 1))).format("yyyy-MM");
                    date[1] = moment().format("yyyy-MM");

                    month = date;
                }
                
                //날짜 변경 시 기존 값 초기화
                this.monthTotalList = [];

                // 해당 기간 총 매출 불러오기
                totalList = (await meta.api.common.salesHistory.getSalesTermTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.clickUser.id,
                    "startMonth": month[0],
                    "endMonth": month[1]
                })).data.items;

                //날짜 형식 변환 및 해당 매출 값 넣어 주기
                totalList.forEach(x => {
                    x.month = moment(x.month).format("yyyy년 MM월");
                    x.monthTotal = this.comma(x.monthTotal);

                    this.monthTotalList.push(x);
                });
            },
            //해당 년도의 월별 총 매출 불러오고 그래프 그리기
            "loadYearTotal": async function(year){
                let totalList = [], temp = [];

                if(!year){
                    year = moment().format("yyyy");
                }
                //해당 년도의 월별 총매출 불러오기
                totalList = (await meta.api.common.salesHistory.getSalesTermTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.clickUser.id,
                    "searchYear": year
                })).data.items;
                
            },
            "loadGatherSales": async function(){
            	this.gatherSales = null;
            	//누적 매출액 불러오기
                let totalList = (await meta.api.common.salesHistory.getSalesTermTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.clickUser.id,
                })).data.items;

                totalList.forEach(e => {
                	this.gatherSales += e.monthTotal;
                });
                this.gatherSales = this.comma(this.gatherSales);
            } 
        },
        "mounted": async function () {
        	this.loadUserList();
        },
        "created": function () {
        },
    });
});