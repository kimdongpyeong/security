var MypageUsersInfoPage;
MypageUsersInfoPage = Vue.component("mypage-uesr-info-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/main/main-detail.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "modifyUser": {},
                    "roleId": "",
                    "profileSrc": "/resources/img/profile.png",
                },
                "visible": {
                    "pwEditdialogVisible": false,
                    "profileEditdialogVisible":false,
                    "profileVisible":false
                },
                "flag": {
                    "pwdFlag": true,
                    "pwdText": null,
                    "newPwdFlag": true,
                    "newPwdText": null,
                    "newPwdCkFlag": true,
                    "newPwdCkText": null,
                    "nicknameFlag": true,
                    "nicknameText": null,
                },
                "profileUploaded": false, // 프로필 사진
                "orgNickname" : "",
                "orgProfileId": ""
            };
        },
        "watch": {
        },
        "methods": {
            "changePwdDialog" : function(){
                let user = this.data.user;
                this.visible.pwEditdialogVisible = true;

                this.$set(this.data.modifyUser, "id", user.id);
                this.$set(this.data.modifyUser, "password", user.password);
                this.data.modifyUser.password = undefined;
                this.data.modifyUser.newPassword = undefined;
                this.data.modifyUser.newPasswordCk = undefined;

                this.flag.pwdFlag = true;
                this.flag.newPwdFlag = true;
                this.flag.newPwdCkFlag = true;

                this.flag.pwdText = null;
                this.flag.newPwdText = null;
                this.flag.newPwdCkText = null;
            },
            "changePwd" : async function(){
                let id = this.data.modifyUser.id;

                this.validateChk('pwd');
                this.validateChk('newPwd');
                this.validateChk('newPwdCk');

                if(this.flag.pwdFlag && this.flag.newPwdFlag && this.flag.newPwdCkFlag){
                    let result = await meta.api.common.myPage.modifyPassword(id, {
                                    "oldPassword" : this.data.modifyUser.password,
                                    "newPassword" : this.data.modifyUser.newPassword
                                });

                    await meta.alert(result.data.resultMsg);

                    if(result.data.result){
                        this.visible.pwEditdialogVisible = false;
                    }
                } else{
                    await meta.alert("양식에 맞게 입력해주세요");
                }
            },
            "loadUserInfo" : async function() {
                user = (await meta.api.common.myPage.getUser(this.data.user.id)).data;
                if(user.saveFileNm != null) {
                    this.$set(user, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                } else {
                    this.$set(user, "profileSrc", this.data.profileSrc);
                }
                user.gender = user.gender.replace('M','남').replace('W','여');
                this.data.user = user;
            },
            "existNickname": async function (nickname) {
                return (await meta.auth.idExists({"nickname": nickname})).data > 0 ? true : false;
            },
            "validateChk": async function(div) {
                let self = this;

                switch(div) {
                    case "pwd" :
                        let pwd = self.data.modifyUser.password;

                        if(pwd === undefined || pwd === null || pwd === ""){
                            self.flag.pwdText = "현재 비밀번호를 입력해주세요";
                            self.flag.pwdFlag = false;
                        } else {
                            self.flag.pwdText = "";
                            self.flag.pwdFlag = true;
                        }
                        break;
                    case "newPwd" :
                        var regPwd = /(?=.*\d{1,50})(?=.*[~``!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g;
                        let newPwd = self.data.modifyUser.newPassword;

                        if(newPwd === undefined || newPwd === null || newPwd === ""){
                            self.flag.newPwdText = "새비밀번호를 입력해주세요";
                            self.flag.newPwdFlag = false;
                        } else if(!regPwd.test(newPwd) || newPwd.search(/\s/) !== -1 || newPwd.length < 8 ){
                            self.flag.newPwdText = "8~16자의 영문 대소문자, 숫자, 특수문자만 가능합니다";
                            self.flag.newPwdFlag = false;
                        } else {
                            self.flag.newPwdText = "";
                            self.flag.newPwdFlag = true;
                        }
                        break;
                    case "newPwdCk" :
                        let newPwdCk = self.data.modifyUser.newPasswordCk;

                        if(newPwdCk === undefined || newPwdCk === null || newPwdCk === ""){
                            self.flag.newPwdCkText = "비밀번호 확인을 입력해주세요";
                            self.flag.newPwdCkFlag = false;
                        } else if(newPwdCk !== self.data.modifyUser.newPassword){
                            self.flag.newPwdCkText = "비밀번호가 일치하지 않습니다";
                            self.flag.newPwdCkFlag = false;
                        } else {
                            self.flag.newPwdCkText = "";
                            self.flag.newPwdCkFlag = true;
                        }
                        break;
                    case "nickname":
                        let nickname = this.data.modifyUser.nickname;

                        if(nickname === null || nickname === undefined || nickname === ""){
                            self.flag.nicknameText = "닉네임을 입력해주세요.";
                            self.flag.nicknameFlag = false;
                        } else if(await this.existNickname(nickname) && nickname !== this.orgNickname){
                            self.flag.nicknameText = "동일한 닉네임이 존재합니다.";
                            self.flag.nicknameFlag = false;
                        } else {
                            self.flag.nicknameText = "";
                            self.flag.nicknameFlag = true;
                        }
                        break;
                    defalut:
                        break;
                }
            },
            "modifyUser": async function(){
                if(await meta.confirm("수정하시겠습니까?")) {
                    (await meta.api.common.myPage.modifyUserIntroduce(this.data.user)).data;
                    await meta.alert("수정되었습니다.");
                    this.$router.push({
                        "path": "/mypage/mentee/main",
                    });
                }
            },
            "setProfile": function() {
                let user = this.data.user;

                this.visible.profileEditdialogVisible = true;
                this.data.modifyUser = {};

                this.$set(this.data.modifyUser, "id", user.id);
                this.$set(this.data.modifyUser, "profileId", user.profileId);
                this.$set(this.data.modifyUser, "nickname", user.nickname);

                if(user.saveFileNm != null) {
                    this.$set(this.data.modifyUser, "profileSrc", "/api/app/images?subpath=profile&filename=" + user.saveFileNm);
                } else {
                    this.$set(this.data.modifyUser, "profileSrc", "/resources/img/profile.png");
                }

                this.orgProfileId= this.data.modifyUser.profileId;
            },
            "modifyProfile" : async function() {
                let fd = new FormData();
                fd.append("id", this.data.modifyUser.id);
                fd.append("nickname", this.data.modifyUser.nickname);
                if(this.$refs.profileFile.files[0] != undefined && this.$refs.profileFile.files[0] != null){
                    fd.append("profileFile", this.$refs.profileFile.files[0]);
                }
                fd.append("fileFlag", this.data.modifyUser.fileFlag);

                if(!this.flag.nicknameFlag){
                    this.validateChk('nickname');
                } else{
                    if(await meta.confirm("수정하시겠습니까?")) {
                        if(this.orgProfileId !== null){
                            fd.append("profileId", this.orgProfileId);
                        }
                        (await meta.api.common.myPage.modifyProfile(fd)).data;
                        await meta.alert("수정되었습니다.");
                        this.visible.profileEditdialogVisible = false;
                        this.loadUserInfo();
                    }
                }

            },
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0) {
                    this.data.modifyUser.fileFlag = "remove";
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    this.data.modifyUser.fileFlag = "remove";
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {
                    self.data.modifyUser.profileSrc = e.target.result;
                }

                reader.readAsDataURL(file);

                this.data.modifyUser.fileFlag = "change";
            },
            "removeImg": async function(){
                this.fileClear();
                this.visible.profileVisible = false;
                this.data.modifyUser.fileFlag = "remove";
            },
            "fileClear": function() {
                this.data.modifyUser.profile = null;
                this.data.modifyUser.profileSrc = "/resources/img/profile.png";
            },
            "removeUser" : async function(){
                let id = this.data.user.id;
                var token;
                if(await meta.confirm("탈퇴하시겠습니까 ?")){
                    (await meta.api.common.myPage.removeUser(id)).data;
                    await meta.alert("회원탈퇴 되었습니다.")
                    token = meta.auth.getToken();
                    await meta.auth.logout(token);
                    if(this.$route.path === "/main") {
                        this.$router.go();
                    } else {
                        this.$router.replace("/");
                    }
                }
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
        },
        "mounted": async function () {
            this.loadUserInfo();
        },
        "created": async function () {
            let user = _.cloneDeep(store.state.app.user);
            let roleId = _.cloneDeep(store.state.app.roleList[0].id);

            this.$set(this.data, "roleId", roleId);
            this.$set(this.data.user, "id", user.id);
        }
    });
});