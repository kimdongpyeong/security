var ImageCustomComponent;
ImageCustomComponent = Vue.component("image-custom-component", async function (resolve) { resolve({
    "template": (await axios.get("/vue/component/image-custom-component.html")).data,
    props: {
        contain: {
            type: Boolean,
            default: true,
        },
        width: {
            type: String,
            default: 'auto',
        },
        height: {
            type: String,
            default: 'auto',
        },
        maxWidth: {
            type: String,
            default: '100%',
        },
        maxHeight: {
            type: String,
            default: '100%',
        },
        customClass: {
            type: String,
            default: '',
        },
        customStyle: {
            type: String,
            default: '',
        },
        src: {
            type: String,
            default: '',
        },
        overlayShow: {
            type: Boolean,
            default: false,
        },
        overlayContent: {
            type: Array,
            default() {
                return [
                    {
                        text: '',
                        show: false,
                    },
                ];
            },
        },
    },
    data() {
        return {
            isLoadingComplete: false,
            srcData: this.src,
        };
    },
    watch: {
        src: {
            async handler(n) {
                this.srcData = n;
            },
            deep: true,
        },
    },
    methods: {
        load(e) {
            this.isLoadingComplete = true;
            this.$emit('load', e);
        },
    },
    created() {
    },
    mounted() {
    },
}); });