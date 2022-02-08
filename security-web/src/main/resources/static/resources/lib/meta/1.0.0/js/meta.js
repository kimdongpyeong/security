var meta;
meta = {
    version: "1.0.0",
    session: {
        timeout: 300
    },
    init: function () {
        axios.defaults.paramsSerializer = function (params) {
            return Qs.stringify(params, {"indices": false});
        }
        ELEMENT.locale(ELEMENT.lang.en);
    },
    alert: function (message) {
        return new Promise(function (resolve, reject) {
            store.commit("app/SET_ALERT", {
                value: true,
                message: message,
                callback: function () {
                    resolve();
                }
            });
        });
    },
    confirm: function (message, oktext = '예', canceltext = '아니오') {
        return new Promise(function (resolve, reject) {
            store.commit("app/SET_CONFIRM", {
                value: true,
                message: message,
                oktext : oktext,
                canceltext : canceltext,
                callback: function (result) {
                    resolve(result);
                }
            });
        });
    },
    auth: {
        login: async function (username, password, signUpWay) {
            var authorization,
                token;
            token = Basil.localStorage.get("token");
            if (token && await meta.auth.authenticated(token)) {
                authorization = "Bearer " + token;
            } else {
                authorization = (await axios({
                    url: "/api/login",
                    method: "post",
                    data: {
                        username: username,
                        password: password,
                        signUpWay: signUpWay
                    }
                })).headers.authorization;
                token = authorization.replace("Bearer ", "");
                Basil.localStorage.set("token", token);
            }
            axios.defaults.headers.Authorization = authorization;
        },
        logout: async function (token) {
            await axios({
                url: "/api/logout",
                method: "post",
                headers: {
                    Authorization: "Bearer " + token
                }
            });
            Basil.localStorage.remove("token");
            delete axios.defaults.headers.Authorization;
        },
        signUp: async function (data) {
             return await axios({
                url: "/api/app/accounts/sign-up",
                method: "post",
                data: data,
            });
        },
        idExists: async function (data) {
            return await axios({
                url: "/api/common/users/id-exists",
                method: "post",
                data: data,
            });
        },
        smsIdExists: async function (data) {
            return await axios({
                url: "/api/common/users/sms-id-exists",
                method: "post",
                data: data,
            });
        },
        authenticated: async function (token) {
            if (token) {
                try {
                    await axios({
                        url: "/api",
                        method: "get",
                        headers: {
                            Authorization: token
                        }
                    });
                    return true;
                } catch (e) {
                    return false;
                }
            } else {
                return false;
            }
        },
        authorize: async function (token) {
            var authentication, user;
            authentication = jwt_decode(token);
            store.commit("app/SET_TOKEN", token);
            store.commit("app/SET_USER", authentication.user);
            store.commit("app/SET_ROLE_LIST", authentication.roleList);

            const session = (await axios({"url": "/api/common/menus/session/" + store.state.app.user.id, "method": "get"})).data;
            store.commit("app/SET_MENU_LIST", session.menuList);
            store.commit("app/SET_TREE_MENU_LIST", session.treeMenuList);
            axios.defaults.headers.Authorization = "Bearer " + token;
        },
        unauthorize: function () {
            store.commit("app/SET_TOKEN", null);
            store.commit("app/SET_USER", null);
            store.commit("app/SET_ROLE_LIST", null);
            store.commit("app/SET_MENU_LIST", null);
            store.commit("app/SET_TREE_MENU_LIST", null);
            delete axios.defaults.headers.Authorization;
            Basil.localStorage.remove("token");
        },
        getToken: function () {
            return Basil.localStorage.get("token");
        },
        getKakaoURL: async function () {
            return await axios({
                url: "/api/common/login/kakao",
                method: "get"
            });
        },
        sendEmail: async function () {
            return await axios({
                url: "/api/common/email/sendMail",
                method: "post",
                data: data,
            });
        },
        emailAuth: async function(data) {
            return await axios({
                url: "/api/app/email/email-Auth",
                method: "post",
                data: data,
            });
        },
        emailcomparison: async function(data) {
            return await axios({
                url: "/api/app/email/email-comparison",
                method: "post",
                data: data,
            });
        },
        emailExists: async function (data) {
            return await axios({
                url: "/api/app/email/email-exists",
                method: "post",
                data: data,
            });
        },
    },
    api: {
        code: {
            getCodeList: function (type) { return axios({"url": "/api/codes/"+type, "method": "get"}); },
        },
        common: {
            api: {
                getApiList: function (params) { return axios({"url": "/api/common/apis", "method": "get", "params": params}); },
                getApi: function (id) { return axios({"url": "/api/common/apis/" + id, "method": "get"}); },
                createApiList: function (data) { return axios({"url": "/api/common/apis?bulk", "method": "post", "data": data}); },
                createApi: function (data) { return axios({"url": "/api/common/apis", "method": "post", "data": data}); },
                modifyApiList: function (data) { return axios({"url": "/api/common/apis", "method": "put", "data": data}); },
                modifyApi: function (id, data) { return axios({"url": "/api/common/apis/" + id, "method": "put", "data": data}); },
                removeApiList: function (data) { return axios({"url": "/api/common/apis", "method": "delete", "data": data}); },
                removeApi: function (id) { return axios({"url": "/api/common/apis/"+ id, "method": "delete"}); }
            },
            menu: {
                getMenuList: function (params) { return axios({"url": "/api/common/menus", "method": "get", "params": params}); },
                getMenu: function (id) { return axios({"url": "/api/common/menus/" + id, "method": "get"}); },
                createMenuList: function (data) { return axios({"url": "/api/common/menus?bulk", "method": "post", "data": data}); },
                createMenu: function (data) { return axios({"url": "/api/common/menus", "method": "post", "data": data}); },
                modifyMenuList: function (data) { return axios({"url": "/api/common/menus", "method": "put", "data": data}); },
                modifyMenu: function (id, data) { return axios({"url": "/api/common/menus/" + id, "method": "put", "data": data}); },
                removeMenuList: function (data) { return axios({"url": "/api/common/menus", "method": "delete", "data": data}); },
                removeMenu: function (id) { return axios({"url": "/api/common/menus/"+ id, "method": "delete"}); }
            },
            role: {
                getRoleList: function (params) { return axios({"url": "/api/common/roles", "method": "get", "params": params}); },
                getRole: function (id) { return axios({"url": "/api/common/roles/" + id, "method": "get"}); },
                createRoleList: function (data) { return axios({"url": "/api/common/roles?bulk", "method": "post", "data": data}); },
                createRole: function (data) { return axios({"url": "/api/common/roles", "method": "post", "data": data}); },
                modifyRoleList: function (data) { return axios({"url": "/api/common/roles", "method": "put", "data": data}); },
                modifyRole: function (id, data) { return axios({"url": "/api/common/roles/" + id, "method": "put", "data": data}); },
                removeRoleList: function (data) { return axios({"url": "/api/common/roles", "method": "delete", "data": data}); },
                removeRoleAllDependencyList: function (data) { return axios({"url": "/api/common/roles/delete-all-dependency", "method": "delete", "data": data}); },
                removeRole: function (id) { return axios({"url": "/api/common/roles/"+ id, "method": "delete"}); }
            },
            roleApi: {
                getRoleApiList: function (params) { return axios({"url": "/api/common/role-apis", "method": "get", "params": params}); },
                getRoleApi: function (roleId, apiId) { return axios({"url": "/api/common/role-apis/" + roleId + "," + apiId, "method": "get"}); },
                createRoleApiList: function (data) { return axios({"url": "/api/common/role-apis?bulk", "method": "post", "data": data}); },
                createRoleApi: function (data) { return axios({"url": "/api/common/role-apis", "method": "post", "data": data}); },
                modifyRoleApiList: function (data) { return axios({"url": "/api/common/role-apis", "method": "put", "data": data}); },
                modifyRoleApi: function (roleId, apiId, data) { return axios({"url": "/api/common/role-apis/" + roleId + "," + apiId, "method": "put", "data": data}); },
                removeRoleApiList: function (data) { return axios({"url": "/api/common/role-apis", "method": "delete", "data": data}); },
                removeRoleApi: function (roleId, apiId) { return axios({"url": "/api/common/role-apis/"+ roleId + "," + apiId, "method": "delete"}); }
            },
            roleMenu: {
                getRoleMenuList: function (params) { return axios({"url": "/api/common/role-menus", "method": "get", "params": params}); },
                getRoleMenu: function (roleId, menuId) { return axios({"url": "/api/common/role-menus/" + roleId + "," + menuId, "method": "get"}); },
                createRoleMenuList: function (data) { return axios({"url": "/api/common/role-menus?bulk", "method": "post", "data": data}); },
                createRoleMenu: function (data) { return axios({"url": "/api/common/role-menus", "method": "post", "data": data}); },
                modifyRoleMenuList: function (data) { return axios({"url": "/api/common/role-menus", "method": "put", "data": data}); },
                modifyRoleMenu: function (roleId, menuId, data) { return axios({"url": "/api/common/role-menus/" + roleId + "," + menuId, "method": "put", "data": data}); },
                removeRoleMenuList: function (data) { return axios({"url": "/api/common/role-menus", "method": "delete", "data": data}); },
                removeRoleMenu: function (roleId, menuId) { return axios({"url": "/api/common/role-menus/"+ roleId + "," + menuId, "method": "delete"}); }
            },
            roleUser: {
                getRoleUserList: function (params) { return axios({"url": "/api/common/role-users", "method": "get", "params": params}); },
                getRoleUser: function (roleId, userId) { return axios({"url": "/api/common/role-users/" + roleId + "," + userId, "method": "get"}); },
                createRoleUserList: function (data) { return axios({"url": "/api/common/role-users?bulk", "method": "post", "data": data}); },
                createRoleUser: function (data) { return axios({"url": "/api/common/role-users", "method": "post", "data": data}); },
                modifyRoleUserList: function (data) { return axios({"url": "/api/common/role-users", "method": "put", "data": data}); },
                modifyRoleUser: function (roleId, userId, data) { return axios({"url": "/api/common/role-users/" + roleId + "," + userId, "method": "put", "data": data}); },
                removeRoleUserList: function (data) { return axios({"url": "/api/common/role-users", "method": "delete", "data": data}); },
                removeRoleUser: function (roleId, userId) { return axios({"url": "/api/common/role-users/"+ roleId + "," + userId, "method": "delete"}); },
                updateRoleUser: function(data) {return axios({"url": "/api/common/role-users"+"/updateRoleUser", "method": "put"});  }
            },
            user: {
                getUserList: function (params) { return axios({"url": "/api/common/users", "method": "get", "params": params}); },
                getUser: function (id) { return axios({"url": "/api/common/users/" + id, "method": "get"}); },
                getReferralId: function (username) { return axios({"url": "/api/common/users/find/" + username, "method": "get"}); },
                createUserList: function (data) { return axios({"url": "/api/common/users?bulk", "method": "post", "data": data}); },
                createUser: function (data) { return axios({"url": "/api/common/users", "method": "post", "data": data}); },
                modifyUserList: function (data) { return axios({"url": "/api/common/users", "method": "put", "data": data}); },
                modifyUser: function (id, data) { return axios({"url": "/api/common/users/" + id, "method": "put", "data": data}); },
                removeUserList: function (data) { return axios({"url": "/api/common/users", "method": "delete", "data": data}); },
                removeUser: function (id) { return axios({"url": "/api/common/users/"+ id, "method": "delete"}); },
                modifyPassword: function (id, data) { return axios({"url": "/api/common/users/" + id + "/password", "method": "put", "data": data}); },
                downloadUserList: async function() {
                    let a, data, url;
                    data = (await axios({
                        "url": "/api/app/excels/userList-download.xlsx",
                        "responseType": "blob",
                        "method": "get",
                    })).data;
                    url = window.URL.createObjectURL(data);
                    a = document.createElement("a");
                    a.setAttribute("download", "사용자 리스트");
                    a.setAttribute("href", url);a.click();
                    window.URL.revokeObjectURL(url);
                },
            },
            code: {
                getCodeList: function (params) { return axios({"url": "/api/common/codes", "method": "get", "params": params}); },
                getCode: function (id) { return axios({"url": "/api/common/codes/" + id, "method": "get"}); },
                createCodeList: function (data) { return axios({"url": "/api/common/codes?bulk", "method": "post", "data": data}); },
                createCode: function (data) { return axios({"url": "/api/common/codes", "method": "post", "data": data}); },
                modifyCodeList: function (data) { return axios({"url": "/api/common/codes", "method": "put", "data": data}); },
                modifyCode: function (id, data) { return axios({"url": "/api/common/codes/" + id, "method": "put", "data": data}); },
                removeCodeList: function (data) { return axios({"url": "/api/common/codes", "method": "delete", "data": data}); },
                removeCode: function (id) { return axios({"url": "/api/common/codes/"+ id, "method": "delete"}); }
            },
            terms: {
                getTermsList: function (params) { return axios({"url": "/api/common/terms", "method": "get", "params": params}); },
                getTerms: function (id) { return axios({"url": "/api/common/terms/" + id, "method": "get"}); },
                getTerms: function (id) { return axios({"url": "/api/common/terms/" + id, "method": "get"}); },
                createTerms: function (data) { return axios({"url": "/api/common/terms", "method": "post", "data": data}); },
                modifyTerms: function (id, data) { return axios({"url": "/api/common/terms/" + id, "method": "put", "data": data}); },
                removeTermsList: function (data) { return axios({"url": "/api/common/terms/", "method": "delete", "data": data}); }
            },
            category: {
                getCategoryList: function (params) { return axios({"url": "/api/common/category", "method": "get", "params": params}); },
                getCategory: function (id) { return axios({"url": "/api/common/category/" + id, "method": "get"}); },
                createCategory: function (data) { return axios({"url": "/api/common/category", "method": "post", "data": data}); },
                modifyCategory: function (id, data) { return axios({"url": "/api/common/category/" + id, "method": "put", "data": data}); },
                removeCategoryList: function (data) { return axios({"url": "/api/common/category", "method": "delete", "data": data}); },
                removeCategory: function (id) { return axios({"url": "/api/common/category/"+ id, "method": "delete"}); }
            },
            myPage: {
                getUser: function (id) { return axios({"url": "/api/common/mypage/" + id, "method": "get"}); },
                modifyUserInfo: function (data) { return axios({"url": "/api/common/mypage/", "method": "put", "data": data}); },
                modifyUserIntroduce: function (data) { return axios({"url": "/api/common/mypage/introduce", "method": "put", "data": data}); },
                modifyProfile: function (data) { return axios({"url": "/api/common/mypage/profile", "method": "put", "data": data}); },
                removeUser: function (id) { return axios({"url": "/api/common/mypage/"+ id, "method": "put"}); },
                modifyPassword: function (id, data) { return axios({"url": "/api/common/mypage/" + id + "/password", "method": "put", "data": data}); },
            },
            lecturerData:{
                getLecturerData: function (id) { return axios({"url": "/api/common/mypage/lecturer-data/" + id, "method": "get"}); },
                getLecturerDataList: function (params) { return axios({"url": "/api/common/mypage/lecturer-data", "method": "get", "params": params}); },
                getLecturerDataFileList: function (params) { return axios({"url": "/api/common/mypage/lecturer-data-file", "method": "get", "params": params}); },
                sortLecturerData: function (data) { return axios({"url": "/api/common/mypage/lecturer-data-sort", "method": "post", "data": data}); },
                createLecturerData: function (data) { return axios({"url": "/api/common/mypage/lecturer-data", "method": "post", "data": data}); },
                modifyLecturerData: function (data) { return axios({"url": "/api/common/lecturer-data", "method": "put", "data": data}); },
            },
            find : {
                findUser: function (username) { return axios({"url": "/api/common/find/" + username, "method": "get"}); },
                modifyPassword: function (id, data) { return axios({"url": "/api/common/find/" + id + "/password", "method": "put", "data": data}); },
            },
            betaLive : {
                getBetaLiveList: function (params) { return axios({"url": "/api/common/beta-live", "method": "get", "params": params}); },
                getBetaLive: function (id) { return axios({"url": "/api/common/beta-live/" + id, "method": "get"}); },
                createBetaLive: function (data) { return axios({"url": "/api/common/beta-live", "method": "post", "data": data}); },
                modifyBetaLive: function (data) { return axios({"url": "/api/common/beta-live", "method": "put", "data": data}); },
            },
            message : {
                getMessageList : function (params) { return axios({"url": "/api/common/messages", "method": "get", "params": params}); },
                getMessage: function (id) { return axios({"url": "/api/common/messages/" + id, "method": "get"}); },
                createMessage: function(data) { return axios({"url": "/api/common/messages", "method": "post", "data": data}); },
                modifyMessage: function(data) { return axios({"url": "/api/common/messages", "method": "put", "data": data, "contentType": false, "processType": false,}); },
            },
            room: {
                getRoomList: function (params) { return axios({"url": "/api/common/room", "method": "get", "params": params}); },
                getRoom: function (id) { return axios({"url": "/api/common/room/" + id, "method": "get"}); },
                createRoom: function (data) { return axios({"url": "/api/common/room", "method": "post", "data": data}); },
                modifyRoom: function ( data) { return axios({"url": "/api/common/room", "method": "put", "data": data}); },
            },
            personalRoom: {
                getPersonalRoomList: function (params) { return axios({"url": "/api/common/personal-room", "method": "get", "params": params}); },
                getPersonalRoom: function (id) { return axios({"url": "/api/common/personal-room/" + id, "method": "get"}); },
                createPersonalRoom: function (data) { return axios({"url": "/api/common/personal-room", "method": "post", "data": data}); },
                modifyPersonalRoom: function (id, data) { return axios({"url": "/api/common/personal-room/" + id, "method": "put", "data": data}); },
                updatePersonalRoom: function (data) { return axios({"url": "/api/common/personal-room", "method": "put", "data": data}); },
            },
            roomUser: {
                getRoomUserList: function (params) { return axios({"url": "/api/common/room-user", "method": "get", "params": params}); },
                createRoomUser: function (data) { return axios({"url": "/api/common/room-user", "method": "post", "data": data}); },
                removeRoomUser: function (data) { return axios({"url": "/api/common/room-user/", "method": "delete", "data": data}); },
            },
            liveCat:{
                getLiveCatList: function (params) { return axios({"url": "/api/common/live-cat", "method": "get", "params": params}); },
                getLiveCat: function (id) { return axios({"url": "/api/common/live-cat/" + id, "method": "get"}); },
                createLiveCat: function (data) { return axios({"url": "/api/common/live-cat", "method": "post", "data": data}); },
                modifyLiveCat: function (data) { return axios({"url": "/api/common/live-cat", "method": "put", "data": data}); },
                removeLiveCat: function (id) { return axios({"url": "/api/common/live-cat/"+ id, "method": "delete"}); },
            },
            introVideo : {
                getCreatorIntroVideoList: function (params) { return axios({"url": "/api/common/intro-video", "method": "get", "params": params}); },
                getCreatorIntroVideo: function (id) { return axios({"url": "/api/common/intro-video/" + id, "method": "get"}); },
                createCreatorIntroVideo: function (data) { return axios({"url": "/api/common/intro-video", "method": "post", "data": data}); },
                modifyCreatorIntroVideo: function (data) { return axios({"url": "/api/common/intro-video", "method": "put", "data": data}); },
                removeCreatorIntroVideo: function (id) { return axios({"url": "/api/common/intro-video/"+ id, "method": "delete"}); },
            },
            liveStreaming: {
                getLiveStreamingList: function (params) { return axios({"url": "/api/common/live-streaming", "method": "get", "params": params}); },
                getLiveStreaming: function (id) { return axios({"url": "/api/common/live-streaming/" + id, "method": "get"}); },
                createLiveStreaming: function (data) { return axios({"url": "/api/common/live-streaming", "method": "post", "data": data}); },
                modifyLiveStreaming: function (data) { return axios({"url": "/api/common/live-streaming", "method": "put", "data": data}); },
                removeLiveStreaming: function (id) { return axios({"url": "/api/common/live-streaming/"+ id, "method": "delete"}); }
            },
            chatNotice : {
                getChatNoticeList : function (params) { return axios({"url": "/api/common/chat-notice", "method": "get", "params": params}); },
                createChatNotice : function (data) { return axios({"url": "/api/common/chat-notice", "method": "post", "data": data}); },
                modifyChatNotice : function (data) { return axios({"url": "/api/common/chat-notice", "method": "put", "data": data}); },
                removeChatNotice : function (data) { return axios({"url": "/api/common/chat-notice/", "method": "delete", "data": data}); },
            },
            personalChatNotice : {
                getChatNoticeList : function (params) { return axios({"url": "/api/common/personal-chat-notice", "method": "get", "params": params}); },
                createChatNotice : function (data) { return axios({"url": "/api/common/personal-chat-notice", "method": "post", "data": data}); },
                modifyChatNotice : function (data) { return axios({"url": "/api/common/personal-chat-notice", "method": "put", "data": data}); },
                removeChatNotice : function (data) { return axios({"url": "/api/common/personal-chat-notice/", "method": "delete", "data": data}); },
            },
            drink : {
                getDrinkList : function (params) { return axios({"url": "/api/common/drink", "method": "get", "params": params}); },
                getDrink: function (id) { return axios({"url": "/api/common/drink/" + id, "method": "get"}); },
            },
            buyDrink : {
                getBuyDrinkList : function (params) { return axios({"url": "/api/common/buy-drink", "method": "get", "params": params}); },
                getBuyDrink: function (id) { return axios({"url": "/api/common/buy-drink/" + id, "method": "get"}); },
                createBuyDrink : function (data) { return axios({"url": "/api/common/buy-drink", "method": "post", "data": data}); },
                modifyBuyDrink : function (data) { return axios({"url": "/api/common/buy-drink", "method": "put", "data": data}); },
            },
            news : {
                getNewsList : function (params) { return axios({"url": "/api/common/news", "method": "get", "params": params}); },
            },
            classRoom: {
                getClassRoomList: function (params) { return axios({"url": "/api/common/classroom", "method": "get", "params": params}); },
                getClassRoom: function (id) { return axios({"url": "/api/common/classroom/" + id, "method": "get"}); },
                createClassRoom: function (data) { return axios({"url": "/api/common/classroom", "method": "post", "data": data}); },
                modifyClassRoom: function (data) { return axios({"url": "/api/common/classroom", "method": "put", "data": data}); },
                removeClassRoom: function (data) { return axios({"url": "/api/common/classroom", "method": "delete", "data": data}); },
            },
            classRoomUser: {
                getClassRoomUserList: function (params) { return axios({"url": "/api/common/classroom-user", "method": "get", "params": params}); },
                getClassRoomUser: function (params) { return axios({"url": "/api/common/classroom-user/", "method": "get", "params": params}); },
                createClassRoomUser: function (data) { return axios({"url": "/api/common/classroom-user", "method": "post", "data": data}); },
                removeClassRoomUser: function (data) { return axios({"url": "/api/common/classroom-user/", "method": "delete", "data": data}); },
            },
            classRoomLiveTime:{
                getClassRoomLiveTimeList: function (params) { return axios({"url": "/api/common/liveTime", "method": "get", "params": params}); },
                getClassRoomIdLiveTime: function (id) { return axios({"url": "/api/common/liveTime/classroom/" + id, "method": "get"}); },
                createClassRoomLiveTime: function (data) { return axios({"url": "/api/common/", "method": "post", "data": data}); },
                modifyClassRoomLiveTime: function (data) { return axios({"url": "/api/common/liveTime", "method": "put", "data": data}); },
                removeClassRoomLiveTime: function (id) { return axios({"url": "/api/common/liveTime/"+ id, "method": "delete"}); }
            },
            subject: {
                getSubjectList: function (params) { return axios({"url": "/api/common/subject", "method": "get", "params": params}); },
                getSubject: function (id) { return axios({"url": "/api/common/subject/" + id, "method": "get"}); },
                createSubject: function (data) { return axios({"url": "/api/common/subject", "method": "post", "data": data}); },
                modifySubject: function (id, data) { return axios({"url": "/api/common/subject/" + id, "method": "put", "data": data}); },
                removeSubjectList: function (data) { return axios({"url": "/api/common/subject", "method": "delete", "data": data}); },
                removeSubject: function (id) { return axios({"url": "/api/common/subject/"+ id, "method": "delete"}); }
            },
            classRoomChatNotice : {
                getClassRoomChatNoticeList : function (params) { return axios({"url": "/api/common/classroom-chat-notice", "method": "get", "params": params}); },
                createClassRoomChatNotice : function (data) { return axios({"url": "/api/common/classroom-chat-notice", "method": "post", "data": data}); },
                modifyClassRoomChatNotice : function (data) { return axios({"url": "/api/common/classroom-chat-notice", "method": "put", "data": data}); },
                removeClassRoomChatNotice : function (data) { return axios({"url": "/api/common/classroom-chat-notice/", "method": "delete", "data": data}); },
            },
            enterUser: {
                getEnterUserList: function (params) { return axios({"url": "/api/common/enter-users", "method": "get", "params": params}); },
                getEnterUser: function (id) { return axios({"url": "/api/common/enter-users/" + id, "method": "get"}); },
                createEnterUser: function (data) { return axios({"url": "/api/common/enter-users", "method": "post", "data": data}); },
                modifyEnterUser: function (id, data) { return axios({"url": "/api/common/enter-users/" + id, "method": "put", "data": data}); },
                getSession: function() { return axios({"url": "/api/common/enter-users/session", "method": "get", }); },
                guestIDExists: async function (data) { return await axios({ url: "/api/common/enter-users/id-exists", method: "post", data: data, }); },
            },
            demo: {
                getDemoList: function (params) { return axios({"url": "/api/common/demo", "method": "get", "params": params}); },
                getDemo: function (id) { return axios({"url": "/api/common/demo/" + id, "method": "get"}); },
                createDemo: function (data) { return axios({"url": "/api/common/demo", "method": "post", "data": data}); },
                modifyDemo: function (data) { return axios({"url": "/api/common/demo", "method": "put", "data": data}); },
                removeDemo: function (id) { return axios({"url": "/api/common/demo/"+ id, "method": "delete"}); }
            },
            notice: {
                getNoticeList: function (params) { return axios({"url": "/api/common/boardTest", "method": "get", "params": params}); },
                getNotice: function (params) { return axios({"url": "/api/common/boardTest/detail", "method": "get", "params": params}); },
            },
            lecturerBoard: {
                getLecturerBoardList: function (params) { return axios({"url": "/api/common/mypage/lecturer/board/list", "method": "get", "params": params}); },
                getLecturerBoard: function (params) { return axios({"url": "/api/common/mypage/lecturer/board", "method": "get", "params": params}); },
            },
            userPeristalsis: {
                createUser: function (data) { return axios({"url": "/api/common/usersPeristalsis", "method": "post", "data": data}); },
                getUser: function (userId) { return axios({"url": "/api/common/usersPeristalsis/" + userId, "method": "get"}); },
            },
            contactUs: {
                getContactUsList: function (params) { return axios({"url": "/api/common/contact-us", "method": "get", "params": params}); },
                getContactUs: function (id) { return axios({"url": "/api/common/contact-us/" + id, "method": "get"}); },
                createContactUs: function (data) { return axios({"url": "/api/common/contact-us", "method": "post", "data": data}); },
                modifyContactUs: function (data) { return axios({"url": "/api/common/contact-us", "method": "put", "data": data}); },
                lastUpdated: function (data) { return axios({"url": "/api/common/contact-us/last", "method": "put", "data": data}); },
            },
            salesHistory: {
                getSalesHistoryList: function (params) { return axios({"url": "/api/common/sales", "method": "get", "params": params}); },
                getSalesTotalList: function (params) { return axios({"url": "/api/common/sales/total", "method": "get", "params": params}); },
                getSalesTermTotalList: function (params) { return axios({"url": "/api/common/sales/term-total", "method": "get", "params": params}); },
                getSalesHistory: function (id) { return axios({"url": "/api/common/sales/" + id, "method": "get"}); },
                createSalesHistory: function (data) { return axios({"url": "/api/common/sales", "method": "post", "data": data}); },
                downloadSalesList: async function(params) {
                    let a, data, url;
                    data = (await axios({
                        "url": "/api/app/excels/salesList-download.xlsx",
                        "responseType": "blob",
                        "method": "get",
                        "params": params
                    })).data;
                    url = window.URL.createObjectURL(data);
                    a = document.createElement("a");
                    a.setAttribute("download", "매출 내역");
                    a.setAttribute("href", url);a.click();
                    window.URL.revokeObjectURL(url);
                },
            },
            paymentHistory: {
                getPaymentHistoryList: function (params) { return axios({"url": "/api/common/payment", "method": "get", "params": params}); },
                getPaymentHistory: function (id) { return axios({"url": "/api/common/payment/" + id, "method": "get"}); },
                getPaymentHistoryByOrderId: function (orderId) { return axios({"url": "/api/common/payment/order/" + orderId, "method": "get"}); },
                createPayment: function (data) { return axios({"url": "/api/common/payment", "method": "post", "data": data}); },
            },
            student: {
                createStudent: function (data) { return axios({"url": "/api/common/student", "method": "post", "data": data}); },
                createStudentGrade: function (data) { return axios({"url": "/api/common/student/detail", "method": "post", "data": data}); },
                createStudentDay: function (data) { return axios({"url": "/api/common/student/", "method": "post", "data": data}); },
                getStudentList: function (params) { return axios({"url": "/api/common/student/List", "method": "get", "params": params}); },
                getStudent: function (id) { return axios({"url": "/api/common/student/" + id, "method": "get"}); },
                getStudentWeek: function (id) { return axios({"url": "/api/common/student/detail/" + id, "method": "get"}); },
                getStudentGradeList: function (params) { return axios({"url": "/api/common/student/detail/grade", "method": "get", "params": params}); },
                getLecturer: function (params) { return axios({"url": "/api/common/student", "method": "get", "params": params}); },
                modifyLinkYn: function (id, data) { return axios({"url": "/api/common/student/" + id, "method": "put", "data": data}); },
                loadStudent: function (data) { return axios({"url": "/api/common/student/loadStudent", "method": "post", "data": data}); },
                modifyStudent: function (id, data) { return axios({"url": "/api/common/student/detail/modify/" + id, "method": "put", "data": data}); },
                modifyStudentWeek: function (data) { return axios({"url": "/api/common/student/detail/modify-week", "method": "put", "data": data}); },
            },
            parents: {
                createParents: function (data) { return axios({"url": "/api/common/parents", "method": "post", "data": data}); },
                getParentsList: function (params) { return axios({"url": "/api/common/parents/List", "method": "get", "params": params}); },
                getParents: function (id) { return axios({"url": "/api/common/parents/" + id, "method": "get"}); },
            },
            lecturerUploadFile:{
                getLecturerUpload: function (id) { return axios({"url": "/api/common/student/detail/upload/" + id, "method": "get"}); },
                getLecturerUploadList: function (params) { return axios({"url": "/api/common/student/detail/upload", "method": "get", "params": params}); },
                getLecturerUploadFileList: function (params) { return axios({"url": "/api/common/student/detail/upload-data-file", "method": "get", "params": params}); },
                sortLecturerUpload: function (data) { return axios({"url": "/api/common/student/detail/upload-data-sort", "method": "post", "data": data}); },
                createLecturerUpload: function (data) { return axios({"url": "/api/common/student/detail/upload-data", "method": "post", "data": data}); },
                modifyLecturerUpload: function (data) { return axios({"url": "/api/common/student/detail/upload-data", "method": "put", "data": data}); },
                modifyLecturerUploadYn: function (id, data) { return axios({"url": "/api/common/student/detail/" + id, "method": "put", "data": data}); },
                removeLecturerUpload: function (id) { return axios({"url": "/api/common/student/detail/"+ id, "method": "delete"}); },
                downloadFile: function (params) { return axios({"url": "/api/common/student/detail/download", "method": "get", "params": params}); },
            },
            calculate: {
                getCalculateList: function (params) { return axios({"url": "/api/common/calculate", "method": "get", "params": params}); },
                downloadCalculateList: async function(params) {
                    let a, data, url;
                    data = (await axios({
                        "url": "/api/app/excels/calculateList-download.xlsx",
                        "responseType": "blob",
                        "method": "get",
                        "params": params
                    })).data;
                    url = window.URL.createObjectURL(data);
                    a = document.createElement("a");
                    a.setAttribute("download", "정산 내역");
                    a.setAttribute("href", url);a.click();
                    window.URL.revokeObjectURL(url);
                },
            },
            environment: {
                getEnvironmentVariableList: function (params) { return axios({"url": "/api/common/environment", "method": "get", "params": params}); },
                getEnvironmentVariable: function (id) { return axios({"url": "/api/common/environment/" + id, "method": "get"}); },
                getEnvironmentVariableKey: function (key) { return axios({"url": "/api/common/environment/key/" + key, "method": "get"}); },
            },
        },
        app: {
            account: {
                createAccount: function (data) { return axios({"url": "/api/app/accounts", "method": "post", "data": data}); },
                modifyAccount: function (data) { return axios({"url": "/api/app/accounts", "method": "put", "data": data}); },
                removeAccount: function (data) { return axios({"url": "/api/app/accounts", "method": "delete", "data": data}); },
            },
            RoleAndRoleMenuAndRoleApi: {
                createRoleAndRoleMenuAndRoleApi: function (data) { return axios({"url": "/api/app/role-menu-apis", "method": "post", "data": data}); },
                modifyRoleAndRoleMenuAndRoleApi: function (data) { return axios({"url": "/api/app/role-menu-apis", "method": "put", "data": data}); },
            },
            dataUpload: {
                getFile: async function (params) {
                    let a, data, url;
                    data = (await  axios({
                        "url": "/api/app/data-uploads/file/" + params.subPath + "/" + params.filename,
                        "responseType": "blob",
                        "method": "get"
                    })).data;
                    url = window.URL.createObjectURL(data);
                    a = document.createElement("a");
                    a.setAttribute("href", url);
                    a.setAttribute("download", params.originFilename);
                    a.click();
                    window.URL.revokeObjectURL(url);
                },
                getZipFile: async function (params) {
                    let a, data, url;
                    data = (await  axios({
                        "url": "/api/app/data-uploads/zip/" + params.subPath+ "/" + params.classId,
                        "responseType": "blob",
                        "method": "get",
                        "params": params
                    })).data;
                    url = window.URL.createObjectURL(data);
                    a = document.createElement("a");
                    a.setAttribute("download", params.zipname);
                    a.setAttribute("href", url);
                    a.click();
                    window.URL.revokeObjectURL(url);
                }
            },
            nice: {
                signUpCheck: function (data) { return axios({"url": "/api/app/nices/sign-up/check-plus", "method": "post", "data": data}); },
                findAccountCheck: function (data) { return axios({"url": "/api/app/nices/find-account/check-plus", "method": "post", "data": data}); },
            },
        },
        util: {
            menu: {
                getTreeMenuList: function (params) { return axios({"url": "/api/util/menus/tree-menus", "method": "get", "params": params}); },
                getDefaultMenuList: function () { return axios({"url": "/api/menu", "method": "get"}); }
            },
            code: {
                getTreeCodeList: function (params) { return axios({"url": "/api/util/codes/tree-codes", "method": "get", "params": params}); }
            },
            category: {
                getTreeCategoryList: function (params) { return axios({"url": "/api/util/categories/tree-categories", "method": "get", "params": params}); }
            }
        }
    },
    util: {
        sort: function (sortBy, sortDesc) {
            let str;
            str = [];
            if(sortBy.length > 0 && sortDesc.length > 0) {
                for (var i = 0;  i < sortBy.length; i++) {
                    str.push(sortBy[i] + "," + (sortDesc[i] ? "desc" : "asc"));
                }
            }
            return str;
        },
        "graphColor": function () {
            return [
                "rgb(255,167,167)", "rgb(255,193,158)", "rgb(255,224,140)", "rgb(206,242,121)", "rgb(183,240,177)",
                "rgb(178,235,244)", "rgb(178,204,255)", "rgb(181,178,255)", "rgb(209,178,255)", "rgb(255,178,245)",
                "rgb(255,178,217)", "rgb(241,95,95)", "rgb(242,150,97)", "rgb(242,203,97)", "rgb(229,216,92)",
                "rgb(188,229,92)", "rgb(134,229,127)", "rgb(92,209,229)", "rgb(103,153,255)", "rgb(107,102,255)",
                "rgb(165,102,255)", "rgb(243,97,220)", "rgb(243,97,166)"
            ];
        },
        "graphColorOpacity": function () {
            return [
                "rgba(255,167,167,0.5)", "rgba(255,193,158,0.5)", "rgba(255,224,140,0.5)", "rgba(206,242,121,0.5)", "rgba(183,240,177,0.5)",
                "rgba(178,235,244,0.5)", "rgba(178,204,255,0.5)", "rgba(181,178,255,0.5)", "rgba(209,178,255,0.5)", "rgba(255,178,245,0.5)",
                "rgba(255,178,217,0.5)", "rgba(241,95,95,0.5)", "rgba(242,150,97,0.5)", "rgba(242,203,97,0.5)", "rgba(229,216,92,0.5)",
                "rgba(188,229,92,0.5)", "rgba(134,229,127,0.5)", "rgba(92,209,229,0.5)", "rgba(103,153,255,0.5)", "rgba(107,102,255,0.5)",
                "rgba(165,102,255,0.5)", "rgba(243,97,220,0.5)", "rgba(243,97,166,0.5)"
            ];
        }
    }
};
meta.init();