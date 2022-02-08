var DashboardClassroomMainPage;
DashboardClassroomMainPage = Vue.component("dashboard-classroom-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/classroom/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "roleId": "",
                    "profileSrc": "/resources/img/profile.png",
                    "classroomList": [],
                },
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            // 유저정보 불러오기 
            "loadUserInfo" : async function() {
                user = (await meta.api.common.myPage.getUser(this.data.user.id)).data;
                if(user.saveFileNm != null) {
                    this.$set(user, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                } else {
                    this.$set(user, "profileSrc", this.data.profileSrc);
                }
//                user.gender = user.gender.replace('M','남').replace('W','여');
                this.data.user = user;
            },
            // 강의 정보 불러오기 ★진행중/종료는 createdDate로 구분중, liveTime이 프론트 정보로 출력이 안됨) 
            "loadClassRoomList": async function() {
                var classroomList = [];
                
                allClassroomList = (await meta.api.common.classRoom.getClassRoomList({
                    "page": 1,
                    "rowSize": 100000000,
                    "deleteYn": "N"
                })).data.items,
                roomUserList = (await meta.api.common.classRoomUser.getClassRoomUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "userId": this.data.user.id
                })).data.items;
                
                allClassroomList.forEach(e => {
                    roomUserList.forEach(el => {
                        if(e.id == el.classroomId){
                            if (e.saveFileNm != null) {
                                this.$set(e, "thumbnailSrc", "/api/app/images?subpath=classRoomProfile&filename=" + e.saveFileNm);
                            } else {
                                this.$set(e, "thumbnailSrc", "/resources/img/base.jpg");
                            }
//                            liveTime = (meta.api.common.classRoomLiveTime.getClassRoomIdLiveTime(e.id)).data;
//                            console.log(liveTime) 대체웨안되는대

                            var currentTime = new Date(),
                                createdDate = new Date(e.createdDate);
                            if(currentTime < createdDate){
                                this.$set(e, "ongoing", "N");
                            } else {
                                this.$set(e, "ongoing", "Y");
                            }
                            
                            classroomList.push(e);
                        }
                    })
                })
                
                this.data.classroomList = _.cloneDeep(classroomList);
            },
            // 강의 상세페이지 이동
            "goClassPage": async function(classroomId){
                this.$router.push({
                    "path": "/classroom/detail",
                    "query": {
                        "id": classroomId
                    }
                });
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
//            let roleId = _.cloneDeep(store.state.app.roleList[0].id);

            if(user)
                this.$set(this.data.user, "id", user.id);
//            if(roleId)
//                this.$set(this.data, "roleId", roleId);
                
            await this.loadUserInfo();
            await this.loadClassRoomList();
        },
        "created": function () {
        },
    });
});