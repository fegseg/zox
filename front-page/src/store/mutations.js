const mutations = {
  SET_TOKEN: (state, token) => {
    state.token = token
    localStorage.setItem("token", token)
  },
  SET_USERINFO: (state, userInfo) => {
    state.userInfo = userInfo
    sessionStorage.setItem("userInfo", JSON.stringify(userInfo))
    console.log("SET_USERINFO完成")
  },
  SET_REMEMBER: (state, rememberInfo) => {
    state.rememberInfo = rememberInfo
    sessionStorage.setItem("rememberInfo", JSON.stringify(rememberInfo))
    console.log("SET_REMEMBER完成")
  },
  SET_RECEIPTINFO: (state, receiptInfo) => {
    state.receiptInfo = receiptInfo
    console.log("SET_RECEIPTINFO完成")
  },
  REMOVE_INFO: (state) => {
    state.token = ""
    state.userInfo = {}
    localStorage.setItem("token", "")
    sessionStorage.setItem("userInfo", JSON.stringify({}))
  },
  REMOVE_REMEMBER: (state) => {
    state.rememberInfo = {}
    sessionStorage.setItem("rememberInfo", JSON.stringify({}))
  }
}

export default mutations;
