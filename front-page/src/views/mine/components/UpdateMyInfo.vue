<template>
  <div>
    <a-button type="link" @click="showModal">
      <u>我要修改</u>
    </a-button>
    <a-modal
      class="update-myInfo-modal"
      title="个人资料 - 修改"
      :visible="visible"
      :confirm-loading="confirmLoading"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="没必要，懒得改啦..."
      okText="填好啦，立即修改！"
    >
      <a-config-provider :locale="locale">
        <a-form :form="form" @submit="handleSubmit">
          <a-form-item v-bind="formItemLayout" label="昵称">
            <a-input
              v-decorator="[
                'nickName',
                {
                  initialValue: user.nickName,
                  rules: [
                    {
                      required: true,
                      message: '请填写您的昵称!',
                      whitespace: true,
                    },
                    {
                      min: 3,
                      max: 20,
                      message: '昵称长度不能小于3个字符，不得超过20个字符!',
                      whitespace: true,
                    },
                  ],
                },
              ]"
              placeholder="有效长度在3~20个字符之间"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="真实姓名">
            <a-input
              v-decorator="[
                'realName',
                {
                  initialValue: user.realName,
                  rules: [
                    {
                      required: true,
                      message: '请填写您的真实姓名!',
                      whitespace: true,
                    },
                    {
                      max: 20,
                      message: '不得超过20个字符!',
                      whitespace: true,
                    },
                  ],
                },
              ]"
              placeholder="请填写您的名字"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="性别">
            <a-radio-group v-model="genderValue" @change="genderChange">
              <a-radio :value="1"> 男 </a-radio>
              <a-radio :value="2"> 女 </a-radio>
            </a-radio-group>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="生日">
            <a-date-picker
              :format="dateFormat"
              v-decorator="[
                'date-picker',
                {
                  initialValue: moment(
                    moment(
                      user.birthday == null ? '2000-01-01' : user.birthday
                    ),
                    dateFormat
                  ),
                  rules: [
                    {
                      type: 'object',
                      required: true,
                      message: '请选择您的出生日期！',
                    },
                  ],
                },
              ]"
              placeholder="点击选择日期"
              @change="birthdayChange"
            />
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="联系手机">
            <a-input
              v-decorator="[
                'phone',
                {
                  initialValue: user.phone,
                  rules: [
                    {
                      required: true,
                      message: '请输入您的手机号码！',
                      whitespace: true,
                    },
                    {
                      min: 11,
                      max: 11,
                      message: '请输入11位的手机号码！',
                      whitespace: true,
                    },
                  ],
                },
              ]"
              placeholder="请填写您的手机号码"
              style="width: 100%"
            >
              <a-select
                slot="addonBefore"
                v-decorator="['prefix', { initialValue: '86' }]"
                style="width: 70px"
              >
                <a-select-option value="86"> +86 </a-select-option>
                <a-select-option value="87"> +87 </a-select-option>
                <a-select-option value="88"> +88 </a-select-option>
                <a-select-option value="89"> +89 </a-select-option>
                <a-select-option value="90"> +90 </a-select-option>
              </a-select>
            </a-input>
          </a-form-item>
          <a-form-item v-bind="formItemLayout" label="所在地区">
            <a-cascader
              v-decorator="[
                'residence',
                {
                  initialValue: areaCodeArr,
                  rules: [
                    {
                      type: 'array',
                      required: true,
                      message: '请选择您的所在地区！',
                    },
                  ],
                },
              ]"
              placeholder="请选择所在地区"
              :options="options"
            />
          </a-form-item>
        </a-form>
      </a-config-provider>
    </a-modal>
  </div>
</template>

<script>
/* 导入的格式化时间的方法 */
import moment from "moment";
import zhCN from "ant-design-vue/es/locale/zh_CN";
/* 导入的地区信息 */
import { regionData, CodeToText, TextToCode } from "element-china-area-data";

