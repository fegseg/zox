<template>
  <div class="updatePassword">
    <a-divider><h3 class="title">找回密码</h3></a-divider>
    <div class="center">
      <div class="updateForm">
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
                  ],
                },
              ]"
              placeholder="请输入您的账号"
            />
            <span slot="label"> 账号 </span>
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
              placeholder="请输入您注册时使用的邮箱"
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
                  placeholder="请输入验证码"
                />
              </a-col>
              <a-col :span="12">
                <a-button
                  class="btn-get-emailCode"
                  @click="getEmailCode()"
                  :disabled="isDisabled"
                  >{{ emailCodeBtnTitle }}</a-button
                >
              </a-col>
            </a-row>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="新密码" has-feedback>
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
              placeholder="请输入新密码"
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
              placeholder="请再次输入密码"
            />
          </a-form-item>

          <a-form-item v-bind="tailFormItemLayout">
            <a-button type="primary" html-type="submit" class="btn-update">
              立即修改
            </a-button>
          </a-form-item>
        </a-form>
      </div>
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
    this.form = this.$form.createForm(this, { name: "update" });
  },
  methods: {
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          console.log("Received values of form: ", values);
          const _this = this;
          this.$axios.post("/user/password/update", values).then((res) => {
            console.log(res);
            if (res.data.message === "修改成功") {
              _this.$message.success(res.data.message + "！");
              // const { href } = this.$router.resolve({
              //   path: "/login",
              // });
              // window.open(href, "_self");
              _this.$router.push("/login");
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
  },
};
</script>


<style lang="less" >
.updatePassword {
  width: 1200px;
  margin: auto;
  margin-bottom: 28px;

  .title {
    color: #000000d9;
    font-size: 22px;
  }

  .center {
    // background-color: #fff;
    border-radius: 10px;
    color: #606266;
    padding: 0 38px;
    min-height: 400px;

    .updateForm {
      width: 600px;
      height: 476px;
      margin: auto;
      padding: 50px 90px 38px 0;
      background-color: #fff;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

      .ant-input {
        height: 37px;
      }

      .ant-form-explain {
        font-size: 13px;
        float: left;
      }

      .emailCode {
        width: 200px;
      }

      .btn-get-emailCode {
        width: 130px;
        height: 37px;
        margin-left: 36px;
      }

      .btn-update {
        width: 300px;
        height: 37px;
        margin-left: -50px;
      }
    }
  }
}
</style>
