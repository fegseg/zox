<template>
  <div class="one-update-addressInfo">
    <a-button
      style="padding: 0; font-size: 16px"
      type="link"
      @click="showModal"
    >
      修改
    </a-button>
    <a-modal
      class="update-address-modal"
      title="收货信息 - 修改"
      :visible="visible"
      @ok="handleOk"
      @cancel="handleCancel"
      cancelText="取 消"
      okText="改好了，确 定"
    >
      <a-form :form="form" @submit="handleSubmit">
        <a-form-item v-bind="formItemLayout" label="收货人">
          <a-input
            v-decorator="[
              'receiptName',
              {
                initialValue: $props.item.receiptName,
                rules: [
                  {
                    required: true,
                    message: '请填写收货人名字！',
                    whitespace: true,
                  },
                  {
                    max: 12,
                    message: '不得超过12个字符！',
                    whitespace: true,
                  },
                ],
              },
            ]"
            placeholder="请填写收货人姓名"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="所在地区">
          <a-cascader
            v-decorator="[
              'receiptAddress',
              {
                initialValue: showArea($props.item.receiptAddress),
                rules: [
                  {
                    type: 'array',
                    required: true,
                    message: '请选择地区！',
                  },
                ],
              },
            ]"
            :options="options"
            placeholder="请选择所在地区"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="详细地址">
          <a-textarea
            v-decorator="[
              'detailsAddress',
              {
                initialValue: $props.item.detailsAddress,
                rules: [
                  {
                    required: true,
                    message: '请填写详细地址便于发货！',
                    whitespace: true,
                  },
                ],
              },
            ]"
            placeholder="请填写详细地址"
            :auto-size="{ minRows: 2, maxRows: 2 }"
            :maxLength="28"
          />
        </a-form-item>
        <a-form-item v-bind="formItemLayout" label="联系手机">
          <a-input
            v-decorator="[
              'phone',
              {
                initialValue: $props.item.phone,
                rules: [
                  {
                    required: true,
                    message: '请填写用于联系的手机号码!',
                  },
                  {
                    min: 11,
                    max: 11,
                    message: '请填写11位的手机号码！',
                    whitespace: true,
                  },
                ],
              },
            ]"
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
        <a-form-item v-bind="tailFormItemLayout">
          <a-checkbox
            :checked="$props.item.isDefault == 1"
            @change="toggleChecked"
          >
            设置为默认收货地址
          </a-checkbox>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script>
/* 导入的地区信息 */
import { regionData, CodeToText, TextToCode } from "element-china-area-data";

export default {
  props: ["item"],
  data() {
    return {
      // 修改资料的modal是否显示，默认不显示，点击修改时变为true
      visible: false,
      // 地区选项
      options: regionData,
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
    this.form = this.$form.createForm(this, { name: "updateReceiverInfo" });
  },
  created() {},
  methods: {
    /* 改变modal的状态 */
    showModal() {
      console.log(this.$props.item);
      this.visible = true;
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
      return str;
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
        return areaCodeArr;
      } else {
        return null;
      }
    },
    toggleChecked() {
      if (this.$props.item.isDefault == 0) {
        this.$props.item.isDefault = 1;
      } else {
        this.$props.item.isDefault = 0;
      }
      console.log(this.$props.item.isDefault);
    },
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          values.receiptAddress = this.chooseArea(values.receiptAddress);
          // values.isDefault = values.isDefault ? 1 : 0;
          console.log("Received values of form: ", values);
          let shoppingAddress = {
            id: this.$props.item.id,
            username: this.$props.item.username,
            receiptName: values.receiptName,
            receiptAddress: values.receiptAddress,
            detailsAddress: values.detailsAddress,
            phone: values.phone,
            isDefault: this.$props.item.isDefault,
          };

          this.$axios
            .post("/update/receiptInfo", shoppingAddress)
            .then((res) => {
              console.log(res);
              if (!res.data.status) {
                this.$message.warning(res.data.message);
              } else {
                this.$emit("fatherMethod");
                this.$message.success("修改成功！");
                this.visible = false;
              }
            });
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
.one-update-addressInfo {
  height: 25px;
  line-height: 25px;
}
.update-address-modal .ant-modal-content {
  width: 525px;
}
.one-update-addressInfo .ant-btn-link {
  color: #cc9977;
  font-size: 14px !important;
  height: 25px;
  line-height: 25px;
}
.one-update-addressInfo .ant-btn-link:hover {
  color: red;
}
.update-address-modal .ant-form {
  margin-right: 35px;
}
.update-address-modal .ant-modal-body {
  padding: 24px 34px 6px 14px;
}
.update-address-modal .ant-modal-footer {
  padding: 18px 16px;
}
.update-address-modal .ant-modal-footer .ant-btn {
  height: 35px;
}
</style>
