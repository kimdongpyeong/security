var Test3LecturerDataPage;
Test3LecturerDataPage = Vue.component("test3-lecturer-data-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/lecturer/main.html")).data,
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "data": null,
                    "dataList": [],
                    "dataFileList": [],
                },
            };
        },
        "watch": {
        },
        "methods": {
            "loadData": async function(){
                let data = (await meta.api.common.lecturerData.getLecturerData(10)).data;
                console.log(data);
            },
            "loadDataList": async function(){
                let dataList = (await meta.api.common.lecturerData.getLecturerDataList({
                    "page": 1,
                    "rowSize": 100000000,
                    "lecturerId": 14,
                })).data.items;
                this.data.dataList = dataList;
                await this.loadDataFileList();
            },
            "loadDataFileList": async function(){
                var i;
                
                for(i=0; i<this.data.dataList.length; i++){
                    let dataFileList = (await meta.api.common.lecturerData.getLecturerDataFileList({
                        "page": 1,
                        "rowSize": 100000000,
                        "lecturerDataId": this.data.dataList[i].id,
                    })).data.items;

                    this.$set(this.data.dataList[i], "dataFileList", dataFileList);
                }
                console.log(this.data.dataList);
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
            
//            await this.loadData();
            await this.loadDataList();
        },
        "created": async function() {
        }
    });
});