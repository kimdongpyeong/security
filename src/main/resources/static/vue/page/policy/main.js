var PolicyPage;
PolicyPage = Vue.component("policy-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/policy/main.html")).data,
        "data": function () {
            return {
            };
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
        },
        "mounted": async function () {
        },
        "created": function () {
        },
    });
});