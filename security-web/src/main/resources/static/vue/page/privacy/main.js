var PrivacyPage;
PrivacyPage = Vue.component("privacy-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/privacy/main.html")).data,
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