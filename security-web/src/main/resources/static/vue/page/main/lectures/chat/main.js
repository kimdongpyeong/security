var MainLecturesClassRoomChatPage;
MainLecturesClassRoomChatPage = Vue.component("main-lectures-class-room-chat-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/main/lectures/chat/main.html")).data,
        "props": {
            "isDevice": { type: Boolean, required: true }
        },
        "data": function () {
            return {
                "data": {
                    "user": {},
                    "other":{},
                    "guest":{},
                    "classroomProfile": "/resources/img/profile.png",
                },
                "guestId":null,
                "today": "",
                "time": "",
                "roomId": null,
                "room":{},
                "rooms": [],
                "allRooms":[],
                "currentUserId": "",
                "currentUserNm": "",
                "roomBossId":"",
                "messages": [],
                "files":null,
                "messagesLoaded": true,
                "roomsLoaded": true,
                "noticeFlag": false,
                "noticeBoxFlag":true,
                "noticeContent":"",
                "nowNotice":{},
                "noticeList":[],
                menuActions: [
                    { name: 'showUser', title: '친구목록' },
                    { name: 'sendCode', title: '초대코드 전송' },
                ],
                "visible": {
                    "enterCodeVisible": false,
                    "userList":false
                },
                "enterClassRoom": {},
                "codeCk": null
                
            };
        },
        "computed": {
            screenHeight() {
                return this.isDevice ? window.innerHeight + 'px' : 'calc(100vh - 80px)'
            }
        },
        "watch": {
            "$store.state.app.chatProfile.profileId": async function(id){
                this.loadOther(id);
            },
        },
        "methods": {
            "init": async function() {
            	document.querySelector('vue-advanced-chat').currentUserId = this.currentUserId
            	document.querySelector('vue-advanced-chat').messages = this.messages
                
            	this.loadClassRoom(this.roomId);
                this.loadNotice(this.roomId);
            },
            "showProfile": function(id){
        		store.state.app.chatProfile.flag = true;
        		this.loadOther(id);
            },
            //로그인된 상대유저 정보
            "loadOther": async function (id) {
                let profile, guestId;

                if(typeof(id) === 'string'){
                    if(id.includes("g")){
                    	guestId = id.substring(2);
                    	let guest = (await meta.api.common.enterUser.getEnterUser(guestId)).data;
                    	
                    	guest.guestYn = true;
                    	this.data.other = _.cloneDeep(guest);
                    } 
                } else{
                	let user = (await meta.api.common.myPage.getUser(id)).data;
                	user.guestYn = false;
                	this.data.other = _.cloneDeep(user);
                }

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
            "loadGuest": async function(id) {
            	let guestList = (await meta.api.common.enterUser.getEnterUserList({
            		"classroomId": this.roomId
            	})).data.items;
            	guestList.forEach(e => {
            		if(e.id === id){
            			this.data.guest = e;
            		}
            	});
            	if(this.data.guest.id){
            		this.currentUserId = "g_" + this.data.guest.id;
            		this.currentUserNm = this.data.guest.name;
            	}
            },
            //공지사항 불러오기
            "loadNotice": async function(roomId){
                let noticeList = (await meta.api.common.classRoomChatNotice.getClassRoomChatNoticeList({
                    "page": 1,
                    "rowSize": 100000000,
                    "classroomId": roomId
                })).data.items;
                this.noticeList = noticeList;

                if(this.noticeList.length > 0){
                    let messageId = this.noticeList[0].messageId
                    let query = classroomMessageRef.orderByChild('_id').equalTo(messageId);
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
                    (await meta.api.common.classRoomChatNotice.removeClassRoomChatNotice({
                        "classroomId": e.classroomId,
                        "messageId": e.messageId
                    })).data;
                    this.$message({
                      message: '공지사항이 삭제되었습니다.',
                      type: 'error'
                    });
                    this.loadNotice(e.classroomId);
                }
            },
            // 해당 강의실의 정보 가져오기
            "loadClassRoom": async function(id){
                let roomName = "",
                    users = [],
                    param = {},
                    allClassRoomList = (await meta.api.common.classRoom.getClassRoomList({
                        "page": 1,
                        "rowSize": 100000000,
                        "deleteYn": "N"
                    })).data.items,
                    roomUserList = (await meta.api.common.classRoomUser.getClassRoomUserList({
                        "page": 1,
                        "rowSize": 100000000,
                        "classroomId": id
                    })).data.items,
                    roomGuestList = (await meta.api.common.enterUser.getEnterUserList({
                        "page": 1,
                        "rowSize": 100000000,
                        "classroomId": id
                    })).data.items,
                    totalRoomUserList = [],
                    classRoomList = [];

                param.page= 1;
                param.rowSize = 100000000;
                param.deleteYn = "N";
                param.id = id;
                if(this.data.user.id){
                	param.userId = this.data.user.id;
                }
                classRoomList = (await meta.api.common.classRoom.getClassRoomList(param)).data.items;
                roomUserList.forEach(e => {
                	totalRoomUserList.push(e);
                });
                roomGuestList.forEach(e => {
                	e.userId = "g_" + e.id;
                	totalRoomUserList.push(e);
                });
                if(classRoomList.length > 0){
                    classRoomList.forEach(e => {
                        this.$set(e,"roomId", e.id);
                        
                        if(e.createdBy === this.currentUserId){
                            roomName = e.title+" 👑";
                        } else{
                            roomName = e.title;
                        }
                        this.$set(e,"roomName", roomName);
                        totalRoomUserList.forEach(x => {
                            if(x.profile){
                                x.profile = "/api/app/images?subpath=profile&filename="+ x.profile
                            } else{
                                x.profile = "/resources/img/profile.png";
                            }
                            users.push({
                                "_id": x.userId,
                                "username" : x.name,
                                "avatar": x.profile,
                                "createdDate": x.createdDate});
                        });
                        this.$set(e, "users", users);
                        this.$set(e, "userCnt", users.length);
                        this.$set(e,"createdBy",e.createdBy);
                    	this.$set(e, "createdDate", moment(e.createdDate).format('MM월 DD일'));
                    	this.$set(e, "currentUserId", this.currentUserId);
                        
                        if(e.saveFileNm == null || e.saveFileNm == undefined || e.saveFileNm == ""){
                            this.$set(e, "avatar", this.data.classroomProfile);
                        } else {
                            this.$set(e,"avatar",'/api/app/images?subpath=classRoomProfile&filename='+e.saveFileNm);
                        }
                        
                        this.rooms.push(e);
                    });
                    
                    document.querySelector('vue-advanced-chat').rooms = this.rooms;
                    this.noticeFlag = true;
                } else{
                    this.noticeFlag = false;
                    if(allClassRoomList.find(el => el.id === Number(id))){
                        allClassRoomList.forEach(xl => {
                            if(xl.id === Number(id)){
                                this.enterClassRoom = xl;
                            }
                        });
                        meta.alert("미참여 강의실은 이용이 불가합니다.");
                        this.visible.enterCodeVisible = true;
                    } else{
                        meta.alert("존재하지않는 채팅방입니다.");
                        this.$router.push({ path: "/"});
                    }
                }
            },
            //메세지 불러오기
            "fetchMessages": async function(e) {
                let query = null,
                    roomUsers = e.detail[0].room.users,
                    roomId = e.detail[0].room.id,
                    roomBoss = e.detail[0].room.createdBy,
                    enterDate = null;

                //입장한 시점 알기
                roomUsers.forEach(x => {
                    if(x._id === this.currentUserId){
                        enterDate = x.createdDate;
                    }
                });
                
                query = classroomMessageRef.orderByChild('roomId').equalTo(roomId);

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
                            let enterDay, enterTime, noMessage;
                            
                            if(this.data.user.id){
                            	enterDay = moment(enterDate).format('yyyy년 MM월 DD일');
                                enterTime = moment(enterDate).format('HH:mm');
                            } else{
                            	enterDay = moment().format('yyyy년 MM월 DD일');
                                enterTime = moment().format('HH:mm');
                            }
                            noMessage = {
                                date: enterDay,
                                timestamp: enterTime,
                            }
                            
                            this.messages.push(noMessage);
                        }
                    });
                });
                this.room = e.detail[0].room;
                document.querySelector('vue-advanced-chat').messages = this.messages
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

                var newPostKey = classroomMessageRef.push().key;

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
                    await classroomMessageRef.update(updates);
                } else{
                	await meta.alert("초대된 사용자만 채팅이 가능합니다");
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

                await database.ref("classroom_messages/"+edit.messageId).update(newMessage);
            },
            //메세지 삭제
            "deleteMessage": async function(e){
                let del = e.detail[0];

                await database.ref("classroom_messages/"+del.message._id).update({
                    deleted: true,
                    deleteDate:new Date()
                });
                //삭제하는 메세지에 파일이 있다면 db에 저장된 파일 삭제하도록
                if (del.message.files) {
                    this.deleteFile({messageId: del.message._id, file: del.message.files});
                }

            },
            //메세지 공지사항등록
            "noticeMessage": async function(e){
                let roomId = e.detail[0].roomId,
                    messageId = e.detail[0].message._id;
                if(this.data.user.id){
                	if (await meta.confirm("채팅방 공지는 1건만 노출가능합니다.\n공지사항 등록하시겠습니까?")) {
                		if(this.noticeList.length > 0){
                			(await meta.api.common.classRoomChatNotice.removeClassRoomChatNotice({
                				"classroomId": this.nowNotice.roomId,
                				"messageId": this.nowNotice.messageId
                			})).data;
                			(await meta.api.common.classRoomChatNotice.createClassRoomChatNotice({
                				"classroomId": roomId,
                				"messageId": messageId
                			})).data;
                		} else{
                			(await meta.api.common.classRoomChatNotice.createClassRoomChatNotice({
                				"classroomId": roomId,
                				"messageId": messageId
                			})).data;
                		}
                		
                		this.$message({
                			message: '공지사항이 등록되었습니다.',
                			type: 'success'
                		});
                		this.loadNotice(roomId);
                	}
                } else{
                	meta.alert("로그인 한 경우에만 공지사항 등록이 가능합니다.");
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
                return messageFile;
            },
            //파일 db저장
            "uploadFile": async function({file, messageId, roomId}) {
                let type = file.extension || file.type
                if (type === 'svg' || type === 'pdf') {
                    type = file.type
                }

                const uploadFileRef = classroomFilesRef
                    .child(String(roomId))
                    .child(String(this.currentUserId))
                    .child(messageId)
                    .child(`${file.name}.${type}`)
                await uploadFileRef.put(file.blob, { contentType: type })

                const url = await uploadFileRef.getDownloadURL()

                const messageDoc = database.ref("classroom_messages/"+messageId);
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
                await database.ref("classroom_messages/"+messageId).update({files: this.files});
            },
            //파일삭제
            "deleteFile": async function({messageId, file}){
                const deleteFileRef = classroomFilesRef
                    .child(String(roomId))
                    .child(String(this.currentUserId))
                    .child(messageId)
                    .child(`${file.name}.${file.extension || file.type}`)
                await deleteFileRef.delete()
            },
            //파일크게보기
            "openFile": function(e) {
                let file = e.detail[0].message.file,
                    action = e.detail[0].action,
                    roomId = e.detail[0].message.roomId,
                    messageId = e.detail[0].message._id,
                    senderId = e.detail[0].message.senderId;

                const downloadRef = classroomFilesRef
                .child(String(roomId))
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
            // 각 채팅방 상단의 더보기 메뉴 클릭 시
            "menuActionHandler": async function(e) {
                let action = e.detail[0].action;
                let roomId = e.detail[0].roomId;
                switch (action.name) {
                    case 'showUser' : 
                        this.visible.userList = true;
                        return;
                    case 'sendCode':
                    	if(this.currentUserId === this.room.createdBy){
                        	console.log(this.room.users.length)
                        	if(this.room.memberNum > this.room.users.length){
                        		this.kakaoShare();
                        	} else{
                        		meta.alert("강의실 최대 인원이 모두 찼습니다.");
                        	}
                    	} else{
                    		meta.alert("방장만 초대가 가능합니다.");
                    	}
                        return;
                }
            },
            "kakaoShare": function() {
                Kakao.Link.sendDefault({
                    objectType: "feed",
                    content: {
                        title: "<" + this.room.title +"> 커뮤니티에 들어와보세요!",
                        imageUrl: "",
                        link: {
//                            mobileWebUrl: 'https://supporti.kr',
                            webUrl: 'http://localhost:9090/main/lectures/enter?id='+this.room.enterCode+this.room.id,
                        },
                    },
                });
            },
            //1:1채팅
            "userChat": async function(userId, username){
                let totalRoomList = [],
                    param = {};
                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');

                let invitingRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": this.currentUserId,
                    "invitedUser": userId,
                })).data.items;
                
                invitingRoomList.forEach(x => {
                    totalRoomList.push(x);
                });

                let invitedRoomList = (await meta.api.common.personalRoom.getPersonalRoomList({
                    "createdUser": userId,
                    "invitedUser": this.currentUserId,
                })).data.items;
                
                invitedRoomList.forEach(x => {
                    totalRoomList.push(x);
                })
                
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
                        "name": "1:1채팅",
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
                            "name": "1:1채팅",
                            "params": {
                                "roomId": newPersonalRoom.id
                            }
                        });
                    }
                }
            },
