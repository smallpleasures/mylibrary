import {getRequest, postRequest, putRequest, deleteRequest} from "@/utils/api";

export default {
    install(Vue) {
        Vue.prototype.getRequest = getRequest
        Vue.prototype.postRequest = postRequest
        Vue.prototype.putRequest = putRequest
        Vue.prototype.deleteRequest = deleteRequest
    },
}