var PickerDatePickerComponent = Vue.component('picker-date-picker-component', function (resolve, reject) {
    axios.get("/vue/component/date-picker.html").then(function (response) {
        resolve({
            "template": response.data,
            "props": {
                "appendIcon": {
                    "type": String,
                    "default": undefined
                },
                "appendOuterIcon": {
                    "type": String,
                    "default": undefined
                },
                "backgroundColor": {
                    "type": String,
                    "default": undefined
                },
                "clearable": {
                    "type": Boolean,
                    "default": false
                },
                "dayFormat": {
                    "type": Function,
                    "default": function (value) {
                        return moment(value, "YYYY-MM-DD").format("D");
                    }
                },
                "dense": {
                    "type": Boolean,
                    "default": false
                },
                "disabled": {
                    "type": Boolean,
                    "default": false
                },
                "height": {
                    "type": [Number, String],
                    "default": undefined
                },
                "hideDetails": {
                    "type": Boolean,
                    "default": false
                },
                "label": {
                    "type": String,
                    "default": ""
                },
                "locale": {
                    "type": String,
                    "default": "ko-kr"
                },
                "outlined": {
                    "type": Boolean,
                    "default": false
                },
                "prependIcon": {
                    "type": String,
                    "default": undefined
                },
                "prependInnerIcon": {
                    "type": String,
                    "default": undefined
                },
                "readonly": {
                    "type": Boolean,
                    "default": true
                },
                "rules": {
                    "type": Array,
                    "default": []
                },
                "validationClass": {
                    "type": String,
                    "default": undefined
                },
                "type": {
                    "type": String,
                    "default": "date"
                },
                "value": {
                    "type": String,
                    "default": ""
                },
                "max": {
                    "type": String,
                    "default": moment().format("YYYY-MM-DD")
                },
                "min": {
                    "type": String,
                    "default": ""
                }
            },
            "data": function () {
                return {
                    "dialog": false
                };
            },
            "watch": {
                "value": {
                    "handler": function (newValue, oldValue) {
                        this.$emit("input", newValue);
                    }
                },
                "dialog": {
                    "handler": function (newValue, oldValue) {
                        newValue && this.$nextTick(function () {
                            this.$refs.datePicker.tableDate = this.value
                                    ? this.value.substring(0, 7)
                                    : moment().format('YYYY-MM');
                            this.$refs.datePicker.activePicker = this.type === "date" ? "DATE" : "MONTH";
                        });
                    }
                },
            },
            "methods": {
                "save": function () {
                    this.$refs.datePickerDialog.save(this.value);
                    this.$emit("input", this.value);
                }
            },
        });
    });
});