const getters = {
  getUser: state => {
    return state.userInfo
  },
  getRemember: state => {
    if (state.rememberInfo != null) {
      if (state.rememberInfo.username != null) {
        return state.rememberInfo
      } else {
        return { username: "", password: "" }
      }
    } else {
      return { username: "", password: "" }
    }
  },
  getReceiptInfo: state => {
    return state.receiptInfo
  },
}

export default getters;
