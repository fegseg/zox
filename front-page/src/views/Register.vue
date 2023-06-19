<template>
  <div class="register-page">
    <h2 class="title"><router-link to="/">mucの药店</router-link>-注册账号</h2>
    <div class="register">
      <div class="register-form">
        <a-form :form="form" @submit="handleSubmit">
          <a-form-item v-bind="formItemLayout">
            <a-input
              v-decorator="[
                'username',
                {
                  rules: [
                    {
                      required: true,
                      message: '请填写您的账号！',
                      whitespace: true,
                    },
                    {
                      min: 6,
                      max: 20,
                      message: '账号的长度不能小于6位或大于20位字符！',
                      whitespace: true,
                    },
                  ],
                },
              ]"
            />
            <span slot="label">
              账号
              <a-tooltip
                title="账号是唯一的，不能与其他人一样，推荐使用手机号码。"
              >
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="邮箱地址">
            <a-input
              v-decorator="[
                'email',
                {
                  rules: [
                    {
                      type: 'email',
                      message: '请填写有效的邮箱地址！',
                    },
                    {
                      required: true,
                      message: '请填写您的邮箱地址！',
                    },
                  ],
                },
              ]"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="验证码">
            <a-row :gutter="8">
              <a-col :span="12">
                <a-input
                  class="emailCode"
                  v-decorator="[
                    'emailCode',
                    {
                      rules: [
                        {
                          required: true,
                          message: '请填写您收得的验证码！',
                        },
                      ],
                    },
                  ]"
                />
              </a-col>
              <a-col :span="12">
                <a-button
                  class="btn-get-emailCode"
                  :disabled="isDisabled"
                  @click="getEmailCode()"
                  >{{ emailCodeBtnTitle }}</a-button
                >
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="密 码" has-feedback>
            <a-input-password
              v-decorator="[
                'password',
                {
                  rules: [
                    { required: true, message: '请填写密码！' },
                    { min: 8, max: 20, message: '密码长度应该在8~20位之间！' },
                    { validator: validateToNextPassword },
                  ],
                },
              ]"
              type="password"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="确认密码" has-feedback>
            <a-input-password
              v-decorator="[
                'confirm',
                {
                  rules: [
                    {
                      required: true,
                      message: '请确认密码！',
                    },
                    {
                      validator: compareToFirstPassword,
                    },
                  ],
                },
              ]"
              type="password"
              @blur="handleConfirmBlur"
            />
          </a-form-item>
          <a-form-item v-bind="tailFormItemLayout">
            <a-checkbox
              v-decorator="['agreement', { valuePropName: 'checked' }]"
              @click="getIsAgreement()"
            >
              已阅读并同意
              <a href="/protocol/service-agreement" target="_blank">
                mucの药店服务协议
              </a>
            </a-checkbox>
          </a-form-item>
          <a-form-item v-bind="tailFormItemLayout">
            <a-button
              type="primary"
              html-type="submit"
              class="btn-register"
              :disabled="btn_disabled"
            >
              注册
            </a-button>
          </a-form-item>
        </a-form>
      </div>
    </div>
    <div class="register-footer">
      <p><a href="/" style="color: #000000a6">mucの药店</a></p>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      emailCodeBtnTitle: "获取验证码",
      count: 60,
      isDisabled: false,
      timer: "",
      btn_disabled: true,
      // 组件自带的
      confirmDirty: false,
      formItemLayout: {
        labelCol: {
          xs: { span: 24 },
          sm: { span: 8 },
        },
        wrapperCol: {
          xs: { span: 24 },
          sm: { span: 16 },
        },
      },
      tailFormItemLayout: {
        wrapperCol: {
          xs: {
            span: 24,
            offset: 0,
          },
          sm: {
            span: 16,
            offset: 8,
          },
        },
      },
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "register" });
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
          const _this = this;
          this.$axios.post("/user/register", values).then((res) => {
            console.log(res);
            if (res.data.message === "注册成功") {
              _this.$message.success(
                res.data.message + "！即将跳转至登录页面..."
              );
              setTimeout(() => {
                _this.$router.push({
                  name: "FrontLogin",
                  params: {
                    key: res.data.data.responseBody,
                  },
                });
              }, 2000);
            } else {
              _this.$message.error(res.data.message + "！");
            }
          });
        }
      });
    },
    handleConfirmBlur(e) {
      const value = e.target.value;
      this.confirmDirty = this.confirmDirty || !!value;
    },
    compareToFirstPassword(rule, value, callback) {
      const form = this.form;
      if (value && value !== form.getFieldValue("password")) {
        callback("请确认两次密码是否一致!");
      } else {
        callback();
      }
    },
    validateToNextPassword(rule, value, callback) {
      const form = this.form;
      if (value && this.confirmDirty) {
        form.validateFields(["confirm"], { force: true });
      }
      callback();
    },
    /* 发送请求，获取邮箱验证码 */
    getEmailCode() {
      let email = this.form.getFieldValue("email");
      if (email == "" || email == null) {
        this.$message.warning("请先输入邮箱！");
      } else {
        // 这里调用倒计时的方法，倒计时开始时，将按钮设为禁用状态
        this.timer = setInterval(this.countdown, 1000);
        this.isDisabled = true;
        this.$axios.get("/get/emailCode/" + email).then((res) => {
          console.log(res);
          this.$notification.open({
            type: "success",
            message: "提示",
            description: "验证码发送成功，请及时前往邮箱查看！（5分钟内有效）",
            placement: "bottomRight",
            duration: 2.5,
          });
        });
      }
    },
    /* 倒计时的方法，点击时被调用，点击获取验证码后，就可以倒计时 */
    countdown() {
      console.log("countDown");
      this.emailCodeBtnTitle = this.count + "s后重新获取";
      this.count--;
      if (this.count == -1) {
        this.isDisabled = false;
        this.emailCodeBtnTitle = "获取验证码";
        clearInterval(this.timer);
        this.count = 60;
      }
    },
    /* 拿到是否同意协议的结果 */
    getIsAgreement() {
      // console.log(!this.form.getFieldValue("agreement"));
      /* this.form.getFieldValue("agreement")刷新页面后点击checkbox返回是undefined,
      再点击没有选中返回的是true，这里刚刚好满足checkbox = true时按钮可用 */
      this.btn_disabled = this.form.getFieldValue("agreement");
    },
  },
};
</script>

