var chartTest;
chartTest = Vue.component("chartTest-page", async function (resolve) {
    resolve({
    "template": (await axios.get("/vue/page/testpage/chartTest/main.html")).data,
        "data": function() {
            return {
                data: {
                    test: null,
                },
                chart: {
                    chartStatistics: {
                        gradient: ["rgba(0,0,51,0.9)", "rgba(0,0,51,0.3)", "rgba(0,0,51,0)"],
                        gradientLine: ['#1d43be','#1d43be','#1d43be'],
                        type: 'bar',
                        data: {
                            labels: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov'], // 하단에 보여질 각 data값들의 Label
                            datasets:  [
                            	{
                            		label: '',
                            		data: ['1000','2300','1700','500','2100','1200','300','1600','900','2500','1300'], // 그래프에 그려질 값 배열
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
                                        size: 10
                                    },
                                    bodyFont: { // Tooltip 중간 내용 글씨 크기
                                        size: 18
                                    },
                                    footerFont: { //Tooltip 하단 내용 글씨 크기
                                        size: 10
                                    },
                                    callbacks: {
                                        title: function(context) { // Tooltip 상단 제목 설정
                                            var title = "이번달 매출" + '\n';
                                            return title;
                                        },
                                        label: function(context) { // Tooltip 중간 내용 설정
                                            var label = context.parsed.y;
                                            return label.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
                                        },
                                        footer: function(context) { // Tooltip 하단 내용 설정
                                            var footer = "" +"% 증가"
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
                                        maxTicksLimit: 100000,
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
                    chartClass: {
                        type: 'bar',
                        data: {
                            labels: ['1','2','3','4','5','6','7','8','9','10','11','12'],
                            datasets: [
                                {
                                    label: '',
                                    data: ['88','40','65','50','75','60','85','57','47','57','68','53'],
                                    borderColor: 'rgba(255,0,50,0.3)',
                                    type: 'line' // bar 차트 안에서 Line(선)을 만드는 값
                                },
                                {
                                    label: '',
                                    data: ['88','40','65','50','75','60','85','57','47','57','68','53'],
                                    borderColor: '#1d43be',
                                    backgroundColor: ['rgba(100,20,255,0.4)'],
                                    fill: "start", // 그래프 안 색 채우기
                                    lineTension: 0.4, // 그래프 곡선 휘임 정도
                                    hoverBackgroundColor: 'rgba(51,0,102,1)', // 마우스가 올라갔을 경우 bar 배경 색
                                },
                            ]
                        },
                        options: {
                            responsive: true,
                            plugins: {
                                legend: {
                                    display: false, // 범례 보이지 않게
                                },
                            tooltip: {
                                // Disable the on-canvas tooltip
                                enabled: false, // custom을 사용하기 위해 기본 Tooltip false
                                mode:'point', // 값을 1개만 출력 하기 위해 mode: point 설정으로 마우스 포인트에 있는 값만 출력
                                external: function(context) {
                                    // Tooltip Element
                                    var tooltipEl = document.getElementById('chartjs-tooltip');
                                    // 첫번째 랜더링 요소
                                    if (!tooltipEl) {
                                        tooltipEl = document.createElement('div');
                                        tooltipEl.id = 'chartjs-tooltip';
                                        tooltipEl.innerHTML = '<table></table>';
                                        document.body.appendChild(tooltipEl);
                                    }
                
                                    // 툴팁 없을 시 숨기기
                                    var tooltipModel = context.tooltip;
                                    if (tooltipModel.opacity === 0) {
                                        tooltipEl.style.opacity = 0;
                                        return;
                                    }
                
                                    // caret 위치 설정
                                    tooltipEl.classList.remove('above', 'below', 'no-transform');
                                    if (tooltipModel.yAlign) {
                                        tooltipEl.classList.add(tooltipModel.yAlign);
                                    } else {
                                        tooltipEl.classList.add('no-transform');
                                    }
                
                                    function getBody(bodyItem) {
                                        return bodyItem.lines;
                                    }
                
                                    // Tooltip 테이블 설정하는 부분 
                                    if (tooltipModel.body) {
                                        var titleLines = tooltipModel.title || [];
                                        var bodyLines = tooltipModel.body.map(getBody) || [];
                                        var footerLines = tooltipModel.body.map(getBody) ;
                
                                        var innerHtml = '<thead>';
                
                                        // Tooltip 상단에 들어갈 title 테이블 값
                                        titleLines.forEach(function(title, i) {
                                            var colors = tooltipModel.labelColors[i];
                                            var style = 'background-color: rgb(255,255,255)';
                                            style += '; border-color:' + colors.borderColor;
                                            style += '; vertical-align: middle';
                                            style += '; border-width: 2px';
                                            style += '; height: 20px;';
                                            var span = '<span style="' + style + '"></span>';
                                            innerHtml += '<tr><th>' + span +  '날짜: ' + title + '월' + '</th></tr>';
                                        });
                                        innerHtml += '</thead><tbody>';
                
                                        // Tooltip 중간에 들어갈 body 테이블 값
                                        bodyLines.forEach(function(body, i) {
                                            var colors = tooltipModel.labelColors[i];
                                            var style = 'background:' + colors.backgroundColor;
                                            style += '; border-color:' + colors.borderColor;
                                            style += '; border-width: 2px';
                                            style += '; height: 30px';
                                            style += '; vertical-align: middle';
                                            var span = '<span style="' + style + '"></span>';
                                            innerHtml += '<tr><td>' + span + '강의 수: ' + body + '</td></tr>';
                                        });
                                        // Tooltip 하단에 들어갈 footer 테이블 값
                                        footerLines.forEach(function(body, i) {
                                        var style = 'text-color: rgb(255,255,255)';
                                        style += '; vertical-align: middle;';
                                        style += '; height: 20px;';
                                        var span = '<span style="' + style + '"></span>';
                                        innerHtml += '<tr><td>' + span + body +'% 증가' + '</td></tr>';
                                        
                                        innerHtml += '</tbody>';
                                        
                                        var tableRoot = tooltipEl.querySelector('table');
                                        tableRoot.innerHTML = innerHtml;
                                        });
                                    }
                
                                    var position = context.chart.canvas.getBoundingClientRect();
                
                                    // Tooltip 보여질 위치, 스타일 글씨 폰트 설정
                                    tooltipEl.style.opacity = 1; //투명도
                                    tooltipEl.style.position = 'absolute'; //위치 설정
                                    tooltipEl.style.left = position.left + tooltipModel.caretX + 'px'; // Tooltip왼쪽 기준 값
                                    tooltipEl.style.top = position.top + window.pageYOffset + tooltipModel.caretY + 'px'; // Tootip상단 기준 값
                                    tooltipEl.style.fontFamily = tooltipModel._bodyFontFamily; // 글꼴
                                    tooltipEl.style.fontSize = tooltipModel.bodyFontSize + 'px'; // 글씨 크기
                                    tooltipEl.style.fontStyle = tooltipModel._bodyFontStyle; // 글씨 스타일
                                    tooltipEl.style.padding = tooltipModel.yPadding + 'px ' + tooltipModel.xPadding + 'px'; // Tootip padding값
                                    tooltipEl.style.pointerEvents = 'none'; // 차트 포인트에 이벤트
                                    tooltipEl.style.backgroundColor = 'white'; // 차트 body 배경 색
                                    tooltipEl.style.border = '1px solid black'; // 차트 body 테두리
                                }
                            }
                            },
                            elements: {
                                point:{
                                    radius: 0
                                }
                            },
                            scales: {
                                xAxes: {
                                    grid:{
                                        display:false
                                    },
                                    ticks:{
                                        color : '#b5b7ca',
                                        maxTicksLimit: 100000,
                                        maxRotation: 0
                                    },
                                    title: {
                                        display: true,
                                        text: "날짜",
                                        position: 'left',
                                    }
                                },
                                yAxes: {
                                    grid: {
                                      borderDash: [2,5] // 배경 점선으로 표시
                                    },
                                    title: {
                                        display: true,
                                        text: "강의 수"
                                    }
                                }
                            }
                        },
                    },
                    lineChart: {
                        gradient: ["rgba(200,200,251,0.9)", "rgba(200,200,251,0.3)", "rgba(200,200,251,0)"],
                        gradientLine: ["rgba(255,0,0,0.9)", "rgba(0,255,0,0.3)", "rgba(0,0,255,0)"],
                        type: 'line',
                        data: {
                            labels: ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov'], // 하단에 보여질 각 data값들의 Label
                            datasets: [
                                {
                                    label: '',
                                    data: ['1000','2300','1700','500','2100','1200','300','1600','900','2500','1300'], // 그래프에 그려질 값 배열
                                    borderColor: '#1d43be', //막대그래프 선 색상
                                    backgroundColor: ['rgba(0,0,51,0.6)'], //막대 그래프 배경 색상
                                    fill: "start", // 그래프 안에 색상 채우기
                                    borderRadius: 50, // 막대 그래프 끝부분 부드럽게 원 마감처리
                                    lineTension: 0.3, // 그래프 곡선 휘임 정도
                                    hoverBackgroundColor: 'rgba(51,0,102,1)',
                                },
                            ]
                        },
                        options: {
                            responsive: true,
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
                                        size: 10
                                    },
                                    bodyFont: { // Tooltip 중간 내용 글씨 크기
                                        size: 18
                                    },
                                    footerFont: { //Tooltip 하단 내용 글씨 크기
                                        size: 10
                                    },
                                    callbacks: {
                                        title: function(context) { // Tooltip 상단 제목 설정
                                            var title = "이번달 매출" + '\n';
                                            return title;
                                        },
                                        label: function(context) { // Tooltip 중간 내용 설정
                                            var label = context.parsed.y;
                                            return label.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "원";
                                        },
                                        footer: function(context) { // Tooltip 하단 내용 설정
                                            var footer = "" +"% 증가"
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
                                    radius: 3
                                }
                            },
                            scales: {
                                x: {
                                    grid:{
                                        display:false
                                    },
                                    ticks:{
                                        color : '#b5b7ca',
                                        maxTicksLimit: 100000,
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
                },
                flag: {
                    blur: false,
                },
            };
        },
        "watch": {
        },
        "methods": {
            init: function() {
            }
        },
        "mounted": function () {
            this.init();
        },
        "created": function () {
        }
    });
});