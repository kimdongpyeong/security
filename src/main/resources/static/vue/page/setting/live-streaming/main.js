var SettingLiveStreamingMainPage;
SettingLiveStreamingMainPage = Vue.component("setting-live-streaming-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/live-streaming/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "liveStreamingList":[],
                    "liveStreaming":{}
                },
                "rules": {
                    "required": value => !!value || '필수항목입니다.',
                }
            };
        },
        "methods": {
            "loadLiveStreamingList": async function(){
				this.data.liveStreamingList = (await meta.api.common.liveStreaming.getLiveStreamingList({
					"page": 1,
					"rowSize": 100000000,
				})).data.items;
			},
            "clickLiveStreaming": async function (id) {
                this.data.liveStreaming = (await meta.api.common.liveStreaming.getLiveStreaming(id)).data;
            },
            "saveLiveStreaming": async function(){
                let self = this,
                    validate,
                    formData = new FormData();

                    validate = this.$refs.form.validate()

                if(!validate){
                    await meta.alert("필수항목을 입력해주세요.");
                    return;
                } else if(self.data.liveStreaming.exposureYn === null || self.data.liveStreaming.exposureYn === undefined){
                    await meta.alert("필수항목을 입력해주세요.");
                    return;
                } else{
                    if(self.data.liveStreaming.id !== null && self.data.liveStreaming.id !== undefined){
                        formData.append("id", self.data.liveStreaming.id);
                    }
                    formData.append("liveTitle", this.data.liveStreaming.liveTitle);
                    formData.append("liveDescription", this.data.liveStreaming.liveDescription);
                    formData.append("youtubeLink", this.data.liveStreaming.youtubeLink);
                    formData.append("liveYn", this.data.liveStreaming.liveYn);
                    formData.append("exposureYn", this.data.liveStreaming.exposureYn);

                    if (await meta.confirm("저장 하시겠습니까?")) {
                        if (self.data.liveStreaming.id) {
                            banner = (await meta.api.common.liveStreaming.modifyLiveStreaming(formData)).data;
                        } else {
                            banner = (await meta.api.common.liveStreaming.createLiveStreaming(formData)).data;
                        }
                        await meta.alert("저장 되었습니다.");
                        await this.loadLiveStreamingList();
                        this.data.liveStreaming = {};
                    }
                }
            },
            "deleteLiveStreaming": async function(){
                if (await meta.confirm("삭제 하시겠습니까?")) {
                    await meta.api.common.liveStreaming.removeLiveStreaming(this.data.liveStreaming.id);
                    await meta.alert("삭제 되었습니다.");
                    await this.loadLiveStreamingList();
                    this.data.liveStreaming = {};
                }
			},
        },
        "mounted": async function () {
            this.loadLiveStreamingList();
        }
    });
});