<style >
.register-page .ant-form-explain {
  font-size: 13px;
}
.register-page .ant-input {
  height: 37px;
}
.register-page .emailCode {
  width: 200px;
}
.register-page .btn-get-emailCode {
  width: 130px;
  height: 37px;
  margin-left: 36px;
}
.register-page .btn-register {
  width: 300px;
  height: 37px;
}

.register-page {
  background-color: #fff;
  width: 100%;
  height: 100%;
  color: #000000a6;
}
.register-page .title {
  text-align: center;
  height: 66px;
  line-height: 66px;
}
.register-page .register {
  height: 600px;
  /* margin-top: 100px; */
  padding-top: 28px;
  background-image: url(../assets/img/bg_register.png);
  background-repeat: no-repeat;
}
.register-page .register-form {
  width: 600px;
  height: 538px;
  margin: auto;
  /* float: right; */
  padding: 50px 90px 38px 0;
  background-color: #fff;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  /* border-radius: 10px; */
}

.register-page .register-footer {
  text-align: center;
  background-color: #fff;
  height: 100px;
  padding-top: 33px;
}
.register-page .register-footer p {
  font-size: 13px;
  height: 10px;
  line-height: 10px;
}

@media screen and (max-width: 820px) {
  .register-page .register-form {
    width: 600px;
    height: 538px;
    padding: 68px 90px 38px 0;
    background-color: #fff;
    float: left;
  }

  .register-page .register-page .title {
    text-align: left;
    margin-left: 208px;
    height: 66px;
    line-height: 66px;
  }

  .register-page .register {
    height: 600px;
    padding-top: 28px;
    background-image: url(../assets/img/bg_register.png);
    background-repeat: no-repeat;
  }

  .register-page .register-footer {
    text-align: left;
    background-color: #fff;
    padding-left: 208px;
    height: 100px;
    padding-top: 33px;
  }
}

@media screen and (max-width: 558px) {
  .register-page .register-form {
    width: 600px;
    height: 700px;
    padding: 68px 90px 38px 0;
    background-color: #fff;
    float: left;
  }

  .register-page .title {
    text-align: left;
    height: 66px;
    line-height: 66px;
  }

  .register-page .register {
    height: 730px;
    /* margin-top: 100px; */
    padding-top: 10px;
    background-image: url(../assets/img/bg_register.png);
    background-repeat: no-repeat;
  }

  .register-page .register-footer {
    text-align: left;
    background-color: #fff;
    height: 100px;
    padding-top: 33px;
  }
}
</style>
