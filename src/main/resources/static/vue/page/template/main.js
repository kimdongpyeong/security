var templatePage;
templatePage = Vue.component("template-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/template/main.html")).data,
    "data": function () {
        return {
        };
    },
    "methods": {
    },
    "mounted": function () {
    }
}); });