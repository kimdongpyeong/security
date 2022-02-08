var SettingCategoryLiveMainPage;
SettingCategoryLiveMainPage = Vue.component("setting-category-live-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/category-live/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "liveList":[],
                    "linkList":[],
                    "betaLive":{},
                    "live":{},
                    "thumbnailSrc": "/resources/img/test.png"
                },
                "select":{
                    "category":[]
                },
                "rules": {
                    "required": value => !!value || '필수항목입니다.',
                }
            };
        },
        "methods": {
            "loadLiveList": async function(){
                this.data.liveList = (await meta.api.common.liveCat.getLiveCatList({
                    "page": 1,
                    "rowSize": 100000000,
                    "sort": ["category_id,asc","publish_yn,asc","!order_no,desc","created_date,asc"]
                })).data.items;
            },
            "loadCategoryList": async function(){
                let cat = (await meta.api.common.category.getCategoryList({
                    "page": 1,
                    "rowSize": 100000000,
                })).data.items;

                cat.forEach(e=> {
                    this.select.category.push({"text": e.title, "value": e.id});
                });
            },
            "clickLiveCat": async function (id) {
                this.data.live = (await meta.api.common.liveCat.getLiveCat(id)).data;
                if(this.data.live.saveFileNm != null) {
                    this.data.thumbnailSrc = "/api/app/images?subpath=liveCat&filename=" + this.data.live.saveFileNm;
                } else {
                    this.data.thumbnailSrc = "/resources/img/test.png";
                }
            },
            "fileClear": function() {
                this.data.live.thumbnail = null;
                this.data.thumbnailSrc = "/resources/img/test.png";
            },
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0) {
                    self.data.live.fileFlag = "remove";
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    self.data.live.fileFlag = "remove";
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {
                    self.data.thumbnailSrc = e.target.result;
                }

                reader.readAsDataURL(file);

                self.data.live.fileFlag = "change";
            },
            "saveLive": async function(){
                let self = this,
                    validate,
                    fd = new FormData();

                validate = this.$refs.form.validate()

                if(!validate){
                    await meta.alert("필수항목을 입력해주세요.");
                    return;
                } else if(self.data.live.categoryId === undefined || self.data.live.categoryId === null){
                    await meta.alert("카테고리를 선택해주세요.");
                    return;
                }  else if(self.data.live.publishYn === null || self.data.live.publishYn === undefined){
                    await meta.alert("게시여부를 선택해주세요.");
                    return;
                } else if(!self.data.live.id && (this.$refs.thumbnail.files[0] === undefined || this.$refs.thumbnail.files[0] === null)){
                    await meta.alert("썸네일을 등록해주세요.");
                    return;
                } else{
                    if(self.data.live.id !== null && self.data.live.id !== undefined){
                        fd.append("id", self.data.live.id);
                    }
                    if(self.data.live.orderNo !== null && self.data.live.orderNo !== undefined){
                        fd.append("orderNo", self.data.live.orderNo);
                    }
                    fd.append("categoryId", self.data.live.categoryId);
                    fd.append("title", self.data.live.title);
                    fd.append("creatorNm", self.data.live.creatorNm);
                    fd.append("instaLink", self.data.live.instaLink);
                    fd.append("notionLink", self.data.live.notionLink);
                    fd.append("publishYn", self.data.live.publishYn);

                    if(this.$refs.thumbnail.files[0] != undefined && this.$refs.thumbnail.files[0] != null){
                        fd.append("thumbnail", this.$refs.thumbnail.files[0]);
                    } else{
                        fd.append("originFileNm", self.data.live.originFileNm);
                        fd.append("saveFileNm", self.data.live.saveFileNm);
                        fd.append("fileSize", self.data.live.fileSize);
                    }
                    if (await meta.confirm("저장 하시겠습니까?")) {
                        if (self.data.live.id) {
                            (await meta.api.common.liveCat.modifyLiveCat(fd)).data;
                        } else {
                            (await meta.api.common.liveCat.createLiveCat(fd)).data;
                        }
                        await meta.alert("저장 되었습니다.");
                        await this.loadLiveList();
                        this.data.live = {};
                        this.data.thumbnailSrc = "/resources/img/test.png";
                    }
                }
            },
            "removeLive": async function(){
                let id = this.data.live.id;

                if (await meta.confirm("삭제 하시겠습니까?")) {
                    await meta.api.common.liveCat.removeLiveCat(id);
                    await meta.alert("삭제 되었습니다.");

                    await this.loadLiveList();
                    this.data.live = {};
                    this.data.thumbnailSrc = "/resources/img/test.png";
                }
            }
        },
        "mounted": async function () {
            this.loadLiveList();
            this.loadCategoryList();
        }
    });
});