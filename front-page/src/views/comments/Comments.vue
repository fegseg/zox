<template>
  <div class="comments">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /> <span><a>我的</a></span
        ><a-icon class="icon-right" type="right" />
        <span><a>交易评价</a></span>
      </div>
    </div>

    <div class="center">
      <h2 class="title">
        订单编号：Y20211217201240bdc43
        <span style="margin: 0 50px">交易时间：{{ createTime }}</span>
        <a-button type="primary" @click="submit">提交</a-button>
      </h2>
      <div class="orderComments" v-for="(drug, index) in drugs">
        <div class="drugItem">
          <span class="drugName">药品名称：{{ drug.drugName }}</span>
          <span
            >满意指数：<a-rate v-model="drug.rate" :allowClear="false"
          /></span>
          <a-textarea
            placeholder="请填写评价信息"
            v-model="drug.message"
            :maxLength="100"
            :autoSize="{ minRows: 4, maxRows: 7 }"
          />
          <a-divider v-if="index != drugs.length - 1" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      drugs: [],
      createTime: "",
    };
  },
  created() {
    this.getDrugsItemByOrderId();
  },
  methods: {
    /* 获取订单的每一项药品进行评价 */
    getDrugsItemByOrderId() {
      console.log(this.$route.params.orderId);
      let formData = new FormData();
      formData.append("orderId", this.$route.params.orderId);
      this.$axios
        .post("/comments/user/order/drugs", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          console.log(res);
          this.drugs = res.data.data.responseBody;
          this.createTime = res.data.data.responseBody[0].gmtCreate;
        })
        .catch((err) => {});
    },
    /* 提交评价 */
    submit() {
      let msgArr = [];
      this.drugs.forEach((item) => {
        msgArr.push(item.message);
      });
      if (msgArr.includes("")) {
        this.$message.warning("评论不能为空！");
      } else {
        this.$axios
          .post("/comments/submit", this.drugs, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          })
          .then((res) => {
            if (res.data.status) {
              this.$message.success("评价提交成功！");
              this.$router.push(
                "/mine/order/" + this.$store.getters.getUser.username
              );
            } else {
              this.$message.error("操作失败！");
            }
          });
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import url("./style/comments.less");
</style>
