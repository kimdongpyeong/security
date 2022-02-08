var SettingLecturerMainPage;
SettingLecturerMainPage = Vue.component("setting-lecturer-main-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/lecturer/main.html")).data,
        "data": function() {
            return {
                "data": {
                    "userList": [],
                },
                "select": {
                    "users": [],
                },
                "visible": {
                    "dialogUserVisible": false,
                },
                "headers": [
                    { text: 'ID', value: 'id', width: '5%', align: 'center' },
                    {
                        text: '이름',
                        align: 'center',
                        sortable: false,
                        value: 'name',
                        width: '20%',
                    },
                    { text: '상태', value: 'status', width: '5%', align: 'center' },
                    { text: '가입방법', value: 'signUpWayNm', width: '10%', align: 'center' },
                    { text: '승인여부', value: 'lecturerApprovalNm', width: '15%', align: 'center' },
                    { text: '메일 주소', value: 'username' },
                ],
            }
        },
        "watch": {
        },
        "methods": {
            "loadUserList": async function() {
                var userList = [];

                userList = (await meta.api.common.user.getUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "lecturerApprovalYn": "N",
                })).data.items;

                userList.forEach(e => {
                    //                    if(e.lecturerApprovalYn == "N")
                    this.$set(e, "lecturerApprovalNm", "미승인");
                    if (e.signUpWay == "G") {
                        this.$set(e, "signUpWayNm", "구글");
                    } else if (e.signUpWay == "K") {
                        this.$set(e, "signUpWayNm", "카카오");
                    } else {
                        this.$set(e, "signUpWayNm", "일반");
                    }
                })
                this.data.userList = userList;

                console.log(this.data.userList);
            },
            "save": async function(value) {
                let i = 0;

                let formData = new FormData();
                console.log(this.select.users);

                if (value == "Y") {
                    formData.append("lecturerApprovalYn", "Y");
                } else if (value == "R") {
                    formData.append("lecturerApprovalYn", "R");
                }
                
                for (i = 0; i < this.select.users.length; i++) {
                    formData.append("userList[" + i + "].id", this.select.users[i].id);
                    formData.append("userList[" + i + "].username", this.select.users[i].username);
                }

                (await meta.api.common.user.modifyUserList(formData)).data;
                await meta.alert("완료되었습니다.")
                this.$router.go();
            },
        },
        "mounted": async function() {
            await this.loadUserList();
        },
        "created": async function() {
        },
    });
});