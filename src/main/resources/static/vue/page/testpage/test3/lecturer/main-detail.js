var Test3LecturerDataMainDetail;
Test3LecturerDataMainDetail = Vue.component("test3-lecturer-data-main-detail", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/lecturer/main-detail.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "dataList": [{
                        "title": "",
                        "subtitle": "",
                        "files": [{
                            file: null
                        }]
                    }],
                    "delData": [],
                    "delFile": [],
                },
                "tempFile": null,
            };
        },
        "watch": {
        },
        "methods": {
            // 자료실 데이터 불러오기
            "loadDataList": async function(){
                let dataList = (await meta.api.common.lecturerData.getLecturerDataList({
                    "page": 1,
                    "rowSize": 100000000,
                    "lecturerId": 14,
                })).data.items;
                this.data.dataList = dataList;
                await this.loadDataFileList();
            },
            // 자료실 데이터 파일들 불러오기
            "loadDataFileList": async function(){
                var i;
                
                for(i=0; i<this.data.dataList.length; i++){
                    let files = (await meta.api.common.lecturerData.getLecturerDataFileList({
                        "page": 1,
                        "rowSize": 100000000,
                        "lecturerDataId": this.data.dataList[i].id,
                    })).data.items;

                    this.$set(this.data.dataList[i], "files", files);
                }
            },
            // 자료실 전체(data테이블) 생성 및 수정
            "save": async function() {
                let self = this,
                    i = 0, j = 0;

                let formData = new FormData();
                console.log(formData)
                
//                formData.append("lecturerId", self.data.user.id);
                formData.append("lecturerId", 14);
                for (i = 0; i < self.data.dataList.length; i++) {
                    if(self.data.dataList[i].id !== null && self.data.dataList[i].id !== undefined){
                        formData.append("params[" + i + "].id", self.data.dataList[i].id);
                    }
                    formData.append("params[" + i + "].title", self.data.dataList[i].title);
                    formData.append("params[" + i + "].subtitle", self.data.dataList[i].subtitle);
                    
                    for(j = 0; j < self.data.dataList[i].files.length; j++) {
                        var inputFile = document.getElementById("inputFile" + i + j).files[0];
                        
                        if(inputFile != undefined || inputFile != null){
                            formData.append("params[" + i + "].files[" + j +"]", inputFile);
                        }
                    }
                }

//                let data = (await meta.api.common.lecturerData.sortLecturerData(formData)).data;
                if(self.data.delData.length !== 0 || self.data.delFile.length !== 0){
                    console.log(self.data.delData);
                    console.log(self.data.delFile);
                }

                await meta.alert("(╯°□°）╯︵ ┻━┻")
            },
            // data테이블 추가
            "addDataList": function() {
                this.data.dataList.push({
                    "title": "",
                    "subtitle": "",
                    "files": [
                        {file: null}
                    ]
                });
            },
            // data테이블 삭제
            "deleteDataList": function(id, idx) {
                this.data.delData.push({"id": id });
                
                this.data.dataList.splice(idx, 1);
    
                if(this.data.dataList.length == 0){
                    this.data.dataList.push({
                        "title": "",
                        "subtitle": "",
                        "files": [
                            {file: null}
                        ]
                    });
                } 
            },
            // 파일 갯수 (dataFile테이블) 추가
            "addDataFileList": function(idx) {
                this.data.delFile.push({"id": id });
                
                this.data.dataList[idx].files.push({file: null})
            },
            // 파일 갯수 (dataFile테이블)삭제
            "deleteDataFileList": function(id, idx, idx2) {
                this.data.delFile.push({"id": id });
                
                this.data.dataList[idx].files.splice(idx2, 1);

                if(this.data.dataList[idx].files.length == 0){
                    this.data.dataList[idx].files.push({
                        file: null,
                    });
                } 
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
            await this.loadDataList();
        },
        "created": async function() {
        }
    });
});