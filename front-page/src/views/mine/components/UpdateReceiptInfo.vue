<template>
  <div class="set_receiptInfo">
    <span @click="showDrawer">
      <slot></slot>
    </span>
    <a-drawer
      title="收货地址信息 - 修改"
      :width="600"
      :visible="visible"
      @close="onClose"
    >
      <a-form :form="form" @submit="handleSubmit" style="margin-right: 80px">
        <a-form-item v-bind="formItemLayout" label="收货人">
          <a-input
            v-decorator="[
              'receiptName',
              {
                initialValue: $props.rowInfo.receiptName,
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
                initialValue: showArea($props.rowInfo.receiptAddress),
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
                initialValue: $props.rowInfo.detailsAddress,
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
                initialValue: $props.rowInfo.phone,
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
            :checked="$props.rowInfo.isDefault == 1"
            @change="toggleChecked"
          >
            设置为默认收货地址
          </a-checkbox>
        </a-form-item>
      </a-form>
      <div
        :style="{
          width: '100%',
          borderTop: '1px solid #e9e9e9',
          padding: '10px 16px',
          background: '#fff',
          textAlign: 'center',
          zIndex: 1,
        }"
      >
        <a-button :style="{ marginRight: '8px' }" @click="onClose">
          取消
        </a-button>
        <a-button type="primary" @click="handleOk"> 立即修改 </a-button>
      </div>
    </a-drawer>
  </div>
</template>
<script>
/* 导入的地区信息 */
import { regionData, CodeToText, TextToCode } from "element-china-area-data";
export default {
  props: ["rowInfo"],
  data() {
    return {
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
    this.form = this.$form.createForm(this, { name: "receiverInfo" });
  },
  methods: {
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
      if (this.$props.rowInfo.isDefault == 0) {
        this.$props.rowInfo.isDefault = 1;
      } else {
        this.$props.rowInfo.isDefault = 0;
      }
      console.log(this.$props.rowInfo.isDefault);
    },
    handleSubmit(e) {
      e.preventDefault();
      this.form.validateFieldsAndScroll((err, values) => {
        if (!err) {
          values.receiptAddress = this.chooseArea(values.receiptAddress);
          // values.isDefault = values.isDefault ? 1 : 0;
          // console.log("Received values of form: ", values);
          let shoppingAddress = {
            id: this.$props.rowInfo.id,
            username: this.$store.getters.getUser.username,
            receiptName: values.receiptName,
            receiptAddress: values.receiptAddress,
            detailsAddress: values.detailsAddress,
            phone: values.phone,
            isDefault: this.$props.rowInfo.isDefault,
          };
          console.log("Received values of form: ", shoppingAddress);

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

    showDrawer() {
      this.visible = true;
    },
    /* 取消 */
    onClose() {
      this.visible = false;
    },
  },
};
</script>

<style scoped>
</style>