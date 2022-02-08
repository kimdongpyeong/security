var findAccountCheckFailPage;
findAccountCheckFailPage = Vue.component("find-account-check-fail-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/find-account/check-fail.html")).data,
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