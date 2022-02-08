var FindAccountPage;
FindAccountPage = Vue.component("find-account-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/find-account/main.html")).data,
        "data": function () {
            return {
                activeName: 'idFindTab',
                "data": {
                    "phone": {},
                    "user": {},
                },
                "find": {
                    "username": null
                },
                "flag": {
                    "nameFlag": true,
                    "nameText": null,
                    "certiFlag": false,
                    "emailFlag": true,
                    "emailText": null,

                    "newPwdFlag" : true,
                    "newPwdCkFlag" : true,
                    "newPwdText" : null,
                    "newPwdCkText" : null,
                },
                "visible": {
                    "idVisible": false,
                    "pwVisible": false,
                },
                "userId": ""
            };
        },
        "watch": {
            "$store.state.app.findAccount.phoneNum": function(val) {
                this.$set(this.data.user, "phoneNum", val);
                this.flag.certiFlag = (val != null)? true : false;
            },
            "$store.state.app.findAccount.username": function(val) {
                this.$set(this.find, "username", val);
                this.flag.certiFlag = (val != null)? true : false;
            },
        },
        "methods": {
            handleClick: async function (tab, event) {
                this.data.phone = (await meta.api.app.nice.findAccountCheck()).data;
                this.$store.commit("app/SET_ACCOUNT", null);

                this.data.user = {};
                this.flag.nameFlag = true;
                this.flag.emailFlag = true;
                this.flag.certiFlag = false;

                this.flag.nameText = null;
                this.flag.emailText = null;
            },
            "getFindId" : async function(){
                if(!this.flag.certiFlag){
                    await meta.alert("핸드폰 인증을 해주세요.");
                } else{
                    this.visible.idVisible = true;
                }
            },
            "getFindPwd" : async function(){
                this.validateChk('email');
                this.validateChk('name');

                this.data.user.newPassword = "";
                this.data.user.newPasswordCk = "";

                this.flag.newPwdFlag = true;
                this.flag.newPwdCkFlag = true;
                this.flag.newPwdText = null;
                this.flag.newPwdCkText = null;

                let data,
                    email = this.data.user.username,
                    name = this.data.user.name,
                    phone = this.data.user.phoneNum;

                if(!this.flag.emailFlag || !this.flag.nameFlag){
                    await meta.alert("양식을 제대로 입력해주세요.");
                } else if(!this.flag.certiFlag){
                    await meta.alert("핸드폰 인증을 해주세요.");
                } else {
                    data = (await meta.api.common.find.findUser(email)).data;
                    this.userId = data.id;
                    if(data === "" || data === null || data === undefined || data.status !== "T"){
                        await meta.alert("존재하지 않는 아이디입니다.");
                    } else{
                        if(name === data.name && phone === data.phoneNum){
                            if(data.signUpWay === "N"){
                                this.visible.pwVisible = true;
                            } else{
                                await meta.alert("SNS로 가입한 경우 비밀번호 변경이 안됩니다.");
                            }
                        } else{
                            await meta.alert("일치하는 회원정보가 없습니다.");
                        }
                    }
                }
            },
            "changePwd" : async function() {
                this.validateChk('newPwd');
                this.validateChk('newPwdCk');

                if(!this.flag.newPwdFlag || !this.flag.newPwdCkFlag){
                    await meta.alert("양식에 맞게 입력해주세요.");
                } else {
                    let data = await meta.api.common.find.modifyPassword(this.userId, {
                                    "newPassword" : this.data.user.newPassword
                                });
                    await meta.alert("변경되었습니다.");
                    this.visible.pwVisible = false;
                    this.$router.go("/");
                }
            },
            "certiPhone" : async function(){
                if(!this.flag.certiFlag) {
                    window.name ="Parent_window";
                    window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
                    document.idPwForm.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
                    document.idPwForm.target = "popupChk";
                    document.idPwForm.submit();
                } else {
                    await meta.alert("이미 인증처리가 되었습니다.");
                }
            },
            "validateChk": async function(div) {
                let self = this;

                switch(div) {
                    case "name":
                        var regName = /^[가-힣a-zA-Z]+$/;
                        let name = self.data.user.name;

                        if(name === undefined){
                            self.flag.nameText = "이름을 입력해주세요";
                            self.flag.nameFlag = false;
                        } else if(!regName.test(name) || name.search(/\s/) !== -1){
                            self.flag.nameText = "한글/영어만 입력해주세요 (특수문자 또는 공백불가)";
                            self.flag.nameFlag = false;
                        } else {
                            self.flag.nameText = "";
                            self.flag.nameFlag = true;
                        }
                        break;
                    case "email" :
                        var regEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
                        let email = self.data.user.username;

                        if(email === undefined){
                            self.flag.emailText = "아이디를 입력해주세요";
                            self.flag.emailFlag = false;
                        } else if(!regEmail.test(email) || email.search(/\s/) !== -1){
                            self.flag.emailText = "이메일 형식에 맞춰서 입력해주세요";
                            self.flag.emailFlag = false;
                        } else {
                            self.flag.emailText = "";
                            self.flag.emailFlag = true;
                        }

                        break;
                    case "newPwd" :
                        var regPwd = /(?=.*\d{1,50})(?=.*[~``!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g;
                        let newPwd = self.data.user.newPassword;

                        if(newPwd === undefined || newPwd === ""){
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
                        let newPwdCk = self.data.user.newPasswordCk;

                        if(newPwdCk === undefined || newPwdCk === ""){
                            self.flag.newPwdCkText = "비밀번호 확인을 입력해주세요";
                            self.flag.newPwdCkFlag = false;
                        } else if(newPwdCk !== self.data.user.newPassword){
                            self.flag.newPwdCkText = "비밀번호가 일치하지 않습니다";
                            self.flag.newPwdCkFlag = false;
                        } else {
                            self.flag.newPwdCkText = "";
                            self.flag.newPwdCkFlag = true;
                        }
                        break;
                    defalut:
                        break;
                }
            },
        },
        "mounted": async function () {
            this.data.phone = (await meta.api.app.nice.findAccountCheck()).data;
        },
        "created": function() {
            this.$store.commit("app/SET_ACCOUNT", null);
        }
    });
});