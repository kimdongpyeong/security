var SettingBoardMainPage;
SettingBoardMainPage = Vue.component("setting-board-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/board/main.html")).data,
        "data": function () {
            return {
				"headers": [
					{
					  text: 'ID',
					  align: 'center',
					  sortable: false,
					  value: 'id',
					  width: '5%',
					},
					{ text: '분류', value: 'kindsNm',width: '15%', align: 'center'},
					{ text: '제목', value: 'title', width: '30%' },
					{ text: '공지여부', value: 'noticeYn', align: 'center' },
					{ text: '삭제여부', value: 'deleteYn', align: 'center' },
					{ text: '생성일', value: 'createdDate', align: 'center' },
				],
            	"board":[],
            	"codeBtnList":[],
            	"codeList":[],
            	"colorList":['black','red','orange','green','blue','gray'],
            	"search":''
            };
        },
        "watch": {
        },
        "methods": {
        	//공지사항 보기
        	"noticeDetail": function(item){
        		this.$router.push({
        			'path': '/settings/board/detail', 
        			'query': {
        				'id': item.id
        			}
        		});
        	},
        	//분류별 색상부여
			"getColor": function(code) {
				let idx = this.codeList.indexOf(code);

				return this.colorList[idx];
			},
			//공지사항 목록 불러오기
			"loadNoticeList": async function(kindsCd){
				let noticeList = (await meta.api.common.notice.getNoticeList({
					"page": 1, 
					"rowSize": 1000000,
					"sort":["noticeYn, asc","importantYn, asc"],
					"kindsCd": kindsCd
				})).data.items;
				
				noticeList.forEach(x => {
					if(x.importantYn === "Y"){
						x.title = "⭐"+x.title
					};
				});
				
				this.board = noticeList;
			},
			//분류코드 불러오기
			"loadCodeList": async function(){
				let codeList = (await meta.api.common.code.getCodeList({
					"page": 1,
					"rowSize": 100000000,
					"parentId": 44
				})).data.items;

				codeList.forEach(e => {
					this.codeList.push(e.value);
					this.codeBtnList.push(e);
				});
			},
			//분류코드버튼 선택 시
			"selectCode": function(cd){
				this.loadNoticeList(cd);
			}
        },
        "mounted": function () {
        	this.loadNoticeList();
        	this.loadCodeList();
        },
    });
});