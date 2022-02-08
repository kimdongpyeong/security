var MypageUserContactUsPage;
MypageUserContactUsPage = Vue.component("mypage-user-contact-us-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/contact-us/main.html")).data,
        "props": {
            roomId: {
                type: Number,
                default: 0
            }
        },
        "data": function() {
            return {
                "data": {
                    "user": {},
                },
                "tabList":["진행중","완료"],
                "today": "",
                "time": "",
                "room": {},
                "rooms": [],
                "currentUserId": "",
                "currentUserNm": "",
                "messages": [],
                "files":null,
                messagesLoaded: true,
                roomsLoaded: true,
                menuActions: [
                    { name: 'complete', title: '완료하기' },
                ],
                "search": '',
                "status":"N",
                "userType":[{"value": "a", "label": "관리자"},{"value": "t", "label": "강사"}],
                "select":{
                	"type":""
                },
            }
        },
        "watch": {
            "$store.state.app.chatMessageBox": async function(val){
            	if(this.status === "Y"){
            		meta.alert("완료된 문의 건으로 채팅이 불가 합니다.");
            	}
            }
        },
        "methods": {
            //초기화
            "init": async function() {
                document.querySelector('vue-advanced-chat').currentUserId = this.currentUserId;
                document.querySelector('vue-advanced-chat').rooms = this.rooms;
                document.querySelector('vue-advanced-chat').messages = this.messages;

                await this.loadContactUsList();
            },
        	//진행중&완료 탭 변경시
            "handleClick": function(tab) {
                let status = tab.label;
                //검색조건 초기화
                this.select.type = "";
                this.search = "";
                //완료상태변경
                if(status === "완료"){
                	this.status = "Y";
                } else{
                	this.status = "N";
                }
                this.roomId = 0;
                //다시 문의리스트 불러오기
                this.loadContactUsList();
            },
            //이름검색
            "searchName": function(){
        		this.loadContactUsList();
            },
            //문의한 강사및 관리자 리스트(채팅방리스트)
            "loadContactUsList": async function(){
            	let contactUsList = (await meta.api.common.contactUs.getContactUsList({
            		"page": 1,
            		"rowSize": 10000,
            		"userId": this.currentUserId,
            		"completedYn": this.status,
            		"lecturerNm": this.search
            	})).data.items;
            	
            	this.rooms = [];
            	
            	await contactUsList.forEach(e => {
                    let users = [];
                    
                    users.push({"_id": e.lecturerId, "username" : e.lecturerNm});
                    users.push({"_id": e.userId, "username" : e.userNm});
                    
                    this.$set(e, "enterDate", e.createdDate);
                    
                    e.createdDate = moment(e.createdDate).format('MM월 DD일');
                    
                    this.$set(e, "roomId", e.id);
                    this.$set(e, "roomName", e.lecturerNm);
                    if(e.lecturerProfile){
                        this.$set(e, "avatar", "/api/app/images?subpath=profile&filename="+e.lecturerProfile);
                    } else{
                        this.$set(e, "avatar", "/resources/img/profile.png");
                    }
                    this.$set(e, "users", users);
                    this.$set(e, "userCnt", users.length);
                    this.$set(e, "createdDate", e.createdDate);
                    this.$set(e, "currentUserId", this.currentUserId);
                    if(e.lastMsgDate){
                    	this.$set(e, "lastMsgDate", moment(e.lastMsgDate).format('YY/MM/DD HH:mm'));
                    }

                    this.rooms.push(e);
                });

            	document.querySelector('vue-advanced-chat').rooms = this.rooms;
            	
            	if(this.rooms.length > 0){
            		if(this.roomId === 0){
            			this.roomId = this.rooms[0].id;
            		}
            	}
            },
            // 방 클릭시 this.select 설정
            "setSelect": async function(room){
            	this.roomId = room.id;
            	this.room = room;
            },
            "fetchMessages": async function(e) {
                let query = null,
                    room = e.detail[0].room.users,
                    roomId = e.detail[0].room.id,
                    enterDate = e.detail[0].room.enterDate;
                
                query = contactUsRef.orderByChild('roomId').equalTo(this.roomId);

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
            },
            //메세지 보내기
            "sendMessage": async function(e) {
                let roomId = e.detail[0].roomId,
                    param={},
                    files = e.detail[0].file,
                    replyMessage = e.detail[0].replyMessage;
                
                this.today = moment(new Date()).format('yyyy년 MM월 DD일');
                this.time = moment(new Date()).format('HH:mm');

                var newPostKey = contactUsRef.push().key;

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
                	if(this.status === 'N'){
                		var updates = {};
                		updates[newPostKey] = message;
                		await contactUsRef.update(updates);
                		//contact_us 테이블에 마지막 업로드 메세지 업데이트
                		(await meta.api.common.contactUs.lastUpdated({
                			"id": roomId,
                			"lastMsgContent": e.detail[0].content,
                			"lastMsgSenderId": this.currentUserId
                		}));
                		//마지막 메세지 내용 및 날짜를 실시간으로 변경해주기 위해
                		let room = this.rooms.find(x => x.id == roomId);
                		room.lastMsgContent = e.detail[0].content;
                		room.lastMsgSenderId = this.currentUserId;
                		room.lastMsgDate = moment().format("YY/MM/DD HH:mm");
                	} else{
                		meta.alert("완료된 건으로 채팅이 불가합니다.");
                	}
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
            	
            	await database.ref("contact_us/"+edit.messageId).update(newMessage);
            },
            //메세지 삭제
            "deleteMessage": async function(e){
                let del = e.detail[0];
                
                if(this.status === 'Y'){
                	meta.alert("완료된 문의는 삭제가 불가합니다.");
                } else{
                	await database.ref("contact_us/"+del.message._id).update({
                		deleted: true,
                		deleteDate:new Date()
                	});
                	//삭제하는 메세지에 파일이 있다면 db에 저장된 파일 삭제하도록
                	if (del.message.files) {
                		this.deleteFile({messageId: del.message._id, file: del.message.files});
                	}
                }
            },
            "noticeMessage": function(){
            	meta.alert("문의하기에서는 공지사항 기능을 사용할 수 없습니다.");
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

                const uploadFileRef = contactUsFilesRef
                    .child(String(this.currentUserId))
                    .child(messageId)
                    .child(`${file.name}.${type}`)
                await uploadFileRef.put(file.blob, { contentType: type })

                const url = await uploadFileRef.getDownloadURL()

                const messageDoc = database.ref("contact_us/"+messageId);
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
                await database.ref("contact_us/"+messageId).update({files: this.files});
            },
            //파일삭제
            "deleteFile": async function({messageId, file}){
                const deleteFileRef = contactUsFilesRef
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

                const downloadRef = contactUsFilesRef
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
                    case 'complete':
                    	this.completedRoom(roomId);
                        return;
                }
            },
            "completedRoom": async function(roomId){
            	if(this.status === "N"){
            		if(await meta.confirm("완료처리하시겠습니까?")){
            			(await meta.api.common.contactUs.modifyContactUs({
            				"id": roomId,
            				"completedYn": "Y"
            			}));
            			meta.alert("완료되었습니다.");
            			this.$router.go();
            		}
            	} else{
            		meta.alert("이미 완료처리된 건입니다.");
            	}
            },
        },
        "mounted": async function() {
            let user = _.cloneDeep(store.state.app.user);
            
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