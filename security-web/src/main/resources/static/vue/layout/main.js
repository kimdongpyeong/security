var MainLayout;
MainLayout = Vue.component("main-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/layout/main.html")).data,
    "data": function () {
        return {
            "data": {
                "user": {},
                "termsList": [],
                "googleUser": {},
                "kakaoUser": {},
                "redirectURL": null
            },
            "terms" : {
                "checkAll": false,
                "checked": [],
                "checkContent": [],
                "contentsList":[],
                "contents":{}
            },
            "visible": {
                "loginVisible": false,
                "signUpVisible": false,
                "menuVisible": false,
            },
            "flag": {
                "saveIdFlag": false,
                "certiFlag": false,
                "inviteFlag": false
            },
            "invite":{
                "lecturerId": null,
                "classroomId": null
            }
        };
    },
    "computed": {
        "name": function () {
            var name,
                user;
            name = "";
            user = _.cloneDeep(store.state.app.user);
            if (user) {
                name = user.name ? user.name : user.username;
            }
            return name;
        },
    },
    "watch": {
        "$store.state.app.loginPopFlag": function(val) {
            if(val) {
                this.openLoginDialog();
            }
        },
        "$store.state.app.signUpPopFlag": function(val) {
            if(val) {
                this.signUpPopDialog('N');
            }
        },
        "$store.state.app.signUpPhoneNum": function(val) {
            this.$set(this.data.user, "phoneNum", val);
            this.flag.certiFlag = (val != null)? true : false;
        },
    },
    "methods": {
        "existUsername": async function (username) {
            return (await meta.auth.idExists({"username": username})).data > 0 ? true : false;
        },
        "existSmsUsername": async function (username) {
            return (await meta.auth.smsIdExists({"username": username})).data > 0 ? true : false;
        },
        "existSignUpWay": async function (username, signUpWay) {
            return (await meta.auth.idExists({"username": username, "signUpWay": signUpWay})).data > 0 ? true : false;
        },
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
        "loadTermsList": async function(){
            let contents = (await meta.api.common.terms.getTermsList()).data.items;
            for(let i in contents) {
                contents[i].checked = false;
            }
            this.terms.checkContent = contents;
        },
        "goDashboard": function(){
            this.$router.push({
                "path": "/dashboard",
            }).catch(()=>{});
        },
        "goClassroom": function(){
            this.$router.push({
                "path": "/classroom",
            }).catch(()=>{});
        },
        "goMypage": function(){
            let roleId = store.state.app.roleList[0].id;

            if(roleId === 3){
                this.$router.push({
                    "path": "/mypage/users/main",
                }).catch(()=>{});
            } else{
                this.$router.push({
                    "path": "/mypage/lecturers/main",
                }).catch(()=>{});
            }
        },
        "openLoginDialog": async function() {
            this.data.username ="";
            this.data.password="";
            this.visible.loginVisible = true;

            if(this.$cookies.get("supportiId")) {
                this.flag.saveIdFlag = true;
                this.$set(this.data, "username", this.$cookies.get("supportiId"));
            }

//            if(this.click.loginPwClicked){
//                this.click.loginPwClicked = false;
//                this.$refs.loginPw.type = "password";
//            }
        },
        "signUpPopDialog": async function(signUpWay) {
            await this.loadTermsList();
            this.visible.signUpVisible = true;
        },
        "googleSetting": function() {
            let self = this;

            gapi.load('auth2', function() {
                var gauth = gapi.auth2.init({
                    client_id: '156442757850-5qq0jrg8k06m7cguaircacc02mr529i1.apps.googleusercontent.com'
                });

                self.data.gauth = gapi.auth2.getAuthInstance();
            });
        },
        "googleLogin": function() {
            let self = this;

            self.data.gauth.signIn().then(function(googleUser){
                self.onSuccess(googleUser);
            }, function(error) {
            });
        },
        "onSuccess": async function(googleUser){
            let self = this, fd = new FormData();

            const userInfo = {
                name : googleUser.getBasicProfile().getName(),
                email : googleUser.getBasicProfile().getEmail(),
                password : '',
            }

            self.data.user.signUpWay = "G";

            for(var key in userInfo) {
                fd.append(key, userInfo[key]);
            }

            if(await self.existSmsUsername(userInfo.email)){
                if(await self.existSignUpWay(userInfo.email, self.data.user.signUpWay)){
                    self.smsLogin(userInfo.email, self.data.user.signUpWay);
                } else{
                    await meta.alert("구글로 가입한 회원이 아닙니다.<br/>기존에 가입경로로 로그인하시길 바랍니다.");
//                    // 이메일로 정보 조회
//                    let userData = (await meta.api.common.user.getReferralId(userInfo.email)).data;
//                    let smsData = (await meta.api.common.userPeristalsis.getUser(userData.id)).data;
//
//                    // 테이블에 userId값과 일치
//                    if( userData.id == smsData.userId || smsData.signUpWay == 'G' ){
//                        self.smsLogin(userData.username, smsData.signUpWay);
//                    } else {
//                        // 값이 없으면 추가
//                        if(await meta.confirm("계정을 연동 하시겠습니까?")){
//                            let smsSignUpWay = (await meta.api.common.userPeristalsis.createUser({userId: userData.id, signUpWay: 'G'})).data.signUpWay;
//                            self.smsLogin(userData.username, smsSignUpWay);
//                        }
//                    }
                }
            } else{
                self.data.googleUser = {};
                self.data.googleUser.name = userInfo.name;
                self.data.googleUser.username = userInfo.email;

                self.saveUser();
            }
        },
        "kakaoLogin": function() {
            window.Kakao.Auth.login({
                scope : "",
                success: this.kakaoUserInfo,
            });
        },
        "kakaoUserInfo": function(authObj) {
            window.Kakao.API.request({
                url:'/v2/user/me',
                success : async (res) => {
                    const kakao_account = res.kakao_account;
                    const userInfo = {
                        name: (kakao_account.name_needs_agreement == false ? kakao_account.name : null),
                        nickname : (kakao_account.profile_nickname_needs_agreement == false ? kakao_account.profile.nickname : null),
                        email : (kakao_account.email_needs_agreement == false ? kakao_account.email : null),
                        password : '',
                        gender: (kakao_account.gender_needs_agreement == false ? kakao_account.gender : null),
                        birth : (kakao_account.birthday_needs_agreement == false ? kakao_account.birthday : null),
                        account_type : 2,
                    }

                    this.kakaoUser = userInfo;
                    this.data.user.signUpWay = "K";

                    let fd = new FormData();

                    for(var key in userInfo) {
                        fd.append(key, userInfo[key]);
                    }

                    if(await this.existSmsUsername(userInfo.email)){
                        if(await this.existSignUpWay(userInfo.email, this.data.user.signUpWay)){
                            this.smsLogin(userInfo.email, this.data.user.signUpWay);
                        } else{
                            await meta.alert("카카오로 가입한 회원이 아닙니다.<br/>기존에 가입경로로 로그인하시길 바랍니다.");
                            // 이메일로 정보 조회
//                            let userData = (await meta.api.common.user.getReferralId(userInfo.email)).data;
//                            let smsData = (await meta.api.common.userPeristalsis.getUser(userData.id)).data;
//
//                            // 테이블에 userId값과 일치
//                            if(userData.id == smsData.userId && smsData.signUpWay == 'K'){
//                                this.smsLogin(userData.username, smsData.signUpWay);
//                            } else {
//                                // 값이 없으면 추가
//                                if(await meta.confirm("계정을 연동 하시겠습니까?")){
//                                    let smsSignUpWay = (await meta.api.common.userPeristalsis.createUser({userId: userData.id, signUpWay: 'K'})).data.signUpWay;
//                                    this.smsLogin(userData.username, smsSignUpWay);
//                                }
//                            }
                        }
                    } else{
                        this.data.kakaoUser.name = (userInfo.name != null)? userInfo.name : userInfo.nickname;
                        this.data.kakaoUser.username = userInfo.email;

                        this.saveUser();
                    }
                },
                fail : error => {
                    meta.alert(error);
                }
            })
        },
        //초대링크의 강의실ID와 강사ID가 알맞는 정보인지 확인
        "invitedRoom": async function(){
            let classroom = (await meta.api.common.classRoom.getClassRoomList({
                "page": 1,
                "rowSize": 100000,
                "id": this.invite.classroomId,
                "createdBy": this.invite.lecturerId
            })).data.items;

            if(classroom.length > 0){
                this.flag.inviteFlag = true;
            }
        },
        "saveUser": async function(){

            let userDto, roleId;
            let fd = new FormData();

            if(this.data.googleUser.username != null) {
                this.$set(this.data.user, "name", this.data.googleUser.name);
                this.$set(this.data.user, "username", this.data.googleUser.username);
            }

            if(this.data.kakaoUser.username != null) {
                this.$set(this.data.user, "username", this.data.kakaoUser.username);
                this.$set(this.data.user, "name", this.data.kakaoUser.name);
            }

            if(this.flag.inviteFlag){
                this.$set(this.data.user,"lecturerId", this.invite.lecturerId);
                this.$set(this.data.user,"classroomId", this.invite.classroomId);
                this.$set(this.data.user,"inviteFlag", this.flag.inviteFlag);
            }

            this.$set(this.data.user, "status", "T");
            userDto = _.cloneDeep(this.data.user);
            roleId = _.cloneDeep(this.data.roleId);

            fd.append("roleId", 2);

            for(var key in userDto) {
                fd.append(key, userDto[key]);
            }

//            this.data.termsList.forEach((item) => fd.append("termsList[]", item));

            if(await meta.confirm("회원가입을 하시겠습니까?")) {
                await meta.auth.signUp(fd);
                await meta.alert("회원가입을 축하드립니다.");

                // 임시
                if(this.data.signUpWay === 'N') {

                } else {
                    this.smsLogin(this.data.user.username, this.data.user.signUpWay);
                }
            }

        },
        /* 로그인 */
        "login": async function (signUpWay) {
            this.$store.commit("app/SET_LOADING", true);
            if(!this.data.username) {
                await meta.alert("아이디를 입력해주세요.");
            } else if (!this.data.password) {
                await meta.alert("비밀번호를 입력해주세요.");
            } else {
                try {
                    let from = this.$route.query.from,
                    role = "&role=" + this.$route.query.role,
                    path = from + role;

                    if(this.flag.saveIdFlag) {
                        this.$cookies.set("supportiId", this.data.username, "30d");
                    } else {
                        this.$cookies.remove("supportiId");
                    }
                    await meta.auth.login(this.data.username, this.data.password, signUpWay);
                    if(from !== undefined && from !== null && from !== "") {
                        this.$router.replace(path);
                    } else{
                        var roleId = (await meta.api.common.user.getReferralId(this.data.username)).data.roleId;
                        this.$router.go();
                    }
                } catch (e) {
                    await meta.alert(e.response.data.message);
                }
            }
            this.$store.commit("app/SET_LOADING", false);
        },
        /* SMS 로그인 */
        "smsLogin": async function (username, signUpWay) {
            this.$store.commit("app/SET_LOADING", true);
            try {
                let from = this.$route.query.redirectURL;
                await meta.auth.login(username, null, signUpWay);
                if(from !== undefined && from !== null && from !== "") {
                    location.href = from;
                } else{
                    var roleId = (await meta.api.common.user.getReferralId(username)).data.roleId;
                    this.$router.go();
                }
            } catch (e) {
                if(e.response.data.code == "Withdrawal") {
                    if(await meta.confirm(e.response.data.message)) {
                        this.kakaoLogin();
                    }
                } else {
                    await meta.alert(e.response.data.message);
                }
            }
            this.$store.commit("app/SET_LOADING", false);
        },
    },
    "created": async function () {
        if(document.getElementById("vuetify-head") != null) {
            document.getElementById("vuetify-head").remove();
        }

        Kakao.init('e06884a5c42a479c4766d55703829af3');
    },
    "mounted": async function () {
        let loginPopDialog = this.$route.query.loginPopDialog;

        if(loginPopDialog) {
            this.visible.loginVisible = true;
        }

        this.invite.lecturerId = this.$route.query.lecturerId;
        this.invite.classroomId = this.$route.query.id;

        if(this.invite.lecturerId && this.invite.classroomId){
            this.invitedRoom();
        }

        this.$store.commit("app/SET_SIGN_UP_PHONENUM", null);
    },
}); });