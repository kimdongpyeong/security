var SettingUserMainPage;
SettingUserMainPage = Vue.component("setting-user-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/user/main.html")).data,
    "data": function () {
        return {
            "data": {
                "userList": [],
                "user": {},
                "role": {
                    "id": null
                },
                "query": {
                    "roleId": "",
                    "status": "",
                    "name": null
                }

            },
            "role" : {
                "roleSelected" : '',
                "dataTable" : {
                    "headers": [
                        {"text": "역할 명", "sortable": false, "value": "name"},
                        {"text": "역할 설명", "sortable": false, "value": "description"},
                    ],
                    "items": [],
                }
            },
            "text": {
                "icon": "mdi-eye-off-outline",
                "type": "password"
            },
            "btn": {
                "saveAccount": {
                    "loading": false
                }
            },
            "statusNameList": {
                "T": "사용",
                "D": "삭제",
                "S": "정지"
            },
            "teacher": {
                "flag": false,
                "refuseFlag": false,
            },
            "select": {
                "gender": {
                    "items": [
                        {"text": "남자", "value": 'M'},
                        {"text": "여자", "value": 'W'}
                    ]
                },
                "signUpWay": {
                    "items": [
                        {"text": "일반", "value": 'N'},
                        {"text": "구글", "value": 'G'},
                        {"text": "카카오", "value": 'K'}
                    ]
                },
                "roles": {
                    "items": [{"text": "전체", "value": ""}]
                },
                "status": {
                    "items": [
                        {"text": "전체", "value": ""},
                        {"text": "사용", "value": "T"},
                        {"text": "정지", "value": "S"},
                        {"text": "삭제", "value": "D"},
                    ]
                },
                "residence":{
                    "items":[]
                },
                "residenceName" : []
            },
            "rules": {
                "required": value => !!value || '필수',
            }
        };
    },
    "watch": {
        "role.roleSelected": function(val) {
            this.teacher.flag = (val == 2)? true : false;
        },
        "data.query.roleId": function() {
            this.loadUserList();
        },
        "data.query.status": function() {
            this.loadUserList();
        },
        "data.query.name": function() {
            this.loadUserList();
        }
    },
    "methods": {
        "showRefuseReason": function() {
            this.teacher.refuseFlag = (this.data.user.teacherCertiYn == 'R')? true : false;
        },
        "loadResidenceList": async function() {
            let list = (await meta.api.common.code.getCodeList({ parentId: 5, rowSize: 100000, sort: ["order_no,asc"] })).data.items;
            list.forEach(e=> {
                this.select.residence.items.push({"text": e.name, "value": e.value});
            });
        },
        "loadUserList": async function () {
            var userList;

            this.data.user = {};

            userList = (await meta.api.common.user.getUserList({
                "page": 1,
                "rowSize": 100000000,
                "roleId": this.data.query.roleId,
                "status": this.data.query.status,
                "name": this.data.query.name,
                "sort": ["role_id,asc", "created_date,desc"]
            })).data.items;

            this.data.userList = userList;

            this.data.userList.forEach(item => {
                item.createdDate = moment(item.createdDate).format("YYYY-MM-DD");
            });
        },
        "loadRoleList": async function () {
            var roleList;
            roleList = (await meta.api.common.role.getRoleList({
                "page": 1,
                "rowSize": 100000000
            })).data.items;

            this.role.dataTable.items = roleList;

            roleList.forEach(e => {
                this.select.roles.items.push({"text": e.name, "value": e.id})
            });
        },
        "clickUser": async function (id) {
            let roleList;

            this.teacher.refuseFlag = false;
            this.data.user = (await meta.api.common.user.getUser(id)).data;

            this.$set(this.data.user, "birthDate", moment(this.data.user.birthDate).format("YYYY-MM-DD"));
            this.teacher.refuseFlag = (this.data.user.teacherCertiYn == 'R')? true : false;

            roleList = (await meta.api.common.roleUser.getRoleUserList({"userId": id})).data.items;
            this.role.roleSelected = roleList[0].roleId;
        },
        "saveAccount": async function () {
            let user = this.data.user;
                validate = this.$refs.form.validate();

            this.btn.saveAccount.loading = true;
            if(user.username.length > 100){
                await meta.alert("유저 아이디는 100자 이하로 작성해주십시오.");
            } else if(user.name.length > 30){
                await meta.alert("이름은 30자 이하로 작성해주십시오.");
            } else if (!validate) {
                await meta.alert("유효한 값을 작성해주세요.");
            } else if(this.role.roleSelected == ''){
                await meta.alert("역할을 입력해주세요.");
            } else if (await meta.confirm("저장 하시겠습니까?")) {
                if(user.id == undefined || user.id == null){
                    await meta.api.app.account.createAccount({"userDto": user, "roleId": this.role.roleSelected});
                } else if(user.id != undefined && user.id != null) {
                    await meta.api.app.account.modifyAccount({"userDto": user, "roleId": this.role.roleSelected});
                }
                await meta.alert("저장 되었습니다.");
                await this.loadUserList();
            }
            this.btn.saveAccount.loading = false;
        },
        "stck": function (str, limit) {
            var o, d, p, n = 0, l = limit == null ? 4 : limit;
            for (var i = 0; i < str.length; i++) {
                var c = str.charCodeAt(i);
                if (i > 0 && (p = o - c) > -2 && p < 2 && (n = p == d ? n + 1 : 0) > l - 3)
                    return false;
                d = p;
                o = c;
            }
            return true;
        },
        "downloadExcel": async function() {
            await (meta.api.common.user.downloadUserList());
        },
    },
    "mounted": async function () {
        Promise.all([
            this.loadUserList(),
            this.loadRoleList(),
            this.loadResidenceList()
        ]);
    }
}); });