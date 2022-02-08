var MainPage;
MainPage = Vue.component("main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/main/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "demoList":[],
                    "categoryList": [],
                    "numberList": ["1", "2", "3", "4", "5", "6", "7", "8", "9", "10"],
                    "classRoom": {
                        "thumbnailSrc": "/resources/img/base.jpg",
                        "title": "",
                        "memberNum": "",
                        "etc": "",
                        "categoryId": ""
                    }
                },
                "visible": {
                    "lectureDialog": false
                },
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
            "data.classRoom.categoryId": function(val) {
                if(val == "23") {
                    this.data.classRoom.etc = "";
                }
            }
        },
        "methods": {
            //데모영상 불러오기
            "loadDemoVideo": async function(){
                let demoList = (await meta.api.common.demo.getDemoList({
                    "page": 1,
                    "rowSize": 100000000,
                    "exposureYn":"Y"
                })).data.items;

                this.data.demoList = _.cloneDeep(demoList);

                if(this.data.demoList.length > 0){
                    this.data.demoVideo = this.data.demoList[0];
                }
            },
            "noDemoVideo": function(){
                meta.alert("데모영상 준비 중입니다.");
            },
            "demoVideo": async function() {
                await meta.alert("데모영상 준비중입니다.");
                return;
            },
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0 || file == undefined) {
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {      // 여기서 e 는 ProgressEvent, event값인거 같은데 값이 어떻게 적용이 되는지? onload가 자동적으로 들어가나?
                    self.data.classRoom.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);
            },
            // 파일 초기화
            "fileClear": function() {
                this.$refs.thumbnail.value = null;
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
            "openLectureDialog": async function() {
                this.visible.lectureDialog = true;
                await this.loadCategoryList();
                await this.lectureDialogClear();
            },
            "lectureDialogClear": async function() {
                this.data.classRoom = {
                    "thumbnailSrc": "/resources/img/base.jpg",
                    "title": "",
                    "memberNum": "",
                    "etc": "",
                    "categoryId": ""
                };
                this.fileClear();
            },
            "createClassRoom": async function() {
                var self = this;

                let title = self.data.classRoom.title;
                if(title === undefined || title === null || title === ""){
                    await meta.alert("강의실 이름을 입력해주세요");
                    return;
                }

                let categoryId = self.data.classRoom.categoryId;
                if(categoryId === undefined || categoryId === null || categoryId === ""){
                    await meta.alert("유형을 선택해주세요.");
                    return;
                }

                let num = self.data.classRoom.memberNum;
                if(num === undefined || num === null){
                    await meta.alert("수강인원을 선택해주세요.");
                    return;
                }

                if(await meta.confirm("'"+this.data.classRoom.title+"' 강의실을 생성하시겠습니까?")) {
                    let fd = new FormData();

                    fd.append("title", this.data.classRoom.title);
                    fd.append("categoryId", this.data.classRoom.categoryId);
                    fd.append("etc", this.data.classRoom.etc);
                    fd.append("memberNum", this.data.classRoom.memberNum);
                    fd.append("deleteYn", 'N');
                    fd.append("openType", "O");
                    fd.append("type", "R");
                    fd.append("createdBy", this.data.user.id);

                    fd.append("endHour", moment().add(1, "H").format("HH"));
                    fd.append("endMin", moment().format("mm"));

                    if(this.$refs.thumbnail.files[0] != undefined || this.$refs.thumbnail.files[0] != null){
                        fd.append("thumbnail", this.$refs.thumbnail.files[0]);
                    }

                    fd.append("liveTime[0].startDate", moment().format("YYYYMMDD"));
                    fd.append("liveTime[0].startTime", moment().format("HH:mm"));

                    // 강의실 저장
                    (await meta.api.common.classRoom.createClassRoom(fd)).data;
                    await meta.alert("'"+this.data.classRoom.title+"' 강의실이 생성되었습니다.");
                    this.$store.commit("app/SET_LOADING", true);
                    this.$router.go();
                }
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
                        let subject = self.data.classRoom.subject;
                        if(title === undefined || title === null || title === ""){
                            self.flag.subjectText = "과목을 선택해주세요.";
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
        },
        "mounted": async function () {
        },
        "created": function () {
            if(store.state.app.user != null) {
                let user = _.cloneDeep(store.state.app.user);
                this.$set(this.data.user, "id", user.id);

//                if(store.state.app.user.roleId != 3) {
//                    location.href = "/dashboard";
//                }
            }
        },
    });
});