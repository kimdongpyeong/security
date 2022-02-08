var MypageLecturerMainPage;
MypageLecturerMainPage = Vue.component("mypage-lecturer-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/lecturer/main/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "deleteClass": [],
                    "user": {},
                    "profileSrc": "/resources/img/profile.png",
                    "classRoomList": {
                        "list": [],
                        "rowSize": 8,
                    },
                },
                "visible": {
                    "deleteDialog": false
                }
            };
        },
        "watch": {
        },
        "methods": {
            // 사용자 정보
            "loadUserInfo": async function(){
                var user = (await meta.api.common.myPage.getUser(this.data.user.id)).data;

                if(user.saveFileNm != null) {
                    this.$set(user, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                } else {
                    this.$set(user, "profileSrc", this.data.profileSrc);
                }

                if(user.phoneNum != null) {
                    user.phoneNum = user.phoneNum.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
                }
                this.data.user = user;
            },
            // 나의 강의실 리스트
            "loadMyClassRoom": async function(){
                let classRoomList = (await meta.api.common.classRoom.getClassRoomList({
                    "page": 1,
                    "rowSize": 100000,
                    "createdBy": this.data.user.id,
                    "deleteYn":"N",
                    "sort": ["id,desc"]
                })).data.items;

                this.data.classRoomList.list = classRoomList;
                this.data.deleteClass = [];
            },
            "moreClassRoomList": function() {
                this.data.classRoomList.rowSize += 4;
            },
            "classCheck": function(id) {
                let arr = this.data.deleteClass;

                if(arr.includes(id)) {
                    arr.splice(arr.indexOf(id, 1));
                } else {
                    arr.push(id);
                }
            },
            "detailClass": function(id) {
                this.$router.push({
                    "query": {
                        "liveId" : id
                    },
                    "path": "/mypage/lecturers/live/detail"
                });
            },
            "deleteClassPop": function() {
                if(this.data.deleteClass.length == 0) {
                    meta.alert("삭제할 클래스를 선택해주세요.");
                } else {
                    this.visible.deleteDialog = true
                }
            },
            "deleteClass": async function() {
                let data = (await meta.api.common.classRoom.removeClassRoom({"deleteClass": this.data.deleteClass})).data;
                this.visible.deleteDialog = false;
                await meta.alert("삭제가 완료되었습니다.");
                this.$store.commit("app/SET_LOADING", true);
                this.$router.go();
            },
        },
        "mounted": async function () {
            this.loadUserInfo();
            this.loadMyClassRoom();
        },
        "created": async function() {
            let user = _.cloneDeep(store.state.app.user);
            this.$set(this.data.user, "id", user.id);
        }
    });
});