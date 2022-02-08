var AlertComponent;
AlertComponent = Vue.component("alert-component", async function (resolve) { resolve({
    "template": (await axios.get("/vue/component/alert.html")).data,
    "props": {
        "value": {
            "type": Boolean,
            "default": false
        },
        "message": {
            "type": String,
            "default": ""
        },
        "callback": {
            "type": Function,
            "default": function () {}
        }
    },
    "data": function () {
        return {
        };
    },
    "watch": {
        "value": function (newValue, oldValue) {
            this.$emit("input", newValue);
        }
    },
    "methods": {
        "ok": function () {
            this.value = false;
            this.callback();
        }
    }
}); });