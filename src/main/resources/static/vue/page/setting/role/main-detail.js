var SettingRoleDetailMainPage;
SettingRoleDetailMainPage = Vue.component("setting-role-detail-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/role/main-detail.html")).data,
        "data": function () {
            return {
                "role_detail": {
                    "panels": {
                        "list": [0]
                    },
                },

                "detailData" : {
                    "id" : null,
                    "name" : null,
                    "description" : null,
                    "value" : null,
                    "createdDate" : null,
                    "lastModifiedDate" : null,
                },
                "roleMenuList" : [],
                "data": {
                    "treeMenuList": [],
                    "menu": {}
                },
                "treeview": {
                    "menuList": {
                        "items": [],
                        "open": [],
                        "active": []
                    }
                },
                "menuSelected":[],
                "apiList" : [],
                "api" : {
                    "apiSelected" : [],
                    "dataTable" : {
                        "headers": [
                            {"text": "API URL", "sortable": false, "value": "url"},
                            {"text": "API 명", "sortable": false, "value": "name"},
                        ],
                        "items": [],
                    }
                },
            };
        },
        "watch": {
            "data.treeMenuList": {
                "handler": function (newValue, oldValue) {
                    this.treeview.menuList.items = this.flatArrayToNestedArray(newValue, "id", "parentId");
                    this.treeview.menuList.open = _.cloneDeep(newValue);
                }
            },
            "menuSelected" : function(val){
            },
        },
        "methods": {

            "loadTreeMenuList": async function (queryId) {
                var self = this;
                var treeMenuList;
                treeMenuList = (await meta.api.util.menu.getTreeMenuList({
                    "page": 1,
                    "rowSize": 100000000,
                    "sort": ["rankingPath,asc"]
                })).data.items;
                for(treeMenu in treeMenuList){
                    treeMenu.thisAuthHaveThisMenu = false;
                }
                this.data.treeMenuList = treeMenuList;
                if(queryId != undefined && queryId != null)
                {
                    return new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                var param = {
                                        "roleId" : queryId,
                                        "page": 1,
                                        "rowSize": 10000000,
                                }

                                return meta.api.common.roleMenu.getRoleMenuList(param);
                            })
                            .then(function (response) {

                                self.roleMenuList = response.data.items;
                                var forDeleteParentItem = _.cloneDeep(self.data.treeMenuList);
                                var tempRoleMenuList = _.cloneDeep(self.roleMenuList);

                                for(var j = 0; j< forDeleteParentItem.length ; j++)
                                {

                                    for(var i = 0; i<tempRoleMenuList.length; i++)
                                    {

                                        if(forDeleteParentItem[j].parentId == tempRoleMenuList[i].menuId)
                                        {
                                            tempRoleMenuList.splice(i,1);
                                            break;
                                        }
                                    }

                                }


                                for(var i = 0; i<tempRoleMenuList.length; i++)
                                {
                                       for(var j = 0; j< forDeleteParentItem.length ; j++)
                                    {
                                           if(tempRoleMenuList[i].menuId == forDeleteParentItem[j].id){

                                               self.menuSelected.push(forDeleteParentItem[j]);
                                               break;

                                           }
                                    }

                                }

                            })
                            .then(function () { resolve(); });
                    });
                }
            },



            "setRoleDeatil": async function (queryId) {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            return  meta.api.common.role.getRole(queryId);
                        })
                        .then(function (response) {
                            self.detailData = response.data;
                        })
                        .then(function () { resolve(); });
                });
            },

            "setApiList": async function (queryId) {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            var param = {
                                    "page": 1,
                                    "rowSize": 10000000,
                            };
                            return meta.api.common.api.getApiList(param);
                        })
                        .then(function (response) {
                            self.api.dataTable.items = response.data.items;
                            if(queryId != undefined && queryId != null)
                            {
                                var param = {
                                        "roleId" : queryId,
                                        "page": 1,
                                        "rowSize": 10000000,
                                };

                                return meta.api.common.roleApi.getRoleApiList(param);
                            } else {
                                return null;
                            }
                        })
                        .then(function (response) {
                            if(response != undefined && response != null)
                            {
                                var roleApiLIst = response.data.items
                                var allApiList = self.api.dataTable.items;
                                for(var i = 0; i< roleApiLIst.length; i++)
                                {
                                    for(var j = 0; j<allApiList.length; j++)
                                    {
                                        if(roleApiLIst[i].apiId === allApiList[j].id)
                                        {
                                            self.api.apiSelected.push(allApiList[j]);
                                            break;
                                        }
                                    }
                                }
                            }

                        })
                        .then(function () { resolve(); });
                });
            },


            "flatArrayToNestedArray": function (flatArray, id, parentId, children) {
                var nestedArray,
                    map,
                    flat,
                    i;
                flatArray = _.cloneDeep(flatArray);
                children = children ? children : "children";
                nestedArray = [];
                map = {};
                for (i = 0; i < flatArray.length; i++) {
                    map[flatArray[i][id]] = i;
                    flatArray[i][children] = [];
                }
                for (i = 0; i < flatArray.length; i++) {
                    flat = flatArray[i];
                    if (flat[parentId]) {
                        flatArray[map[flat[parentId]]][children].push(flat);
                    } else {
                        nestedArray.push(flat);
                    }
                }
                return nestedArray;
            },
            "registerNewRole" : async function () {
                var self = this;
                if(self.detailData.name == null || self.detailData.name == ""){
                    meta.alert("권한명을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.name.length > 100){
                    meta.alert("권한명은 100자 이하로 작성해주십시오.");
					return;					
				}

                if(self.detailData.description == null || self.detailData.description == ""){
                    meta.alert("설명을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.description.length > 500){
                    meta.alert("설명은 500자 이하로 작성해주십시오.");
					return;					
                }

                if(self.detailData.value == null || self.detailData.value == ""){
                    meta.alert("값을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.value.length > 100){
                    meta.alert("값은 100자 이하로 작성해주십시오.");
					return;					
                }

                if (await meta.confirm("저장 하시겠습니까?")) {
                    var forPushParentItem = _.cloneDeep(self.data.treeMenuList);

                    var roleMenuListId = [];
                    var roleApiListId = [];
                    for(var i =0; i< self.menuSelected.length ; i++)
                    {
                        roleMenuListId.push(self.menuSelected[i].id);
                        var exsitsParentIdMenu = self.menuSelected[i];
                        while(true){
                            var isNoHaveParent = false;
                            for (var j = 0; j < forPushParentItem.length; j++) {
                                if(exsitsParentIdMenu.parentId === forPushParentItem[j].id)
                                {
                                    roleMenuListId.push(forPushParentItem[j].id);
                                    exsitsParentIdMenu = (forPushParentItem.splice(j,1))[0];
                                    isNoHaveParent = false;
                                    break;
                                } else {
                                    isNoHaveParent = true;
                                }



                            }
                            if(exsitsParentIdMenu.parentId == undefined || exsitsParentIdMenu.parentId == null || isNoHaveParent)
                            {
                                break;
                            }


                        }
                    }
                    roleMenuListId.sort(function(a, b) { // 오름차순
                        return a - b;
                    });

                    for(var i = 0; i< self.api.apiSelected.length; i++)
                    {
                        roleApiListId.push(self.api.apiSelected[i].id);
                    }
                    var roleMenuListIdString = "";
                    for(var i = 0; i<roleMenuListId.length; i++)
                    {
                        if(roleMenuListIdString === "")
                            roleMenuListIdString = roleMenuListIdString + roleMenuListId[i];
                        else
                            roleMenuListIdString = roleMenuListIdString + "," + roleMenuListId[i];
                    }

                    var roleApiListIdString = "";
                    for(var i = 0; i< roleApiListId.length; i++)
                    {
                        if(roleApiListIdString === "")
                            roleApiListIdString = roleApiListIdString + roleApiListId[i];
                        else
                            roleApiListIdString = roleApiListIdString + "," + roleApiListId[i];
                    }
                    var params = {
                        "name" : self.detailData.name,
                        "description" :  self.detailData.description,
                        "value" :  self.detailData.value,
                        "roleMenuListIdString" : roleMenuListIdString,
                        "roleApiListIdString" : roleApiListIdString
                    };

                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                return meta.api.app.RoleAndRoleMenuAndRoleApi.createRoleAndRoleMenuAndRoleApi(params);
                            })
                            .then(function () { resolve(); });
                    });
                    await meta.alert('저장에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/roles",
                    });
                }
            },
            "updateRole" : async function () {
                var self = this;
                if(self.detailData.name == null || self.detailData.name == ""){
                    meta.alert("권한명을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.name.length > 100){
                    meta.alert("권한명은 100자 이하로 작성해주십시오.");
					return;					
				}

                if(self.detailData.description == null || self.detailData.description == ""){
                    meta.alert("설명을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.description.length > 500){
                    meta.alert("설명은 500자 이하로 작성해주십시오.");
					return;					
                }

                if(self.detailData.value == null || self.detailData.value == ""){
                    meta.alert("값을 반드시 입력하세요.");
                    return;
                } else if(self.detailData.value.length > 100){
                    meta.alert("값은 100자 이하로 작성해주십시오.");
					return;					
                }

                if (await meta.confirm("수정 하시겠습니까?")) {
                    var forPushParentItem = _.cloneDeep(self.data.treeMenuList);

                    var roleMenuListId = [];
                    var roleApiListId = [];
                    for(var i =0; i< self.menuSelected.length ; i++)
                    {
                        roleMenuListId.push(self.menuSelected[i].id);
                        var exsitsParentIdMenu = self.menuSelected[i];
                        while(true){
                            var isNoHaveParent = false;
                            for (var j = 0; j < forPushParentItem.length; j++) {
                                if(exsitsParentIdMenu.parentId === forPushParentItem[j].id)
                                {
                                    roleMenuListId.push(forPushParentItem[j].id);
                                    exsitsParentIdMenu = (forPushParentItem.splice(j,1))[0];
                                    isNoHaveParent = false;
                                    break;
                                } else {
                                    isNoHaveParent = true;
                                }

                            }
                            if(exsitsParentIdMenu.parentId == undefined || exsitsParentIdMenu.parentId == null || isNoHaveParent)
                            {
                                break;
                            }


                        }
                    }
                    roleMenuListId.sort(function(a, b) { // 오름차순
                        return a - b;
                    });

                    for(var i = 0; i< self.api.apiSelected.length; i++)
                    {
                        roleApiListId.push(self.api.apiSelected[i].id);
                    }
                    var roleMenuListIdString = "";
                    for(var i = 0; i<roleMenuListId.length; i++)
                    {
                        if(roleMenuListIdString === "")
                            roleMenuListIdString = roleMenuListIdString + roleMenuListId[i];
                        else
                            roleMenuListIdString = roleMenuListIdString + "," + roleMenuListId[i];
                    }

                    var roleApiListIdString = "";
                    for(var i = 0; i< roleApiListId.length; i++)
                    {
                        if(roleApiListIdString === "")
                            roleApiListIdString = roleApiListIdString + roleApiListId[i];
                        else
                            roleApiListIdString = roleApiListIdString + "," + roleApiListId[i];
                    }
                    var params = {
                        "id" : self.detailData.id,
                        "name" : self.detailData.name,
                        "description" :  self.detailData.description,
                        "value" :  self.detailData.value,
                        "roleMenuListIdString" : roleMenuListIdString,
                        "roleApiListIdString" : roleApiListIdString
                    };

                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                 return meta.api.app.RoleAndRoleMenuAndRoleApi.modifyRoleAndRoleMenuAndRoleApi(params);
                            })
                            .then(function () { resolve(); });
                    });
                    await meta.alert('수정에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/roles",
                    });
                }

            },

        },


        "mounted": function () {
            var queryId = this.$route.query.id;
            if(queryId != undefined && queryId!= null)
            {
                Promise.resolve()
                    .then(this.setRoleDeatil(queryId),
                            this.loadTreeMenuList(queryId),
                            this.setApiList(queryId),
                            /*this.setRoleMenuList(queryId),*/
                            /*this.setRoleApiList(queryId),*/
                            );

            } else{
                Promise.resolve()
                .then(
                        this.loadTreeMenuList(),
                        this.setApiList(),);
            }
        },
    });
});