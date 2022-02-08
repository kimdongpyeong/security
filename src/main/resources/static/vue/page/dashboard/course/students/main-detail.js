var StudentListDetail;
StudentListDetail = Vue.component("dashboard-studentListDetail-page", async function(resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/course/students/main-detail.html")).data,
        "data": function() {
            return {
                data: {
                    studentId: null,
                    stduentInfo: [],
                    week: null,
                    "user": {},
                    "uploadList": [],
                    "dataList": [{
                        "title": "",
                        "desc": "",
                        "deleteYn": "N",
                        "files": [{
                            file: null
                        }]
                    }],
                    "dataFileList": [],
                    "delData": [],
                    "delFile": [],
                    subjectCode: null,
                    classCode: null,
                    profileModify: "N",
                    scheduleModify: "N",
                    grade: {
                        classInput: null,
                        getClass: null,
                        subjectInput: null,
                        getSubject: null,
                        date: null,
                        score: null,
                        specificity: null,
                        gradeList: [],
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
                        classRoom: null,
                    }
                },
            };
        },
        "computed": {
        },
        "watch": {
            "data.grade.getClass" : function(v){
                this.gradeList()
            },
            "data.grade.getSubject" : function(v){
                this.gradeList()
            },
        },
        "methods": {
            async LoadList(){
                this.data.stduentInfo = (await meta.api.common.student.getStudent(this.data.studentId)).data;
                this.data.stduentInfo.gender = this.data.stduentInfo.gender.replace('M','남').replace('W','여');
                this.data.stduentInfo.lectureType = this.data.stduentInfo.lectureType.replace('O','온라인').replace('F','오프라인');
                this.data.week = (await meta.api.common.student.getStudentWeek(this.data.studentId)).data;
                console.log(this.data.week)
            },
            
            //성적 저장
            async gradeSave(){
                if(await meta.confirm("성적을 등록 하시겠습니까?")) {
                    let studentSave= await meta.api.common.student.createStudentGrade({
                        "lecturerStudentId": this.data.studentId,
                        "classificationCd": this.data.grade.classInput,
                        "subjectCd": this.data.grade.subjectInput,
                        "gradeInputDate": this.data.grade.date,
                        "score": this.data.grade.score,
                        "specificity": this.data.grade.specificity,
                    });
                    this.gradeList();
                    await meta.alert("등록 완료 되었습니다.");
                }
            },
            
            //성적 조회
            async gradeList(){
                let gradeList= (await meta.api.common.student.getStudentGradeList({
                    "lecturerStudentId": this.data.studentId,
                    "classificationCd": this.data.grade.getClass,
                    "subjectCd": this.data.grade.getSubject,
                })).data.items;
                
                this.data.grade.gradeList = gradeList;
            },
            
            // 자료실 데이터 불러오기
            "loadDataList": async function(){
                let dataList = (await meta.api.common.lecturerUploadFile.getLecturerUploadList({
                    "page": 1,
                    "rowSize": 100000000,
                    "lecturerStudentId": this.data.studentId,
                    "deleteYn": "N",
                })).data.items;
                this.data.dataList = dataList;
                console.log(dataList)
                await this.loadDataFileList();
            },
            
            
            // 자료실 데이터 파일들 불러오기
            "loadDataFileList": async function(){
                var i;
                
                for(i=0; i<this.data.dataList.length; i++){
                    let files = (await meta.api.common.lecturerUploadFile.getLecturerUploadFileList({
                        "page": 1,
                        "rowSize": 100000000,
                        "lecturerStudentFileId": this.data.dataList[i].id,
                    })).data.items;
                    this.$set(this.data.dataList[i], "files", files);
                }
            },
            
            // 자료실 전체(data테이블) 생성 및 수정 
            "save": async function() {
                let self = this,
                    i = 0, j = 0;
                let formData = new FormData();
                console.log(self);
                
//                formData.append("lecturerId", self.data.user.id);
                formData.append("lecturerStudentId", this.data.studentId);
                for (i = 0; i < self.data.dataList.length; i++) {
                    if(self.data.dataList[i].id !== null && self.data.dataList[i].id !== undefined){
                        formData.append("params[" + i + "].id", self.data.dataList[i].id);
                    }
                    formData.append("params[" + i + "].title", self.data.dataList[i].title);
                    formData.append("params[" + i + "].desc", self.data.dataList[i].desc);
                    formData.append("params[" + i + "].deleteYn", self.data.dataList[i].deleteYn);
                    
                    for(j = 0; j < self.data.dataList[i].files.length; j++) {
                        var inputFile = document.getElementById("inputFile" + i + j).files[0];
                        
                        if(inputFile != undefined || inputFile != null){
                            formData.append("params[" + i + "].files[" + j +"]", inputFile);
                        }
                    }
                }

                let data = (await meta.api.common.lecturerUploadFile.sortLecturerUpload(formData)).data;
                if(self.data.delData.length !== 0 || self.data.delFile.length !== 0){
                    console.log(self.data.delData);
                    console.log(self.data.delFile);
                }

                await meta.alert("저장");
                this.loadDataList();
            },
            
            // data테이블 추가
            "addDataList": function() {
                this.data.dataList.push({
                    "title": "",
                    "desc": "",
                    deleteYn: "N",
                    "files": [
                        {file: null}
                    ]
                });
            },
            
            // data테이블 삭제
            "deleteDataList": async function(id, idx) {
                this.data.delData.push({"id": id });
                this.data.dataList.splice(idx, 1);
                if(this.data.dataList.length == 0){
                    this.data.dataList.push({
                        "title": "",
                        "desc": "",
                        "deleteYn": "Y",
                        "files": [
                            {file: null}
                        ]
                    });
                }
                /*let data = (await meta.api.common.lecturerUploadFile.sortLecturerUpload({"id":id, "deleteYn": "Y", } )).data;*/
                await meta.api.common.lecturerUploadFile.modifyLecturerUploadYn(id, {"deleteYn":'Y'});
            },
            
            // 파일 갯수 (dataFile테이블) 추가
            "addDataFileList": function(idx) {
                this.data.delFile.push({"id": id });
                
                this.data.dataList[idx].files.push({file: null})
            },
            
            // 파일 갯수 (dataFile테이블)삭제
            "deleteDataFileList": async function(id, idx, idx2) {
                this.data.delFile.push({"id": id });
                
                this.data.dataList[idx].files.splice(idx2, 1);

                if(this.data.dataList[idx].files.length == 0){
                    this.data.dataList[idx].files.push({
                        file: null,
                    });
                }
                await meta.api.common.lecturerUploadFile.removeLecturerUpload(id);
            },
            
            "fileDownload": async function(val, type){
                let originFilename = null;
                let filename = null
                if (type.toUpperCase() === 'HWP') {
                }else if (type.toUpperCase() === 'DOC') {
                }else if (type.toUpperCase() === 'PPT') {
                }else if (type.toUpperCase() === 'JPG') {
                }else if (type.toUpperCase() === 'PNG') {
                }else if (type.toUpperCase() === 'TXT') {
                }else if (type.toUpperCase() === 'PDF') {
                }
                try {
                    await meta.api.app.dataUpload.getFile({originFilename: val.originFileNm, filename: val.originFileNm, subPath: "form"});
                } catch (e) {
                    meta.alert("파일이 존재하지 않습니다.");
                }
            },
            
            "studentProfileModify": async function() {
                await meta.api.common.student.modifyStudent(this.data.studentId, this.data.stduentInfo)
            },
            "studentScheduleModify": async function() {
                var i;
                for( i = 0; i<this.data.week.length; i++){
                    await meta.api.common.student.modifyStudentWeek({
                        "id":this.data.week[i].id,
                        "lecturerStudentId":this.data.week[i].lecturerStudentId,
                        "dayNum":this.data.stduentInfo.dayNum,
                        "startDate":this.data.stduentInfo.startDate,
                        "endDate":this.data.stduentInfo.endDate,
                    })
                }
            },
        },
        "created": function () {
        },
        "mounted": async function () {
            this.data.studentId =_.cloneDeep(this.$route.query.id);
            this.LoadList();
            this.gradeList();
            
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
            await this.loadDataList();
             
            let classCode = (await meta.api.common.code.getCodeList({
                "page": 1,
                "rowSize": 100000000,
                "parentId": 113
            })).data.items;
            this.data.classCode = classCode;
            
            let subjectCode = (await meta.api.common.code.getCodeList({
                "page": 1,
                "rowSize": 100000000,
                "parentId": this.data.studentId
            })).data.items;
            this.data.subjectCode = subjectCode;
            
            let codeList = (await meta.api.common.code.getCodeList({
                "page": 1,
                "rowSize": 100000000,
                "parentId": 28
            })).data.items;
            this.data.student.education = codeList;
            
            allClassRoomList = (await meta.api.common.classRoom.getClassRoomList({
                "page": 1,
                "rowSize": 100000000,
                "deleteYn": "N"
            })).data.items;
            this.data.student.classRoom = allClassRoomList;
            
            console.log(allClassRoomList);
        }
    });
});