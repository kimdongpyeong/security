var ParentsTest;
ParentsTest = Vue.component("test5-parentsTest-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test5/parentsTest/main.html")).data,
        "data": function() {
            return {
                data: {
                    lecturer:{
                        id: null,
                    },
                    parents:{
                        name: null,
                        phoneNum: null,
                        studentName: null,
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
                        "createdBy": this.data.lecturer.id
                    });

                    await meta.alert("등록 완료 되었습니다.");
                }
            }
        },
        "created": function () {
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            this.data.lecturer.id = user.id;
        }
    });
});