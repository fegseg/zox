<template>
  <div class="login-page">
    <div class="login">
      <div class="front-login">
        <h2 class="title">
          <a href="/" style="color: #000000a6">mucの药店</a>
        </h2>
        <a-form
          id="components-form-demo-normal-login"
          :form="form"
          class="login-form"
          @submit="handleSubmit"
        >
          <a-form-item>
            <a-input
              v-decorator="[
                'username',
                {
                  initialValue:
                    this.$route.params.key == undefined
                      ? this.$store.getters.getRemember.username
                      : this.$route.params.key.username,
                  rules: [{ required: true, message: '请输入您的用户名!' }],
                },
              ]"
              placeholder="用户名"
            >
              <a-icon
                slot="prefix"
                type="user"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
          </a-form-item>

          <a-form-item>
            <a-input-password
              v-decorator="[
                'password',
                {
                  initialValue:
                    this.$route.params.key == undefined
                      ? this.$store.getters.getRemember.password
                      : this.$route.params.key.password,
                  rules: [{ required: true, message: '请输入账号密码!' }],
                },
              ]"
              placeholder="密码"
              autocomplete
            >
              <a-icon
                slot="prefix"
                type="lock"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input-password>
          </a-form-item>
          <a-form-item>
            <a-input
              v-decorator="[
                'verificationCode',
                {
                  rules: [{ required: true, message: '请输入验证码!' }],
                },
              ]"
              placeholder="验证码"
              class="verify-code"
            >
              <a-icon
                slot="prefix"
                type="code"
                style="color: rgba(0, 0, 0, 0.25)"
              />
            </a-input>
            <a href="javascript:;">
              <img
                style="width: 130px; height: 48px"
                :src="verificationUrl"
                @click="refreshVerification()"
              />
            </a>
          </a-form-item>
          <a-form-item>
            <a-checkbox
              v-decorator="[
                'remember',
                {
                  valuePropName: 'checked',
                  initialValue: true,
                },
              ]"
            >
              记住我
            </a-checkbox>
            <a class="login-form-forgot" href="/password/update"> 忘记密码 </a>
            <a-button
              type="primary"
              html-type="submit"
              class="login-form-button"
            >
              登&nbsp;录
            </a-button>
            还没有账号，前往
            <a href="/register" target="blank">注册</a>！
          </a-form-item>
        </a-form>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "normal_login" });
    console.log("登录form====>", this.form);

    console.log("username====>", this.form.getFieldValue("username"));
    console.log(this.$route.params);
    console.log(this.$route.params.key == undefined);
  },
  created() {},
  data() {
    return {
      verificationUrl: this.$store.state.imgBaseUrl + "verification",
    };
  },
  methods: {
    /* 提交登录 */
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFields((err, values) => {
        if (!err) {
          const _this = this;
          this.$axios.post("/user/login", values).then((res) => {
            console.log(res);
            if (res.data.message === "登录成功") {
              this.$message.success(res.data.message + "！");
              setTimeout(() => {
                const token = res.headers["authorization"];
                const userInfo = res.data.data.userInfo;
                // 共享用户信息
                _this.$store.commit("SET_TOKEN", token);
                _this.$store.commit("SET_USERINFO", userInfo);
                console.log(this.$route.path);
                console.log(this.$route);
                console.log(this.$router);
                // const { href } = this.$router.resolve({
                //   path: "/",
                // });
                // window.open(href, "_self");
                console.log("当前路径=>", this.$route.path);
                console.log("from路径=>", this.$route.form);
                console.log("go路径=>", this.$route.go);
                console.log(this.$route.params.key != undefined);
                if (this.$route.params.key != undefined) {
                  // this.$router.push("/");
                  const { href } = this.$router.resolve({
                    name: "Index",
                    path: "/"
                  });
                  window.open(href, "_self");
                } else {
                  this.$router.go(-1);
                }
              }, 100);

              if (values.remember) {
                let isRememberInfo = {
                  username: values.username,
                  password: values.password,
                  remember: values.remember,
                };
                _this.$store.commit("SET_REMEMBER", isRememberInfo);
              } else {
                _this.$store.commit("REMOVE_REMEMBER");
              }
            } else {
              _this.$message.error(res.data.message + "！");
              _this.refreshVerification();
            }
          });
        }
      });
    },
    /* 刷新验证码 */
    refreshVerification() {
      this.verificationUrl =
        this.$store.state.imgBaseUrl + "/verification" + "?" + Math.random();
    },
  },
};
</script>
<style>
#components-form-demo-normal-login .login-form {
  max-width: 300px;
}
#components-form-demo-normal-login .login-form-forgot {
  float: right;
}
#components-form-demo-normal-login .login-form-button {
  width: 100%;
}
.login-page {
  background-image: url(../assets/img/04.png);
  background-repeat: no-repeat;
  height: 100%;
  padding: 58px 0;
  margin-top: 2px;
}

.login-page .front-login {
  background-color: #fff;
  width: 500px;
  height: 423px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  margin: auto;
  position: relative;
  padding: 28px 56px 0;
  text-align: left;
}

.login-page .front-login .title {
  text-align: center;
  margin-bottom: 18px;
  height: 38px;
  line-height: 38px;
}
.login-page .front-login .ant-input {
  height: 38px;
}
.login-page .front-login .verify-code {
  width: 200px;
  margin-right: 38px;
}
.login-page .front-login .ant-form-explain {
  font-size: 13px;
  text-align: left;
}
.login-page .front-login .login-form-button {
  height: 35px;
}
</style>