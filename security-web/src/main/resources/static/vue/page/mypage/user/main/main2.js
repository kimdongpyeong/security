var MypageUsersMainPage;
MypageUsersMainPage = Vue.component("mypage-users-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/main/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "user": {},
                    "classRoom":{
                        "thumbnail":"",
                        "thumbnailSrc":"/resources/img/profile.png",
                        "memberNum": 1,
                        "deleteYn":"N",
                        "fileFlag":""
                    },
                    "modifyClassRoom":{},
                    "profileSrc": "/resources/img/profile.png",
                    "introduceContent":"",
                    "rowSize": 1,
                    "title": null,
                },
                "classRoomList":[],
                "totalRoomList":[],
                "select":{
                    "subjectList":[],
                    "detailSubjectList":[],
                    "subjectItems":[]
                },
                "checked": {
                    "subject":[],
                },
                "visible": {
                    "addClassRoomVisible": false,
                    "thumbnailVisible":false
                },
                "flag": {
                    "titleText": null,
                    "subjectText": null,
                    "thumbnailText": null,
                    "titleFlag": false,
                    "subjectFlag": false,
                    "thumbnailFlag": false,
                    "subjectClick": false
                },
                "profileUploaded":false,
                "isIndeterminate": true,
            };

        },
        "watch": {
            "checked.subject": function(e){
                if(e.length !== 0){
                    this.flag.subjectFlag = true;
                    this.flag.subjectText = "";
                }
            },
            "data.classRoom.thumbnail":function(e){
                if(e){
                    this.flag.thumbnailFlag = true;
                    this.flag.thumbnailText = "";
                }
            }
        },
        "methods": {
            //초기화
            "init": function(){
                this.visible.addClassRoomVisible = true;
                this.flag.titleText = null;
                this.flag.subjectText = null;
                this.flag.thumbnailText = null;
                this.flag.titleFlag = true;
                this.flag.subjectFlag = true;
                this.flag.thumbnailFlag = true;
                this.flag.subjectClick = false;
            },
            // 과목 전체 체크
            "checkSubjectChange": function(value) {
                let checkedCount = value.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.select.subjectItems.length;
            },
            // 유저 정보 불러오기
            "loadUserInfo": async function(){
                var user = {};
                user = (await meta.api.common.myPage.getUser(this.data.user.id)).data;
                if(user.saveFileNm != null) {
                    this.$set(user, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                } else {
                    this.$set(user, "profileSrc", this.data.profileSrc);
                }
                /*user.phoneNum = user.phoneNum.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");*/
                user.gender = user.gender.replace('M','남').replace('W','여')
                this.data.user = user;
                console.log(this.data.user)
            },
            // 과목불러오기
            "loadSubjectList": async function(){
                let subjectList = (await meta.api.common.subject.getSubjectList({
                    "page": 1,
                    "rowSize": 100000000,
                    "publishYn": "Y"
                })).data.items;
                subjectList.forEach(x => {
                    if(x.parentId === null){
                        this.select.subjectList.push({"value": x.id, "title": x.title});
                    } else{
                        this.select.detailSubjectList.push({"parentId":x.parentId, "value": x.id, "title":x.title})
                    }
                });
                console.log(subjectList)
            },
            // 나의 강의실 리스트
            "loadMyClassRoom": async function(){
                let classRoomList = (await meta.api.common.classRoomUser.getClassRoomUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "userId": this.data.user.id,
                    "deleteYn": "N"

                })).data.items;
                this.classRoomList = classRoomList;
                console.log(this.classRoomList)
            },
            // 해당 강의실의 정보 가져오기
            "loadClassRoom": async function(id){
                let classRoom = (await meta.api.common.classRoom.getClassRoom(id)).data;
                this.$set(classRoom,"thumbnailSrc",'/api/app/images?subpath=classRoomProfile&filename='+classRoom.saveFileNm);
                this.data.classRoom = classRoom;
            },
            // 상위 과목버튼 클릭시 나타나는 하위 과목 리스트 나타내기
            "checkSubject": function(item){
                this.isIndeterminate = true;
                this.flag.subjectClick = true;
                this.select.subjectItems = [];
                this.select.detailSubjectList.forEach(e => {
                    if(e.parentId == item.value){
                        this.$set(e, "parentTitle", item.title);
                        this.select.subjectItems.push(e);
                    }
                });
            },
            // 과목 선택 chip 삭제
            "removeSubject": function(idx){
                this.checked.subject.splice(idx, 1);
            },
            // 파일 선택(파일을 변경할때)
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0) {
                    self.data.classRoom.fileFlag = "remove";
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.data.classRoom.fileFlag = "remove";
                    self.fileClear();
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {
                    self.data.classRoom.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);

                self.data.classRoom.fileFlag = "change";
            },
            // 이미지 삭제
            "removeImg": async function(){
                this.fileClear();
                this.visible.thumbnailVisible = false;
            },
            // 썸네일 파일 초기화
            "fileClear": function() {
                this.data.classRoom.thumbnail = null;
                this.data.classRoom.thumbnailSrc = this.data.profileSrc;
                this.data.classRoom.fileFlag = "remove";
            },
            "validateChk": async function(div) {
                let self = this;

                switch(div) {
                    case "title":
                        let title = self.data.classRoom.title;
                        if(title === undefined || title === null || title === ""){
                            self.flag.titleText = "강의실 이름을 입력해주세요";
                            self.flag.titleFlag = false;
                        } else {
                            self.flag.titleText = "";
                            self.flag.titleFlag = true;
                        }
                        break;
                    case "subject":
                        let subject = self.checked.subject.length;
                        if(subject === 0){
                            self.flag.subjectText = "하나이상의 과목을 선택해주세요.";
                            self.flag.subjectFlag = false;
                        } else{
                            self.flag.subjectText = "";
                            self.flag.subjectFlag = true;
                        }
                        break;
                    case "thumbnail":
                        let thumbnail = self.$refs.thumbnail.files[0];
                        if(thumbnail === undefined || thumbnail === null){
                            self.flag.thumbnailText = "강의실 프로필 사진을 설정해주세요.";
                            self.flag.thumbnailFlag = false;
                        } else{
                            self.flag.thumbnailText = "";
                            self.flag.thumbnailFlag = true;
                        }
                        break;
                    defalut:
                        break;
                }
            },
            "removeClassRoom": async function(id){
                (await meta.api.common.classRoomUser.removeClassRoomUser({"userId": this.data.user.id,"classroomId": id})).data;
                await meta.alert("'" + this.data.title + "' 강의실이 삭제되었습니다.");
                this.visible.addClassRoomVisible = false;
                this.$router.go();
            },
            "loadPersonalChat": async function(){
                let totalRoomList = [];

                let invitingRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdOutYn": "N",
                    "createdUser": this.data.user.id
                })).data.items;

                invitingRoomList.forEach(x => {
                    totalRoomList.push(x);
                });

                let invitedRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "invitedOutYn": "N",
                    "invitedUser": this.data.user.id
                })).data.items;

                invitedRoomList.forEach(x => {
                    totalRoomList.push(x);
                });
                console.log(totalRoomList)
                this.totalRoomList = totalRoomList;
            },
            "goPersonalChat": async function(i){
                let room = this.totalRoomList[i],
                    param = {}, username = "";
                console.log(room);
                if(room.invitedUser){
                    username = room.createdNm;
                } else{
                    username = room.invitedNm;
                }
                if(room.createdOutYn === "N" && room.invitedOutYn === "N"){
                    this.$message({
                        message: username+"님과의 채팅방으로 이동",
                        type: 'success'
                      });
                } else{
                    param.id = room.id;
                    if(room.createdOutYn == "Y" && room.invitedOutYn == "N"){
                        param.createdOutYn = "N";
                    } else if (room.invitedOutYn == "Y" && room.createdOutYn == "N"){
                        param.invitedOutYn = "N";
                    } else {
                        param.createdOutYn = "N";
                        param.invitedOutYn = "N";
                    }

                    await meta.api.common.personalRoom.updatePersonalRoom(param);

                    this.$message({
                        message: username+"님과의 채팅방으로 이동",
                        type: 'success'
                    });
                }
                this.$router.push({
                    "name": "1:1채팅",
                    "params": {
                        "roomId": room.id
                    }
                });
            },
            // 로그아웃
            "logout": async function () {
                var token;
                token = meta.auth.getToken();
                await meta.auth.logout(token);
                if(this.$route.path === "/main") {
                    this.$router.go();
                } else {
                    this.$router.replace("/");
                }
            },
            "goClassPage": async function(classroomId){
                this.$router.push({
                    "path": "/main/lectures/detail",
                    "query": {
                        "id": classroomId
                    }
                });
            },
        },
        "mounted": async function () {
            this.loadUserInfo();
            this.loadSubjectList();
            this.loadMyClassRoom();
            this.loadPersonalChat();
        },
        "created": async function() {
            let user = _.cloneDeep(store.state.app.user);
            this.$set(this.data.user, "id", user.id);
        }
    });
});