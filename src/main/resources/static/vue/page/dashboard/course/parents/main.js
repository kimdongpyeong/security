var ParentsList;
ParentsList = Vue.component("dashboard-parents-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/course/parents/main.html")).data,
        "data": function() {
            return {
                data: {
                    search: null,
                    lecturer:{
                        id: null,
                    },
                    parents:{
                        name: null,
                        phoneNum: null,
                        studentName: null,
                        lectureType: null,
                        lectureName: null,
                        parentsList:[],
                    }
                },
                "visible": {
                    "en": false,
                },
            };
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            async save(){
                if(await meta.confirm("학부모를 등록 하시겠습니까?")) {
                    let parentsSave= await meta.api.common.parents.createParents({
                        "name": this.data.parents.name,
                        "phoneNum": this.data.parents.phoneNum,
                        "studentName": this.data.parents.studentName,
                        "lectureType": this.data.parents.lectureType,
                        "lectureName": this.data.parents.lectureName,
                        "createdBy": this.data.lecturer.id
                    });

                    await meta.alert("등록 완료 되었습니다.");
                }
            },
            
            async parentsList(){
                parentsList = (await meta.api.common.parents.getParentsList({
                    "name": this.data.search,
                    "studentName": this.data.search,
                    "page": 1,
                    "rowSize": 10000,
                })).data.items;
                this.data.parents.parentsList = parentsList;
                this.data.parents.parentsList.lectureType = this.data.parents.parentsList.lectureType.replace('O','온라인').replace('F','오프라인');
                console.log(parentsList)
            }
        },
        "created": function () {
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            this.data.lecturer.id = user.id;
            
            this.parentsList();
        }
    });
});