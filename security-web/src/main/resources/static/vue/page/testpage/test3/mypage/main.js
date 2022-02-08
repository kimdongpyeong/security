var Test3MypageCreatorMain;
Test3MypageCreatorMain = Vue.component("test3-mypage-creator-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/mypage/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "user": {},
                    "profileSrc": "/resources/img/profile.png",

                    "createdRoomList" : [],
                    "personalRoomList" : [],

                    "liveList": [],
                },
            };

        },
        "watch": {
        },
        "methods": {
            // 유저 정보 불러오기
            "loadUserInfo": async function(){
                var user = {};
                user = (await meta.api.common.myPage.getUser(this.data.user.id)).data;
                if(user.saveFileNm != null) {
                    this.$set(user, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                    user.phoneNum = user.phoneNum.replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3");
                } else {
                    this.$set(user, "profileSrc", "/resources/img/profile.png");
                }
                this.data.user = user;
            },
            // (커뮤니티) 채팅방 불러오기
            "loadCreatedRoomList": async function() {
                var roomList = [],
                    roomIdList = [];
                //접속한 유저가 생성한 채팅방 불러오기
                roomList = (await meta.api.common.room.getRoomList({
                    "page": 1,
                    "rowSize": 100000000,
                    "deleteYn": "N",
                    "createdBy": this.data.user.id
                })).data.items;
                //불러온 채팅방ID를 roomIdList에 담아줌
                for(i in roomList){
                    roomIdList.push(roomList[i].id)
                }
                //roomIdList에 담긴 채팅방의 유저 불러오기
                let allRoomUserList = (await meta.api.common.roomUser.getRoomUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roomIdList": roomIdList
                })).data.items;
                this.data.createdRoomList = [];
                roomList.forEach(e => {
                    let roomName, users = [], profile;
//					e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    if(e.codeYn === "Y"){
                            roomName = "🔒 "+e.title;
                    } else{
                            roomName = e.title;
                    }
                    this.$set(e, "roomName", roomName);

                    allRoomUserList.forEach(el => {
                        if(e.id == el.roomId) {
                            users.push({
                                "_id": el.userId,
                                "username" : el.nickname,
                                "avatar": el.profile,
                                "createdDate": el.createdDate});
                        }
                    });
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "createdBy", e.createdBy);
                    if(e.saveFileNm == null || e.saveFileNm == undefined || e.saveFileNm == ""){
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    } else {
                        this.$set(e, "avatar", "/api/app/images/roomprofile/" + e.saveFileNm);
                    }
                    this.data.createdRoomList.push(e);
                });
            },
            // 1:1 채팅방 불러오기
            "loadPersonalRoomList": async function() {
                var invitingRoomList = [],
                    invitedRoomList = [];

                invitingRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": this.data.user.id,
                    "createdOutYn": "N"
                })).data.items;

                invitedRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "invitedUser": this.data.user.id,
                    "invitedOutYn": "N"
                })).data.items;

                this.data.personalRoomList = [];
                invitingRoomList.forEach(e => {
                    let users = [];
                    if(e.invitedOutYn === "Y"){
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdDate": e.createdDate});
                    } else{
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"createdDate": e.createdDate});
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdDate": e.createdDate});
                    }
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    this.$set(e, "roomName", e.invitedNm);
                    if(e.invitedProfile){
                        this.$set(e, "avatar", "/api/app/images?subpath=profile&filename="+e.invitedProfile);
                    } else{
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    }
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);

                    this.data.personalRoomList.push(e);
                });
                invitedRoomList.forEach(e => {
                    let users = [];
                    if(e.createdOutYn === "Y"){
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"createdDate": e.createdDate});
                    } else{
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"createdDate": e.createdDate});
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdDate": e.createdDate});
                    }
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    this.$set(e, "roomName", e.createdNm);
                    if(e.createdProfile){
                        this.$set(e, "avatar", "/api/app/images?subpath=profile&filename="+e.createdProfile);
                    } else{
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    }
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);

                    this.data.personalRoomList.push(e);
                });
            },


            "logout": async function () {	// 로그아웃
                var token;
                token = meta.auth.getToken();
                await meta.auth.logout(token);
                if(this.$route.path === "/main") {
                    this.$router.go();
                } else {
                    this.$router.replace("/");
                }
            },
        },
        "mounted": async function () {
            await this.loadUserInfo();
            await this.loadCreatedRoomList();
            await this.loadPersonalRoomList();
        },
        "created": async function() {
            let user = _.cloneDeep(store.state.app.user);
            this.$set(this.data.user, "id", user.id);
        }
    });
});