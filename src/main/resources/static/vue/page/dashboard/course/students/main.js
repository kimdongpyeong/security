var StudentList;
StudentList = Vue.component("dashboard-students-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/course/students/main.html")).data,
        "data": function() {
            return {
                data: {
                    lecturer:{
                        id: null,
                    },
                    student:{
                        name: null,
                        phoneNum: null,
                        email: null,
                        gender: null,
                        educationCd: null,
                        education: null,
                        lectureType: null,
                        lectureName: null,
                        startDate: null,
                        endDate: null,
                        week: [],
                        lectureNum: null,
                        studentList: [],
                        studentSearch: null,
                    }
                },
                "rules": {
                    "email": (value) => {
                        let message, flag
                        flag = false;
                        if (value == null) {
                            message = '이메일을 입력해 주세요.'
                        } else if (!/^[0-9a-zA-Z]([~'!'#$%^&*()-_+<>?:{}]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/g.test(value)) {
                            message = '이메일 형식에 맞지 않는 메일 주소입니다. 다시 입력해 주세요.'
                        } else {
                            flag = true;
                        }
                        return flag || message;
                    },
                },
                "flag": {
                    "emailFlag": true,
                    "emailText": null,
                    "saveFlag":false
                },
                "visible": {
                    "en": false,
                },
            };
        },
        "computed": {
        },
        "watch": {
            "data.student.week": function(val) {
                console.log(val);
            }
        },
        "methods": {
            async sendMessage(id){
                this.$router.push({
                    "path": "/dashboard/course/sendMessage",
                });
                console.log(id)
            },
            // 학생 등록 부분
            async save(){
                if(await meta.confirm("학생을 등록 하시겠습니까?")) {
                    let studentSave= await meta.api.common.student.createStudent({
                        "name": this.data.student.name,
                        "phoneNum": this.data.student.phoneNum,
                        "email": this.data.student.email,
                        "gender": this.data.student.gender,
                        "educationCd": this.data.student.educationCd,
                        "lectureType": this.data.student.lectureType,
                        "lectureName": this.data.student.lectureName,
                        "startDate": this.data.student.startDate,
                        "endDate": this.data.student.endDate,
                        "lectureNum": this.data.student.lectureNum,
                        "createdBy": this.data.lecturer.id
                    });

                    await meta.api.common.student.createStudentDay({
                        "lecturerStudentId": studentSave.data.id,
                        "day": this.data.student.week
                    })
                    await meta.alert("등록 완료 되었습니다.");
                }
            },
            
            // 불러오기 부분
            async LoadStudent(){
                //리스트로 보내기
                // 강사 ID로 학생 목록 불러오기
                let student = (await meta.api.common.student.loadStudent({"lecturerId": this.data.lecturer.id}));
                
                await meta.alert(student.data + "명의 학생이 등록 되었습니다.");
                
                this.LoadList();
                this.visible.en = false;

                //tb_invite_student 테이블에 강사 ID로 학생 정보들을 불러와서 for문으로 학생 리스트를 tb_lecturer_student 테이블에 값을 insert시키려 햇었습니다.
                //lecturer_student테이블 insert

//                var i;
//                for ( i = 0; i < student.length; i++){
//                    //tb_invite_student 테이블에 연결 여부 'Y'로 변환
//                    await meta.api.common.student.modifyLinkYn(student[i].studentId, {"linkYn":'Y'});
//                    //tb_invite_student 테이블에 있는 학생을 불러와서 tb_lecturer_student테이블에 insert
//                    await meta.api.common.student.insertStudent({
//                            "lecturerId": this.data.lecturer.id,
//                            "studentId": student[i].studentId,
//                            "name": student[i].userName,
//                            "phoneNum": student[i].userPhone,
//                            "email": student[i].userEmail,
//                            "lectureName": student[i].classTitle,
//                            "createdBy": student[i].lecturerId,
//                            "linkYn": student[i].linkYn,
//                        })
//                }

                //팀장님이 한번 봐주시는 걸로
//                var i;
//                for ( i = 0; i < student.length; i++){ 
//                    var userList= [];
//                    userList = (await meta.api.common.user.getUserList({
//                        "page": 1,
//                        "rowSize": 100000000,
//                        "userId": student[i].studentId,
//                        "status": "T",
//                    })).data.items;
//                    this.data.student.studentList[i] = userList;
//                }
//                console.log(this.data.student.studentList)
                
            },

            async LoadList(){
                studentList = (await meta.api.common.student.getStudentList({
                    "createdBy": this.data.lecturer.id,
                    "name": this.data.student.studentSearch,
                    "page": 1,
                    "rowSize": 10000,
                })).data.items;
                studentList.forEach(x => {
                    x.lectureType = (x.lectureType == "O")? "온라인" : "오프라인";
                });                
                this.data.student.studentList = studentList; 
                console.log(this.data.student.studentList)
            },
            //학생 상세 페이지
            async detail(id){
                this.$router.push({
                    "path": "/dashboard/course/students/detail",
                    "query": {
                        "id": id
                    }
                });
                console.log(id)
            },
            handleCheckedDrinkChange(value) {
              let checkedCount = value.length;
              this.checkAll = checkedCount === this.drinkList.length;
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.drinkList.length;
            },
        },
        "created": function () {
        },
        "mounted": async function () {
            this.LoadList();
            
            let codeList = (await meta.api.common.code.getCodeList({
                "page": 1,
                "rowSize": 100000000,
                "parentId": 28
            })).data.items;
            this.data.student.education = codeList;
            
            let user = _.cloneDeep(store.state.app.user);
            this.data.lecturer.id = user.id;
        }
    });
});