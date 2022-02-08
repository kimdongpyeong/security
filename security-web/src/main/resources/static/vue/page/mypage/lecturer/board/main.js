var lecturerBoardPage;
lecturerBoardPage = Vue.component("mypage-lecturer-board-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/lecturer/board/main.html")).data,
        "data": function () {
            return {
                "data":{
                    userId: null,
                },
                "headers": [
                    {
                      text: 'ID',
                      align: 'center',
                      sortable: false,
                      value: 'id',
                      width: '5%',
                    },
                    { text: '제목', value: 'title', width: '30%' },
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
                    'path': '/mypage/lecturers/board/list', 
                    'query': {
                        'id': item.id
                    }
                });
            },
            //공지사항 목록 불러오기
            "loadNoticeList": async function(){
                let noticeList = (await meta.api.common.lecturerBoard.getLecturerBoardList({
                    "lecturerId": this.data.userId,
                    "page": 1, 
                    "rowSize": 1000000,
                })).data.items;
                console.log(noticeList)
                this.board = noticeList;
            },
            //분류코드 불러오기
        },
        "mounted": function () {
            let user = _.cloneDeep(store.state.app.user);
            this.data.userId = user.id;
            this.loadNoticeList();
            
        },
    });
});