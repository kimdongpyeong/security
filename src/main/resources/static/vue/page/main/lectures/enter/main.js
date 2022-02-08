var MainLecturesEnterMainPage;
MainLecturesEnterMainPage = Vue.component("main-lectures-enter-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/main/lectures/enter/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "user":{
                        "type":"S",
                        "name": "",
                        "grade":"",
                        "email":"",
                        "phoneNum":""
                    },
                    "id": null,
                    "userId": null,
                    "classroomId":null,
                    "enterCode": null,
                    "compareCode": null,
                    "enterUserId": null,
                    "classRoomTitle": null,
                    "enterCount": null,
                    "countRemainder": null,
                    "roomMaxNum": null,
                },
                "guestId":null,
                "select":{
                    "grade":['예비 고1','고1','고2','고3','N수','기타']
                },
                "visible":{
                    "userInfoVisible": false
                },
                "flag":{
                    "nameFlag": true,
                    "nameText": null,
                    "gradeFlag": true,
                    "gradeText": null,
                    "emailFlag": true,
                    "emailText": null,
                    "phoneFlag": true,
                    "phoneText": null,
                }
            };
        },
        "methods": {
            "validateChk": async function(div) {
                let self = this;

                switch(div) {
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

                    case "grade":
                        let grade = self.data.user.grade;

                        if(grade === undefined || grade === null || grade === ""){
                            self.flag.gradeText = "학년을 선택해주세요.";
                            self.flag.gradeFlag = false;
                        } else{
                            self.flag.gradeText = "";
                            self.flag.gradeFlag = true;
                        }
                        break;

                    case "email" :
                        var regEmail = /^([\w-]+(?:\.[\w-]+)*)@((?:[\w-]+\.)*\w[\w-]{0,66})\.([a-z]{2,6}(?:\.[a-z]{2})?)$/i;
                        let email = self.data.user.email;

                        if(email === undefined){
                            self.flag.emailText = "이메일을 입력해주세요";
                            self.flag.emailFlag = false;
                        } else if(!regEmail.test(email) || email.search(/\s/) !== -1){
                            self.flag.emailText = "이메일 형식에 맞춰서 입력해주세요";
                            self.flag.emailFlag = false;
                        } else {
                            self.flag.emailText = "";
                            self.flag.emailFlag = true;
                        }

                        break;

                    case "phone":
                        let phone= self.data.user.phoneNum;
                        var regNum = /^[0-9]*$/;

                        if(phone === undefined || phone === null || phone === ""){
                            self.flag.phoneText = "핸드폰 번호를 입력해주세요";
                            self.flag.phoneFlag = false;
                        } else if(!regNum.test(phone) || phone.search(/\s/) !== -1){
                            self.flag.phoneText = "숫자만 입력해주세요";
                            self.flag.phoneFlag = false;
                        } else{
                            self.flag.phoneText = "";
                            self.flag.phoneFlag = true;
                        }
                        break;
                    defalut:
                        break;
                }
            },
            "loadClassRoom": async function(){
                let data = (await meta.api.common.classRoom.getClassRoom(this.data.classroomId)).data;
                this.compareCode = data.enterCode + data.id;
            },
            "enter": async function(){
                let param = {};

                this.validateChk('name');
                this.validateChk('grade');
                this.validateChk('email');
                this.validateChk('phone');
                if(this.flag.nameFlag && this.flag.gradeFlag && this.flag.emailFlag && this.flag.phoneFlag){
                    if ( this.compareCode == this.data.enterCode ){
                        if (await meta.confirm("강의실에 입장하시겠습니까?")) {
                            param = this.data.user;
                            param.guestCode = this.data.enterCode;
                            param.classroomId = this.data.classroomId;
                            let EnterUser = (await meta.api.common.enterUser.createEnterUser(param)).data
                            this.data.enterUserId = EnterUser.id;
                            this.$router.push({
                                "path": "/main/lectures/detail",
                                "query": {
                                    "id": this.data.classroomId,
                                }
                            });
                        }
                    }else{
                        meta.alert("옳바른 경로가 아닙니다.")
                    }
                } else{
                    meta.alert("양식에 맞게 입력해주세요.")
                }
            },
            "loadClassRoomUser": async function(){
                let code =_.cloneDeep(this.$route.query.id);
                this.data.classroomId = code.substring(8);
                roomUserList = (await meta.api.common.classRoomUser.getClassRoomUserList({
                        "page": 1,
                        "rowSize": 100000000,
                        "classroomId": this.data.classroomId
                    })).data.items,
                    roomMaxNum = (await meta.api.common.classRoom.getClassRoom(this.data.classroomId)).data.memberNum,
                    roomGuestCount = (await meta.api.common.enterUser.getEnterUserList({
                    	"classroomId": this.data.classroomId
                    	})).data.totalRows;
                //입장 최대인원수
                this.data.roomMaxNum = roomMaxNum; 
                //참여중인 인원 수
                this.data.enterCount = parseInt(roomUserList.length) + parseInt(roomGuestCount);
                //참여가능 인원 수 = 최대인원수 - 참여중인 인원수
                this.data.countRemainder = parseInt(roomMaxNum) - this.data.enterCount;
                
                //참여가능 인원 수가 0보다 클 때 입장 가능
                let myEnterCheckList = (await meta.api.common.classRoomUser.getClassRoomUser({
                    "page": 1,
                    "rowSize": 100000000,
                    "classroomId": this.data.classroomId,
                    "userId": this.data.user.id
                })).data.items;
                
                // 로그인한 본인이 참여중인 강의실이면 바로 이동
            	if(myEnterCheckList.length > 0){
                    this.$router.push({
                        "path": "/main/lectures/chat",
                        "query": {
                            "id": this.data.classroomId
                        }
                    });
            	} else{
            		//참여가능 인원 수가 양수일 경우 입장 가능
            		if(this.data.countRemainder > 0){
            			if(this.compareCode == this.data.enterCode ){
                            if (await meta.confirm("강의실에 입장하시겠습니까?")) {
                                (await meta.api.common.classRoomUser.createClassRoomUser({
                                    "classroomId": this.data.classroomId,
                                    "userId": this.data.user.id
                                })).data;
                                
                                this.$router.push({
                                    "path": "/main/lectures/chat",
                                    "query": {
                                        "id": this.data.classroomId
                                    }
                                });
                            } else{
                                this.$router.replace("/");
                            }
                        } else{
                        	meta.alert("올바르지 않은 경로입니다.");
                        }
            		} else{
            			meta.alert("강의실 참여 인원이 모두 차 입장이 불가합니다.");
                        this.$router.replace("/");
            		}
            	}
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            let code =_.cloneDeep(this.$route.query.id);
            if(user){
            	this.$set(this.data.user, "id", user.id);
                this.loadClassRoomUser();
            } else{
            	store.state.app.loginPopFlag = true;
            }
            this.data.classroomId = code.substring(8);
            this.data.enterCode = code;
            this.loadClassRoom();
        },
        "created": async function() {
        }
    });
});