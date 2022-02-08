var findAccountCheckSuccessPage;
findAccountCheckSuccessPage = Vue.component("find-account-check-success-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/find-account/check-success.html")).data,
        "props": {
        },
        "data": function () {
            return {
                result: null,
            };
        },
        "methods": {
            "getResult": async function () {
                let result = (await axios({
                    url: "/api/app/nices/find-account/checkplus-success",
                    method: "post",
                    data: {
                        encodeData: this.$route.query.EncodeData
                    }
                })).data;

                self.close();

                if (result !== undefined && result !== null) {
                    if(result.code === 0) {
                        opener.meta.alert(result.message);
                        opener.store.commit("app/SET_ACCOUNT", {phoneNum: result.phoneNum, username: result.username});
                    } else {
                        opener.meta.alert(result.message);
                    }
                }
            }
        },
        "mounted": async function () {
            await this.getResult();
        }
    });
});