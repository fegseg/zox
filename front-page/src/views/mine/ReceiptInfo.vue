<template>
  <div class="receiptInfo">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" />
        <span>我的</span>
        <a-icon class="icon-right" type="right" />
        <span>管理收货地址</span>
      </div>
    </div>

    <div class="center">
      <a-config-provider :locale="locale">
        <a-table
          :columns="columns"
          :data-source="data"
          :pagination="false"
          :loading="loading"
          bordered
          style="width: 100%"
        >
          <span slot="customTitle">收货人姓名</span>
          <span slot="receiptName" slot-scope="text">{{ text }} </span>
          <span slot="receiptAddress" slot-scope="text, record"
            >{{ showAddress(record.receiptAddress) }}
          </span>

          <span slot="status" slot-scope="text, record">
            <a-tag class="tag_default" color="cyan" v-if="record.isDefault == 1"
              >默认</a-tag
            >
            <span class="set_default" @click="setToDefault(record.id)" v-else
              >设置为默认</span
            >
          </span>
          <span slot="action" slot-scope="text, record">
            <UpdateReceiptInfo
              :rowInfo="record"
              class="update"
              @fatherMethod="getAddressInfo()"
              style="display: inline"
            >
              修改</UpdateReceiptInfo
            >
            <a-divider type="vertical" />
            <a-popconfirm
              title="确定要删除这条地址信息吗?"
              ok-text="确定"
              cancel-text="取消"
              @confirm="confirm(record.id)"
            >
              <span class="delete"> 删除 </span>
            </a-popconfirm>
          </span>
          <template slot="title" slot-scope="currentPageData">
            温馨提示：为了能及时将货物送至您的手中，请确保填写的是有效信息！&emsp;
            <SetReceiptInfo
              @fatherMethod="getAddressInfo()"
              class="add"
              style="display: inline"
              ><a-icon type="plus" /> 添加收货信息</SetReceiptInfo
            >
            （最多只能保存 8 条收货信息，已添加
            <span class="add_number">{{ data.length }}</span>
            条。）
          </template>
        </a-table>
      </a-config-provider>
    </div>
  </div>
</template>

<script>
import zhCN from "ant-design-vue/es/locale/zh_CN";
import SetReceiptInfo from "@/views/mine/components/SetReceiptInfo";
import UpdateReceiptInfo from "@/views/mine/components/UpdateReceiptInfo";
const columns = [
  {
    dataIndex: "receiptName",
    key: "receiptName",
    slots: { title: "customTitle" },
    scopedSlots: { customRender: "receiptName" },
    width: "12%",
  },
  {
    title: "联系电话",
    dataIndex: "phone",
    key: "phone",
    width: "13%",
  },
  {
    title: "收货地区",
    dataIndex: "receiptAddress",
    scopedSlots: { customRender: "receiptAddress" },
    key: "receiptAddress",
    width: "20%",
  },
  {
    title: "详细地址",
    dataIndex: "detailsAddress",
    key: "detailsAddress",
  },
  {
    title: "状态",
    key: "status",
    dataIndex: "status",
    scopedSlots: { customRender: "status" },
    width: "10%",
  },
  {
    title: "操作",
    key: "action",
    scopedSlots: { customRender: "action" },
    width: "12%",
  },
];

export default {
  components: { SetReceiptInfo, UpdateReceiptInfo },
  data() {
    return {
      data: [],
      columns,
      locale: zhCN,
      loading: true,
    };
  },
  created() {
    this.getAddressInfo();
  },
  methods: {
    getAddressInfo() {
      this.$axios
        .get("/query/receiptInfo/" + this.$store.getters.getUser.username, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          console.log(res);
          this.data = res.data.data.data;
          this.loading = false;
        });
    },
    setToDefault(id) {
      // console.log(id);
      this.$axios
        .get("/set/default/" + this.$store.getters.getUser.username + "/" + id, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          // console.log(res);
          this.getAddressInfo();
          this.$message.success("更改默认地址成功！");
        });
    },
    deleteOneInfo(id) {
       let formData = new FormData();
      formData.append("id", id);
      this.$axios.post("/receiptInfo/delete",formData,{
         headers: {
            Authorization: localStorage.getItem("token"),
          },
        }).then((res) => {
        this.getAddressInfo();
        this.$message.success("删除成功！");
      });
    },
    showAddress(value) {
      let arr = value.split("/");
      let str = "";
      for (const item of arr) {
        str += item;
      }
      return str;
    },
    confirm(id) {
      this.deleteOneInfo(id);
    },
    father() {
      console.log("父组件￥￥￥￥￥");
    },
  },
};
</script>

<style scoped>
@import "./style/receiptInfo.css";
</style>