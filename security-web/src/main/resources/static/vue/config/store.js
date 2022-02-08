var store;
store = new Vuex.Store({
    "modules": {
        /* app */
        "app": {
            "namespaced": true,
            "state": function () {
                return {
                    "token": null,
                    "user": null,
                    "roleList": null,
                    "menuList": null,
                    "treeMenuList": null,
                    "alert": {
                        "value": false,
                        "message": "",
                        "callback": function () {}
                    },
                    "confirm": {
                        "value": false,
                        "message": "",
                        "oktext" :  '확인',
                        "canceltext" : '취소',
                        "callback": function () {}
                    },
                    "showMenuPathName" : "",
                    "loading" : {
                        "zIndex" : 9999,
                        "overlay" : false
                    },
                    "signUpPhoneNum": null,
                    "findAccount": {
                        "phoneNum": null,
                        "username": null,
                    },
                    "loginPopFlag": false,
                    "signUpPopFlag": false,
                    "chatProfile": {
                        "flag": false,
                        "profileId": null
                    },
                    "chatMessageBox": {
                        "flag": false,
                        "currentUserId": null
                    }
                };
            },
            "getters": {
            },
            "mutations": {
                "SET_TOKEN": function (state, payload) {
                    state.token = payload;
                },
                "SET_USER": function (state, payload) {
                    state.user = payload;
                },
                "SET_ROLE_LIST": function (state, payload) {
                    state.roleList = payload;
                },
                "SET_MENU_LIST": function (state, payload) {
                    state.menuList = payload;
                },
                "SET_TREE_MENU_LIST": function (state, payload) {
                    state.treeMenuList = payload;
                },
                "SET_ALERT": function (state, payload) {
                    state.alert = payload;
                },
                "SET_CONFIRM": function (state, payload) {
                    state.confirm = payload;
                },
                "SET_LOADING": function (state, payload) {
                    state.loading.overlay = payload;
                },
                "SET_SIGN_UP_PHONENUM": function (state, val){
                    state.signUpPhoneNum = val;
                },
                "SET_ACCOUNT": function (state, val){
                    if(val) {
                        state.findAccount.phoneNum = val.phoneNum;
                        state.findAccount.username = val.username;
                    } else {
                        state.findAccount.phoneNum = null;
                        state.findAccount.username = null;
                    }
                },
                "SET_CHAT_PROFILE": function (state, val){
                    state.chatProfile = val;
                },
                "SET_CHAT_MESSAGEBOX": function (state, val){
                    state.chatMessageBox = val;
                },
            },
            "actions": {
            }
        }
    }
});