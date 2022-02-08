var SettingTermsDetailMainPage;
SettingTermsDetailMainPage = Vue.component("setting-terms-detail-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/setting/terms/main-detail.html")).data,
        "data": function () {
            return {
                "terms_detail": {
                    "panels": {
                        "list": [0]
                    },
                },
                "detailData" : {
                    "id" : null,
                    "title" : null,
                    "contents" : null,
                    "publishYn" : null,
                    "requiredYn" : null,
                    "createdDate" : null,
                    "lastModifiedDate" : null,
                },

            };
        },
        "watch": {
        },
        "methods": {

            "setTermsDeatil": async function (queryId) {
                var self = this;
                return new Promise(function (resolve, reject) {
                    Promise.resolve()
                        .then(function () {
                            return  meta.api.common.terms.getTerms(queryId);
                        })
                        .then(function (response) {
                            self.detailData = response.data;
                        })
                        .then(function () { resolve(); });
                });
            },

            "registerNewTerms" : async function () {
                var self = this;

                if(self.detailData.title == null || self.detailData.title == "") {
                    await meta.alert("약관 제목을 반드시 입력하세요.");
                    return;
                }
                if(self.detailData.contents == null || self.detailData.contents == "") {
                    await meta.alert("약관 내용을 반드시 입력하세요.");
                    return;
                }
                if(self.detailData.publishYn == null || self.detailData.publishYn == "") {
                    await meta.alert("게시여부를 반드시 선택하세요.");
                    return;
                }

                if(self.detailData.requiredYn == null || self.detailData.requiredYn == "") {
                    await meta.alert("필수여부를 반드시 선택하세요.");
                    return;
                }

                if (await meta.confirm("저장 하시겠습니까?")) {

                    var params = {
                            "title" : self.detailData.title,
                            "contents" : self.detailData.contents,
                            "publishYn" : self.detailData.publishYn,
                            "requiredYn" : self.detailData.requiredYn,
                    };

                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                return meta.api.common.terms.createTerms(params);
                            })
                            .then(function () { resolve(); });
                    });
                    await meta.alert('저장에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/terms"
                    });
                }
            },
            "updateTerms" : async function () {
                var self = this;

                if(self.detailData.title == null || self.detailData.title == "") {
                    await meta.alert("약관 제목을 반드시 입력하세요.");
                    return;
                }
                if(self.detailData.contents == null || self.detailData.contents == "") {
                    await meta.alert("약관 내용을 반드시 입력하세요.");
                    return;
                }
                if(self.detailData.publishYn == null || self.detailData.publishYn == "") {
                    await meta.alert("게시여부를 반드시 선택하세요.");
                    return;
                }

                if(self.detailData.requiredYn == null || self.detailData.requiredYn == "") {
                    await meta.alert("필수여부를 반드시 선택하세요.");
                    return;
                }

                if (await meta.confirm("수정 하시겠습니까?")) {

                    var params = {
                            "id" : self.detailData.id,
                            "title" : self.detailData.title,
                            "contents" : self.detailData.contents,
                            "publishYn" : self.detailData.publishYn,
                            "requiredYn" : self.detailData.requiredYn,
                    };

                    new Promise(function (resolve, reject) {
                        Promise.resolve()
                            .then(function () {
                                return meta.api.common.terms.modifyTerms(params.id , params);
                            })
                            .then(function () { resolve(); });
                    });
                    await meta.alert('수정에 성공했습니다.');
                    this.$router.push({
                        "path": "/settings/terms",
                    });
                }
            },

        },


        "mounted": function () {
            var queryId = this.$route.query.id;
            if(queryId != undefined && queryId!= null)
            {
                Promise.resolve()
                    .then(this.setTermsDeatil(queryId),
                        );

            }
        },
    });
});