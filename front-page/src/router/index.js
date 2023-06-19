import Vue from 'vue'
import VueRouter from 'vue-router'

import routes from './routes'

Vue.use(VueRouter)


const router = new VueRouter({
  base: '/dist' ,
  mode: 'history',
  base: process.env.BASE_URL,
  routes,
  scrollBehavior(to, from, savedPosition) {
    // return 期望滚动到哪个的位置
    return { x: 0, y: 0 }
  }
})

// 如果要设置标题，拦截后设置标题
router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = to.meta.title
  }
  next()
})


router.beforeEach((to, from, next) => {
  // 判断是否需要登录拦截
  if (to.meta.requireAuth) {
    const userInfo = window.sessionStorage.getItem("userInfo")
    // 本地存储没有信息，说明没有登录，跳转到登录页面
    if (userInfo == null || userInfo == "{}") {
      next({
        path: "/login",
      })
    } else {
      next();
    }
  } else { // 不需要拦截
    next();
  }

})


// 没有的页面就跳转到404
router.beforeEach((to, from, next) => {
  if (to.matched.length === 0) {
    next({ path: "/404" })
  } else {
    next()
  }
})


// 避免修改密码后，进行登录，登录成功会跳回上一个页面也就是修改密码的页面
/** 如果是从修改密码页面跳转到等登录页面，那么因为设置了登录成功会返回上一个页面，所以如果从登录页面跳转到
  修改密码页面，就重定向到主页。
  正常点击忘记密码的话，由于没有直接用路由跳转， 是没有路由路径的 from.path=""
**/
router.beforeEach((to, from, next) => {
  // console.log("to=>", to)
  // console.log("from=>", from)
  // console.log("next=>", next)
  // next()
  if (from.path === "/login" && to.path === "/password/update") {
    next({ path: "/" })
  } else {
    next()
  }
})

export default router
