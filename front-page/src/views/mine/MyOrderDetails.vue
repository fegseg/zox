<template>
  <div class="myOrderDetails">
    <div class="breadcrumb">
      <router-link :to="{ path: '/' }">
        <span style="font-weight: normal">首页</span></router-link
      >
      <a-icon class="icon-right" type="right" />
      <a :href="'/mine/order/' + username">交易订单</a>
      <a-icon class="icon-right" type="right" />
      <span>订单详情</span>
    </div>

    <div class="center">
      <div class="order_steps">
        <a-steps :current="orderInfo.status + 1" status="process">
          <a-step title="购买药品" :description="orderInfo.gmtCreate" />
          <a-step title="付款" :description="orderInfo.gmtCreate" />
          <a-step title="药店发货" :description="orderInfo.status == 2 ?orderInfo.gmtCreate :''" />
          <a-step title="确认收货" :description="orderInfo.status == 3 ?orderInfo.gmtModified : ''" />
          <a-step title="评价" :description="orderInfo.status == 4 ?orderInfo.gmtModified : ''" />
        </a-steps>
      </div>
      <div class="orderInfo">
        <div class="body">
          <div class="title">订单信息</div>
          <div class="item">
            <p>
              <span>收货人：</span>&emsp;&emsp;&emsp;&emsp;{{
                orderInfo.receiptName
              }}
            </p>
            <p>
              <span>联系方式：</span>&emsp;&emsp;&emsp;{{ orderInfo.phone }}
            </p>
            <p>
              <span>收货地址：</span>&emsp;&emsp;&emsp;{{
                showAddress(orderInfo.receiptAddress)
              }}，{{ orderInfo.detailsAddress }}
            </p>
            <p>
              <span>买家留言：</span>&emsp;&emsp;&emsp;{{
                orderInfo.message == "" ? "---" : orderInfo.message
              }}
            </p>
            <p>
              <span>订单编号：</span>&emsp;&emsp;&emsp;{{ orderInfo.orderId }}
            </p>
            <p>
              <span>交易时间：</span>&emsp;&emsp;&emsp;{{ orderInfo.gmtCreate }}
            </p>
            <p>
              <span>交易状态：</span
              ><span style="color: #1890ffb5"
                >&emsp;&emsp;&emsp;{{ judgeStatus(orderInfo.status) }}</span
              >
            </p>
            <p><span>商家：</span>&emsp;&emsp;&emsp;&emsp;&emsp;mucの药店</p>
            <p v-if="orderInfo.complaint == 1">
              <span>投诉状态：</span
              ><span style="color: red"
                >&emsp;&emsp;&emsp;投诉成功！请耐心等待商家为您处理...</span
              >
            </p>
            <p v-if="orderInfo.complaint == 1">
              <span>投诉理由：</span
              >&emsp;&emsp;&emsp;{{orderInfo.reason}}
            </p>
          </div>
        </div>
      </div>
      <div class="orderDetails">
        <a-table
          :columns="columns"
          :data-source="orderDetails"
          :pagination="false"
          :locale="{ emptyText: '还没有添加药品，快前往首页看看吧！' }"
        >
          <!-- 药品图片 -->
          <span slot="picture" slot-scope="text, record">
            <a href="javascript:;" @click="goToThisDrugPage(record.drugName)"
              ><img class="picture" :src="record.picture" alt="药品图片"
            /></a>
          </span>
          <!-- 药品信息 -->
          <span slot="drugInfo" slot-scope="text, record" class="drugInfo">
            <a href="javascript:;" @click="goToThisDrugPage(record.drugName)"
              ><span class="name">{{ record.drugName }}</span></a
            ><br />
            <span class="standard">规格：{{ record.standard }}</span>
          </span>
          <!-- 单价 -->
          <span slot="price" slot-scope="text, record" class="price">
            ￥{{ parseFloat(record.price).toFixed(2) }}
          </span>
          <!-- 数量 -->
          <span slot="amount" slot-scope="text, record" class="amount">
            {{ record.amount }}
          </span>
          <!-- 优惠方式 -->
          <span slot="discountTitle"
            >优惠方式
            <a-tooltip
              ><a-icon type="question-circle" />
              <template slot="title"> 成为本店会员最高可享85折优惠 </template>
            </a-tooltip></span
          >
          <span slot="discount" slot-scope="text, record">
            <!-- {{ record.amount }} -->
            无优惠
          </span>
        </a-table>
      </div>
      <div class="payment">
        <p>
          <span>药品总价：</span
          ><span class="money"
            >￥{{ parseFloat(orderInfo.payment).toFixed(2) }}</span
          >
        </p>
        <p><span>优惠：</span><span class="money">无</span></p>
        <p><span>运费 ( 快递 ) ：</span><span class="money">￥0.00</span></p>
        <p>
          <span class="endPay">实付款：</span
          ><span class="money endPay"
            >￥{{ parseFloat(orderInfo.payment).toFixed(2) }}</span
          >
        </p>
      </div>
    </div>
  </div>
