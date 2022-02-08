var signUpCheckSuccessPage;
signUpCheckSuccessPage = Vue.component("sign-up-check-success-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/blank/sign-up/check-success.html")).data,
        "props": {
        },
        "data": function () {
            return {
            };
        },
        "methods": {
            "getResult": async function () {
                let result = (await axios({
                    url: "/api/app/nices/sign-up/checkplus-success",
                    method: "post",
                    data: {
                        encodeData: this.$route.query.EncodeData
                    }
                })).data;

                self.close();

                if (result !== undefined && result !== null) {
                    if(result.code === 0) {
                        opener.store.commit("app/SET_SIGN_UP_PHONENUM", result.phoneNum);
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