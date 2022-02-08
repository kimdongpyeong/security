var SettingTermsMainPage;
SettingTermsMainPage = Vue.component("setting-terms-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/terms/main.html")).data,
        "data": function () {
            return {
                "terms": {
                    "panels": {
                        "list": [0]
                    },
                    "selected" : [],
                    "dataTable": {
                        "headers": [
                            {"text": "ID", "sortable": false, "value": "id"},
                            {"text": "제목", "sortable": false, "value": "title"},
                            {"text": "게시여부", "sortable": false, "value": "publishYn"},
                            {"text": "필수여부", "sortable": false, "value": "requiredYn"},
                            {"text": "등록 날짜", "sortable": false, "value": "createdDate"},
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
                },
            };
        },
        "watch": {
            "terms.dataTable.page": {
                "handler": function (newValue, oldValue) {
                    this.setTermsList();
                },
                "deep": true
            }
        },
        "methods": {
            "handleClick" : function(value) {
                this.$router.push({
                    "path": "/settings/terms/detail",
                    "query": {
                        "id": value.id
                    }
                });
            },
            "setTermsList": async function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            var param = {
                                    "page": self.terms.dataTable.page,
                                    "rowSize": self.terms.dataTable.itemsPerPage,
                                    "adminYn" : 1
                                    
                            };

                            self.terms.dataTable.loading = true;
                            return meta.api.common.terms.getTermsList(param);
                        })
                        .then(function (response) {
                            var data = response.data;
                            self.terms.dataTable.items = data.items;
                            self.terms.dataTable.serverItemsLength = data.totalRows;
                            self.terms.dataTable.loading = false;
                        })
                        .then(function () { resolve(); });
                });
            },
            "removeTermsList": async function() {
				let idList = [],
					list = this.terms.selected,
					i = 0;
				
				for(i = 0; i < list.length; i++) {
					idList.push(list[i].id);
				}
				
				if(idList == 0) {
                    await meta.alert("삭제할 항목을 선택해주세요.")
					return;
				} else {
					if(await meta.confirm("삭제하시겠습니까?")){
						(await meta.api.common.terms.removeTermsList(idList));
	                    await meta.alert("삭제되었습니다.")
	                 /*   token = meta.auth.getToken();
	                    await meta.auth.logout(token);*/
	                    if(this.$route.path === "/settings/terms") {
	                        this.$router.go();
	                    } else {
	                        this.$router.replace("/");
	                    }
	                }
					//setTermsList
				}
			}
        },
        "mounted": function () {
            Promise.resolve()
                .then(this.setTermsList);
        },
    });
});