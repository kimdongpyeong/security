var SettingCodeMainPage;
SettingCodeMainPage = Vue.component("setting-code-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/code/main.html")).data,
    "data": function () {
        return {
            "data": {
                "treeCodeList": [],
                "code": {"show": null}
            },
            "treeview": {
                "codeList": {
                    "items": [],
                    "open": [],
                    "active": []
                }
            },
            "autocomplete": {
                "parentId": {
                    "items": []
                }
            },
            "btn": {
                "saveCode": {
                    "loading": false
                },
                "removeCode": {
                    "loading": false
                }
            }
        };
    },
    "watch": {
        "data.treeCodeList": {
            "handler": function (newValue, oldValue) {
                this.treeview.codeList.items = this.flatArrayToNestedArray(newValue, "id", "parentId");
                this.treeview.codeList.open = _.cloneDeep(newValue);
                this.autocomplete.parentId.items = _.cloneDeep(newValue);

            }
        },
        "treeview.codeList.active": {
            "handler": function (newValue, oldValue) {
                this.data.code = newValue[0] ? _.cloneDeep(newValue[0]) : {"show": null};
            }
        }
    },
    "methods": {
        "loadTreeCodeList": async function () {
            var treeCodeList;
            treeCodeList = (await meta.api.util.code.getTreeCodeList({
                "page": 1,
                "rowSize": 100000000,
                "sort": ["orderNoPath,asc"]
            })).data.items;
            this.data.treeCodeList = treeCodeList;
        },
        "saveCode": async function () {
            var code;
            code = this.data.code;
            this.btn.saveCode.loading = true;
            if(code.name.length > 50){
				await meta.alert("이름은 50자 이하로 작성해주십시오")
			} else if(code.description.length > 200){
				await meta.alert("설명은 200자 이하로 작성해주십시오")
			} else if(code.value.length > 50){
				await meta.alert("값은 50자 이하로 작성해주십시오")
			} else if (await meta.confirm("저장 하시겠습니까?")) {
                if (code.id) {
                    code = (await meta.api.common.code.modifyCode(code.id, code)).data;
                } else {
                    code = (await meta.api.common.code.createCode(code)).data;
                }
                await meta.alert("저장 되었습니다.");
                await this.loadTreeCodeList();
                this.treeview.codeList.active = [code];
            }
            this.btn.saveCode.loading = false;
        },
        "removeCode": async function () {
            var code;
            code = this.data.code;
            this.btn.removeCode.loading = true;
            if (await meta.confirm("삭제 하시겠습니까?")) {
                await meta.api.common.code.removeCode(code.id);
                await meta.alert("삭제 되었습니다.");
                await this.loadTreeCodeList();
                this.treeview.codeList.active = [];
            }
            this.btn.removeCode.loading = false;
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
        }
    },
    "mounted": async function () {
        Promise.all([
            this.loadTreeCodeList()
        ]);
    }
}); });