export default {
  data() {
    return {
      // 用户信息
      user: {},
      // 地区选项
      options: regionData,
      // 性别的对应的genderValue，1男 2女
      genderValue: 1,
      // 生日时间的格式
      dateFormat: "YYYY-MM-DD",
      locale: zhCN,
      // 修改资料的modal是否显示，默认不显示，点击修改时变为true
      visible: false,
      // 回显地址的编码数组
      areaCodeArr: [],
      // 提交交是否显示加载动画
      confirmLoading: false,
      // 表单布局
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
      /* 要更新的用户信息,传给后端的 */
      userInfo: {
        username: this.$route.params.username,
        nickName: "",
        realName: "",
        gender: "",
        birthday: "",
        phone: "",
        address: "",
      },
    };
  },
  beforeCreate() {
    this.form = this.$form.createForm(this, { name: "receiverInfo" });
  },
  methods: {
    /* 改变modal的状态 */
    showModal() {
      this.echoTnfo();
      this.visible = true;
    },
    /* 操作时间的方法，导入 */
    moment,
    /* 根据用户名回显个人信息 */
    echoTnfo() {
      let username = this.$route.params.username;
      this.$axios
        .get("/front/user/info/" + username, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          this.user = res.data.data.data;
          // console.log(this.user);
          if (res.data.data.data.gender === "女") {
            this.genderValue = 2;
          } else {
            this.genderValue = 1;
          }
          this.showArea(res.data.data.data.address);
        });
    },
    /* 区域地址改变,传给后端的 */
    chooseArea(areaArr) {
      // 展示的你选择的值 是一个areaCode
      // 把areaCode转换成地名并拼接起来
      let str = "";
      for (let i = 0; i < areaArr.length; i++) {
        if (i < areaArr.length - 1) {
          str += CodeToText[areaArr[i]] + "/";
        } else {
          str += CodeToText[areaArr[i]];
        }
      }
      this.userInfo.address = str;
    },
    /* 前端展示从后端查到的地址 */
    showArea(address) {
      if (address != null && address != "") {
        let addressArr = address.split("/");
        let areaCodeArr = [];
        if (addressArr.length == 2) {
          areaCodeArr.push(TextToCode[addressArr[0]].code);
          areaCodeArr.push(TextToCode[addressArr[0]][addressArr[1]].code);
        } else if (addressArr.length == 3) {
          areaCodeArr.push(TextToCode[addressArr[0]].code);
          areaCodeArr.push(TextToCode[addressArr[0]][addressArr[1]].code);
          areaCodeArr.push(
            TextToCode[addressArr[0]][addressArr[1]][addressArr[2]].code
          );
        } else {
          return null;
        }
        this.areaCodeArr = areaCodeArr;
      } else {
        return null;
      }
    },
    /* 性别选项改变时触发 */
    genderChange(e) {
      // console.log("radio checked", e.target.value);
      let gender = e.target.value == 1 ? "男" : "女";
      this.userInfo.gender = gender;
    },
    /* 生日发生改变时触发 */
    birthdayChange(date, dateString) {
      this.userInfo.birthday = dateString;
    },
    /* 处理提交 */
    handleSubmit(e) {
      e.preventDefault();
      this.$axios
        .get("/has/user/" + this.$store.getters.getUser.username)
        .then((res) => {
          if (res.data.data.isLogin) {
            this.form.validateFieldsAndScroll((err, values) => {
              if (!err) {
                // console.log("Received values of form: ", values);
                this.chooseArea(values.residence);
                this.userInfo.nickName = values.nickName;
                this.userInfo.realName = values.realName;
                this.userInfo.phone = values.phone;
                if (this.userInfo.birthday === "") {
                  this.userInfo.birthday = "2000-01-01";
                }
                let gender = this.genderValue == 1 ? "男" : "女";
                this.userInfo.gender = gender;
                // console.log("userInfo====>" + this.userInfo);
                this.$axios
                  .post("/user/update/info", this.userInfo)
                  .then((res) => {
                    // console.log(res);
                    if (res.data.code == 200) {
                      this.confirmLoading = true;
                      setTimeout(() => {
                        this.visible = false;
                        this.confirmLoading = false;
                        this.$message.success("修改成功！");
                        this.$emit("fatherMethod");
                      }, 1000);
                    }
                  });
              }
            });
          } else {
            this.$message.error("检测到您的登录状态异常，请重新登录！");
            this.$store.commit("REMOVE_INFO");
            setTimeout(() => {
              const { href } = this.$router.resolve({
                path: "/login",
              });
              window.open(href, "_self");
            }, 1000);
          }
        });
    },
    /* 确定提交 */
    handleOk(e) {
      this.handleSubmit(e);
    },
    /* 取消 */
    handleCancel(e) {
      this.visible = false;
    },
  },
};
</script>

<style>
.update-myInfo-modal .ant-modal-content {
  width: 555px;
}
.update-myInfo-modal .ant-form {
  margin-right: 65px;
}
.update-myInfo-modal .ant-modal-body {
  padding: 24px 24px 6px 24px;
}
.update-myInfo-modal .ant-modal-footer {
  padding: 18px 16px;
}
.update-myInfo-modal .ant-modal-footer .ant-btn {
  height: 35px;
}
</style>
