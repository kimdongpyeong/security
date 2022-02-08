var MypageLecturerLiveEditPage;
MypageLecturerLiveEditPage = Vue.component("mypage-lecturer-live-edit-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/lecturer/live/edit/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "clientId": "mA4vpr2MRgiY30D40wOCsg", // 로컬용
//                    "clientId": "QX3YrlnXQFqDsZC725O_Yg", // 운영용
                    "redirectUri": "http://localhost:9090",
//                    "redirectUri": "http://meta-soft.iptime.org:9090",
                    "newLiveInfo": {
                        "title": null,
                        "endHour": null,
                        "endMin": null,
                        "zoomLink": null,
                    },
                    "newLiveTimeList": [],
                    "thumbnailSrc": "/resources/img/base.jpg",
                    "temp": null,
                },
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
            // 라이브 생성
            "createLiveMeeting": async function() {
                let self = this,
                    i = 0;

                let formData = new FormData();

                formData.append("classroomId", 20);
                formData.append("title", self.data.newLiveInfo.title);
                for (i = 0; i < self.data.newLiveTimeList.length; i++) {
                    formData.append("liveTime[" + i + "].startTime", self.data.newLiveTimeList[i].startTime);
                    formData.append("liveTime[" + i + "].startDate", self.data.newLiveTimeList[i].startDate);
                }
                formData.append("endHour", self.data.newLiveInfo.endHour);
                formData.append("endMin", self.data.newLiveInfo.endMin);

                if (this.$refs.thumbnail.files[0] != undefined || this.$refs.thumbnail.files[0] != null)
                    formData.append("thumbnail", this.$refs.thumbnail.files[0]);

                let data = (await meta.api.common.classRoomLive.createClassRoomLive(formData)).data;
                await meta.alert("끗~~@!!!@##$!!!")
                self.data.zoomLink = data.zoomLink;


            },
            // LiveTime 테이블 추가
            "setLiveTimeList": function() {
                this.data.newLiveTimeList.push({
                    startDate: null,
                    startTime: null,
                })
            },
            // LiveTime 테이블 삭제
            "deleteLiveTimeListRow": function(id, idx) {
                this.data.newLiveTimeList.splice(idx, 1);

                if(this.data.newLiveTimeList.length == 0){
                    this.data.newLiveTimeList.push({
                        startDate: null,
                        startTime: null,
                    });
                }
            },
            // 파일 선택(파일을 변경할때)
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
                    self.data.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);
            },
            // 파일 초기화
            "fileClear": function() {
                this.data.thumbnailSrc = "/resources/img/base.png";
                this.$refs.thumbnail.value = null;
            },
//            "cancel": function(){
//                Object.assign((this.$data).select, this.$options.data().select);
//                this.data.thumbnailSrc = "/resources/img/profile.png";
//                this.$refs.thumbnailSrc.value = null;
//            },
        },
        "mounted": async function () {
            this.setLiveTimeList();
        },
        "created": async function() {
        }
    });
});