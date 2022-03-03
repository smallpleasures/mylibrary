import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import 'font-awesome/css/font-awesome.min.css'
import plugin from "@/plugins/plugin";


Vue.use(ElementUI, {size:'small'})
Vue.use(plugin)

Vue.config.productionTip = false

const vc = new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')

console.log(vc)