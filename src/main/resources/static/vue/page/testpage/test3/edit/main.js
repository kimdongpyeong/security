var Test3LiveEditPage;
Test3LiveEditPage = Vue.component("test3-live-edit-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/edit/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "user": {},
                    "queryId": null,
                    "clientId": "mA4vpr2MRgiY30D40wOCsg", // 로컬용
//                    "clientId": "QX3YrlnXQFqDsZC725O_Yg", // 운영용
                    "redirectUri": "http://localhost:9090",
//                    "redirectUri": "http://meta-soft.iptime.org:9090",
                    "categoryList": [],
                    "tempImg": null,
                    "classRoom": {
                        "thumbnailSrc": "/resources/img/base.jpg",
                        "type": "N",
                        "title": "",
                        "memberNum": "",
                        "categoryId": "",
                        "openType": [],
                        "deleteYn": "N",
                        "fileFlag": "",
                    },
                    "timeSet":{
                        "endHour": "1",
                        "endMin": "0",      // 임시 데이터
                    },
                    "liveTimeList": [{
                        "title": null,
                        "startDate": null,
                        "startTime": null,
                    }],
                    deleteList:[],
                },
                "numberList": ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
            };
        },
        "watch": {
        },
        "methods": {
            "createMeeting": async function() {
                let win = window.open("https://zoom.us/oauth/authorize?response_type=code&client_id=" + this.data.clientId + "&redirect_uri="+this.data.redirectUri+"/api/common/zoom/createOauthMeeting");

                setTimeout(async function() {

                }, 2000);
            },
            // 카테고리 불러오기
            "loadCategoryList": async function() {
                this.data.categoryList = [];

                let categoryList = (await meta.api.common.category.getCategoryList({
                    "page": 1,
                    "rowSize": 100000000,
                })).data.items;

                categoryList.forEach(e => {
                    this.data.categoryList.push({"text": e.title, "id": e.id});
                });
            },
            // 클래스 불러오기
            "loadClassRoom": async function() {
//                let classRoom = (await meta.api.common.classRoom.getClassRoom(this.data.queryId)).data;
                let classRoom = (await meta.api.common.classRoom.getClassRoom(35)).data;
                if(classRoom.saveFileNm){
                    classRoom.thumbnailSrc = "/api/app/images?subpath=classRoomProfile&filename="+ classRoom.saveFileNm;
                } else{
                    classRoom.thumbnailSrc = "/resources/img/base.jpg";
                }
                this.data.classRoom = _.cloneDeep(classRoom);
                await this.loadLiveTimeList();
            },
            // 클래스 타임 테이블 불러오기
            "loadLiveTimeList": async function(){
                let liveTimeList = (await meta.api.common.classRoomLiveTime.getClassRoomLiveTimeList({
                        "page": 1,
                        "rowSize": 100000000,
//                        "classroomId": this.data.classroom.id,
                        "classroomId": 35,
                    })).data.items;
                
//                await this.forEndTime(liveTimeList[0]);
                
                liveTimeList.forEach(e => {
                    this.$set(e, "startDate", e.startTime.substring(0,10));
                    this.$set(e, "startTime", e.startTime.substring(11,16));
                    e.startDate = moment(e.startDate).format('yyyyMMDD');
                })
                
                this.data.liveTimeList = _.cloneDeep(liveTimeList);
                console.log(this.data.classRoom);
                console.log(this.data.liveTimeList);
            },
            // 라이브 정보의 종료 시간 다시 계산해주기
            "forEndTime": async function(time){
                startTime = moment(time.startTime);
                endTime = moment(time.endTime);
                hours = endTime.diff(startTime, 'hours');
                minutes = endTime.diff(startTime, 'minutes') % 60;
                this.$set(this.data.timeSet, "endHour", hours);
                this.$set(this.data.timeSet, "endMin", minutes);
            },
            // LiveTime 테이블 추가
            "setLiveTimeList": function() {
                this.data.liveTimeList.push({
                    "title": null,
                    "startDate": null,
                    "startTime": null,
                })
            },
            // LiveTime 테이블 삭제
            "deleteLiveTimeListRow": function(id, idx) {
                if(id !== undefined){
                    this.data.deleteList.push(id)
                }
                
                this.data.liveTimeList.splice(idx, 1);

                if(this.data.liveTimeList.length == 0){
                    this.data.liveTimeList.push({
                        "title": null,
                        "startDate": null,
                        "startTime": null,
                    });
                }                    
            },
            // 라이브 생성/수정        (삭제의 경우 클래스페이지의 리스트or디테일페이지 개별 삭제 여부)
            "save": async function() {
                var self = this;

                let title = self.data.classRoom.title;
                if(title === undefined || title === null || title === ""){
                    await meta.alert("강의실 이름을 입력해주세요");
                    return;
                }

                let num = self.data.classRoom.memberNum;
                if(num === undefined || num === null){
                    await meta.alert("수강인원을 선택해주세요.");
                    return;
                }

                let thumbnail = self.$refs.thumbnail.files[0];
                if(this.data.classRoom.saveFileNm == undefined && (thumbnail === undefined || thumbnail === null)){
                    await meta.alert("썸네일을 선택해주세요.");
                    return;
                }

                if(await meta.confirm("'"+this.data.classRoom.title+"' 강의실을 생성/수정하시겠습니까?")) {
                    let formData = new FormData(),
                        i = 0;
                    
                    if(this.data.classRoom.id !== null && this.data.classRoom.id !== undefined){
                        formData.append("id", this.data.classRoom.id);
                    }
                    formData.append("type", this.data.classRoom.type);
                    formData.append("title", this.data.classRoom.title);
                    formData.append("memberNum", this.data.classRoom.memberNum);
                    formData.append("categoryId", this.data.classRoom.categoryId);
                    formData.append("openType", this.data.classRoom.openType);
                    formData.append("deleteYn", 'N');
//                    formData.append("createdBy", this.data.user.id);
                    formData.append("createdBy", 14);
                    if(this.$refs.thumbnail.files[0] != undefined || this.$refs.thumbnail.files[0] != null){
                        formData.append("thumbnail", this.$refs.thumbnail.files[0]);
                    }
                    formData.append("fileFlag", this.data.classRoom.fileFlag);
                    formData.append("endHour", this.data.timeSet.endHour);
                    formData.append("endMin", this.data.timeSet.endMin);
                    
                    for(i = 0; i < this.data.liveTimeList.length; i++){
                        if(this.data.liveTimeList[i].id !== null && this.data.liveTimeList[i].id !== undefined){
                            formData.append("liveTime[" + i + "].id", this.data.liveTimeList[i].id);
                        }
                        formData.append("liveTime[" + i + "].title", this.data.liveTimeList[i].title);
                        formData.append("liveTime[" + i + "].startDate", this.data.liveTimeList[i].startDate);
                        formData.append("liveTime[" + i + "].startTime", this.data.liveTimeList[i].startTime);
                    }
                    
                    // 강의실 저장
                    if(this.data.classRoom.id == null && this.data.classRoom.id == undefined){
                        (await meta.api.common.classRoom.createClassRoom(formData)).data;
//                        self.data.zoomLink = data.zoomLink;
                        await meta.alert("'"+this.data.classRoom.title+"' 강의실이 생성되었습니다.");
                    } else {
                        if(this.data.deleteList.length > 0){
                            this.data.deleteList.forEach(e =>{
                                console.log(e);
                                meta.api.common.classRoomLiveTime.removeClassRoomLiveTime(e);
                            })
                        }
                        (await meta.api.common.classRoom.modifyClassRoom(formData)).data;
//                        self.data.zoomLink = data.zoomLink;
                        await meta.alert("'"+this.data.classRoom.title+"' 강의실이 수정되었습니다.");
                    }
                }
            },
            // 파일 선택(파일을 변경할때)
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0 || file == undefined) {
                    self.fileClear();
                    this.data.classRoom.fileFlag = "remove";
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    this.data.classRoom.fileFlag = "remove";
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {
                    self.data.classRoom.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);
                
                this.data.classRoom.fileFlag = "change";
            },
            // 파일 초기화
            "fileClear": function() {
                this.data.classRoom.thumbnailSrc = "/resources/img/base.png";
                this.$refs.thumbnail.value = null;
            },
//            "cancel": function(){
//                Object.assign((this.$data).select, this.$options.data().select);
//                this.data.classRoom.thumbnail = "/resources/img/base.png";
//                this.$refs.thumbnail.value = null;
//            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
            
            if(this.$route.query.id !== undefined){
                this.data.queryId = this.$route.query.id;
            }
            
            await this.loadCategoryList();
            await this.loadClassRoom();
        },
        "created": async function() {
        }
    });
});