var Test3Page;
Test3Page = Vue.component("test3-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/test3/main.html")).data,
        "data": function() {
            return {
                "today": "",
                "time": "",
                "roomId": null,
                "codeCk":null,
                "secretRoom":{},
                "newRoom": {
                    "id": null,
                    "title": null,
                    "introduction": null,
                    "maxMember": null,
                    "deleteYn": "N",
                    "createdBy": null,
                    "codeYn": "N",
                    "code": "",
                },
                "room": {},
                "selectRoom": {
                    "userList": []
                },
                "select": {
                    "roomId": null,
//					"roomName": null,
                    "userId": null,
                    "userNickname": null,
                    "users":[],
                    "createdRoomList": [],
                    "selectedRoom": {},
                    "roomUser": [],
                },
                "data": {
                    "userList": [],
                    "allRoomUserList": [],
                    "user": {},
                    "other":{},
                    "roomProfile": "/resources/img/profile.png",
                },
                "visible":{
                    "roomListFormVisible": false,
                    "roomUserFormVisible": false,
                    "addRoomVisible": false,
                    "enterCodeVisible":false,
                    "userList": false,
                    "imgVisible": false
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
                    { name: 'manageUser', title: 'Manage User' },
                    { name: 'manageRoom', title: 'Manage Room' },
                    { name: 'leaveChat', title: 'Leave Chat' },
                ],
                "noticeFlag": false,
                "noticeBoxFlag":true,
                "noticeContent":"",
                "nowNotice":{},
                "noticeList":[],
                search: '',
                "warningFlag":false,
                "typingList": [],
                "srcList": [],
                "fileSrc":""
            }
        },
        "computed": {
        },
        "watch": {
            "$store.state.app.chatProfile.profileId": async function(id){
                this.loadOther(id);
            }
        },
        "methods": {
            "init": async function() {
                document.querySelector('vue-advanced-chat').currentUserId = this.currentUserId
                document.querySelector('vue-advanced-chat').rooms = this.rooms
                document.querySelector('vue-advanced-chat').messages = this.messages

                await this.loadRoomList();
                await this.loadUserList();
                await this.loadAllRoomList();
            },
            "goMyPage": function(){
                store.state.app.chatProfile.flag = false;
                if(this.data.user.roleId === 3){
                    this.$router.push({"path": "/mypage/mentee/main"})
                } else{
                    this.$router.push({"path": "/mypage/mentor/main"})
                }
            },
            "goPersonalRoom": function(){
                this.$router.push({"path": "/test2"})
            },
            "showProfile": function(id){
                store.state.app.chatProfile.flag = true;
                this.loadOther(id);
            },
            //로그인된 상대유저 정보
            "loadOther": async function (id) {
                let profile;

                let user= (await meta.api.common.myPage.getUser(id)).data;
                this.data.other = _.cloneDeep(user);

                if(this.data.other.saveFileNm){
                    profile = "/api/app/images?subpath=profile&filename="+ this.data.other.saveFileNm;
                } else{
                    profile = "/resources/img/profile.png";
                }
                this.$set(this.data.other,"profile",profile);
            },
            //로그인된 유저 정보
            "loadUser": async function (id) {
                let user= (await meta.api.common.myPage.getUser(id)).data;
                this.data.user = _.cloneDeep(user);
            },
            //채팅방생성 시, 리셋버튼
            "newRoomReset":function(){
                this.newRoom.title=null;
                this.newRoom.deleteYn="N";
                this.newRoom.codeYn="N";
                this.newRoom.code=null;
                this.newRoom.maxMember=null;
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
                    (await meta.api.common.chatNotice.removeChatNotice({
                        "roomId": e.roomId,
                        "messageId": e.messageId
                    })).data;
                }
                this.$message({
                  message: '공지사항이 삭제되었습니다.',
                  type: 'error'
                });
                this.loadNotice(e.roomId);
            },
            //타이핑중인 메세지
            "typingMessage": async function(e) {
                if(!this.warningFlag){
                    this.typingList.push(e.detail[0].message)
                    if(this.typingList.length == 1){
                        await meta.alert("자유로운 소통은 환영, 상처되는 말은 No");
                        this.warningFlag = true;
                        this.typingList = [];
                    }
                }
            },
            "fetchMessages": async function(e) {
                let query = null,
                    room = e.detail[0].room.users,
                    roomId = e.detail[0].room.id,
                    roomBoss = e.detail[0].room.createdBy,
                    enterDate = null;

                room.forEach(x => {
                    if(x._id === this.currentUserId){
                        enterDate = x.createdDate;
                    }
                });

                query = messageRef.orderByChild('roomId').equalTo(roomId);

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
                                avatar: message.val().avatar,
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
                this.selectRoom.userList = [];
                this.room = e.detail[0].room;
                this.loadNotice(this.room.id);
                this.room.users.forEach(x => {
                    if(x.avatar){
                        x.avatar = "/api/app/images?subpath=profile&filename="+ x.avatar
                    } else{
                        x.avatar = "/resources/img/profile.png";
                    }
                    this.selectRoom.userList.push(x);
                })
            },
            //공지사항 불러오기
            "loadNotice": async function(roomId){
                let noticeList = (await meta.api.common.chatNotice.getChatNoticeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roomId": roomId
                })).data.items;
                this.noticeList = noticeList;

                if(this.noticeList.length > 0){
                    let messageId = this.noticeList[0].messageId
                    let query = messageRef.orderByChild('_id').equalTo(messageId);
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
            //메세지 보내기
            "sendMessage": async function(e) {
                let roomId = e.detail[0].roomId,
                    profile, param={};
                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');
                let files = e.detail[0].file,
                    replyMessage = e.detail[0].replyMessage;

                if(this.data.user.saveFileNm){
                    profile = "/api/app/images?subpath=profile&filename="+ this.data.user.saveFileNm;
                } else{
                    profile = "/resources/img/profile.png";
                }

                var newPostKey = messageRef.push().key;

                const message = {
                    _id: newPostKey,
                    roomId: roomId,
                    content: e.detail[0].content,
                    senderId: this.currentUserId,
                    senderNm: this.currentUserNm,
                    date: this.today,
                    timestamp: this.time,
                    avatar: profile,
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
                    await messageRef.update(updates);
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

                await database.ref("messages/"+edit.messageId).update(newMessage);
            },
            //메세지 공지사항등록
            "noticeMessage": async function(e){
                let roomId = e.detail[0].roomId,
                    messageId = e.detail[0].message._id;
                if (await meta.confirm("채팅방 공지는 1건만 노출가능합니다.\n공지사항 등록하시겠습니까?")) {
                    if(this.noticeList.length > 0){
                        (await meta.api.common.chatNotice.removeChatNotice({
                            "roomId": this.nowNotice.roomId,
                            "messageId": this.nowNotice.messageId
                        })).data;
                        (await meta.api.common.chatNotice.createChatNotice({
                            "roomId": roomId,
                            "messageId": messageId
                        })).data;
                    } else{
                        (await meta.api.common.chatNotice.createChatNotice({
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

                await database.ref("messages/"+del.message._id).update({
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

                const messageDoc = database.ref("messages/"+messageId);
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
                await database.ref("messages/"+messageId).update({files: this.files});
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

                this.srcList = [];

                switch (action){
                    case 'preview':
                        window.open(file.url, '_blank');
                        this.fileSrc = file.url;
                        this.srcList.push(file.url);
                        this.visible.imgVisible = true;
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
            "fetchMoreRooms": function() {
            },
            "userChat": async function(userId, username){
                let totalRoomList = [],
                    param = {};
                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');

                let invitingRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": this.currentUserId,
                    "invitedUser": userId,
                })).data.items;
                if(invitingRoomList.length > 0){
                    invitingRoomList.forEach(x => {
                        totalRoomList.push(x);
                    })
                }

                let invitedRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": userId,
                    "invitedUser": this.currentUserId,
                })).data.items;
                if(invitedRoomList.length > 0){
                    invitedRoomList.forEach(x => {
                        totalRoomList.push(x);
                    })
                }
                if(totalRoomList.length > 0){
                    let room = totalRoomList[0];
                    if(room.createdOutYn === "N" && room.invitedOutYn === "N"){
                        this.$message({
                            message: username+"님과의 채팅방으로 이동",
                            type: 'success'
                          });
                    } else{
                        param.id = room.id;
                        if(room.createdOutYn == "Y" && room.invitedOutYn == "N"){
                            param.createdOutYn = "N";
                        } else if (room.invitedOutYn == "Y" && room.createdOutYn == "N"){
                            param.invitedOutYn = "N";
                        } else {
                            param.createdOutYn = "N";
                            param.invitedOutYn = "N";
                        }
                        await meta.api.common.personalRoom.updatePersonalRoom(param);
                    }
                    store.state.app.chatProfile.flag = false;
                    this.$message({
                        message: username+"님과의 채팅방으로 이동",
                        type: 'success'
                      });
                    this.$router.push({
                        "name": "테스트화면",
                        "params": {
                            "roomId": room.id
                        }
                    })
                } else{
                    if (await meta.confirm(username + "님과 채팅하시겠습니까?" )) {
                        let newPersonalRoom = (await meta.api.common.personalRoom.createPersonalRoom({
                            "createdUser": this.currentUserId,
                            "invitedUser": userId,
                            "createdOutYn": "N",
                            "invitedOutYn": "N",
                            "deleteYn": "N"
                        })).data;
                        var newPostKey = personalMessageRef.push().key;
                        const message = {
                            _id: newPostKey,
                            roomId: newPersonalRoom.id,
                            content: "'"+username+"'님과 채팅방이 생성되었습니다.",
                            senderId: "0",
                            date: this.today,
                            timestamp: this.time,
                            edited : false,
                            deleted: false,
                            createdDate: new Date()
                        };
                        var updates = {};
                        updates[newPostKey] = message;
                        await personalMessageRef.update(updates);

                        this.$router.push({
                            "name": "테스트화면",
                            "params": {
                                "roomId": newPersonalRoom.id
                            }
                        });
                    }
                }
            },
            "enterRoom": async function(room) {
              if(room.codeYn === "Y"){
                  this.secretRoom = room;
                  this.codeCk = null;
                  this.visible.enterCodeVisible = true;
              } else{
                if (await meta.confirm(room.title + "방에 입장하시겠습니까?" )) {
                    meta.api.common.roomUser.createRoomUser({
                        "roomId": room.id,
                        "userId": this.currentUserId,
                    });
                    await this.$router.go();
                }
              }
            },
            "sameCode": async function(){
                var regNum = /^[0-9]*$/;

                if(this.codeCk){
                    if(!regNum.test(this.codeCk) || this.codeCk.search(/\s/) !== -1 || this.codeCk.length != 4){
                        meta.alert("참여코드는 숫자 4자리로 입력해주세요");
                    } else{
                        if(this.codeCk === this.secretRoom.code){
                            meta.api.common.roomUser.createRoomUser({
                                "roomId": this.secretRoom.id,
                                "userId": this.currentUserId,
                            });
                            await this.$router.go();
                        } else{
                            meta.alert("참여코드가 일치하지 않습니다.");
                        }
                    }
                } else{
                    meta.alert("참여코드를 입력해주세요");
                }
            },
            // 모든 채팅방 불러오기
            "loadAllRoomList": async function(){
                let roomIdList=[],
                    allRoomList=[];
                allRoomList = (await meta.api.common.room.getRoomList({
                    "page": 1,
                    "rowSize": 100000000,
                    "deleteYn": "N"
                })).data.items;

                for(i in allRoomList){
                    roomIdList.push(allRoomList[i].id)
                }
                //roomIdList에 담긴 채팅방의 유저 불러오기
                let allRoomUserList = (await meta.api.common.roomUser.getRoomUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roomIdList": roomIdList
                })).data.items;

                this.allRooms = [];
                allRoomList.forEach(e => {
                    let users = [];
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    if(e.codeYn === "Y"){
                        this.$set(e, "roomName", "🔒 "+e.title);
                    } else{
                        this.$set(e, "roomName", e.title);
                    }
                    if(e.createdBy === this.currentUserId){
                        this.$set(e, "roomBoss", true);
                    } else{
                        this.$set(e,"roomBoss",false);
                    }
                    allRoomUserList.forEach(el => {
                        if(e.id == el.roomId) {
                            users.push({"_id": el.userId, "username" : el.nickname});
                        }
                    });
                    users.forEach(x =>{
                        if(x._id == this.currentUserId){
                            this.$set(e, "enterFlag", true);
                        }
                    })
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "count", users.length+" / "+e.maxMember);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "createdBy", e.createdBy);
                    this.$set(e, "currentUserId", this.currentUserId);

                    this.allRooms.push(e);

                });
            },
            // 채팅방 리스트 불러오기
            "loadRoomList": async function() {
                var roomList = [],
                    roomIdList = [];
                //접속한 유저가 포함된 채팅방 불러오기
                roomList = (await meta.api.common.room.getRoomList({
                    "page": 1,
                    "rowSize": 100000000,
                    "deleteYn": "N",
                    "userId": this.currentUserId
                })).data.items;

                //불러온 채팅방ID를 roomIdList에 담아줌
                for(i in roomList){
                    roomIdList.push(roomList[i].id)
                }
                //roomIdList에 담긴 채팅방의 유저 불러오기
                let allRoomUserList = (await meta.api.common.roomUser.getRoomUserList({
                    "page": 1,
                    "rowSize": 100000000,
                    "roomIdList": roomIdList
                })).data.items;

                this.rooms = [];
                roomList.forEach(e => {
                    let roomName, users = [], profile;
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    this.$set(e, "roomId", e.id);
                    if(e.codeYn === "Y"){
                        if(e.createdBy === this.currentUserId){
                            roomName = "🔒 "+e.title+" 👑";
                        } else{
                            roomName = "🔒 "+e.title;
                        }
                    } else{
                        if(e.createdBy === this.currentUserId){
                            roomName = e.title+" 👑";
                        } else{
                            roomName = e.title;
                        }
                    }
                    this.$set(e, "roomName", roomName);

                    allRoomUserList.forEach(el => {
                        if(e.id == el.roomId) {
                            users.push({
                                "_id": el.userId,
                                "username" : el.nickname,
                                "avatar": el.profile,
                                "createdDate": el.createdDate});
                        }
                    });
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "createdBy", e.createdBy);
                    this.$set(e, "currentUserId", this.currentUserId);
                    if(e.saveFileNm == null || e.saveFileNm == undefined || e.saveFileNm == ""){
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    } else {
                        this.$set(e, "avatar", "/api/app/images/roomprofile/" + e.saveFileNm);
                    }

                    this.rooms.push(e);

                });

                document.querySelector('vue-advanced-chat').rooms = this.rooms;
            },
            // 선택한 유저가 생성한 룸 불러오기
            "setCreatedRoomList": async function(val) {
                this.rooms.forEach(e => {
                    if(e.createdBy == val){
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
            // 유저관리 팝업창 오픈
            "roomUserFormVisible": function(){
                this.visible.roomUserFormVisible = true;
            },
            // 채팅방 관리 팝업창 오픈
            "roomListFormVisible": function(){
                this.visible.roomListFormVisible = true;
            },
            //채팅방생성 팝업 띄우기
            "addRoom": function(){
                this.visible.addRoomVisible = true;
                this.newRoomReset();
                this.loadAllRoomList();
            },
            // 팝업 내려갈 때, this.select 초기화
            "cancel": function(){
                this.visible.roomListFormVisible = false;
                this.visible.roomUserFormVisible = false;
                Object.assign((this.$data).select, this.$options.data().select);
                this.data.roomProfile = "/resources/img/profile.png";
                this.$refs.roomProfile.value = null;
            },
            // 채팅방 생성
            "createRoom": async function() {
                var regNum = /^[0-9]*$/;
                this.newRoom.createdBy = this.data.user.id;
                await this.setCreatedRoomList(this.newRoom.createdBy);

                if(this.data.user.roleId == 3){
                    meta.alert("채팅방 생성은 크리에이터만 가능합니다.");
                } else if(this.select.createdRoomList.length > 2) {
                    meta.alert("방은 총 3개까지 작성 가능합니다.");
                } else {
                    if(this.newRoom.title){
                        if(this.$refs.roomProfile.files[0] != undefined || this.$refs.roomProfile.files[0] != null){
                            if(this.newRoom.codeYn == "N"){
                                this.newRoom.code = null;
                                this.newRoomCreate();
                            } else{
                                if(this.newRoom.code){
                                    if(!regNum.test(this.newRoom.code) || this.newRoom.code.search(/\s/) !== -1 || this.newRoom.code.length != 4){
                                        meta.alert("비밀번호는 숫자 4자리로 입력해주세요");
                                    } else{
                                        this.newRoomCreate();
                                    }
                                } else{
                                    meta.alert("비밀번호를 입력해주세요");
                                }
                            }
                        } else{
                            meta.alert("프로필 사진을 넣어주세요");
                        }
                    } else{
                        meta.alert("채팅방 이름을 입력해주세요");
                    }
                }
            },
            "newRoomCreate": async function(){
                let formData = new FormData();

                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');

                formData.append("title", this.newRoom.title);
                formData.append("introduction", this.newRoom.introduction);
                formData.append("maxMember", this.newRoom.maxMember);
                formData.append("deleteYn", this.newRoom.deleteYn);
                formData.append("codeYn", this.newRoom.codeYn);
                formData.append("code", this.newRoom.code);
                formData.append("createdBy", this.newRoom.createdBy);

                if(this.$refs.roomProfile.files[0] != undefined || this.$refs.roomProfile.files[0] != null)
                        formData.append("roomProfile", this.$refs.roomProfile.files[0]);

                if (await meta.confirm("등록하시겠습니까?")) {
                    var newRoom = (await meta.api.common.room.createRoom(formData)).data;
                    await meta.api.common.roomUser.createRoomUser({
                                    "roomId": newRoom.id,
                                    "userId": newRoom.createdBy,
                                });

                    var newPostKey = messageRef.push().key;
                    const message = {
                        _id: newPostKey,
                        roomId: newRoom.id,
                        content: "'"+newRoom.title+"' 방이 생성되었습니다.",
                        senderId: "0",
                        date: this.today,
                        timestamp: this.time,
                        edited : false,
                        deleted: false,
                        createdDate: new Date()
                    };
                    var updates = {};
                    updates[newPostKey] = message;
                    await messageRef.update(updates);
                    this.$message({
                          message: "'"+newRoom.title+"' 방 생성 완료",
                          type: 'success'
                        });
                    this.$router.go();
                }
            },
            // 각 채팅방 상단의 더보기 메뉴 클릭 시
            "menuActionHandler": async function(e) {
                let action = e.detail[0].action;
                let roomId = e.detail[0].roomId;
                this.select.roomId = roomId;
                switch (action.name) {
                    case 'manageUser':
                        await this.setSelectedRoom(this.select.roomId);
                        this.roomUserFormVisible();
                        return;
                    case 'manageRoom':
                        await this.setSelectedRoom(this.select.roomId);
                        if (this.room.createdBy == this.data.user.id) {
                            this.data.roomProfile = _.cloneDeep(this.select.selectedRoom.avatar);
                            this.roomListFormVisible();
                        } else {
                            meta.alert("채팅방 관리는 본인이 생성한 방만 가능합니다");
                        }
                        return;
                    case 'leaveChat':
                        await this.setSelectedRoom(this.select.roomId);
                        await this.leaveChat(this.select.selectedRoom);
                        return;
                }
            },
            // this.select 값 입력
            "setSelectedRoom": async function(id) {
                var room = this.rooms.find(e => e.id == id)
                this.select.selectedRoom = _.cloneDeep(room);
                this.select.userNickname="";
                this.select.selectedRoom.users.forEach(e=> {
                    this.select.roomUser.push({"text": e.username, "value": e._id});
                });
            },
            // 방 수정 및 삭제
            "modifyRoom": async function(value) {
                var regNum = /^[0-9]*$/;
                let code = this.select.selectedRoom.code;
                await this.setCreatedRoomList(this.select.selectedRoom.createdBy);
                var temp = this.data.userList.find(e => e.id == this.select.selectedRoom.createdBy)

                if(this.select.selectedRoom.id == null || this.select.selectedRoom.id == undefined){
                    meta.alert("채팅방이 없습니다");
                } else {
                    if(value == null || value == undefined){
                        if(temp.roleId == 3){
                            meta.alert("채팅방 관리는 크리에이터만 가능합니다.");
                        } else if(this.select.createdRoomList.length > 2 && this.select.selectedRoom.createdBy !== this.room.createdBy) {
                            meta.alert(this.temp.nickname + "님이 관리하는 방의 갯수가 초과되었습니다.");
                        } else {
                            if(this.select.selectedRoom.codeYn == "N"){
                                code = "";
                                await this.modify();
                            } else {
                                if(code){
                                    if(!regNum.test(code) || code.search(/\s/) !== -1 || code.length !== 4){
                                        meta.alert("비밀번호는 숫자 4자리로 입력해주세요");
                                    } else {
                                        await this.modify();
                                    }
                                } else {
                                    meta.alert("비밀번호를 입력해주세요");
                                }
                            }
                        }
                    } else if(value== "delete") {
                        await this.modify("delete");
                    }
                }
            },
            "modify" : async function(value) {
                let formData = new FormData();
                if(value == null || value == ""){
                    if (await meta.confirm("수정하시겠습니까?")) {
                        formData.append("id", this.select.selectedRoom.id);
                        formData.append("title", this.select.selectedRoom.title);
                        formData.append("introduction", this.select.selectedRoom.introduction);
                        formData.append("maxMember", this.select.selectedRoom.maxMember);
                        formData.append("deleteYn", "N");
                        formData.append("codeYn", this.select.selectedRoom.codeYn);
                        formData.append("code", this.select.selectedRoom.code);
                        formData.append("createdBy", this.select.selectedRoom.createdBy);
                        if(this.$refs.roomProfile.files[0] != null && this.$refs.roomProfile.files[0] != undefined ){
                            formData.append("roomProfile", this.$refs.roomProfile.files[0]);
                        }

                        (await meta.api.common.room.modifyRoom(formData)).data;
                        await meta.alert("수정되었습니다.");
                        this.$router.go();
                    }
                } else if (value == "delete") {
                    if (await meta.confirm("삭제하시겠습니까?")) {
                        formData.append("id", this.room.id);
                        formData.append("title", this.room.title);
                        formData.append("introduction", this.room.introduction);
                        formData.append("maxMember", this.room.maxMember);
                        formData.append("deleteYn", "Y");
                        formData.append("codeYn", this.room.codeYn);
                        formData.append("code", this.room.code);
                        formData.append("createdBy", this.room.createdBy);

                        (await meta.api.common.room.modifyRoom(formData)).data;
                        await meta.alert("삭제되었습니다.");
                        this.$router.go();
                    }
                }
            },
            "selectUser": function(username){
                this.select.userNickname = username;
            },
            // 채팅방에 유저 초대 (방에 참여한 사람은 다른 사람 초대 가능)
            "inviteUser": function() {
                if(this.select.selectedRoom.userCnt < this.select.selectedRoom.maxMember) {
                    var temp = this.data.userList.find(e => e.nickname == this.select.userNickname)
                    if(temp !== undefined){
                        this.select.userId = _.cloneDeep(temp.id);
                        if(this.select.selectedRoom.users.find(e => e._id == this.select.userId) == undefined) {
                            meta.api.common.roomUser.createRoomUser({
                                "roomId": this.select.selectedRoom.id,
                                "userId": this.select.userId,
                            });
                            meta.alert(this.select.userNickname + "이(가) 초대되었습니다.");
                            this.$router.go();
                        } else {
                            meta.alert("이미 초대된 유저입니다.");
                        }
                    } else{
                        meta.alert("유저가 존재하지 않습니다");
                    }
                } else {
                    meta.alert("최대인원수를 초과하셨습니다");
                }
            },
            // 채팅방에 유저 추방
            "removeUser": async function(){
                if(this.select.selectedRoom.createdBy == this.data.user.id) {

                    var temp = this.data.userList.find(e => e.nickname == this.select.userNickname)
                    if(temp !== undefined){
                        this.select.userId = _.cloneDeep(temp.id);

                        if(this.select.selectedRoom.users.find(e => e._id == this.select.userId) == undefined) {
                            meta.alert("이미 초대된 유저입니다.");
                        } else {
                            // 방 폭파 (방장나가기)
                            if(this.select.selectedRoom.createdBy == this.select.userId){
                                await this.leaveChat(this.select.selectedRoom);
                            }
                            if (await meta.confirm(this.select.userNickname + "를 추방하시겠습니까?" )) {
                                meta.api.common.roomUser.removeRoomUser({
                                    "roomId": this.select.selectedRoom.id,
                                    "userId": this.select.userId,
                                });
                                meta.alert(this.select.userNickname + "이(가) 추방되었습니다.");
                                await this.$router.go();
                            }
                        }
                    } else{
                        meta.alert("유저가 존재하지 않습니다");
                    }
                } else {
                    meta.alert("권한이 없습니다")
                }
            },
            // 채팅방 나가기 (본인)
            "leaveChat": async function(room){

                if(room.createdBy == this.data.user.id){
                    if (await meta.confirm("방장이 나갈 시에 방이 사라집니다\n 나가시겠습니까?" )) {
                        if(this.noticeList.length > 0){
                            let roomId = this.noticeList[0].roomId,
                                messageId = this.noticeList[0].messageId;
                            (await meta.api.common.chatNotice.removeChatNotice({
                                "roomId": roomId,
                                "messageId": messageId
                            })).data;
                        }
                        await this.modify("delete");
                        meta.api.common.roomUser.removeRoomUser({
                            "roomId": room.id,
                            "userId": this.data.user.id,
                        });
                    }
                } else {
                    if (await meta.confirm(room.roomName + "에서 나가시겠습니까?" )) {
                        meta.api.common.roomUser.removeRoomUser({
                            "roomId": room.id,
                            "userId": this.data.user.id,
                        });
                    }
                }
            },
            // 파일 초기화
            "fileClear": function() {
                this.newRoom.roomProfile = null;
                this.room.avatar = null;
                this.data.roomProfile = "/resources/img/profile.png";
            },
            // 파일 등록
            "thumbFile": function(event) {
                let file = event.target.files[0],
                    self = this;
                const reader = new FileReader();

                if (file == null || file.size === 0) {
                    self.fileClear();
                    return;
                } else if(!file.type.match("image.*")) {
                    self.fileClear();
                    meta.alert('이미지만 첨부 가능합니다');
                    return;
                }

                reader.onload = function (e) {
                    self.data.roomProfile= e.target.result;
                }

                reader.readAsDataURL(file);
            },
        },
        "mounted": async function() {
            let user = _.cloneDeep(store.state.app.user);
            if(user){
                this.loadUser(user.id);
                this.$set(this.data.user, "id", user.id);
                this.currentUserId = user.id;
                this.currentUserNm = user.nickname;
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