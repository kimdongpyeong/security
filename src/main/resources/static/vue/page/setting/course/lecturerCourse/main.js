var SettingLecturerCourseMainPage;
SettingLecturerCourseMainPage = Vue.component("setting-lecturerCourse-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/course/lecturerCourse/main.html")).data,
    "data": function () {
        return {
            "data": {
                "userList": [],
                "user": {},
                "role": {
                    "id": null
                },
                "query": {
                    "name": null
                },
                "student": {
                    studentList: [],
                    studentInfo: [],
                },
            },
            "flag": {
                studentDetailPopup: false,
            },
        };
    },
    "watch": {
        "data.query.name": function() {
            this.loadUserList();
        }
    },
    "methods": {
        "loadRoleList": async function () {
            var roleList;
            roleList = (await meta.api.common.role.getRoleList({
                "page": 1,
                "rowSize": 100000000
            })).data.items;

            this.role.dataTable.items = roleList;

            roleList.forEach(e => {
                this.select.roles.items.push({"text": e.name, "value": e.id})
            });
        },
        
        "loadUserList": async function () {
            userList = (await meta.api.common.user.getUserList({
                "roleId": 2,
                "page": 1,
                "rowSize": 100000000,
                "status": "T",
                "name": this.data.query.name,
                "sort": ["name,asc", "created_date,desc"]
            })).data.items;
            this.data.userList.forEach(item => {
                item.createdDate = moment(item.createdDate).format("YYYY-MM-DD");
            });
            this.data.userList = userList;
            console.log(userList)
        },
        
        "clickStudentList": async function(id) {
            studentList = (await meta.api.common.student.getStudentList({
                "createdBy": id,
                "page": 1,
                "rowSize": 10000,
            })).data.items;
            this.data.student.studentList = studentList;
            console.log(this.data.student.studentList)
        },
        "studentPopup": async function(id) {
            this.flag.studentDetailPopup = true;
            this.data.student.studentInfo = (await meta.api.common.student.getStudent(id)).data;
            this.data.student.studentInfo.gender = this.data.student.studentInfo.gender.replace('M','남').replace('W','여');
            this.data.student.studentInfo.lectureType = this.data.student.studentInfo.lectureType.replace('O','온라인').replace('F','오프라인');
            console.log(this.data.student.studentInfo)
        },
        
    },
    "mounted": async function () {
        this.loadUserList();
    }
}); });