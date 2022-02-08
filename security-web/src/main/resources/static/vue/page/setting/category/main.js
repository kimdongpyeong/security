var SettingCategoryMainPage;
SettingCategoryMainPage = Vue.component("setting-category-main-page", async function (resolve) { resolve({
    "template": (await axios.get("/vue/page/setting/category/main.html")).data,
    "data": function () {
        return {
            "data": {
                "treeCategoryList": [],
                "category": {"show": null},
                "maxOrder": 1,
            },
            "treeview": {
                "categoryList": {
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
                "saveCategory": {
                    "loading": false
                },
                "removeCategory": {
                    "loading": false
                }
            },
            "exposureCategoryList": {
                "Y": "노출",
                "N": "숨김"
            },
            "rules": {
                "required": value => !!value || '필수항목입니다.',
            }
        };
    },
    "watch": {
        "data.treeCategoryList": {
            "handler": function (newValue, oldValue) {
                this.treeview.categoryList.items = this.flatArrayToNestedArray(newValue, "id", "parentId");
                this.treeview.categoryList.open = _.cloneDeep(newValue);
                this.autocomplete.parentId.items = _.cloneDeep(newValue);
            }
        },
        "treeview.categoryList.active": {
            "handler": function (newValue, oldValue) {
                this.data.category = newValue[0] ? _.cloneDeep(newValue[0]) : {"show": null};
            }
        },
    },
    "methods": {
        "loadCategoryList": async function () {
            var treeCategoryList;
            treeCategoryList = (await meta.api.util.category.getTreeCategoryList({
                "page": 1,
                "rowSize": 100000000,
                "sort": ["orderNoPath,asc"]
            })).data.items;
            this.data.treeCategoryList = treeCategoryList;

            this.data.treeCategoryList.forEach(x => {
                x.tempTitle = x.title + "(순위 : " + x.orderNo +  ")";
            })
        },
        "saveCategory": async function () {
            let category, validate;

            category = this.data.category;
            validate = this.$refs.form.validate();
            this.btn.saveCategory.loading = true;

            if(!validate){
                await meta.alert("필수항목을 입력해주세요.");
            } else{
                if (await meta.confirm("저장 하시겠습니까?")) {
                    if (category.id) {
                        category = (await meta.api.common.category.modifyCategory(category.id, category)).data;
                    } else {
                        category = (await meta.api.common.category.createCategory(category)).data;
                    }
                    await meta.alert("저장 되었습니다.");
                    await this.loadCategoryList();
                    this.data.category = {};
                }
            }
            this.btn.saveCategory.loading = false;
        },
        "removeCategory" : async function(){
            var category;
            category = this.data.category;
            this.btn.removeCategory.loading = true;
            if (await meta.confirm("삭제 하시겠습니까?")) {
                await meta.api.common.category.removeCategory(category.id);
                await meta.alert("삭제 되었습니다.");
                await this.loadCategoryList();
                this.data.category = {};
            }
            this.btn.removeCategory.loading = false;

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
        this.loadCategoryList();
    }
}); });