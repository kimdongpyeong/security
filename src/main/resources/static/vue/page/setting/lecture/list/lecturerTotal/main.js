var SettingLecturerTotalMainPage;
SettingLecturerTotalMainPage = Vue.component("setting-lecturerTotal-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/lecture/list/lecturerTotal/main.html")).data,
    "data": function () {
        return {
            "data": {
                "userList": [],
                "user": {},
                "role": {
                    "id": null
                },
                "query": {
                    "name": null
                },
                "parents": {
                    parentsList: [],
                    parentsInfo: [],
                },
            },
            "flag": {
                parentsDetailPopup: false,
            },
        };
    },
    "watch": {
        "data.query.name": function() {
            this.loadUserList();
        }
    },
    "methods": {
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
        
        "loadUserList": async function () {
            userList = (await meta.api.common.user.getUserList({
                "roleId": 2,
                "page": 1,
                "rowSize": 100000000,
                "status": "T",
                "name": this.data.query.name,
                "sort": ["name,asc", "created_date,desc"]
            })).data.items;
            this.data.userList.forEach(item => {
                item.createdDate = moment(item.createdDate).format("YYYY-MM-DD");
            });
            this.data.userList = userList;
            console.log(userList)
        },
        
        "clickParentsList": async function(id) {
            parentsList = (await meta.api.common.parents.getParentsList({
                "createdBy": id,
                "page": 1,
                "rowSize": 10000,
            })).data.items;
            this.data.parents.parentsList = parentsList;
            console.log(this.data.parents.parentsList)
        },
        "parentsPopup": async function(id) {
            this.flag.parentsDetailPopup = true;
            this.data.parents.parentsInfo = (await meta.api.common.parents.getParents(id)).data;
            console.log(this.data.parents.ParentsInfo)
        },
        
    },
    "mounted": async function () {
        this.loadUserList();
    }
}); });