var CourseLayout;
CourseLayout = Vue.component("course-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/course/course.html")).data,
    "data": function () {
        return {
        };
    },
    "methods": {
    }
}); });