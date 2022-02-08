var DashboardLectureListDetailPage;
DashboardLectureListDetailPage = Vue.component("dashboard-lecture-list-detail-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/lecture/list/main-detail.html")).data,
        "data": function () {
            return {
            	"data":{
            		"user":{},
                    "classRoom": {
                        "thumbnailSrc": "/resources/img/base.jpg",
                        "type": "",
                        "title": "",
                        "memberNum": "",
                        "categoryId": "",
                        "etc": "",
                        "openType": "",
                        "deleteYn": "N",
                        "fileFlag": "",
                        "enterCode": "",
                        "createdBy": ""
                    },
                    "classTime":{
                    	"date": null,
                    	"startTime": null,
                    	"endTime": null
                    },
                    "categoryList":[],
                    "numberList":[]
            	},
            	"flag":{
            		"typeText": null,
            		"typeFlag": true,
            		"categoryText": null,
            		"categoryFlag": true,
            		"titleText": null,
            		"titleFlag": true,
            		"memberText": null,
            		"memberFlag": true,
            		"openTypeText": null,
            		"openTypeFlag": true,
            		"dateText": null,
            		"dateFlag": true
            	}
            };
        },
        "computed": {
        },
        "watch": {
        	"data.classRoom.type": function(e){
        		// 유형 일반 선택 시, 실시간 선택 시에만 나타나는 입력항목 초기화
        		if(e === 'N'){
        			this.data.classTime.date = null;
        			this.data.classTime.startTime = null;
        			this.data.classTime.endTime = null;
        		}
        	},
        	"data.classRoom.categoryId": function(e){
        		// 카테고리 기타외 선택 시, 기타 선택 시에만 나타나는 입력항목 초기화
        		if(e !== 23){
        			this.data.classRoom.etc = "";
        		}
        	},
        	"data.classRoom.openType": function(e){
        		// 공개 선택 시, 비공개 선택 시 나타나는 입력항목 초기화
        		if(e === 'O'){
        			this.data.classRoom.enterCode = null;
        		}
        	}
        },
        "methods": {
        	//취소버튼 클릭 시
            "goList": function() {
                this.$router.push({
                    "path": "/dashboard/lecture/list"
                });
            },
            // 카테고리 불러오기
            "loadCategoryList": async function() {
            	let numberList = [], categoryList = [];
                var i;
                
                this.data.categoryList = [];

                categoryList = (await meta.api.common.category.getCategoryList({
                    "page": 1,
                    "rowSize": 100000000,
                })).data.items;
                
                // 과목 카테고리 저장
                categoryList.forEach(e => {
                    this.data.categoryList.push({"text": e.title, "id": e.id});
                });
                
                // 최대인원수 리스트 저장
                for(i = 1; i < 21; i++){
                	numberList.push(i);
                }
                this.data.numberList = numberList;
            },
            // 해당 classroomId의 강의정보 불러오기
            "loadClassRoom": async function(id){
            	let classRoom = (await meta.api.common.classRoom.getClassRoom(id)).data;

            	if(classRoom.saveFileNm){
                    classRoom.thumbnailSrc = "/api/app/images?subpath=classRoomProfile&filename="+ classRoom.saveFileNm;
                } else{
                    classRoom.thumbnailSrc = "/resources/img/base.jpg";
                }
            	classRoom.classEndTime = "";
            	console.log(classRoom)
            	this.data.classRoom = _.cloneDeep(classRoom);
            	
            	if(this.data.classRoom.type === "R"){
            		this.loadClassTime(id);
            	}
            },
            //불러온 강의 유형이 실시간일 경우 예상 날짜 및 시간 불러오기
            "loadClassTime": async function(id){
                let classTime = (await meta.api.common.classRoomLiveTime.getClassRoomLiveTimeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "classroomId": id,
                })).data.items;
                
                classTime.forEach(e => {
                    this.$set(e, "date", e.startTime.substring(0,10));
                    this.$set(e, "startTime", e.startTime.substring(11,16));
                    this.$set(e, "endTime", e.endTime.substring(11,16));
                	e.date = moment(e.date).format("yyyyMMDD");
                })
                
                this.data.classTime = _.cloneDeep(classTime[0]);
            },
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0 || file == undefined) {
                    self.fileClear();
                    self.data.classRoom.fileFlag = "remove";
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    meta.alert('이미지만 첨부 가능합니다');
                    self.data.classRoom.fileFlag = "remove";
                    return;
                }

                reader.onload = function (e) {
                    self.data.classRoom.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);
                
                self.data.classRoom.fileFlag = "change";
            },
            // 파일 초기화
            "fileClear": function(val) {
                if(val === 'reset'){
                	this.data.classRoom.thumbnailSrc = "/resources/img/base.jpg";
                	this.data.classRoom.fileFlag = "remove";
                }
                this.$refs.thumbnail.value = null;
            },
            "validateChk": async function(div) {
                let self = this;

                switch(div) {
                    case "type":
                        let type = self.data.classRoom.type;
                        if(type === undefined || type === null || type === ""){
                            self.flag.typeText = "유형을 선택해주세요";
                            self.flag.typeFlag = false;
                        } else {
                            self.flag.typeText = "";
                            self.flag.typeFlag = true;
                        }
                        break;
                    case "category":
                        let category = self.data.classRoom.categoryId,
                        	etc = self.data.classRoom.etc;
                        if(category === undefined || category === null || category === ""){
                            self.flag.categoryText = "카테고리를 선택해주세요.";
                            self.flag.categoryFlag = false;
                        } else{
                        	// 선택한 카테고리가 '기타'일 시, 입력사항 유효성도 체크
                        	if(category === 23){
                        		if(etc === undefined || etc === null || etc === ""){
                        			self.flag.categoryText = "기타 카테고리를 입력해주세요";
                        			self.flag.categoryFlag = false;
                        		} else{
                                    self.flag.categoryText = "";
                                    self.flag.categoryFlag = true;
                        		}
                        	} else{
                        		self.flag.categoryText = "";
                        		self.flag.categoryFlag = true;
                        	}
                        }
                        break;
                    case "title":
                        let title = self.data.classRoom.title;
                        if(title === undefined || title === null || title === ""){
                            self.flag.titleText = "유형을 선택해주세요";
                            self.flag.titleFlag = false;
                        } else {
                            self.flag.titleText = "";
                            self.flag.titleFlag = true;
                        }
                        break;
                    case "member":
                        let member = self.data.classRoom.memberNum;
                        if(member === undefined || member === null || member === ""){
                            self.flag.memberText = "최대 인원 수를 선택해주세요";
                            self.flag.memberFlag = false;
                        } else {
                            self.flag.memberText = "";
                            self.flag.memberFlag = true;
                        }
                        break;
                    case "openType":
                        let openType = self.data.classRoom.openType,
                        	enterCode = self.data.classRoom.enterCode;
                        var regNum = /^[0-9]*$/;
                        if(openType === undefined || openType === null || openType === ""){
                            self.flag.openTypeText = "공개 유형을 선택해주세요";
                            self.flag.openTypeFlag = false;
                        } else {
                        	if(openType === 'C'){
                        		if(enterCode === undefined || enterCode === null || enterCode === ""){
                        			self.flag.openTypeText = "비밀번호를 입력해주세요";
                        			self.flag.openTypeFlag = false;
                        		} else if(enterCode.length < 4){
                        			self.flag.openTypeText = "4자리 입력해주세요";
                        			self.flag.openTypeFlag = false;
                        		} else if(!regNum.test(enterCode) || enterCode.search(/\s/) !== -1){
                        			self.flag.openTypeText = "숫자만 입력해주세요";
                        			self.flag.openTypeFlag = false;
                        		} else{
                                    self.flag.openTypeText = "";
                                    self.flag.openTypeFlag = true;
                        		}
                        	} else{
                        		self.flag.openTypeText = "";
                        		self.flag.openTypeFlag = true;
                        	}
                        }
                        break;
                    case "date":
                        let date = self.data.classTime.date,
                        	startTime = self.data.classTime.startTime,
                        	endTime = self.data.classTime.endTime;

                        if(date === undefined || date === null || date === ""){
                            self.flag.dateText = "예상 날짜를 선택해주세요";
                            self.flag.dateFlag = false;
                        } else if(startTime === undefined || startTime === null || startTime === ""){
                            self.flag.dateText = "예상 시작 시간을 선택해주세요";
                            self.flag.dateFlag = false;
                        } else if(endTime === undefined || endTime === null || endTime === ""){
                            self.flag.dateText = "예상 종료 시간을 선택해주세요";
                            self.flag.dateFlag = false;
                        } else {
                            self.flag.dateText = "";
                            self.flag.dateFlag = true;
                        }
                        break;
                    defalut:
                        break;
                	}
                },
                // 강의실 등록
                "createClass": async function(){
                    let classRoom, fd = new FormData();
                    
                    this.data.classRoom.createdBy = this.data.user.id;
                	
                    this.validateChk('type');
                	this.validateChk('category');
                	this.validateChk('title');
                	this.validateChk('member');
                	this.validateChk('openType');
                	if(this.data.classRoom.type === 'R'){
                		this.validateChk('date');
                	}
                	
                    //유효성 체크
                	if(this.flag.categoryFlag && this.flag.dateFlag && this.flag.memberFlag 
                			&& this.flag.openTypeFlag && this.flag.titleFlag && this.flag.typeFlag){
                    	
                    	// 썸네일 있으면 파일 저장
                        if(this.$refs.thumbnail.files[0] != undefined || this.$refs.thumbnail.files[0] != null){
                            fd.append("thumbnail", this.$refs.thumbnail.files[0]);
                        }
                        // 강의 유형이 실시간이면 예상시간 저장
                    	if(this.data.classRoom.type === 'R'){
                    		fd.append("classTime.date", this.data.classTime.date);
                    		fd.append("classTime.startTime", this.data.classTime.startTime);
                    		fd.append("classTime.endTime", this.data.classTime.endTime);
                    	}
                    	
                        classRoom = _.cloneDeep(this.data.classRoom);
                        
                        for(var key in classRoom) {
                            fd.append(key, classRoom[key]);
                        }
                        //강의실 id가 존재하면 수정
                        if(this.data.classRoom.id){
                        	fd.append("id", this.data.classRoom.id);

                        	if(await meta.confirm("강의실을 수정하시겠습니까?")){
                        		// 강의 수정
                        		(await meta.api.common.classRoom.modifyClassRoom(fd)).data;
                        		await meta.alert("'" + this.data.classRoom.title + "' 강의실 수정이 완료되었습니다.")
                        		this.$router.replace("/dashboard/lecture/list");
                        	}
                        } else{
                        	if(await meta.confirm("강의실을 생성하시겠습니까?")){
                        		// 강의 생성
                        		(await meta.api.common.classRoom.createClassRoom(fd)).data;
                        		await meta.alert("'" + this.data.classRoom.title + "' 강의실 생성이 완료되었습니다.")
                        		this.$router.replace("/dashboard/lecture/list");
                        	}
                        }
                		
                	} else{
                		meta.alert("모든 정보를 기입해주세요.");
                	}
                }
        },
        "mounted": async function () {
        	let user = _.cloneDeep(store.state.app.user),
        		classroomId = this.$route.query.id;
        	
        	this.$set(this.data.user, "id", user.id);
        	
        	if(classroomId){
        		this.loadClassRoom(classroomId);
        	}
        	this.loadCategoryList()
        },
        "created": async function() {
        }
    });
});