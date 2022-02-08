var AlgoPage;
AlgoPage = Vue.component("algo-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/testpage/algo/main.html")).data,
        "data": function () {
            return {
                "data":{
                    "user":{}
                },
                "type": ['전체','탄산음료','이온음료','과일쥬스','에너지드링크'],
                "drinkList":[],
                "buyDrinkList":[],
                checkAll: false,
                checkedDrink: [],
                isIndeterminate: true
            }
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            handleCheckAllChange(val) {
              this.checkedDrink = val ? this.drinkList : [];
              this.isIndeterminate = false;
            },
            handleCheckedDrinkChange(value) {
              let checkedCount = value.length;
              this.checkAll = checkedCount === this.drinkList.length;
              this.isIndeterminate = checkedCount > 0 && checkedCount < this.drinkList.length;
            },
            "loadDrinkList": async function(){
                let drinkList = (await meta.api.common.drink.getDrinkList({
                    "page": 1,
                    "rowSize": 100000000,
                })).data.items;
                console.log(drinkList);
                this.drinkList = drinkList;
            },
            "loadBuyDrink": async function(){
                let buyDrinkList = (await meta.api.common.buyDrink.getBuyDrinkList({
                    "page": 1,
                    "rowSize": 100000000,
                    "userId": this.data.user.id,
                    "sort": ['drink_id,asc']
                })).data.items;
                console.log(buyDrinkList);
                this.buyDrinkList = buyDrinkList;
            },
            "buyDrink": async function(){
                let drinkIdList=[],
                    param={};
                if(this.data.user.id){
                    if(this.checkedDrink.length === 0){
                        meta.alert("하나이상의 음료를 선택해주세요!");
                    } else{
                        this.checkedDrink.forEach(e => {
                            drinkIdList.push(e.id);
                        });
                        param.userId = this.data.user.id;
                        param.drinkIdList = drinkIdList;

                        await meta.api.common.buyDrink.createBuyDrink(param);
                        meta.alert("음료가 정상적으로 담겼습니당");
                        this.checkedDrink = [];
                        this.$router.go();
                    }
                }else{
                    meta.alert("로그인 후 담아주세요!");
                }
            }
        },
        "mounted": async function () {
            this.loadDrinkList();
            let user = _.cloneDeep(store.state.app.user);
            if(user){
                this.$set(this.data.user, "id", user.id);
                this.$set(this.data.user, "nickname", user.nickname);
                this.loadBuyDrink();
            }
        },
        "created": async function () {
        },
    });
});