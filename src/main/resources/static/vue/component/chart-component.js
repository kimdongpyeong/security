var FullcalendarMainComponent = Vue.component('chart-component', function (resolve, reject) {
    axios.get("/vue/component/chart-component.html").then(function (response) {
        resolve({
            "template": response.data,
            "props": {
                "config": {
                    "type": Object,
                    "default": function () {
                        return {};
                    }
                },
                "id": {
                    "type": String,
                    "default": function () {
                        return {};
                    }
                },
                "gradient": {
                    "type": Object,
                    "default": function () {
                        return {};
                    }
                },
                "gradientLine": {
                    "type": Object,
                    "default": function () {
                        return {};
                    }
                }
            },
            "data": function () {
                return {
                    "chart": undefined
                };
            },
            "watch": {
                "config.options.onClick": {
                    "handler": function (newValue, oldValue) {
                        this.config.options.onClick = newValue;
                    },
                    "deep": true
                },
                "config.options.onHover": {
                    "handler": function (newValue, oldValue) {
                        this.config.options.onClick = newValue;
                    },
                    "deep": true
                },
                "config.data.labels": {
                    "handler": function (newValue, oldValue) {
                        this.chart.data.labels = _.cloneDeep(newValue);
                        this.chart.update();
                    },
                    "deep": true
                },
                "config.data.datasets": {
                    "handler": function (newValue, oldValue) {
                        this.chart.data.datasets = _.cloneDeep(newValue);
                        this.chart.update();
                    },
                    "deep": true
                }
            },
            "created": function () {
            },
            "mounted": function () {
                var config;
                config = _.cloneDeep(this.config);
                
                if(this.gradient != null && this.gradient.length > 0) {
                    var gradient = this.$refs.chart.getContext('2d').createLinearGradient(0, 0, 0, 450);
                    var gradientLine = this.$refs.chart.getContext('2d').createLinearGradient(0, 0, 0, 450);

                    gradient.addColorStop(0, this.gradient[0]);
                    gradient.addColorStop(0.5, this.gradient[1]);
                    gradient.addColorStop(1, this.gradient[2]);
                    
                    gradientLine.addColorStop(0, this.gradientLine[0]);
                    gradientLine.addColorStop(0.5, this.gradientLine[1]);
                    gradientLine.addColorStop(1, this.gradientLine[2]);

                    this.$set(config.data.datasets[0], "backgroundColor", gradient);
                    this.$set(config.data.datasets[0], "borderColor", gradientLine);
                }

                this.chart = new Chart(this.$refs.chart.getContext('2d'), config);
            },
            "beforeDestroy": function () {
                this.chart.destroy();
                this.chart = undefined;
            }
        });
    });
});