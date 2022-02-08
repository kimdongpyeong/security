var boardTestDetail;
boardTestDetail = Vue.component("boardTestDetail-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/boardTest/main-detail.html")).data,
        "data": function () {
            return {
                "data": {
                    "noticeId": null,
                    "noticeDesc": null,
                    "noticeTitle": null,
                },
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            async loadNotice(){
                let data = (await meta.api.common.notice.getNotice({"id": this.data.noticeId})).data;
                this.data.noticeTitle = data.title;
                this.data.noticeDesc = data.desc;
                console.log(data)
            }
        },
        "mounted": async function () {
            this.data.noticeId =_.cloneDeep(this.$route.query.id);
            this.loadNotice();
        },
        "created": async function () {
        },
    });
});