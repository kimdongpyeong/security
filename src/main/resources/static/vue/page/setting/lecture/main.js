var LectureLayout;
LectureLayout = Vue.component("lecture-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/lecture/main.html")).data,
    "data": function () {
        return {
        };
    },
    "methods": {
    }
}); });