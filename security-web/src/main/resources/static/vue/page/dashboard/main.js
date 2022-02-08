var DashboardPage;
DashboardPage = Vue.component("dashboard-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "modifyUser": {}
                },
                "select": {
                    "bankList": []
                },
                "visible": {
                    "userInfoDialog": false
                }
            };
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            "loadUserInfo": async function() {
                this.data.user = (await meta.api.common.user.getUser(this.data.user.id)).data;
            },
            "openUserInfoDialog": async function() {
                this.data.modifyUser = _.cloneDeep(this.data.user);
                this.data.modifyUser.bankCd = (this.data.modifyUser.bankCd == null)? "" : this.data.modifyUser.bankCd;
                this.visible.userInfoDialog = true;
                await this.loadBankList();
            } ,
            "modifyUserInfo": async function() {
                (await meta.api.common.user.modifyUser(this.data.user.id, this.data.modifyUser)).data;

                await meta.alert("정보가 수정되었습니다.");
                this.visible.userInfoDialog = false;
                this.loadUserInfo();
            },
            "loadBankList": async function() {
                this.select.bankList = [];

                let codeList = (await meta.api.common.code.getCodeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "parentId": 50
                })).data.items;

                codeList.forEach(e => {
                    this.select.bankList.push({"text": e.name, "value": e.value});
                });
            },
        },
        "mounted": async function () {
            if(store.state.app.user != null) {
                let user = _.cloneDeep(store.state.app.user);
                this.$set(this.data.user, "id", user.id);
                await this.loadUserInfo();
            }
        },
        "created": function () {
        },
    });
});