</template>

<script>
const columns = [
  {
    title: "药品图片",
    dataIndex: "picture",
    scopedSlots: { customRender: "picture" },
    align: "center",
  },
  {
    title: "药品信息",
    dataIndex: "drugInfo",
    scopedSlots: { customRender: "drugInfo" },
  },
  {
    title: "单价 (元)",
    dataIndex: "price",
    scopedSlots: { customRender: "price" },
    align: "center",
  },
  {
    dataIndex: "amount",
    title: "数量",
    scopedSlots: { customRender: "amount" },
    align: "center",
  },
  {
    dataIndex: "discount",
    slots: { title: "discountTitle" },
    scopedSlots: { customRender: "discount" },
    align: "center",
  },
];
export default {
  data() {
    return {
      columns,
      username: "",
      orderId: "",
      // 清单
      orderDetails: [],
      // 订单信息
      orderInfo: {},
    };
  },
  created() {
    this.username = this.$store.getters.getUser.username;
    this.orderId = this.$route.params.orderId;
    console.log(this.$route.params.orderId);
    this.getOrderDetails();
    this.getOrderInfo();
  },
  methods: {
    /* 根据订单编号获取订单清单细节 */
    getOrderDetails() {
      let formData = new FormData();
      formData.append("orderId", this.orderId);
      this.$axios
        .post("/order/details", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          let orderDetails = res.data.data.responseBody;
          let newArr = [];
          // 表格需要每一条数据有一个唯一key，我在这里给它加上
          for (const item of orderDetails) {
            let newItem = {
              key: item.id,
              picture: item.picture,
              amount: item.amount,
              drugName: item.drugName,
              gmtCreate: item.gmtCreate,
              price: item.price,
              standard: item.standard,
            };
            newArr.push(newItem);
          }
          this.orderDetails = newArr;
          console.log("清单", res.data.data.responseBody);
        });
    },
    /* 根据订单编号，获取订单的收货地址之类的信息 */
    getOrderInfo() {
      let formData = new FormData();
      formData.append("orderId", this.orderId);
      this.$axios
        .post("/order/info", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          this.orderInfo = res.data.data.responseBody;
          console.log("订单", res.data.data.responseBody);
        });
    },
    showAddress(value) {
      if (value != "" && value != null) {
        let arr = value.split("/");
        let str = "";
        for (const item of arr) {
          str += item;
        }
        return str;
      }
    },
    /* 跳转到这个药的详情页面 */
    goToThisDrugPage(drugName) {
      const { href } = this.$router.resolve({
        path: "/drug/" + drugName,
      });
      window.open(href, "_blank");
    },
    /* 判断交易状态 */
    judgeStatus(status) {
      switch (status) {
        case 0:
          return "交易取消！";
        case 1:
          return "等待发货中...";
        case 2:
          return "快递已到达，请您确认收货。";
        case 3:
          return "交易成功！";
        case 4:
          return "交易成功！";
        default:
          return "交易失败！";
      }
    },
  },
};
</script>

<style lang="less" scoped>
@import "./style/myOrderDetails.less";
</style>
