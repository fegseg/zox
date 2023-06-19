import Vue from 'vue'
import Vuex from 'vuex'

import mutations from './mutations.js'
import getters from './getters.js'
import actions from './actions.js'
import modules from './modules.js'

Vue.use(Vuex)

const state = {
  token: "",
  userInfo: JSON.parse(sessionStorage.getItem("userInfo")),
  rememberInfo: JSON.parse(sessionStorage.getItem("rememberInfo")),
  focusMode: false,
  // 图片的前缀url
  imgBaseUrl: "http://localhost:9000/",
  // imgBaseUrl: "http://121.41.91.37:9000/",
  // 收货地址信息
  receiptInfo: "",
}

export default new Vuex.Store({
  state,
  mutations,
  getters,
  actions,
  modules
})
