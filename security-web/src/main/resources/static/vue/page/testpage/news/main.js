var NewsPage;
NewsPage = Vue.component("news-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/news/main.html")).data,
        "data": function () {
            return {
                "data":{},
                "keyword":"",
                "newsList":[],
                "loadingFlag":false
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            "search": async function(){
                this.newsList=[];
                this.loadingFlag = true;
                if(this.keyword){
                    let newsList = (await meta.api.common.news.getNewsList({
                        "keyword":this.keyword
                    })).data;
                    this.newsList = newsList;
                    this.loadingFlag = false;
                } else{
                    meta.alert("키워드를 입력해주세요");
                }
            }
        },
        "mounted": async function () {
        },
        "created": async function () {
        },
    });
});