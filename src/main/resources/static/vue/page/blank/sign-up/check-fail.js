var signUpCheckFailPage;
signUpCheckFailPage = Vue.component("sign-up-check-fail-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/blank/sign-up/check-fail.html")).data,
        "data": function () {
            return {
                result: null,
            };
        },
        "methods": {
        },
        "created": async function () {
        }
    });
});