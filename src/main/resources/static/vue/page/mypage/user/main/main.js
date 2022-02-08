var MypageUsersMainPage;
MypageUsersMainPage = Vue.component("mypage-users-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/mypage/user/main/main.html")).data,
        "data": function () {
            return {
            	"data":{
            		"user":{}
            	},
            	"lecturerList":[],
            	"select":{
            		"lecturer": null,
            		"lecturerNm": null
            	}
            };
        },
        "watch": {
        	"select.lecturer": function(e){
        		this.lecturerList.forEach(x => {
        			if(x.value === e){
        				this.select.lecturerNm = x.name;
        			}
        		});
        	}
        },
        "methods": {
        	//강사, 관리자 리스트 불러오기
        	"loadLecturerList": async function(){
        		let userList = (await meta.api.common.user.getUserList({
            		"page": 1,
            		"rowSize": 10000,
        		})).data.items;

        		userList.forEach(x => {
        			if(x.roleId !== 3){
        				this.lecturerList.push({"label": x.id +" "+ x.name, "value": x.id, "name": x.name});
        			}
        		});
        	},
        	//선택한 대상에게 문의하기
        	"goContactUs": async function(){
                let today = moment(new Date()).format('yyyy년 MM월 DD일'),
                	time = moment(new Date()).format('HH:mm');
                
        		if(this.select.lecturer){
        			//기존에 문의 진행중인 건 있는지 확인
        			let existContact = (await meta.api.common.contactUs.getContactUsList({
        				"lecturerId": this.select.lecturer,
        				"userId": this.data.user.id,
        				"completedYn": 'N'
        			})).data.items;

					//선택한 강사와 기존에 이미 진행중인 건이 있으면 바로 넘어가고, 아니면 새로 생성
        			if(existContact.length > 0){
    					this.$router.push({
    						"name": "사용자 문의하기",
    						"params": {
    							"roomId": existContact[0].id
    						}
    					});
        			} else{
        				if(await meta.confirm("문의하시겠습니까?")){
        					//새로 문의하기 방 생성
    						let createContactUs = (await meta.api.common.contactUs.createContactUs({
    							"lecturerId": this.select.lecturer,
    							"userId": this.data.user.id,
    							"completedYn": 'N'
    						})).data;
    						
    						var newPostKey = contactUsRef.push().key;
    						
    						const message = {
    								_id: newPostKey,
    								roomId: createContactUs.id,
    								content: "무엇을 도와드릴까요?",
    								senderId: this.select.lecturer,
    								senderNm: this.select.lecturerNm,
    								date: today,
    								timestamp: time,
    								edited : false,
    								deleted: false,
    								createdDate: new Date()
    						};
    						
    						var updates = {};
    						updates[newPostKey] = message;
    						await contactUsRef.update(updates);
    						
    						//마지막메세지 업데이트
    						(await meta.api.common.contactUs.lastUpdated({
    							"id": createContactUs.id,
    							"lastMsgContent": "무엇을 도와드릴까요?",
    							"lastMsgSenderId": this.select.lecturer
    						}));
    						
    						this.$router.push({
    							"name": "사용자 문의하기",
    							"params": {
    								"roomId": createContactUs.id
    							}
    						});
        				}
        			}
        		} else{
        			meta.alert("문의할 대상을 먼저 선택해주세요.");
        		}
        	},
        	//문의 목록으로 이동
        	"goContactUsList": async function(){
        		this.$router.push({
                    "path": "/mypage/users/contact-us"
                });
        	}
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            
            if(user){
                this.$set(this.data.user, "id", user.id);
                this.loadLecturerList();
            } else{
                await meta.alert("로그인이 필요한 서비스입니다.");
            } 
        },
        "created": async function() {
        }
    });
});