<template>
  <div id="components-layout-yjx-basic">
    <a-layout>
      <!-- 容器布局的头部 -->
      <a-layout-header>
        <div class="logo">
          <router-link to="/">
            <img src="../assets/1.jpg" width="" height="50" />
          </router-link>
        </div>
        <!-- 导航栏 -->
        <a-menu v-model="current" mode="horizontal" class="yjx-menu">
          <a-menu-item key="home" @click="goToHome()">
            <a-icon type="home" />首页
          </a-menu-item>
          <a-menu-item key="shoppingCart" @click="goToShoppingCart()">
            <a-icon type="shopping-cart" />购物车
          </a-menu-item>
          <a-menu-item key="star" @click="goToFavorites()">
            <a-icon type="star" />收藏夹
          </a-menu-item>
          <a-menu-item key="appstore">
            <a href="/category"> <a-icon type="appstore" />药品分类</a>
          </a-menu-item>
          <a-menu-item key="shopping-unknown"
            ><a href="/shopping/guide"><a-icon type="file-unknown" /> 购物指南</a>
          </a-menu-item>
          <a-menu-item key="help">
            <a href="/help"><a-icon type="file-protect" />售后服务</a>
          </a-menu-item>

          <a-menu-item class="login-register-item">
            <div class="login-register" v-if="isShowLogin">
              <a href="/login">登录</a>&nbsp;/
              <a href="/register" target="blank">注册</a>
            </div>
            <div class="user-avatar" v-else>
              <a-dropdown placement="bottomCenter" class="avatar-dropdown">
                <a class="ant-dropdown-link" @click="(e) => e.preventDefault()">
                  <a-avatar size="large" icon="user" :src="avatar" alt="头像" />
                </a>
                <a-menu slot="overlay" class="login-register-down">
                  <a-menu-item>
                    <span @click="goToMyInfo()">我的资料</span>
                  </a-menu-item>
                  <a-menu-item>
                    <span @click="goToMyOrder()">我的订单</span>
                  </a-menu-item>
                  <a-menu-item>
                    <span @click="goToReceiptInfo()">收货地址</span>
                  </a-menu-item>
                  <a-menu-item>
                    <span @click="logout()">退出账号</span>
                  </a-menu-item>
                </a-menu>
              </a-dropdown>
            </div>
          </a-menu-item>
        </a-menu>
      </a-layout-header>

      <!-- 容器布局的body部分 -->
      <a-layout-content>
        <router-view />
      </a-layout-content>

      <!-- 回到顶部 -->
      <back-top />

      <!-- 容器布局的底部 -->
      <a-layout-footer>
        <Footer />
      </a-layout-footer>
    </a-layout>
  </div>
</template>

<script>
import Search from "@/components/Search";
import BackTop from "@/components/BackTop.vue";
import Footer from "@/views/home/Footer";

