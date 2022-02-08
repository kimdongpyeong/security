var SettingClassRoomCourseMainPage;
SettingClassRoomCourseMainPage = Vue.component("setting-classRoomCourse-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/course/classRoomCourse/main.html")).data,
    "data": function () {
        return {
            "data": {
                "classRoomList": [],
                "user": {},
                "role": {
                    "id": null
                },
                "query": {
                    "name": null
                },
                "classRoom": {
                    classRoomStudents: [],
                    studentInfo: [],
                },
            },
            "flag": {
                classRoomDetailPopup: false,
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
            classRoomList = (await meta.api.common.classRoom.getClassRoomList({
                    "page": 1,
                    "rowSize": 100000000,
                    "deleteYn":"N",
                    "title": this.data.query.name,
                    "sort": ["id,desc"]
                })).data.items;
            
            classRoomList.forEach(x => {
                x.openType = (x.lectureType == "O")? "온라인" : "오프라인";
            });
            
            this.data.classRoomList = classRoomList; 
            console.log(classRoomList)
        },
        
        "clickParentsList": async function(id) {
            let classRoomList = (await meta.api.common.classRoomUser.getClassRoomUserList({
                "page": 1,
                "rowSize": 100000000,
                "classroomId": id,
                "deleteYn": "N"
            })).data.items;
            this.data.classRoom.classRoomStudents = classRoomList;
            console.log(classRoomList)
        },
        "clickDetailPopup": async function(id) {
            this.flag.classRoomDetailPopup = true;
            this.data.classRoom.studentInfo = (await meta.api.common.user.getUser(id)).data;
            console.log(id)
            console.log(this.data.classRoom.studentInfo)
        },
        
    },
    "mounted": async function () {
        this.loadUserList();
    }
}); });