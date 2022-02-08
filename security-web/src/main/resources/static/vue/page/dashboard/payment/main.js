var DashboardPaymentMainPage;
DashboardPaymentMainPage = Vue.component("dashboard-payment-main-page", async function (resolve) {
    resolve({
        "template": (await axios.get("/vue/page/dashboard/payment/main.html")).data,
        "data": function () {
        },
        "computed": {
        },
        "watch": {
        },
        "methods": {
            "onClickReport": function() {
                window.open("https://creativepartners.co.kr/service/tax", "_blank");
            },
            "onClickAgree": function() {
                window.open("https://www.notion.so/Creative-Partners-edac2ebaa5cf40558bb1298fb7d21318", "_blank");
            },
        },
        "mounted": async function () {
            let user = _.cloneDeep(store.state.app.user);
            if(user)
                this.$set(this.data.user, "id", user.id);
        },
        "created": function () {
        },
    });
});