export default {
  components: { Search, BackTop, Footer },
  data() {
    return {
      current: [],
      user: {},
      isShowLogin: true,
      avatar:
        "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png",
    };
  },
  methods: {
    /* 返回主页 */
    goToHome() {
      this.$router.push("/").catch((err) => {});
    },
    /* 登录成功就获取用户信息 */
    getUser() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          let user = this.$store.getters.getUser;
          this.user = user;
          if (user.avatar != null && user.avatar != "") {
            this.avatar = user.avatar;
          }
          this.isShowLogin = !this.isShowLogin;
          console.log("登录成功！");
        }
      } else {
        console.log("没有获取到用户信息。");
      }
    },
    /* 前往购物车页面 */
    goToShoppingCart() {
      if (this.$store.getters.getUser != null) {
        this.$axios
          .get("/has/user/" + this.$store.getters.getUser.username)
          .then((res) => {
            if (res.data.data.isLogin) {
              this.$router
                .push("/shopping/cart/" + this.user.username)
                .catch((err) => {});
            } else {
              if (
                this.$store.state.userInfo != null &&
                this.$store.state.userInfo.username != null
              ) {
                this.$message.error("检测到您的登录状态异常，请重新登录！");
                this.$store.commit("REMOVE_INFO");
                setTimeout(() => {
                  const { href } = this.$router.resolve({
                    path: "/login",
                  });
                  window.open(href, "_self");
                }, 1000);
              } else {
                this.$message.warning("登录之后才能查看购物车！");
              }
            }
          });
      } else {
        this.$message.warning("登录之后才能查看购物车！");
      }
    },
    /* 前往收藏夹页面 */
    goToFavorites() {
      if (this.$store.getters.getUser != null) {
        this.$axios
          .get("/has/user/" + this.$store.getters.getUser.username)
          .then((res) => {
            if (res.data.data.isLogin) {
              this.$router
                .push("/favorites/" + this.user.username)
                .catch((err) => {});
            } else {
              if (
                this.$store.state.userInfo != null &&
                this.$store.state.userInfo.username != null
              ) {
                this.$message.error("检测到您的登录状态异常，请重新登录！");
                this.$store.commit("REMOVE_INFO");
                setTimeout(() => {
                  const { href } = this.$router.resolve({
                    path: "/login",
                  });
                  window.open(href, "_self");
                }, 1000);
              } else {
                this.$message.warning("登录之后才能访问收藏夹！");
              }
            }
          });
      } else {
        this.$message.warning("登录之后才能访问收藏夹！");
      }
    },
    /* 前往个人资料页面 */
    goToMyInfo() {
      this.$axios
        .get("/has/user/" + this.$store.getters.getUser.username)
        .then((res) => {
          console.log(res.data.data.isLogin);
          if (res.data.data.isLogin) {
            this.$router
              .push("/mine/info/" + this.user.username)
              .catch((err) => {});
          } else {
            this.$message.error("检测到您的登录状态异常，请重新登录！");
            this.$store.commit("REMOVE_INFO");
            setTimeout(() => {
              const { href } = this.$router.resolve({
                path: "/login",
              });
              window.open(href, "_self");
            }, 500);
          }
        });
    },
    /* 前往我的订单页面 */
    goToMyOrder() {
      this.$axios
        .get("/has/user/" + this.$store.getters.getUser.username)
        .then((res) => {
          console.log(res.data.data.isLogin);
          if (res.data.data.isLogin) {
            this.$router
              .push("/mine/order/" + this.user.username)
              .catch((err) => {});
          } else {
            this.$message.error("检测到您的登录状态异常，请重新登录！");
            this.$store.commit("REMOVE_INFO");
            setTimeout(() => {
              const { href } = this.$router.resolve({
                path: "/login",
              });
              window.open(href, "_self");
            }, 500);
          }
        });
    },
    /* 前往收货地址页面 */
    goToReceiptInfo() {
      this.$axios
        .get("/has/user/" + this.$store.getters.getUser.username)
        .then((res) => {
          console.log(res.data.data.isLogin);
          if (res.data.data.isLogin) {
            this.$router
              .push("/mine/receiptInfo/" + this.user.username)
              .catch((err) => {});
          } else {
            this.$message.error("检测到您的登录状态异常，请重新登录！");
            this.$store.commit("REMOVE_INFO");
            setTimeout(() => {
              const { href } = this.$router.resolve({
                path: "/login",
              });
              window.open(href, "_self");
            }, 500);
          }
        });
    },
    /* 退出 */
    logout() {
      this.$axios
        .get("/has/user/" + this.$store.getters.getUser.username)
        .then((res) => {
          console.log(res.data.data.isLogin);
          if (res.data.data.isLogin) {
            this.$axios
              .get("/logout/" + this.$store.getters.getUser.username, {
                headers: {
                  Authorization: localStorage.getItem("token"),
                },
              })
              .then((res) => {
                this.$store.commit("REMOVE_INFO");
                this.isShowLogin = !this.isShowLogin;
                this.$message.success("退出成功！");
                this.$router.push("/").catch((err) => {});
              });
          } else {
            this.$message.error("检测到您的登录状态异常，请重新登录！");
            this.$store.commit("REMOVE_INFO");
            setTimeout(() => {
              const { href } = this.$router.resolve({
                path: "/login",
              });
              window.open(href, "_self");
            }, 500);
          }
        });
    },
  },
  created() {
    this.getUser();
  },
  mounted() {
    window.addEventListener("unload", function (event) {
      navigator.sendBeacon("/log", "foo=bar");
    });
  },
};
</script>

<style scoped>
/* 容器布局 */
#components-layout-yjx-basic {
  text-align: center;
}
#components-layout-yjx-basic .ant-layout-header {
  padding: 0;
  /* position: sticky; */
  /* top: 0px; */
  z-index: 100;
}
#components-layout-yjx-basic .ant-layout-footer {
  color: #000000a6;
}
#components-layout-yjx-basic .ant-layout-footer {
  line-height: 1.5;
}

#components-layout-yjx-basic .yjx-menu {
  line-height: 63px;
  width: 100%;
  text-align: left;
  box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.1);
}

#components-layout-yjx-basic .logo {
  width: 150px;
  height: 62px;
  float: left;
  margin-left: 8%;
  margin-right: 3%;
  font-size: 20px;
  font-weight: 700;
}
#components-layout-yjx-basic .logo img {
  margin-bottom: 6px;
}

#components-layout-yjx-basic .login-register-item {
  border: none !important;
  width: 40px;
  text-align: center;
  padding: 0;
  margin-left: 15%;
}
#components-layout-yjx-basic .login-register a {
  color: #000000a6;
}
#components-layout-yjx-basic .login-register a:hover {
  color: #1890ff;
}

.login-register-down {
  margin-top: 20px;
}
</style>
