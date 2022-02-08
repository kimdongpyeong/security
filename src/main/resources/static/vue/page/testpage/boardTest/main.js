var boardTest;
boardTest = Vue.component("boardTest-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/boardTest/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "search": null,
                    "kind": null,
                    "btnClick": null,
                },
                "notice": {
                    "dataTable": {
                        "headers": [
                            {"text": "번호", "sortable": true, "value": "id", "align": "start", "width": "10%"},
                            {"text": "", "sortable": false, "value": "recImportantYn", "align": "start", "width": "10%"},
                            {"text": "제목", "sortable": true, "value": "recTitle", "align": "start", "width": "80%"},
                        ],
                        "items": [],
                        "page": 1,
                        "itemsPerPage": 10,
                    },
                    "pagination": {
                        "length": 10,
                        "totalVisible": 10
                    },
                },
                "kindsCode": {
                    "kinds": [],
                },
                panels: [],
            }
        },
        "computed": {
        },
        "watch": {
            "data.kind": function(val) {
                this.loadNoticeList();
            }
        },
        "methods": {
            //공지사항 목록 출력 값 가져오기
            async loadNoticeList() {
                let data = (await meta.api.common.notice.getNoticeList({"page": 1, "rowSize": 1000000, "deleteYn":'N', sort: ["important_yn,asc"], "title": this.data.search, "kindsCd": this.data.kind })).data.items;
                data.forEach(e=> {
                    e.recTitle = e.importantYn == 'Y' ? ( "※중요※ " + e.title ) : e.title;
                    e.recImportantYn = e.importantYn == 'Y' ? ("※중요※") : "";
                })

                this.notice.dataTable.items = data;
                this.notice.dataTable.serverItemsLength = data.length;
                console.log(this.notice.dataTable.items);
            },

            // 분류 코드 가져오는 부분
            async kindsCd(){
                let data = (await meta.api.common.code.getCodeList({ "parentId": 44, "rowSize": 100000 })).data.items;
                this.kindsCode.kinds = data;
                console.log(data)
            },
            
            async noticeId(items){
                this.$router.push({
                    "path": "/boardTest/detail",
                    "query": {
                        "id": items.id
                    }
                })
            },
        },
        "mounted": async function () {
            this.loadNoticeList();
            this.kindsCd();
        },
        "created": async function () {
        },
    });
});