var SignUpPage;
SignUpPage = Vue.component("sign-up-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/sign-up/main.html")).data,
        "data": function() {
            return {
                "dialog": undefined,
                "selection": null,
                "treeMenuList": [],
                "secondMenuList": [],
                data: {
                    successMessage: null,
                    authPass: null,
                    email: null,
                    authCode: null,
                    "termsList": [],
                    "googleUser": {},
                    "kakaoUser": {},
                    "phone": {},
                    "user": {},
                    "profileSrc": "/resources/img/profile.png",
                    "roleId": null,
                    "username" : null,
                    "password" : null,
                    "gauth": null,
                    "referralId":null,
                    "youtubeLink": "",
                },
                "btn": {
                    "signIn": {
                        "loading": false
                    }
                },
                "select": {
                    "residence":{
                        "items":[]
                    },
                },
                "terms" : {
                    "checkAll": false,
                    "checked": [],
                    "checkContent": [],
                    "contentsList":[],
                    "contents":{}
                },
                "click": {
                    "profileUploaded": false,
                    "pwClicked": false,
                    "pwCheckClicked": false,
                    "normalclicked": false,
                    "teacherclicked": false,
                    "loginPwClicked": false,
                },
                "auth": {
                    "div": null,
                    "isSendMail": false
                },
                "flag": {
                    "certiFlag": false,
                    "phoneText": null,
                    "emailFlag": true,
                    "emailText": null,
                    "pwdFlag": true,
                    "pwdText": null,
                    "pwdCkFlag": true,
                    "pwdCkText": null,
                    "nameFlag": true,
                    "nameText": null,
                    "nicknameFlag": true,
                    "nicknameText": null,
                    "ageFlag": true,
                    "ageText": null,
                    "saveIdFlag": false,
                    "termFlag":false,
                    "saveFlag":false
                },
                "visible": {
                    "step01Visible": false,
                    "signIndialogVisible": false,
                    "cancelVisible": false,
                    "termsVisible": false,
                    "profileVisible": false,
                    "menuVisible": false
                },
                "text" : {
                    "type" : "password"
                },
                "rules": {
                    "email": (value) => {
                        let message, flag
                        flag = false;
                        if (value == null) {
                            message = '이메일을 입력해 주세요.'
                        } else if (!/^[0-9a-zA-Z]([~'!'#$%^&*()-_+<>?:{}]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/g.test(value)) {
                            message = '이메일 형식에 맞지 않는 메일 주소입니다. 다시 입력해 주세요.'
                        } else {
                            flag = true;
                        }
                        return flag || message;
                    },
                },
                "keyword": "",
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
                    this.signInDialog();
                }
            },
            "$store.state.app.signUpPopFlag": function(val) {
                if(val) {
                    this.showStep01Dialog('N');
                }
            },
            "$store.state.app.treeMenuList": {
                "handler": async function (n) {
                    let newTreeMenuList = _.cloneDeep(n);
                    if(newTreeMenuList !== undefined && newTreeMenuList !== null) {
                        this.treeMenuList = newTreeMenuList;
                    }else {
                        this.treeMenuList = (await meta.api.util.menu.getDefaultMenuList()).data.items;
                    }
                }
            },
            "$store.state.app.signUpPhoneNum": function(val) {
                this.$set(this.data.user, "phoneNum", val);
                this.flag.certiFlag = (val != null)? true : false;
            },
            "data.user.username" : function(v){
            },
            "data.user.password" : function(v){
            },
            "data.user.name" : function(v){
            },
            "data.user.nickname" : function(v){
            },
            "data.user.recommender" : function(v){
            },
            "flag": {
                handler: function(n, o) {
                    if(this.flag.emailFlag && this.flag.pwdFlag && this.flag.pwdCkFlag && this.flag.nameFlag
                            && this.flag.nicknameFlag && this.flag.certiFlag && this.flag.termFlag) {
                        this.flag.saveFlag = true;
                    } else {
                        this.flag.saveFlag = false;
                    }
                },
                deep: true
            }
        },
        "methods": {
            "emailSend": async function() {
                let data = {
                    email: this.data.email
                }
                this.data.successMessage = (await meta.auth.emailAuth(data)).data
                if (this.data.successMessage === 'success'){
                    meta.alert("해당 이메일로 인증번호가 발송 되었습니다.");
                } else {
                    meta.alert("아이디 혹은 이메일을 정확히 입력해 주세요.");
                }
            },
            "authCode": async function(){
                let data = {
                    email: this.data.email
                }
                authSuccess = (await meta.auth.emailcomparison(data)).data.code
                if ( authSuccess === this.data.authCode ){
                    meta.alert("인증이 완료되었습니다.");
                    this.data.authPass = "success";
                } else {
                    meta.alert("인증번호가 틀렸습니다.");
                }
            },
            "init": async function() {
                Kakao.init('e06884a5c42a479c4766d55703829af3');
            },
            "goMypage": function(){
                this.$router.push({
                    "path": "/mypage/lecturers/main",
                });
            },
            "myPage": function(){
                let roleId = store.state.app.roleList[0].id;
    
                if(roleId === 3){
                    this.$router.push({
                        "path": "/mypage/users/main",
                    });
                } else{
                    this.$router.push({
                        "path": "/mypage/lecturers/main",
                    });
                }
            },
            "search" : function(keyword){
                if(keyword !== null && keyword !==''){
                    this.$router.push({
                        "path": "/zclass/classes",
                        "query": {
                            "search": keyword
                        }
                    });
                    this.keyword = "";
                } else{
                    this.$router.push({
                        "path": "/zclass/classes"
                    });
                }
            },
            "modalClose": function() {
                this.visible.cancelVisible = true;
            },
            "visibleClear": function() {
                this.visible.step01Visible = false;
                this.visible.signIndialogVisible = false;
                this.visible.cancelVisible = false;
                this.visible.termsVisible = false;
                document.documentElement.style.overflow = 'auto';
            },
            "loadTermsList": async function(){
                let contents = (await meta.api.common.terms.getTermsList()).data.items;
                for(let i in contents) {
                    contents[i].checked = false;
                }
                this.terms.checkContent = contents;
            },
            "signUpShowPassword" : function(){
                let self = this;
    
                if(!self.click.pwClicked || !self.click.pwCheckClicked){
                    self.click.pwClicked = true;
                    self.click.pwCheckClicked = true;
                    this.$refs.pwd.type = "text";
                    this.$refs.pwdCk.type = "text";
                } else{
                    self.click.pwClicked = false;
                    self.click.pwCheckClicked = false;
                    this.$refs.pwd.type = "password";
                    this.$refs.pwdCk.type = "password";
                }
            },
            "loginShowPassword" : function(){
                let self = this;
    
                if(self.click.loginPwClicked === false){
                    self.click.loginPwClicked = true;
                    this.$refs.loginPw.type = "text";
                } else{
                    self.click.loginPwClicked = false;
                    this.$refs.loginPw.type = "password";
                }
            },
            "existUsername": async function (username) {
                return (await meta.auth.idExists({"username": username})).data > 0 ? true : false;
            },
            "existSmsUsername": async function (username) {
                return (await meta.auth.smsIdExists({"username": username})).data > 0 ? true : false;
            },
            "existNickname": async function (nickname) {
                return (await meta.auth.idExists({"nickname": nickname})).data > 0 ? true : false;
            },
            "existSignUpWay": async function (username, signUpWay) {
                return (await meta.auth.idExists({"username": username, "signUpWay": signUpWay})).data > 0 ? true : false;
            },
            "stck": function (str, limit) {
                var o, d, p, n = 0, l = limit == null ? 4 : limit;
                for (var i = 0; i < str.length; i++) {
                    var c = str.charCodeAt(i);
                    if (i > 0 && (p = o - c) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > l - 3)
                        return false;
                    d = p;
                    o = c;
                }
                return true;
            },
            "logout": async function () {
                var token;
                token = meta.auth.getToken();
                await meta.auth.logout(token);
                if(this.$route.path === "/") {
                    this.$router.go();
                } else {
                    this.$router.replace("/");
                }
            },
            "move": function (path) {
                if(path !== this.$route.path){
                    this.dialog = false;
                    this.$router.push({path});
                }
            },
            "showMenu": function (id) {
                let menuList = [];
    
                this.activeId = id;
    
                for(let i in this.treeMenuList) {
                    if(this.treeMenuList[i].parentId == id) {
                        menuList.push(this.treeMenuList[i]);
                    }
                }
    
                if(menuList.length > 0) {
                    this.secondMenuList = menuList;
                } else {
                    this.secondMenuList = [];
                }
            },
        "contentCheck": function() {
            let flag = true,
                contents = this.terms.checkContent;

            let requiredCnt = this.terms.checkContent.filter(x => x.requiredYn == 'Y').length,
                cnt = 0;

            for(let item of contents) {
                if(!item.checked) {
                    flag = false;
                }

                if(item.requiredYn == "Y" && item.checked) {
                    cnt++;
                }
            }

            if(requiredCnt == cnt) {
                this.flag.termFlag = true;
            } else {
                this.flag.termFlag = false;
            }

            this.terms.checkAll = flag;
        },
        "checkedAll": function(checked) {
            let contents = this.terms.checkContent;

            this.terms.checkAll = checked;
            for(let i in contents) {
                contents[i].checked = this.terms.checkAll;
            }
        },
            "certiPhone" : async function(){
                if(!this.flag.certiFlag) {
                    window.name ="Parent_window";
                    window.open('', 'popupChk', 'width=500, height=550, top=100, left=100, fullscreen=no, menubar=no, status=no, toolbar=no, titlebar=yes, location=no, scrollbar=no');
                    document.signUpForm.action = "https://nice.checkplus.co.kr/CheckPlusSafeModel/checkplus.cb";
                    document.signUpForm.target = "popupChk";
                    document.signUpForm.submit();
                } else {
                    await meta.alert("이미 인증처리가 되었습니다.");
                }
            },
            "showStep01Dialog": async function(signUpWay) {
                this.data.user = {};
                this.data.user.username = this.data.email;
                
                this.loadTermsList();
                
                if(!this.data.user.gender) {
                    this.$set(this.data.user, "gender", "W");
                }
                
                if(this.data.googleUser.username != null) {
                    this.$set(this.data.user, "name", this.data.googleUser.name);
                    this.$set(this.data.user, "username", this.data.googleUser.username);
                }
    
                if(this.data.kakaoUser.username != null) {
                    this.$set(this.data.user, "username", this.data.kakaoUser.username);
                    this.$set(this.data.user, "name", this.data.kakaoUser.name);
                    this.$set(this.data.user, "gender", this.data.kakaoUser.gender);
                }
    
                this.$set(this.data.user, "phoneNum", null);
    
                this.data.roleId = null;
                this.click.normalclicked = false;
                this.click.teacherclicked = false;
    
                this.flag.emailFlag = true;
                this.flag.emailCkFlag = false;
                this.flag.pwdFlag = true;
                this.flag.pwdCkFlag = true;
                this.flag.nameFlag = true;
                this.flag.nicknameFlag = true;
                this.flag.certiFlag = false;
                this.flag.ageFlag = true;
    
                this.flag.emailText = null;
                this.flag.pwdText = null;
                this.flag.pwdCkText = null;
                this.flag.nameText = null;
                this.flag.nicknameText = null;
                this.flag.ageText = null;
    
                this.$set(this.data.user, "signUpWay", signUpWay);
                this.visible.step01Visible = true;
    
                this.data.phone = (await meta.api.app.nice.signUpCheck()).data;
            },
            "validateChk": async function(div) {
                let self = this;
    
                switch(div) {
                    case "email" :
                        var regEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
                        let email = self.data.user.username;
    
                        if(email === undefined){
                            self.flag.emailText = "이메일을 입력해주세요";
                            self.flag.emailFlag = false;
                        } else if(!regEmail.test(email) || email.search(/\s/) !== -1){
                            self.flag.emailText = "이메일 형식에 맞춰서 입력해주세요";
                            self.flag.emailFlag = false;
                        } else if(await this.existUsername(email)){
                            self.flag.emailText = "중복된 아이디입니다";
                            self.flag.emailFlag = false;
                            self.flag.emailCkFlag = false;
                        } else {
                            self.flag.emailText = "";
                            self.flag.emailFlag = true;
                        }
    
                        break;
                    case "pwd" :
                        var regPwd = /(?=.*\d{1,50})(?=.*[~``!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g;
                        let pwd = self.data.user.password;
    
                        if(pwd === undefined){
                            self.flag.pwdText = "비밀번호를 입력해주세요";
                            self.flag.pwdFlag = false;
                        } else if(!regPwd.test(pwd) || pwd.search(/\s/) !== -1 || pwd.length < 8 ){
                            self.flag.pwdText = "8~16자의 영문 대소문자, 숫자, 특수문자만 가능합니다";
                            self.flag.pwdFlag = false;
                        } else {
                            self.flag.pwdText = "";
                            self.flag.pwdFlag = true;
                        }
                        break;
                    case "pwdCk" :
                        var regPwdCk = /(?=.*\d{1,50})(?=.*[~``!@#$%\^&*()-+=]{1,50})(?=.*[a-zA-Z]{2,50}).{8,50}$/g;
                        let pwdCk = self.data.user.passwordCk;
    
                        if(pwdCk === undefined){
                            self.flag.pwdText = "비밀번호 확인을 입력해주세요";
                            self.flag.pwdCkFlag = false;
                        } else if(pwdCk !== self.data.user.password){
                            self.flag.pwdText = "비밀번호가 일치하지 않습니다";
                            self.flag.pwdCkFlag = false;
                        } else {
                            self.flag.pwdText = "";
                            self.flag.pwdCkFlag = true;
                        }
                        break;
                    case "name":
                        var regName = /^[가-힣a-zA-Z]+$/;
                        let name = self.data.user.name;
    
                        if(name === undefined || name === null || name === ""){
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
                    case "nickname":
                        let nickname = this.data.user.nickname;
    
                        if(nickname === null || nickname === undefined || nickname === ""){
                            self.flag.nicknameText = "닉네임을 입력해주세요";
                            self.flag.nicknameFlag = false;
                        } else if(await this.existNickname(nickname)){
                            self.flag.nicknameText = "동일한 닉네임이 존재합니다";
                            self.flag.nicknameFlag = false;
                        } else if(nickname.length > 11) {
                            self.flag.nicknameText = "닉네임은 10자 이하로 작성해주세요";
                            self.flag.nicknameFlag = false;
                        } else {
                            self.flag.nicknameText = "";
                            self.flag.nicknameFlag = true;
                        }
                        break;
                    case "age":
                        let age = self.data.user.age;
                        var regNum = /^[0-9]*$/;
                        if(age !== null && age !== undefined && age !== ""){
                            if(!regNum.test(age) || age.search(/\s/) !== -1){
                                self.flag.ageText = "숫자만 입력해주세요";
                                self.flag.ageFlag = false;
                            } else{
                                self.flag.ageText = "";
                                self.flag.ageFlag = true;
                            }
                        } else{
                            self.flag.ageText = "";
                            self.flag.ageFlag = true;
                            self.data.user.age = 0;
                        }
                        break;
                    defalut:
                        break;
                }
            },
            "saveAccount": async function () {
                console.log("qwe")
                this.validateChk('email');
                this.validateChk('pwd');
                this.validateChk('pwdCk');
                this.validateChk('name');
                this.validateChk('nickname');
    
                if(this.data.user.signUpWay==='N'&&this.flag.emailFlag && this.flag.pwdFlag
                        && this.flag.pwdCkFlag && this.flag.nameFlag && this.flag.nicknameFlag && this.flag.ageFlag) {
                    this.sameForm();
                } else if(this.data.user.signUpWay !='N' && this.flag.nameFlag && this.flag.nicknameFlag && this.flag.ageFlag){
                    this.sameForm();
                } else{
                    await meta.alert("입력된 양식에 맞지 않습니다.");
                    return;
                }
            },
            "sameForm": async function(){
                let contents = this.terms.checkContent;
                this.data.termsList = [];
                for(let i in contents) {
                    if(contents[i].requiredYn == 'Y' && !contents[i].checked) {
                        await meta.alert("필수 약관 동의를 선택해주세요.");
                        return;
                    }
                    if(contents[i].checked){
                        this.data.termsList.push(contents[i].id);
                    }
                }
                this.saveUser();
            },
            "saveUser": async function(){
    
                let userDto, roleId;
                let fd = new FormData();
                this.$set(this.data.user, "status", "T");
                userDto = _.cloneDeep(this.data.user);
                roleId = _.cloneDeep(this.data.roleId);
    
                fd.append("roleId", 3);
    
                for(var key in userDto) {
                    fd.append(key, userDto[key]);
                }
                
                this.data.termsList.forEach((item) => fd.append("termsList[]", item));
                
                if(await meta.confirm("회원가입을 하시겠습니까?")) {
                    await meta.auth.signUp(fd);
                    await meta.alert("회원가입을 축하드립니다.");
                    this.$router.go("/");
                }
    
            },
            "signInDialog": async function() {
                this.data.username ="";
                this.data.password="";
                this.visible.signIndialogVisible = true;
    
                if(this.$cookies.get("supportiId")) {
                    this.flag.saveIdFlag = true;
                    this.$set(this.data, "username", this.$cookies.get("supportiId"));
                }
    
                if(this.click.loginPwClicked){
                    this.click.loginPwClicked = false;
                    this.$refs.loginPw.type = "password";
                }
            },
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();
    
                if (file == null || file.size === 0) {
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }
    
                reader.onload = function (e) {
                    self.data.profileSrc = e.target.result;
                }
    
                reader.readAsDataURL(file);
            },
            "fileClear": function() {
                this.data.user.profile = null;
                this.data.profileSrc = "/resources/img/profile.png";
            },
            "googleSetting": function() {
                let self = this;
    
                gapi.load('auth2', function() {
                    var gauth = gapi.auth2.init({
                        client_id: '369059048668-ni1eugv2c4bhf717au7dnhsbmp24keoc.apps.googleusercontent.com'
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
                        this.smsLogin(userInfo.email, self.data.user.signUpWay);
                    } else{
                        await meta.alert("구글로 가입한 회원이 아닙니다.\n\n기존에 가입경로로 로그인하시길 바랍니다.");
                        return;
                    }
                } else{
                    if(await meta.confirm("가입하시겠습니까?")){
                        self.data.googleUser = {};
                        self.data.googleUser.name = userInfo.name;
                        self.data.googleUser.username = userInfo.email;
    
                        self.showStep01Dialog('G');
                    }
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
                            nickname : (kakao_account.profile_nickname_needs_agreement == false ? kakao_account.profile.nickname : null),
                            email : (kakao_account.email_needs_agreement == false ? kakao_account.email : null),
                            password : '',
                            gender: (kakao_account.gender_needs_agreement == false ? kakao_account.gender : null),
                            birth : (kakao_account.birthday_needs_agreement == false ? kakao_account.birthday : null),
                            account_type : 2,
                        }
                        this.kakaoUser = userInfo;
                        this.data.user.signUpWay = "K";
                        userInfo.gender = (userInfo.gender === "female" ? "W" : "M");
    
                        let fd = new FormData();
    
                        for(var key in userInfo) {
                            fd.append(key, userInfo[key]);
                        }
    
                        if(await this.existSmsUsername(userInfo.email)){
                            if(await this.existSignUpWay(userInfo.email, this.data.user.signUpWay)){
                                this.smsLogin(userInfo.email, this.data.user.signUpWay);
                            } else{
                                await meta.alert("카카오로 가입한 회원이 아닙니다.\n\n기존에 가입경로로 로그인하시길 바랍니다.");
                            }
                        } else{
                            if(await meta.confirm("가입하시겠습니까?")){
                                this.data.kakaoUser.name = userInfo.nickname;
                                this.data.kakaoUser.gender = userInfo.gender;
                                this.data.kakaoUser.username = userInfo.email;
    
                                this.showStep01Dialog('K');
                            }
                        }
                    },
                    fail : error => {
                        meta.alert(error);
                    }
                })
            },
            "kakaoShare": function() {
                Kakao.Link.sendDefault({
                    objectType: "feed",
                    content: {
                        title: "10대들의 고민 상담소, Z.PLAY",
                        description: "Z-PLAY 링크공유",
                        imageUrl: "https://supporti.kr/resources/img/supporti_link.png",
                        link: {
                            mobileWebUrl: 'https://supporti.kr',
                            webUrl: 'https://supporti.kr',
                        },
                    },
                    buttons: [
                        {
                            title: '웹으로 이동',
                            link: {
                                mobileWebUrl: 'https://supporti.kr',
                                webUrl: 'https://supporti.kr',
                            },
                        },
                        {
                            title: '앱으로 이동',
                            link: {
                                mobileWebUrl: 'https://supporti.kr',
                                webUrl: 'https://supporti.kr',
                            },
                        },
                    ]
                });
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
                    let from = this.$route.query.from,
                        role = "&role=" + this.$route.query.role,
                        path = from + role;
    
                    await meta.auth.login(username, null, signUpWay);
                    if(from !== undefined && from !== null && from !== "") {
                        this.$router.replace(path);
                    } else{
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
            "getTermsDetail": async function(i){
                let termsContents = this.terms.checkContent[i];
    
                if(termsContents.requiredYn === "Y"){
                    this.terms.contents.title = "[필수]  " + termsContents.title;
                } else {
                    this.terms.contents.title = "[선택]  " + termsContents.title;
                }
    
                this.terms.contents.contents = termsContents.contents.split('\n').join('<br />');
                this.visible.termsVisible = true;
            },
        },
        "created": function () {
            let treeMenuList = _.cloneDeep(this.$store.state.app.treeMenuList);
            if(treeMenuList !== undefined && treeMenuList !== null) {
                this.treeMenuList = treeMenuList;
            }else {
            }
            for(var i = 0; i < this.treeMenuList.length; i++) {
                if(this.treeMenuList[i].show === 'T' && this.$route.fullPath.startsWith(this.treeMenuList[i].path)) {
                    this.selection = i;
                    break;
                }
            }
    
            this.$store.commit("app/SET_SIGN_UP_PHONENUM", null);
        },
        "mounted": async function () {
            this.init();
        }
    });
});