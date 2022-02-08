var DataTableCustomComponent;
DataTableCustomComponent = Vue.component("data-table-custom-component", async function (resolve) { resolve({
    "template": (await axios.get("/vue/component/data-table-custom-component.html")).data,
    "props": {
        "headers": {
            "type": Array,
            "default": [
                {"text": "template1", "sortable": true, "value": "template1", "align": "center", "width": "100", "cellClass": "w-10"},
                {"text": "template2", "sortable": true, "value": "template2", "align": "center", "width": "100", "cellClass": "w-10"},
                {"text": "template3", "sortable": true, "value": "template3", "align": "center", "width": "100", "cellClass": "w-10"},
                {"text": "template4", "sortable": true, "value": "template4", "align": "center", "width": "100", "cellClass": "w-10"},
                {"text": "template5", "sortable": true, "value": "template5", "align": "center", "width": "100", "cellClass": "text-truncate w-10"},
            ]
        },
        "items": {
            "type": Array,
            "default": []
        },
        "itemClass": {
            "type": String,
            "default": ""
        },
        "totalRows": {
            "type": Number,
            "default": 0
        },
        "loading": {
            "type": Boolean,
            "default": false
        },
        "uploadHide": {
            "type": Boolean,
            "default": false
        },
        "customHide": {
            "type": Boolean,
            "default": true
        },
        "customButtonTxt": {
            "type": String,
            "default": "",
        },
        "addHide": {
            "type": Boolean,
            "default": false
        },
        "removeHide": {
            "type": Boolean,
            "default": false
        },
        "showSelect": {
            "type": Boolean,
            "default": false
        },
        "multipleSelect": {
            "type": Boolean,
            "default": true
        },
        "itemKey": {
            "type": String,
            "default": "id"
        },
        "countTitle": {
            "type": Object,
            "default": {
                "front": "전체",
                "end": "건"
            }
        },
        "cell": {
            "type": Object,
            "default": {}
        },
        "downloadHide": {
            "type": Boolean,
            "default": false
        },
        "createdHide": {
            "type": Boolean,
            "default": true
        },
        "search": {
            "type": Boolean,
            "default": false
        },
        "page" : {
            "type" : Number,
            "default" : 1
        },
        "dataTableRepaint" : {
            "type" : Number,
            "default" : 1
        },
        "itemsPerPage" : {
            "type" : String,
            "default" : 10
        },
        "dense": {
            "type": Boolean,
            "default": false
        },
        "contentClass": {
            "type": String,
            "default": "tableline"
        },
        "rowClassConditional": {
            "type": Object,
            "default": null
        },
        "sortBy": {
            "type": Array,
            "default": []
        },
        "sortDesc": {
            "type": Array,
            "default": []
        },
        "groupBy": {
            "type": Array,
            "default": []
        },
        "groupDesc": {
            "type": Array,
            "default": []
        },
        "multiSort": {
            "type": Boolean,
            "default": false
        },
        "mustSort": {
            "type": Boolean,
            "default": false
        },
        "itemsPerPageHide": {
            "type": Boolean,
            "default": false
        },
        "countHide": {
            "type": Boolean,
            "default": false
        },
        "maxRow": {
            "type": Number,
            "default": 1
        },
        "checkboxConditional": {
            "type": Object,
            "default": null
        },
        "researchFlag": {
            "type": Number,
            "default": 0
        },
        "dataProps": {
            "type": Array,
            "default": [],
        },
        "radioProps": {
            "type": Object,
            "default": null,
        },
    },
    "data": function () {
        return {
            "paginationList": [
                {"text": "5개 보기", "value": 5},
                {"text": "10개 보기", "value": 10},
                {"text": "20개 보기", "value": 20},
                {"text": "30개 보기", "value": 30},
                {"text": "50개 보기", "value": 50},
                {"text": "100개 보기", "value": 100},
                {"text": "500개 보기", "value": 500},
            ],
            "options": {
                "page": 1,
                "itemsPerPage": 10,
                "sortBy": [],
                "sortDesc": [],
                "groupBy": [],
                "groupDesc": [],
                "multiSort": false,
                "mustSort": false
            },
            "data": [],
            "rediaoData": null,
            "checkAll": false,
            "indeterminate": false,
            "checkTotal": null,
            disabled: false,
        };
    },
    "watch": {
        "dataProps": {
            "handler": function (n,o) {
                this.data = n;
            },
            "deep": true
        },
        "radioProps": {
            "handler": function (n,o) {
                this.rediaoData = n;
            },
            "deep": true
        },

        "options": {
            "handler": function (n,o) {
                this.$emit("reload", n);
            },
            "deep": true
        },
        "loading": {
            "handler": function (n,o) {
                if(!n) {
                    this.data = [];
                    if(this.checkboxConditional !== null) {
                        let c = 0;
                        this.items.forEach(e=>{
                            if(this.isTrue(this.checkboxConditional, e)) {
                                c++;
                            }
                        })
                        this.checkTotal = c;
                    } else {
                        this.checkTotal = this.options.itemsPerPage;
                    }
                }
            },
            "deep": true
        },
        "search": {
            "handler": function (n,o) {
                if(n) {
                    if(this.options.page !== 1) {
                        this.options.page = 1;
                    }else {
                        this.$emit("reload", this.options);
                    }
                }
            }
        },
        "researchFlag": {
            "handler": function (n,o) {
                if(n !== o) {
                    this.$emit("reload", this.options);
                }
            }
        },
        "data": {
            "handler": function (n) {
                let item = [];
                if(this.items.length === 0) {
                    this.checkAll = false;
                    this.indeterminate = false;
                }else{
                    if(this.checkboxConditional !== null) {
                        n.forEach(e=> {
                            if(this.isTrue(this.checkboxConditional, e)) {
                                item.push(e);
                            }
                        });
                    } else {
                        item = n;
                    }
                    this.$emit("selected", item);
                    if(item.length === this.checkTotal) {
                        this.checkAll = true;
                        this.indeterminate = false;
                    } else if (item.length === 0) {
                        this.checkAll = false;
                        this.indeterminate = false;
                    } else {
                        this.checkAll = false;
                        this.indeterminate = true;
                    }
                }
            },
            "deep": true
        },
        "rediaoData": {
            "handler": function (n) {
                let item = null;
                if(this.items.length === 0) {
                    this.indeterminate = false;
                }else{
                    if(this.checkboxConditional !== null) {
                        if(this.isTrue(this.checkboxConditional, n)) {
                            item = n;
                        }
                    } else {
                        item = n;
                    }
                    this.$emit("selected", item);
                    if (n !== undefined && n != null) {
                        this.indeterminate = false;
                    } else {
                        this.indeterminate = true;
                    }
                }
            },
            "deep": true
        },
        "items": {
            "handler": function (n) {
                this.dataTableRepaint += 1;
            },
            "deep": true
        },
    },
    "computed": {
        "disabled": function () {
            return this.loading || this.totalRows === 0 ? true : false;
        },
        "count": function () {
            var finalCount=null, temp;
            var alterCount = this.totalRows;
            while(alterCount > 1000){
                temp = alterCount % 1000;
                alterCount = Math.floor(alterCount / 1000);
                finalCount = ","+String(temp);
            }
            if(finalCount != null) finalCount = String(alterCount) + finalCount;
            else finalCount = this.totalRows;
            return this.countTitle.front + " " + finalCount + this.countTitle.end;
        },
    },
    "methods": {
        "clickRow": function (e) {
            this.$emit("click:row", e);
        },
        "excelUpload": function () {
            this.$emit("upload");
        },
        "remove": function () {
            this.$emit("remove", this.data);
        },
        "add": function () {
            this.$emit("add");
        },
        "clickIcon": function (item, header) {
            const clicked = { item, header };
            this.$emit("click:icon", clicked);
        },
        "changeAutocomplete": function (id, header, item) {
            const selected = { id, header, item };
            this.$emit("change:autocomplete", selected);
        },
        "clickButton": function (item, header) {
            const clicked = {item, header};
            this.$emit("click:button", clicked);
        },
        "inputTextField": function (value, item, header) {
            const clicked = {value, item, header};
            this.$emit("input:textField", clicked);
        },
        "inputClickAfterTextField": function (value, item, header) {
            const clicked = {value, item, header};
            this.$emit("input:clickAfterTextField", clicked);
        },
        "download": function(){
            this.$emit("download")
        },
        "customFunction": function(){
            this.$emit("customFunction")
        },
        "clickImg": function (item, header) {
            const clicked = {item, header};
            this.$emit("click:img", clicked);
        },
        "clickMultiButton": function (item, header, title) {
            const clicked = {item, header, title};
            this.$emit("click:multiButton", clicked);
        },
        "isTrue": function (conditional, item) {
            if(conditional !== undefined && conditional !== null) {
                if(conditional[0] === undefined){
                    switch (conditional.inequality) {
                        case "gt":
                            return item[conditional.column] > conditional.target;
                        case "ge":
                            return item[conditional.column] >= conditional.target;
                        case "lt":
                            return item[conditional.column] < conditional.target;
                        case "le":
                            return item[conditional.column] <= conditional.target;
                        case "eq":
                            return item[conditional.column] === conditional.target;
                        case "ne":
                            return item[conditional.column] !== conditional.target;
                        case "nu":
                            return item[conditional.column] !== undefined;
                        case "nn":
                            return item[conditional.column] != null;
                        case "nb":
                            return item[conditional.column] !== '';
                        case "nunb":
                            return item[conditional.column] !== undefined && item[conditional.column] != null && item[conditional.column] !== '';
                        case "iu":
                            return item[conditional.column] === undefined;
                        case "in":
                            return item[conditional.column] == null;
                        case "ib":
                            return item[conditional.column] === '';
                        case "iunb":
                            return item[conditional.column] === undefined || item[conditional.column] == null || item[conditional.column] === '';
                        case "dgt":
                            return moment(item[conditional.column])._isValid && moment(conditional.target)._isValid && moment(item[conditional.column]).isAfter( moment(conditional.target));
                        case "dlt":
                            return moment(item[conditional.column])._isValid && moment(conditional.target)._isValid && moment(item[conditional.column]).isBefore( moment(conditional.target));
                        default:
                            return false;
                    }
                } else {
                    let bool = true;
                    for(var i = 0; i < conditional.length; i = i + 2) {
                        let subBool = true;
                        switch (conditional[i].inequality) {
                            case "gt":
                                subBool =  item[conditional[i].column] > conditional[i].target;
                                break;
                            case "ge":
                                subBool = item[conditional[i].column] >= conditional[i].target;
                                break;
                            case "lt":
                                subBool = item[conditional[i].column] < conditional[i].target;
                                break;
                            case "le":
                                subBool = item[conditional[i].column] <= conditional[i].target;
                                break;
                            case "eq":
                                subBool = item[conditional[i].column] === conditional[i].target;
                                break;
                            case "ne":
                                subBool = item[conditional[i].column] !== conditional[i].target;
                                break;
                            case "nu":
                                subBool = item[conditional[i].column] !== undefined;
                                break;
                            case "nn":
                                subBool = item[conditional[i].column] != null;
                                break;
                            case "nb":
                                subBool = item[conditional[i].column] !== '';
                                break;
                            case "nunb":
                                subBool = item[conditional[i].column] !== undefined && item[conditional[i].column] != null && item[conditional[i].column] !== '';
                                break;
                            case "iu":
                                subBool = item[conditional[i].column] === undefined;
                                break;
                            case "in":
                                subBool = item[conditional[i].column] == null;
                                break;
                            case "ib":
                                subBool = item[conditional[i].column] === '';
                                break;
                            case "iunb":
                                subBool = item[conditional[i].column] === undefined || item[conditional[i].column] == null || item[conditional[i].column] === '';
                                break;
                            case "dgt":
                                subBool = moment(item[conditional[i].column])._isValid && moment(conditional[i].target)._isValid && moment(item[conditional[i].column]).isAfter( moment(conditional[i].target));
                                break;
                            case "dlt":
                                subBool = moment(item[conditional[i].column])._isValid && moment(conditional[i].target)._isValid && moment(item[conditional[i].column]).isBefore( moment(conditional[i].target));
                                break;
                            default:
                                subBool = false;
                        }
                        if(i === 0) {
                            bool = subBool;
                        } else {
                            if (conditional[i-1].toUpperCase() === "AND") {
                                bool = bool && subBool;
                            } else if (conditional[i-1].toUpperCase() === "OR") {
                                bool = bool || subBool;
                            }
                        }
                    }
                    return bool;
                }
            } else {
                return true;
            }
        },
        "getClass": function (conditional, item) {
            let bool = false;
            if(conditional !== undefined && conditional !== null) {
                switch (conditional.inequality) {
                    case "gt":
                        bool = item[conditional.column] > conditional.target;
                        break;
                    case "ge":
                        bool = item[conditional.column] >= conditional.target;
                        break;
                    case "lt":
                        bool = item[conditional.column] < conditional.target;
                        break;
                    case "le":
                        bool = item[conditional.column] <= conditional.target;
                        break;
                    case "eq":
                        bool = item[conditional.column] === conditional.target;
                        break;
                    case "ne":
                        bool = item[conditional.column] !== conditional.target;
                        break;
                    case "nu":
                        bool = item[conditional.column] !== undefined;
                        break;
                    case "nn":
                        bool = item[conditional.column] != null;
                        break;
                    case "nb":
                        bool = item[conditional.column] !== '';
                        break;
                    case "nunb":
                        bool = item[conditional.column] !== undefined && item[conditional.column] != null && item[conditional.column] !== '';
                        break;
                    case "iu":
                        bool = item[conditional.column] === undefined;
                        break;
                    case "in":
                        bool = item[conditional.column] == null;
                        break;
                    case "ib":
                        bool = item[conditional.column] === '';
                        break;
                    case "iunb":
                        bool = item[conditional.column] === undefined || item[conditional.column] == null || item[conditional.column] === '';
                        break;
                    case "dgt":
                        bool = moment(item[conditional.column])._isValid && moment(conditional.target)._isValid && moment(item[conditional.column]).isAfter( moment(conditional.target));
                        break;
                    case "dlt":
                        bool = moment(item[conditional.column])._isValid && moment(conditional.target)._isValid && moment(item[conditional.column]).isBefore( moment(conditional.target));
                        break;
                    default:
                        bool = false;
                }
                if (bool) {
                    return conditional.cssClass;
                }else {
                    return conditional.defaultClass !== undefined  && conditional.defaultClass !== null ? conditional.defaultClass : "";
                }
            } else {
                return "";
            }
        },
        "clickStyleText": function (item, header) {
            const clicked = {item, header};
            this.$emit("click:styleText", clicked);
        },
        "clickHtml": function (item, header) {
            const clicked = {item, header};
            this.$emit("click:html", clicked);
        },
        "toggleStatus": function (headerType, rKey) {
            this.$set(this.cell[headerType].readonlyFlag, rKey, false);
        },
        "enterTextField": function (item, headerType, rKey) {
            item.memo = this.cell[headerType].memo[rKey];
            this.$set(this.cell[headerType].readonlyFlag, rKey, true);
            this.$emit("enter:clickAfterTextField", item);
        },
        "moveCreatePage": function () {
            this.$router.push({
                "path": "/asisadmin/user/write"
            });
        },
    },
    "created": function () {
        this.options = {
                "page": this.page,
                "itemsPerPage": this.itemsPerPage,
                "sortBy": this.sortBy,
                "sortDesc": this.sortDesc,
                "groupBy": this.groupBy,
                "groupDesc": this.groupDesc,
                "multiSort": this.multiSort,
                "mustSort": this.mustSort
        };
    }
}); });