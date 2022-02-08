var MypagePersonalChatMainPage;
MypagePersonalChatMainPage = Vue.component("mypage-personal-chat-main-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/personal-chat/main.html")).data,
        "props": {
            roomId: {
                type: Number,
                default: 0
            }
        },
        "data": function() {
            return {
                "today": "",
                "time": "",
                "codeCk":null,
                "secretRoom":{},
                "newRoom": {
                    "id": null,
                    "title": null,
                    "maxMember": null,
                    "deleteYn": "N",
                    "createdBy": null,
                    "codeYn": "N",
                    "code": null
                },
                "room": {},
                "select": {
                    "roomId": null,
//					"roomName": null,
                    "userId": null,
                    "userNickname": null,
                    "users":[],
                    "createdRoomList": [],
                    "selectedRoom": {},
                },
                "data": {
                    "userList": [],
                    "allRoomUserList": [],
                    "user": {},
                },
                "visible":{
                    "roomListFormVisible": false,
                    "roomUserFormVisible": false,
                    "addRoomVisible": false,
                    "enterCodeVisible":false
                },
                "dataTable": {
                    "headers": [
                        {"text": "유저 아이디", "sortable": false, "value": "_id"},
                        {"text": "유저 이름", "sortable": false, "value": "username"},
                    ],
                    "items": [],
                    "serverItemsLength": 0,
                    "page": 1,
                    "itemsPerPage": 10
                },
                "rooms": [],
                "allRooms":[],
                "currentUserId": "",
                "currentUserNm": "",
                "roomBossId":"",
                "messages": [],
                "files":null,
                messagesLoaded: true,
                roomsLoaded: true,
                disableForm: false,
                invitedUsername: '',
                inviteRoomId: null,
                menuActions: [
                    { name: 'leaveChat', title: 'Leave Chat' },
                ],
                "noticeFlag": false,
                "noticeBoxFlag":true,
                "noticeContent":"",
                "noticeList":[],
                "nowNotice":{},
                search: '',
                "other":{
                    "outYn":"",
                    "createdUser":"",
                    "invitedUser":""
                }
            }
        },
        "computed": {
        },
        "watch": {
            "$store.state.app.chatMessageBox": async function(val){
                if(!this.$cookies.get("messageAlert")){
                    await meta.alert("자유로운 소통은 환영, 상처되는 말은 No");
                    this.$cookies.set("messageAlert", this.currentUserId);
                } else{
                    if(this.$cookies.get("messageAlert") != this.currentUserId){
                        await meta.alert("자유로운 소통은 환영, 상처되는 말은 No");
                        this.$cookies.set("messageAlert", this.currentUserId);
                    }
                }
            }
        },
        "methods": {
            handleClick(tab, event) {
                console.log(tab, event);
            },
            "init": async function() {
                document.querySelector('vue-advanced-chat').currentUserId = this.currentUserId
                document.querySelector('vue-advanced-chat').rooms = this.rooms
                document.querySelector('vue-advanced-chat').messages = this.messages

                await this.loadRoomList();
                await this.setCreatedRoomList();
                await this.loadUserList();
            },
            //공지사항 닫기
            "closeNotice": function(){
                this.noticeBoxFlag = false;
            },
            //공지사항 열기
            "openNotice": function(){
                this.noticeBoxFlag = true;
            },
            //공지사항 삭제
            "removeNotice": async function(e){
                if (await meta.confirm("공지사항을 삭제하시겠습니까?")) {
                    (await meta.api.common.personalChatNotice.removeChatNotice({
                        "roomId": e.roomId,
                        "messageId": e.messageId
                    })).data;
                    this.$message({
                      message: '공지사항이 삭제되었습니다.',
                      type: 'error'
                    });
                    this.loadNotice(e.classroomId);
                }
            },
            "fetchMessages": async function(e) {
                let query = null,
                    room = e.detail[0].room.users,
                    roomId = e.detail[0].room.id,
                    enterDate = null;

                room.forEach(x => {
                    if(x._id === this.currentUserId){
                        if(x.createdEnterDate){
                            enterDate = x.createdEnterDate;
                        } else {
                            enterDate = x.invitedEnterDate;
                        }
                    }
                });

                query = personalMessageRef.orderByChild('roomId').equalTo(roomId);

                this.messages = [];

                await query.on('value', messages => {
                    this.messages.length = 0;

                    messages.forEach(message => {

                        if(message.val().createdDate != null && moment(message.val().createdDate).format("YYYY-MM-DD HH:mm:ss") >= enterDate) {

                            let temp = {
                                senderId: message.val().senderId,
                                _id: message.key,
                                roomId: message.val().roomId,
                                date: message.val().date,
                                timestamp: message.val().timestamp,
                                username: message.val().senderNm,
                                content: message.val().content,
                                edited: message.val().edited,
                                deleted: message.val().deleted,
                                seen: true
                            }
                            if(message.val().replyMessage){
                                temp.replyMessage = message.val().replyMessage;
                                if(message.val().replyMessage.files){
                                    temp.replyMessage.file = message.val().replyMessage.files;
                                }
                            }
                            if(message.val().files){
                                temp.file = message.val().files;
                            }
                            this.messages.push(temp);
                        } else{
                            let enterDay = moment(enterDate).format('yyyy년 MM월 DD일'),
                                enterTime = moment(enterDate).format('HH:mm');

                            let noMessage = {
                                date: enterDay,
                                timestamp: enterTime,
                            }
                            this.messages.push(noMessage);
                        }
                    });
                });
                document.querySelector('vue-advanced-chat').messages = this.messages
                await this.setSelect(e);
            },
            // 방 클릭시 this.select 설정
            "setSelect": function(e){
                this.room = e.detail[0].room;
                this.loadNotice(this.room.id);
                if(this.currentUserId === this.room.createdUser){
                    this.other.outYn = this.room.invitedOutYn;
                    this.other.invitedUser = this.room.invitedUser;
                } else{
                    this.other.outYn = this.room.createdOutYn;
                    this.other.createdUser = this.room.createdUser;
                }
            },
            //공지사항 불러오기
            "loadNotice": async function(roomId){
                let noticeList = (await meta.api.common.personalChatNotice.getChatNoticeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roomId": roomId
                })).data.items;
                this.noticeList = noticeList;

                if(this.noticeList.length > 0){
                    let messageId = this.noticeList[0].messageId
                    let query = personalMessageRef.orderByChild('_id').equalTo(messageId);
                    await query.on('value', messages => {
                        messages.forEach(message => {
                            this.noticeContent = message.val().content;
                            this.$set(this.nowNotice,"roomId", message.val().roomId);
                            this.$set(this.nowNotice,"messageId", message.val()._id);
                        });
                    });
                }
                this.noticeBoxFlag = true;
            },
            "invitedOther": async function(roomId){
                let param = {};

                param.id = roomId;
                if(this.other.createdUser){
                    param.createdOutYn = "N";
                } else{
                    param.invitedOutYn = "N";
                }
                await meta.api.common.personalRoom.updatePersonalRoom(param);
                this.other.outYn = "N";
            },
            //메세지 보내기
            "sendMessage": async function(e) {
                let roomId = e.detail[0].roomId,
                    param={};
                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');
                let files = e.detail[0].file,
                    replyMessage = e.detail[0].replyMessage;

                if(this.other.outYn === "Y"){
                    await this.invitedOther(roomId);
                }

                var newPostKey = personalMessageRef.push().key;

                const message = {
                    _id: newPostKey,
                    roomId: roomId,
                    content: e.detail[0].content,
                    senderId: this.currentUserId,
                    senderNm: this.currentUserNm,
                    date: this.today,
                    timestamp: this.time,
                    edited : false,
                    deleted: false,
                    createdDate: new Date()
                };
                if (replyMessage) {
                    message.replyMessage = {
                        _id: replyMessage._id,
                        content: replyMessage.content,
                        senderId: replyMessage.senderId,
                        username: replyMessage.username
                    }
                    if (replyMessage.file) {
                        message.replyMessage.files = replyMessage.file
                    }
                }
                if (files) {
                    message.files = this.formattedFiles(files)
                    await this.uploadFile({ file: files, messageId: newPostKey, roomId })
                }
                if(this.currentUserId){
                    var updates = {};
                    updates[newPostKey] = message;
                    await personalMessageRef.update(updates);
                } else{
                    await meta.alert("로그인 후 채팅 가능합니다");
                }
            },
            //메세지 수정
            "editMessage": async function(e){
                let edit = e.detail[0], files;

                const newMessage = {
                        content: edit.newContent,
                        edited: true,
                        editDate:new Date()
                    };
                if (edit.file !== null && edit.file !== undefined && edit.file !=="") {
                    newMessage.files = this.formattedFiles(edit.file);
                    await this.uploadFile({ file: edit.file, messageId: edit.messageId, roomId: edit.roomId })
                } else {
                    newMessage.files = null;
                }

                await database.ref("personal_messages/"+edit.messageId).update(newMessage);
            },
            //메세지 공지사항등록
            "noticeMessage": async function(e){
                let roomId = e.detail[0].roomId,
                    messageId = e.detail[0].message._id;
                if (await meta.confirm("채팅방 공지는 1건만 노출가능합니다.\n공지사항 등록하시겠습니까?")) {
                    if(this.noticeList.length > 0){
                        (await meta.api.common.personalChatNotice.removeChatNotice({
                            "roomId": this.nowNotice.roomId,
                            "messageId": this.nowNotice.messageId
                        })).data;
                        (await meta.api.common.personalChatNotice.createChatNotice({
                            "roomId": roomId,
                            "messageId": messageId
                        })).data;
                    } else{
                        (await meta.api.common.personalChatNotice.createChatNotice({
                            "roomId": roomId,
                            "messageId": messageId
                        })).data;
                    }

                    this.$message({
                      message: '공지사항이 등록되었습니다.',
                      type: 'success'
                    });
                    this.loadNotice(roomId);
                }
            },
            //메세지 삭제
            "deleteMessage": async function(e){
                let del = e.detail[0];

                await database.ref("personal_messages/"+del.message._id).update({
                    deleted: true,
                    deleteDate:new Date()
                });
                //삭제하는 메세지에 파일이 있다면 db에 저장된 파일 삭제하도록
                if (del.message.files) {
                    this.deleteFile({messageId: del.message._id, file: del.message.files});
                }

            },
            "formattedFiles": function(files) {
                const messageFile = {
                    blob:{ size: files.size, type: files.type},
                    name: files.name,
                    size: files.size,
                    type: files.type,
                    extension: files.extension || files.type,
                    url: files.url || files.localUrl
                }
                return messageFile
            },
            //파일 db저장
            "uploadFile": async function({file, messageId, roomId}) {
                let type = file.extension || file.type
                if (type === 'svg' || type === 'pdf') {
                    type = file.type
                }

                const uploadFileRef = filesRef
                    .child(String(this.currentUserId))
                    .child(messageId)
                    .child(`${file.name}.${type}`)
                await uploadFileRef.put(file.blob, { contentType: type })

                const url = await uploadFileRef.getDownloadURL()

                const messageDoc = database.ref("personal_messages/"+messageId);
                messageDoc.on('value', (snapshot) => {
                  const data = snapshot.val();
                  const files = data.files;
                  if (files.url === file.localUrl) {
                      files.url = url;
                  }
                  this.$set(this,"files",files);
                  this.updateFile(messageId);
                });
            },
            //파일db저장후 메세지데이터 내 url변경
            "updateFile": async function(messageId) {
                await database.ref("personal_messages/"+messageId).update({files: this.files});
            },
            //파일삭제
            "deleteFile": async function({messageId, file}){
                const deleteFileRef = filesRef
                    .child(String(this.currentUserId))
                    .child(messageId)
                    .child(`${file.name}.${file.extension || file.type}`)
                await deleteFileRef.delete()
            },
            //파일크게보기
            "openFile": function(e) {
                let file = e.detail[0].message.file,
                    action = e.detail[0].action,
                    messageId = e.detail[0].message._id,
                    senderId = e.detail[0].message.senderId;

                const downloadRef = filesRef
                .child(String(senderId))
                .child(messageId)
                .child(`${file.name}.${file.extension || file.type}`);

                switch (action){
                    case 'preview':
                        window.open(file.url, '_blank');
                        break;
                    case 'download':
                        downloadRef.getDownloadURL().then(function(url) {
                            var xhr = new XMLHttpRequest();
                            xhr.responseType = 'blob';
                            xhr.onload = function(event) {
                              var blob = xhr.response;
                              var a = document.createElement('a');
                              a.href = window.URL.createObjectURL(blob);
                              a.download = `${file.name}.${file.extension || file.type}`;
                              a.style.display = 'none';
                              document.body.appendChild(a);
                              a.click();
                              delete a;
                            };
                            xhr.open('GET', url);
                            xhr.send();
                          }).catch(function(error) {
                          });
                        break;
                }
            },
            // 채팅방 리스트 불러오기
            "loadRoomList": async function() {
                var invitingRoomList = [],
                    invitedRoomList = [];

                invitingRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": this.currentUserId,
                    "createdOutYn": "N"
                })).data.items;

                invitedRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "invitedUser": this.currentUserId,
                    "invitedOutYn": "N"
                })).data.items;

                this.rooms = [];
                invitingRoomList.forEach(e => {
                    let users = [];
                    if(e.invitedOutYn === "Y"){
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdEnterDate": e.createdEnterDate});
                    } else{
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"invitedEnterDate": e.invitedEnterDate});
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdEnterDate": e.createdEnterDate});
                    }
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    this.$set(e, "roomName", e.invitedNm);
                    if(e.invitedProfile){
                        this.$set(e, "avatar", "/api/app/images?subpath=profile&filename="+e.invitedProfile);
                    } else{
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    }
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "currentUserId", this.currentUserId);

                    this.rooms.push(e);
                });
                invitedRoomList.forEach(e => {
                    let users = [];
                    if(e.createdOutYn === "Y"){
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"invitedEnterDate": e.invitedEnterDate});
                    } else{
                        users.push({"_id": e.invitedUser, "username" : e.invitedNm,"invitedEnterDate": e.invitedEnterDate});
                        users.push({"_id": e.createdUser, "username" : e.createdNm,"createdEnterDate": e.createdEnterDate});
                    }
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    this.$set(e, "roomName", e.createdNm);
                    if(e.createdProfile){
                        this.$set(e, "avatar", "/api/app/images?subpath=profile&filename="+e.createdProfile);
                    } else{
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    }
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "currentUserId", this.currentUserId);

                    this.rooms.push(e);
                });
                document.querySelector('vue-advanced-chat').rooms = this.rooms;
            },
            // 본인이 생성한 룸 불러오기 (본인이 생성한 채팅방에 본인 있어야함!)
            "setCreatedRoomList": async function() {
                this.select.createdRoomList = [];
                this.rooms.forEach(e => {
                    if(e.createdBy == this.data.user.id){
                        this.select.createdRoomList.push(e);
                    }
                })
            },
            // 전체 유저 리스트 (초대가능 여부 보기위해서)
            "loadUserList": async function () {
                var userList= [];
                userList = (await meta.api.common.user.getUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "status": "T",
                })).data.items;
                this.data.userList = userList;
            },
            // 각 채팅방 상단의 더보기 메뉴 클릭 시
            "menuActionHandler": async function(e) {
                let action = e.detail[0].action;
                let roomId = e.detail[0].roomId;
//				this.select.roomId = roomId;
                switch (action.name) {
                    case 'leaveChat':
                        await this.setSelectedRoom(roomId);
                        await this.leaveChat(this.select.selectedRoom);
                        return;
                }
            },
            // this.select 값 입력
            "setSelectedRoom": async function(id) {
                var room = this.rooms.find(e => e.id == id)
                this.select.selectedRoom = _.cloneDeep(room);
                this.select.userNickname="";
            },
            "selectUser": function(username){
                this.select.userNickname = username;
            },
            // 채팅방 나가기 (본인)
            "leaveChat": async function(room){
                let param = {};
                param.createdUser = room.createdUser;
                param.invitedUser = room.invitedUser;
                param.createdEnterDate = room.createdEnterDate;
                param.invitedEnterDate = room.invitedEnterDate;
                if (await meta.confirm(room.roomName + "에서 나가시겠습니까?" )){
                    if(this.currentUserId === room.createdUser){
                        param.createdOutYn = "Y";
                        param.invitedOutYn = room.invitedOutYn;
                    } else{
                        param.invitedOutYn = "Y";
                        param.createdOutYn = room.createdOutYn;
                    }
                    await meta.api.common.personalRoom.modifyPersonalRoom(room.id, param);
                    await this.$router.go();
                }
            },
        },
        "mounted": async function() {
            let user = _.cloneDeep(store.state.app.user);

            this.setSelectedRoom(this.roomId)
            if(user){
                this.$set(this.data.user, "id", user.id);
                this.currentUserId = user.id;
                this.currentUserNm = user.name;
                await this.init();
                document.querySelector('vue-advanced-chat').menuActions = this.menuActions
            } else{
                await meta.alert("로그인이 필요한 서비스입니다.");
            }
        },
        "created": async function() {
        },
    });
});