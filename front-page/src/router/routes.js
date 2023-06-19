/* 前台相关的页面 */
const Home = () => import(/* webpackChunkName: "front" */"@/views/Home")
const FrontLogin = () => import(/* webpackChunkName: "front" */"@/views/Login")
const UpdatePassword = () => import(/* webpackChunkName: "front" */"@/views/UpdatePassword")
const Register = () => import(/* webpackChunkName: "front" */"@/views/Register")
const Test = () => import("@/views/Test")


// home 主页
const Drug = () => import(/* webpackChunkName: "home" */"@/views/home/Drug")
// category 分类
const CategoryList = () => import(/* webpackChunkName: "category" */"@/views/category/CategoryList")
const Category = () => import(/* webpackChunkName: "category" */"@/views/category/Category")
// drug_details 药品详细信息
const DrugDetails = () => import(/* webpackChunkName: "drug" */"@/views/drug_details/DrugDetails")
// protocol 协议
const ServiceAgreement = () => import(/* webpackChunkName: "protocol" */"@/views/protocol/ServiceAgreement")
// search 搜索
const SearchResult = () => import(/* webpackChunkName: "search" */"@/views/search/SearchResult")
// mine 我的
const MyInfo = () => import(/* webpackChunkName: "mine" */"@/views/mine/MyInfo")
const ReceiptInfo = () => import(/* webpackChunkName: "mine" */"@/views/mine/ReceiptInfo")
const MyOrder = () => import(/* webpackChunkName: "mine" */"@/views/mine/MyOrder")
const MyOrderDetails = () => import(/* webpackChunkName: "mine" */"@/views/mine/MyOrderDetails")
// comments 评价
const Comments = () => import(/* webpackChunkName: "mine" */"@/views/comments/Comments")
// shopping_cart 购物车
const ShoppingCart = () => import(/* webpackChunkName: "shopping_cart" */"@/views/shopping_cart/ShoppingCart")
// favorites
const Favorites = () => import(/* webpackChunkName: "favorites" */"@/views/favorites/Favorites")
// shoppingGuide 购物指南导航页面
const ShoppingGuide = () => import(/* webpackChunkName: "shoppingGuide" */"@/views/shopping_guide/ShoppingGuide")
// help 帮助中心
const HelpCenter = () => import(/* webpackChunkName: "help" */"@/views/help/HelpCenter")
// health_tips 建康小贴士
const HealthTipsDetails = () => import(/* webpackChunkName: "health_tips" */"@/views/health_tips/HealthTipDetails")
const HealthTipsCategorys = () => import(/* webpackChunkName: "health_tips" */"@/views/health_tips/HealthTipsCategorys")
const OneCategoryHealthTips = () => import(/* webpackChunkName: "health_tips" */"@/views/health_tips/OneCategoryHealthTips")
// order 订单
const Order = () => import(/* webpackChunkName: "order" */"@/views/order/Order")
// error 错误页面
const ErrorPage = () => import(/* webpackChunkName: "front" */"@/views/error/ErrorPage")
const WebSocket = () => import("@/views/websocket")




const routes = [
  {
    path: '/websocket',
    name: 'websocket',
    component: WebSocket
  },
  {
    path: '/',
    redirect: '/drug'
  },
  {
    path: '/register',
    name: 'Register',
    component: Register,
    meta: {
      title: 'mucの药店-注册'
    }
  },
  {
    path: "/home",
    name: "Home",
    component: Home,
    children: [
      {
        path: '/login',
        name: "FrontLogin",
        component: FrontLogin,
        meta: {
          title: 'mucの药店-登录'
        }
      },
      {
        path: '/password/update',
        name: "UpdatePassword",
        component: UpdatePassword,
        meta: {
          title: 'mucの药店-找回密码'
        }
      },
      {
        path: '/drug',
        name: 'Drug',
        component: Drug,
        meta: {
          title: "mucの药店-首页"
        }
      },
      {
        path: '/category',
        name: 'CategoryList',
        component: CategoryList,
        meta: {
          title: "mucの药店-全部药品分类"
        }
      },
      {
        path: '/category/:categoryName',
        name: 'Category',
        component: Category,
      },
      {
        path: '/drug/:name',
        name: 'DrugDetails',
        component: DrugDetails,
      },
      {
        path: '/protocol/service-agreement',
        name: 'ServiceAgreement',
        component: ServiceAgreement,
        meta: {
          title: "mucの药店-服务协议"
        }
      },
      {
        path: '/search/result/:value',
        name: 'SearchResult',
        component: SearchResult,
        meta: {
          title: "mucの药店-搜索结果"
        }
      },
      {
        path: '/mine/info/:username',
        name: 'MyInfo',
        component: MyInfo,
        meta: {
          title: "mucの药店-我的资料",
          requireAuth: true
        }
      },
      {
        path: '/mine/receiptInfo/:username',
        name: 'ReceiptInfo',
        component: ReceiptInfo,
        meta: {
          title: "mucの药店-我的收货地址",
          requireAuth: true
        }
      },
      {
        path: '/mine/order/:username',
        name: 'MyOrder',
        component: MyOrder,
        meta: {
          title: "mucの药店-我的交易订单",
          requireAuth: true
        }
      },
      {
        path: '/mine/order/details/:orderId',
        name: 'MyOrderDetails',
        component: MyOrderDetails,
        meta: {
          title: "mucの药店-订单详情",
          requireAuth: true
        }
      },
      {
        path: '/order/comments/:orderId',
        name: 'Comments',
        component: Comments,
        meta: {
          title: "mucの药店-交易评价",
          requireAuth: true
        }
      },
      {
        path: '/shopping/cart/:username',
        name: 'ShoppingCart',
        component: ShoppingCart,
        meta: {
          title: "mucの药店-我的购物车",
          requireAuth: true
        }
      },
      {
        path: '/favorites/:username',
        name: 'Favorites',
        component: Favorites,
        meta: {
          title: "mucの药店-我的收藏",
          requireAuth: true
        }
      },
      {
        path: '/shopping/guide',
        name: 'ShoppingGuide',
        component: ShoppingGuide,
        meta: {
          title: "mucの药店-购物指南",
        }
      },
      {
        path: '/help',
        name: 'HelpCenter',
        component: HelpCenter,
        meta: {
          title: "mucの药店-帮助中心",
        }
      },
      {
        path: '/order/confirm/',
        name: 'Order',
        component: Order,
        meta: {
          title: "mucの药店-订单信息确认",
          requireAuth: true
        }
      },
      {
        path: '/health/tips/details/:username/:title',
        name: 'HealthTipsDetails',
        component: HealthTipsDetails,
      },
      {
        path: '/health/tips/categorys',
        name: 'HealthTipsCategorys',
        component: HealthTipsCategorys,
        meta: {
          title: "mucの药店-健康资讯"
        }
      },
      {
        path: '/health/tips/:category',
        name: 'OneCategoryHealthTips',
        component: OneCategoryHealthTips,
      },
      {
        path: '/test',
        component: Test,
        meta: {
          title: "测试"
        }
      }
    ]
  },
  {
    path: "/404",
    name: "404",
    component: ErrorPage,
    meta: {
      title: "没有找到"
    }
  },
  // {
  //   path: "*",
  //   redirect: "/404"
  // }
]

export default routes
