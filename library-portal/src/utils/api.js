import axios from "axios";
import {Message} from 'element-ui';

// 请求拦截器
axios.interceptors.request.use(config => {
    if (sessionStorage.getItem('tokenStr')) {
        config.headers.Authorization = sessionStorage.getItem('tokenStr')
    }
    return config
})

// 响应拦截器
axios.interceptors.response.use(success => {
    if (success.status && success.status === 200) {
        // 后端：500 业务逻辑错误，401 未登录，403 无权访问  业务逻辑错误
        if (success.data.code === 500 || success.data.code === 401 || success.data.code === 403) {
            Message.error({message: success.data.message})
            return
        }
        if (success.data.message) {
            Message.success({message: success.data.message})
        }
    }
    return success.data
}, error => { // 没访问到后端接口
    return
})

// 预备前置路径
let base = '';

// 传送 json 格式的 post 请求
export const postRequest = (url, params) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: params
    })
}

// 传送 json 格式的 get 请求
export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        data: params
    })
}

// 传送 json 格式的 put 请求
export const putRequest = (url, params) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: params
    })
}

// 传送 json 格式的 delete 请求
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        data: params
    })
}
