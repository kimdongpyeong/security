var DashboardPaymentSalesPage;
DashboardPaymentSalesPage = Vue.component("dashboard-payment-sales-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/payment/sales/main.html")).data,
        "data": function () {
            return {
                "month": moment().format("yyyy년 MM월"),
                "year": moment().format("yyyy"),
                "startDay": moment().format("yyyy-MM-01"),
                "lastDay": "",
                "data":{
                    "user":{}
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
                    "days": null
                },
                "visible":{
                    "salesVisible": false
                },
                "dateOption":{
                    "disabledDate": this.disabledDate
                },
                "salesList":[],
                "monthTotalList":[],
                "select":{
                    "month": null,
                    "monthTerm": null,
                    "year": null
                },
                "download":{
                	"selectMonth": null,
                	"createdBy": null,
                	"startDate": null,
                	"endDate": null,
                	"startMonth": null,
                	"endMonth": null
                },
                "chartMonth": {
                    gradient: ["rgba(0,0,51,0.9)", "rgba(0,0,51,0.3)", "rgba(0,0,51,0)"],
                    gradientLine: ['#1d43be','#1d43be','#1d43be'],
                    type: 'bar',
                    data: {
                        labels: ['01월','02월','03월','04월','05월','06월','07월','08월','09월','10월','11월',"12월"], // 하단에 보여질 각 data값들의 Label
                        datasets: [
                            {
                                label: '',
                                data: [], // 그래프에 그려질 값 배열
                                borderColor: '#1d43be', //막대그래프 선 색상
                                backgroundColor: ['rgba(0,0,51,0.6)'], //막대 그래프 배경 색상
                                fill: "start", // 그래프 안에 색상 채우기
                                borderRadius: 50, // 막대 그래프 끝부분 부드럽게 원 마감처리
                                hoverBackgroundColor: 'rgba(51,0,102,1)',
                            },
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: { // 원본 Chartjs에서 그려주는 부제 안보이게
                                display: false,
                            },
                            tooltip: {
                                yAlign: 'bottom', // Tooltip 상단 표시
                                displayColors: false, // Tooltip 안에 값을 표시해주는 상자 삭제
                                backgroundColor: 'rgb(12,22,70)', // Tooltip박스 전체 색상
                                titleAlign: 'center', // Tooltip 상단 제목 정렬
                                bodyAlign: 'center', // Tooltip 중간 내용 정렬
                                footerAlign: 'center', // Tooltip 하단 내용 정렬
                                titleFont: { // Tooltip 상단 제목 글씨 크기
                                    size: 12
                                },
                                bodyFont: { // Tooltip 중간 내용 글씨 크기
                                    size: 16
                                },
                                footerFont: { //Tooltip 하단 내용 글씨 크기
                                    size: 12
                                },
                                callbacks: {
                                    title: function(context) { // Tooltip 상단 제목 설정
                                        var title = context[0].label + " 매출" + '\n';
                                        return title;
                                    },
                                    label: function(context) { // Tooltip 중간 내용 설정
                                        var label = context.parsed.y;
                                        return label.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
                                    },
                                    footer: function(context) { // Tooltip 하단 내용 설정
                                        var footer, index = context[0].dataIndex,
                                            beforeSales, nowSales, subtractSales, rate;
                                        if(index > 0){
                                            beforeSales = context[0].dataset.data[index - 1];
                                            nowSales = context[0].parsed.y;
                                            if(nowSales > beforeSales){
                                                subtractSales = nowSales - beforeSales;
                                                rate = (subtractSales/nowSales * 100).toFixed(2);
                                                footer = rate + "% 증가"
                                            } else if(beforeSales > nowSales){
                                                subtractSales = beforeSales - nowSales;
                                                rate = (subtractSales/beforeSales * 100).toFixed(2);
                                                footer = rate + "% 감소"
                                            }
                                        }
                                        return footer;
                                    },
                                    labelColor: function(context) { // Tooltip 중간 내용 색상 설정
                                        return {
                                            borderColor: 'rgb(0, 0, 0)',
                                            backgroundColor: 'rgb(255, 255, 255)',
                                            borderWidth: 2,
                                            borderDash: [2, 2],
                                            borderRadius: 2,
                                        };
                                    },
                                    labelTextColor: function(context) { // Tooltip 중간 내용 글씨 색상
                                        return 'rgb(255,255,255)';
                                    },
                                }

                            },
                        },
                        elements: {
                            point:{
                                radius: 0
                            }
                        },
                        scales: {
                            x: {
                                grid:{
                                    display:false
                                },
                                ticks:{
                                    color : '#b5b7ca',
                                    maxTicksLimit: 10000000,
                                    maxRotation: 0
                                }
                            },
                            y: {
                                grid:{
                                    borderDash: [2,5] // 배경 점선으로 표시
                                },
                                ticks: {
                                },
                            }
                        },
                    },
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
            //초기화
            "reset": function(){
                //팝업창 내리기
                this.visible.salesVisible = false;
                //안의 내용 초기화
                this.input.date = [];
                this.input.count = 1;
                this.input.money = '';
                this.input.method = null;
            },
            //매출등록
            "setSales": async function(){
                let self = this, param={};
                console.log(self.input)
                if(self.input.date.length > 0 && self.input.money !== null && self.input.money !== '' && self.input.method !== null){
                    param.salesStartDate = moment(self.input.date[0]).format('yyyy-MM-DD');
                    param.salesEndDate = moment(self.input.date[1]).format('yyyy-MM-DD');
                    param.count = self.input.count;
                    param.totalSales = this.uncomma(self.input.money);
                    param.paymentMethodCd = self.input.method;
                    param.createdBy = self.data.user.id;

                    if(await meta.confirm("매출 등록을 하시겠습니까?")){
                        (await meta.api.common.salesHistory.createSalesHistory(param)).data;
                        meta.alert("등록되었습니다.");
                        this.$router.go();
                    }
                } else{
                    meta.alert("모든 입력사항이 필수입니다.");
                }
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
                
                //다운로드 시 조건에 맞는 값을 불러오기 위해 값넣어두기
                this.download.startDate = startDay;
                this.download.endDate = endDay;
                
                //기존 들어있는 값 초기화
                this.salesList = [];
                this.total.days = null;
                this.total.sales = null,
                this.total.payment = null;

                //매출&결제
                salesTotalList = (await meta.api.common.salesHistory.getSalesTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.user.id,
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
                
                //다운로드 시 조건에 맞는 값을 불러오기 위해 값넣어두기
                this.download.startMonth = month[0];
                this.download.endMonth = month[1];
                
                //날짜 변경 시 기존 값 초기화
                this.monthTotalList = [];

                // 해당 기간 총 매출 불러오기
                totalList = (await meta.api.common.salesHistory.getSalesTermTotalList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.user.id,
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
                    "createdBy": this.data.user.id,
                    "searchYear": year
                })).data.items;

                //선택되는 년도의 월별 매출 데이터 초기화
                this.chartMonth.data.datasets[0].data = [];
                //date형식 변환
                totalList.forEach(e => {
                    e.month = moment(e.month).format("MM월");
                });

                //해당하는 달의 매출이 있으면 값 넣어주고, 없으면 0 값 넣어줌
                this.chartMonth.data.labels.forEach(x => {
                    let data = totalList.find(e => e.month == x);

                    if(data != null && data != undefined) {
                        this.chartMonth.data.datasets[0].data.push(data.monthTotal);
                    } else{
                        this.chartMonth.data.datasets[0].data.push(0);
                    }
                });
            },
            "downloadExcel": async function() {
            	this.download.createdBy = this.data.user.id;
            	this.download.selectMonth = this.month;
                
            	await (meta.api.common.salesHistory.downloadSalesList(this.download));
            }
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);

            this.$set(this.data.user, "id", user.id);
            this.loadSalesTotal();
            this.loadMonthTotal();
            this.loadYearTotal();
        },
        "created": function () {
        },
    });
});