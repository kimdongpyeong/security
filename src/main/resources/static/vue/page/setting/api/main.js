var SettingApiMainPage;
SettingApiMainPage = Vue.component("setting-api-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/api/main.html")).data,
        "data": function () {
            return {
                "api": {
                    "panels": {
                        "list": [0]
                    },
                    "selected" : [],
                    "dataTable": {
                        "headers": [
                            {"text": "API URL", "sortable": false, "value": "url"},
                            {"text": "API 명", "sortable": false, "value": "name"},
                            {"text": "API 종류", "sortable": false, "value": "method"},
                            {"text": "생성한 날짜", "sortable": false, "value": "createdDate"},
                            {"text": "수정한 날짜", "sortable": false, "value": "lastModifiedDate"},
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
            "api.dataTable.page": {
                "handler": function (newValue, oldValue) {
                    this.setApiList();
                },
                "deep": true
            }
        },
        "methods": {
            "handleClick" : function(value) {
                this.$router.push({
                    "path": "/settings/apis/detail",
                    "query": {
                        "id": value.id
                    }
                });
            },
            "setApiList": async function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            var param = {
                                    "page": self.api.dataTable.page,
                                    "rowSize": self.api.dataTable.itemsPerPage
                            };

                            self.api.dataTable.loading = true;
                            return meta.api.common.api.getApiList(param);
                        })
                        .then(function (response) {
                            var data = response.data;
                            self.api.dataTable.items = data.items;
                            self.api.dataTable.serverItemsLength = data.totalRows;
                            self.api.dataTable.loading = false;
                        })
                        .then(function () { resolve(); });
                });
            },

            "deleteApiList" : async function() {
                var self = this;

                if (await meta.confirm("삭제 하시겠습니까?")) {
                    var selectedApi = self.api.selected;


                    var idList = [];
                    for(var i = 0; i< selectedApi.length; i++)
                    {
                        if(parseInt(selectedApi[i].id) > 9)
                            idList.push(selectedApi[i].id);
                        else
                            await meta.alert('기초 API는 삭제할수 없습니다.');
                    }
                    (await meta.api.common.api.removeApiList(idList));


                    await meta.alert('삭제에 성공했습니다.');
                    this.setApiList();
                }

            },

        },
        "mounted": function () {
            Promise.resolve()
                .then(this.setApiList);
        },
    });
});