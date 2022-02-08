var SendMessage;
SendMessage = Vue.component("dashboard-sendMessage-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/course/sendMessage/main.html")).data,
        "data": function() {
            return {
                data: {
                    studentList:[],
                },
            };
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            async LoadList(){
                studentList = (await meta.api.common.student.getStudentList({
                    "page": 1,
                    "rowSize": 10000,
                })).data.items;
                studentList.forEach(x => {
                    x.lectureType = (x.lectureType == "O")? "온라인" : "오프라인";
                });                
                this.data.studentList = studentList; 
                console.log(this.data.studentList)
            },
        },
        "created": function () {
        },
        "mounted": async function () {
            this.LoadList();
        }
    });
});