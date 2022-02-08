var ConfirmComponent;
ConfirmComponent = Vue.component("confirm-component", async function (resolve) { resolve({
    "template": (await axios.get("/vue/component/confirm.html")).data,
    "props": {
        "value": {
            "type": Boolean,
            "default": false
        },
        "message": {
            "type": String,
            "default": ""
        },
        "oktext": {
            "type": String,
            "default": "확인"
        },
        "canceltext": {
            "type": String,
            "default": "취소"
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
        },
    },
    "methods": {
        "ok": function () {
            this.value = false;
            this.callback(true);
        },
        "cancel": function () {
            this.value = false;
            this.callback(false);
        }
    },
    "mounted": async function () {
    }
}); });