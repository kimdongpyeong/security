var SettingRoleMainPage;
SettingRoleMainPage = Vue.component("setting-role-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/role/main.html")).data,
        "data": function () {
            return {
                "role": {
                    "panels": {
                        "list": [0]
                    },
                    "selected" : [],
                    "dataTable": {
                        "headers": [
                            {"text": "권한명", "sortable": false, "value": "name"},
                            {"text": "연결된 API 갯수", "sortable": false, "value": "connectedApiCnt"},
                            {"text": "연결된  메뉴 갯수", "sortable": false, "value": "connectedMenuCnt"},
                            {"text": "설명", "sortable": false, "value": "description"},
                            {"text": "생성한 날짜", "sortable": false, "value": "createdDate"},
                            {"text": "수정한 시간", "sortable": false, "value": "lastModifiedDate"},
                        ],
                        "items": [],
                        "loading": false,
                        "serverItemsLength": 0,
                        "page": 1,
                        "itemsPerPage": 10
                    },
                    "pagination": {
                        "length": 10,
                        "totalVisible": 10
                    },
                }
            };
        },

        "watch": {
            "role.dataTable.page": {
                "handler": function (newValue, oldValue) {
                    this.setRoleList();
                },
                "deep": true
            }
        },
        "methods": {
            "handleClick" : function(value) {
                this.$router.push({
                    "path": "/settings/roles/detail",
                    "query": {
                        "id": value.id
                    }
                });
            },
            "setRoleList": async function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            var params = {
                                    "page": self.role.dataTable.page,
                                    "rowSize": self.role.dataTable.itemsPerPage
                                };
                            self.role.dataTable.loading = true;
                            return meta.api.common.role.getRoleList(params);
                        })
                        .then(function (response) {

                            var data = response.data;
                            self.role.dataTable.items = data.items;
                            self.role.dataTable.serverItemsLength = data.totalRows;
                            self.role.dataTable.loading = false;
                        })
                        .then(function () { resolve(); });
                });
            },
            "deleteRoleList" : async function() {
                var self = this;

                if (await meta.confirm("삭제 하시겠습니까?")) {
                    var selectedRole = self.role.selected;


                    var idList = [];
                    for(var i = 0; i< selectedRole.length; i++)
                    {
                        if(selectedRole[i].id != 1 && selectedRole[i].id != 2 && selectedRole[i].id != 3)
                            idList.push(selectedRole[i].id);
                        else
                            alert('기초 권한(최고 관리자, 멘토, 멘티)은 삭제할수 없습니다.');
                    }

                    (await meta.api.common.role.removeRoleAllDependencyList(idList));


                    await meta.alert('삭제에 성공했습니다.');
                    this.setRoleList();
                }

            },

        },

        "mounted": function () {
            Promise.resolve()
                .then(this.setRoleList);
        },

    });
});