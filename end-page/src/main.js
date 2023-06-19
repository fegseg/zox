import Vue from 'vue'
import App from './App.vue'

// router 路由跳转
import router from './router'

// vueX 状态共享
import store from './store'

// axios 发送请求
import axios from 'axios'
import './network/axios'
Vue.prototype.$axios = axios

// element-ui 全局
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
Vue.use(ElementUI);

// 重写message方法，限制个数
import { message } from '@/utils/resetMessage';
Vue.prototype.$message = message;
import "@/assets/less/message.less"

//Chrome51 版本以后，Chrome 增加了新的事件捕获机制－Passive Event Listeners；
//解决 ===> 违反：没有添加被动事件监听器来阻止'touchstart'事件，请考虑添加事件管理者'passive'，以使页面更加流畅。
import 'default-passive-events'



Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
