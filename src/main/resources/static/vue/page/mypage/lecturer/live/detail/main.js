var MypageLecturerLiveDetailPage;
MypageLecturerLiveDetailPage = Vue.component("mypage-lecturer-live-detail-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/lecturer/live/detail/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "classroomId": null,
                    "clientId": "mA4vpr2MRgiY30D40wOCsg", // 로컬용
//                    "clientId": "QX3YrlnXQFqDsZC725O_Yg", // 운영용
                    "redirectUri": "http://localhost:9090",
//                    "redirectUri": "http://meta-soft.iptime.org:9090",
                    "timeList": null,
                    "username": null,
                    "password": null,
                },
                "script": [
                    "https://source.zoom.us/2.1.1/lib/vendor/react.min.js",
                    "https://source.zoom.us/2.1.1/lib/vendor/react-dom.min.js",
                    "https://source.zoom.us/2.1.1/lib/vendor/redux.min.js",
                    "https://source.zoom.us/2.1.1/lib/vendor/redux-thunk.min.js",
                    "https://source.zoom.us/2.1.1/lib/vendor/lodash.min.js",
                    "https://source.zoom.us/zoom-meeting-2.1.1.min.js"
                ],
                "flag": {
                    "userShow": false
                }
            };
        },
        "watch": {
        },
        "methods": {
            "getClassroomTimeList": async function(classroomId) {
                let params = {
                    "classroomId": classroomId
                }

                let data = (await axios({ "url": "/api/common/liveTime", "method": "get", params: params})).data.items;

                this.data.timeList = data;
                console.log(data);
            },
            "goLiveMeeting": async function(id) {

                let data = (await axios({ "url": "/api/common/liveTime/"+id, "method": "get"})).data,
                    zoomLink = data.zoomLink;

                if(zoomLink == null || zoomLink == "") {
                    window.open("https://zoom.us/oauth/authorize?response_type=code&client_id=" + this.data.clientId + "&redirect_uri="+this.data.redirectUri+"/api/common/liveTime/createZoomLink?liveTimeId="+ id);
                } else {
                    window.open(zoomLink);
                }
            },
            "goZoom": function() {
                var leaveUrl = 'http://localhost:9090/main';

                ZoomMtg.preLoadWasm();
                ZoomMtg.prepareJssdk();

                ZoomMtg.init({
                  leaveUrl: leaveUrl,
                  success: (success) => {
                    document.getElementById("zmmtg-root").style.display = "block";
                    document.getElementById("blogLinkBtn").style.display = "none";
                    ZoomMtg.join({
                        signature: "akdrbnloaDlUdUN5SmpGSXRiSGFudy44NTQ2MzkyMDAzNy4xNjQxNTQxNDcxODg2LjAuMDQ3V3c0TTVVb1k1UGNJdml2bW5LTjlidnFGTnQxSTFNTkFjMzZxa1Frdz0", // role in signature needs to be 0
                        apiKey: "jGknyhh9TuCyJjFItbHanw",
                        meetingNumber: "85463920037",
                        userName: "학생1",
                        success: (success) => {
                            console.log(success)
                        },
                        error: (error) => {
                            console.log(error)
                        }
                    })
                  },
                  error: (error) => {
                    console.log(error)
                  }
                })
            }
        },
        "mounted": async function () {
            let classroomId = this.$route.query.classroomId;

            if(classroomId != null && classroomId != "") {
                this.data.classroomId = classroomId;
            } else {
                await meta.alert("잘못된 접근입니다.");
                this.$router.push("/main");
            }

            await this.getClassroomTimeList(classroomId);
        },
        "created": function() {
        }
    });
});