//            "enterChat": async function(){
//                if(this.codeCk){
//                    if(this.codeCk.length === 8){
//                        if(this.enterClassRoom.enterCode === this.codeCk){
//                            if(await meta.confirm("'"+this.enterClassRoom.title+"' 강의실에 입장하시겠습니까?")){
//                                // 강의실 - 유저 입장
//                                (await meta.api.common.classRoomUser.createClassRoomUser({
//                                    "classroomId": this.enterClassRoom.id,
//                                    "userId": this.data.user.id
//                                })).data;
//                                this.$router.go();
//                            }
//                        } else{
//                            meta.alert("초대코드가 맞지 않습니다.\n다시 입력해주시길 바랍니다.")
//                        }
//                    } else{
//                        meta.alert("8자리로 입력해주세요.");
//                    }
//                } else{
//                    meta.alert("초대코드를 입력해주세요.");
//                }
//            },
            "goMyPage": function(){
                store.state.app.chatProfile.flag = false;
                if(this.data.user.roleId === 3){
                    this.$router.push({"path": "/mypage/users/main"})
                } else{
                    this.$router.push({"path": "/mypage/lecturers/main"})
                }
            },
            "getSessionUser": async function() {
            	let guestId = (await meta.api.common.enterUser.getSession()).data.guestId;
            	this.guestId = guestId;
            }
        },
        "mounted": async function () {
            let classroomId = this.$route.query.id;
            this.roomId = classroomId;
            let user = _.cloneDeep(store.state.app.user);
            
            if(user){
                this.loadUser(user.id);
                this.$set(this.data.user, "id", user.id);
                this.currentUserId = user.id;
                this.currentUserNm = user.name;
                await this.init();
                document.querySelector('vue-advanced-chat').menuActions = this.menuActions
            } else{
            	await this.getSessionUser();
            	if(this.guestId){
            		await this.loadGuest(this.guestId);
            		if(this.currentUserId){
            			await this.init();
            			document.querySelector('vue-advanced-chat').menuActions = this.menuActions
            		} else{
                		meta.alert("초대받은 사용자만 이용가능한 서비스입니다.")
            		}
            	} else{
            		meta.alert("초대받은 사용자만 이용가능한 서비스입니다.")
            	}
            }
        },
    });
});