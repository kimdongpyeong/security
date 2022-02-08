var SettingMenuMainPage;
SettingMenuMainPage = Vue.component("setting-menu-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/menu/main.html")).data,
    "data": function () {
        return {
            "data": {
                "treeMenuList": [],
                "menu": {"sideShow": null, "show": null, "publicyStatus": null},
            },
            "rules": {
                "required": value => !!value || '필수'
            },
            "treeview": {
                "menuList": {
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
                "saveMenu": {
                    "loading": false
                },
                "removeMenu": {
                    "loading": false
                }
            }
        };
    },
    "watch": {
        "data.treeMenuList": {
            "handler": function (newValue, oldValue) {
                this.treeview.menuList.items = this.flatArrayToNestedArray(newValue, "id", "parentId");
                this.treeview.menuList.open = _.cloneDeep(newValue);
                this.autocomplete.parentId.items = _.cloneDeep(newValue);

            }
        },
        "treeview.menuList.active": {
            "handler": function (newValue, oldValue) {
                this.data.menu = newValue[0] ? _.cloneDeep(newValue[0]) : {"sideShow": null,"show": null, "publicyStatus": null};
            }
        }
    },
    "methods": {
        "loadTreeMenuList": async function () {
            var treeMenuList;
            treeMenuList = (await meta.api.util.menu.getTreeMenuList({
                "page": 1,
                "rowSize": 100000000,
                "sort": ["rankingPath,asc"]
            })).data.items;
            this.data.treeMenuList = treeMenuList;
        },
        "saveMenu": async function () {
            var menu;
            menu = this.data.menu;
            this.btn.saveMenu.loading = true;

            if((menu.name == null || menu.name == '') && menu.name.length > 100) {
                await meta.alert("이름은 100자 이하로 입력해주세요");
                return;
            } else if((menu.description == null || menu.description == '') && menu.description.length > 500) {
                await meta.alert("설명은 500자 이하로 입력해주세요");
                return;
            } else if((menu.path == null || menu.path == '') && menu.path.length > 500) {
                await meta.alert("경로는 500자 이하로 입력해주세요");
                return;
            }else if(await meta.confirm("저장 하시겠습니까?")) {
                if (menu.id) {
                    menu = (await meta.api.common.menu.modifyMenu(menu.id, menu)).data;
                } else {
                    menu = (await meta.api.common.menu.createMenu(menu)).data;
                }
                await meta.alert("저장 되었습니다.");
                await this.loadTreeMenuList();
                this.treeview.menuList.active = [menu];
            }
            this.btn.saveMenu.loading = false;
        },
        "removeMenu": async function () {
            var menu;
            menu = this.data.menu;
            this.btn.removeMenu.loading = true;
            if (await meta.confirm("삭제 하시겠습니까?")) {
                await meta.api.common.menu.removeMenu(menu.id);
                await meta.alert("삭제 되었습니다.");
                await this.loadTreeMenuList();
                this.treeview.menuList.active = [];
            }
            this.btn.removeMenu.loading = false;
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
            this.loadTreeMenuList()
        ]);
    }
}); });