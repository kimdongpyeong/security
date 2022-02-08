var BlankLayout;
BlankLayout = Vue.component("blank-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/layout/blank.html")).data,
    "data": function () {
        return {
        };
    },
    "methods": {
    }
}); });