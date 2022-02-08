var SettingDemoMainPage;
SettingDemoMainPage = Vue.component("setting-demo-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/demo/main.html")).data,
        "data": function () {
            return {
                "data": {
                	"demoList":[],
                	"demo":{},
                    "liveStreamingList":[],
                    "liveStreaming":{}
                },
                "rules": {
                    "required": value => !!value || '필수항목입니다.',
                }
            };
        },
        "methods": {
        	"loadDemoList": async function(){
        		let demoList = (await meta.api.common.demo.getDemoList({
        			"page": 1,
        			"rowSize": 100000000
        		})).data.items;
        		
        		this.data.demoList = demoList;
        	},
            "clickDemo": async function (id) {
                let demo = (await meta.api.common.demo.getDemo(id)).data;
                this.data.demo = demo;
            },
            "saveDemo": async function(){
                let validate, self = this,
                    fd = new FormData();

                    validate = this.$refs.form.validate();

                if(!validate){
                    await meta.alert("필수항목을 입력해주세요.");
                    return;
                } else if(!self.data.demo.exposureYn){
                    await meta.alert("필수항목을 입력해주세요.");
                    return;
                } else{
                    if(self.data.demo.id){
                        fd.append("id", self.data.demo.id);
                    }
                    fd.append("title", this.data.demo.title);
                    fd.append("youtubeLink", this.data.demo.youtubeLink);
                    fd.append("exposureYn", this.data.demo.exposureYn);

                    if (await meta.confirm("저장 하시겠습니까?")) {
                        if (self.data.demo.id) {
                        	(await meta.api.common.demo.modifyDemo(fd)).data;
                        } else {
                            (await meta.api.common.demo.createDemo(fd)).data;
                        }
                        await meta.alert("저장 되었습니다.");
                        await this.loadDemoList();
                        this.data.demo = {};
                    }
                }
            },
            "deleteDemo": async function(){
                if (await meta.confirm("삭제 하시겠습니까?")) {
                    await meta.api.common.demo.removeDemo(this.data.demo.id);
                    await meta.alert("삭제 되었습니다.");
                    await this.loadDemoList();
                    this.data.demo = {};
                }
			},
        },
        "mounted": async function () {
            this.loadDemoList();
        }
    });
});