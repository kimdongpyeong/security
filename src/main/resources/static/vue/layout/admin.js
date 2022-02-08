var AdminLayout;
AdminLayout = Vue.component("admin-layout", async function (resolve) { resolve({
    "template": (await axios.get("/vue/layout/admin.html")).data,
    "data": function () {
        return {
            "drawer": undefined
        };
    },
    "computed": {
        "name": function () {
            var name,
                user,
                person;
            name = "";
            user = _.cloneDeep(store.state.app.user);
            person = _.cloneDeep(store.state.app.person);
            if (user && person) {
                name = person
                        ? person.name
                        : user.username;
                name += " 님";
            }
            return name;
        }
    },
    "methods": {
        "logout": async function () {
            var token;
            token = meta.auth.getToken();
            await meta.auth.logout(token);
            if(this.$route.path === "/main") {
                this.$router.go();
            } else {
                this.$router.replace("/");
            }
        },
    },
    "mounted": function() {
        var headLink = document.createElement("link");

        headLink.type = "text/css";
        headLink.rel = "stylesheet";
        headLink.id = "vuetify-head";
        headLink.href = "/resources/lib/vuetify/2.4.5/css/vuetify.min.css";
        document.head.append(headLink);
    }
}); });