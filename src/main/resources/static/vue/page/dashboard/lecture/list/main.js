var DashboardLectureListMainPage;
DashboardLectureListMainPage = Vue.component("dashboard-lecture-list-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/lecture/list/main.html")).data,
        "data": function () {
            return {
                "data": {
                	"deleteRealClass":[],
                	"deleteNormalClass":[],
                	"deleteEndClass": [],
                    "deleteClassType": null,
                    "user": {},
                    "profileSrc": "/resources/img/profile.png",
                    "classRoomList": {
                        "real": [],
                        "normal": [],
                        "end": [],
                        "realRowSize": 8,
                        "normalRowSize": 8,
                        "endRowSize": 8,
                    },
                },
                "visible": {
                    "deleteDialog": false
                },
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
                })).data.items, 
                today = moment().format("yyyy-MM-DD HH:mm:ss");

                //실시간, 일반 강의 구분하여 저장
                classRoomList.forEach(e => {
                	if(e.type === 'R'){
                		if(e.classEndTime > today){
                			this.data.classRoomList.real.push(e);
                		} else{
                			this.data.classRoomList.end.push(e);
                		}
                	} else{
                		this.data.classRoomList.normal.push(e);
                	}
                });

                this.data.deleteClass = [];
            },
            "moreClassRoomList": function(type) {
            	if(type === 'R'){
            		this.data.classRoomList.realRowSize += 4;
            	} else if(type === 'N'){
            		this.data.classRoomList.normalRowSize += 4;
            	} else{
            		this.data.classRoomList.endRowSize += 4;
            	}
            },
            "lessClassRoomList": function(type) {
            	if(type === 'R'){
            		this.data.classRoomList.realRowSize -= 4;
            	} else if(type === 'N'){
            		this.data.classRoomList.normalRowSize -= 4;
            	} else{
            		this.data.classRoomList.endRowSize -= 4;
            	}
            },
            "classCheck": function(id, type) {
                let rArr = this.data.deleteRealClass,
                	nArr = this.data.deleteNormalClass,
                	eArr = this.data.deleteEndClass;

                if(type === 'R'){
                	if(rArr.includes(id)) {
                		rArr.splice(rArr.indexOf(id, 1));
                	} else {
                		rArr.push(id);
                	}
                } else if(type === 'N'){
                	if(nArr.includes(id)) {
                		nArr.splice(nArr.indexOf(id, 1));
                	} else {
                		nArr.push(id);
                	}
                } else{
                	if(eArr.includes(id)) {
                		eArr.splice(eArr.indexOf(id, 1));
                	} else {
                		eArr.push(id);
                	}
                }
            },
            "detailClass": function(id) {
                this.$router.push({
                    "query": {
                        "id" : id
                    },
                    "path": "/dashboard/lecture/list/detail"
                });
            },
            "deleteClassPop": function(type) {
            	this.data.deleteClassType = type;

            	if(type === 'R'){
                    if(this.data.deleteRealClass.length == 0) {
                        meta.alert("삭제할 클래스를 선택해주세요.");
                    } else {
                        this.visible.deleteDialog = true;
                    }
                } else if(type === 'N'){
                    if(this.data.deleteNormalClass.length == 0) {
                        meta.alert("삭제할 클래스를 선택해주세요.");
                    } else {
                        this.visible.deleteDialog = true
                    }
                } else{
                    if(this.data.deleteEndClass.length == 0) {
                        meta.alert("삭제할 클래스를 선택해주세요.");
                    } else {
                        this.visible.deleteDialog = true
                    }
                }
            },
            "deleteClass": async function() {
            	let deleteClass = [], data;
            	
                if(this.data.deleteClassType === 'R'){
                	deleteClass = this.data.deleteRealClass;
                } else if(this.data.deleteClassType === 'N'){
                	deleteClass = this.data.deleteNormalClass;
                } else{
                	deleteClass = this.data.deleteEndClass;
                }
            	
                (await meta.api.common.classRoom.removeClassRoom({"deleteClass": deleteClass}));
                this.visible.deleteDialog = false;
                await meta.alert("삭제가 완료되었습니다.");
                this.$store.commit("app/SET_LOADING", true);
                this.$router.go();
            },
            "createClass": function() {
              this.$router.push({
                  "path": "/dashboard/lecture/list/detail"
              });
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