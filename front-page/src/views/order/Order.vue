<template>
  <div class="order-info">
    <div class="header">
      <div class="breadcrumb">
        <span>购买</span>
        <a-icon class="icon-right" type="right" />
        <span>订单信息确认</span>
      </div>
    </div>

    <div class="center">
      <a-divider orientation="left">
        <OneCreateAddressInfo>
          <span slot="front"> 1、选择 /</span>
          <span slot="end"> &nbsp;新增收货地址</span>
        </OneCreateAddressInfo>
      </a-divider>
      <OneSelectAddress class="empty" />
      <a-divider orientation="left">2、确认订单信息</a-divider>
      <div class="order-page-second">
        <a-table
          :columns="columns"
          :data-source="data"
          :pagination="true"
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
          <!-- 小计 -->
          <span slot="subtotal" slot-scope="text, record" class="subtotal"
            >￥{{ parseFloat(record.amount * record.price).toFixed(2) }}</span
          >
        </a-table>
      </div>
      <a-divider orientation="left">3、提交订单</a-divider>

      <div class="submit-orders">
        <a-card class="card">
          <div class="submit-orders-left">
            <div class="orderRemark">
              <span>订单备注：</span>
              <a-textarea
                v-model="remark"
                placeholder="选填，如有重要需求，请尽量先与本店协商！"
                :auto-size="{ minRows: 3, maxRows: 5 }"
                :maxLength="255"
              />
            </div>
            <div class="agreement">
              <a-checkbox :checked="checked" @change="toggleChecked">
                我已阅读并同意:
                <a href=""> 《mucの药店交易条款》 </a>
              </a-checkbox>
            </div>
          </div>
          <div class="submit-orders-right">
            <p>
              共<span
                style="
                  color: #f34e0d;
                  font-size: 16px;
                  font-family: Verdana, Geneva, Tahoma, sans-serif;
                "
                >&nbsp;{{ totalNumber }}&nbsp;</span
              >种药品，合计：<span
                style="
                  color: #f34e0d;
                  font-size: 16px;
                  font-family: Verdana, Geneva, Tahoma, sans-serif;
                "
                >￥{{ parseFloat(totalMoney).toFixed(2) }}</span
              >
            </p>
            <p>
              优惠折扣：<span
                style="
                  display: inline-block;
                  color: #ff4d4f;
                  font-size: 16px;
                  width: 47px;
                  text-align: center;
                "
                >无</span
              >
            </p>
            <p>
              运费：<span
                style="
                  color: #f34e0d;
                  font-size: 16px;
                  font-family: Verdana, Geneva, Tahoma, sans-serif;
                "
                >￥0.00</span
              >
            </p>
            <p>
              付款金额：<span
                style="
                  color: #f34e0d;
                  font-size: 26px;
                  font-weight: 500;
                  font-family: Verdana, Geneva, Tahoma, sans-serif;
                "
                >￥{{ payment }}</span
              >
            </p>
            <p style="margin-top: 16px">
              <a-button
                type="danger"
                class="btn_submit"
                :disabled="!checked"
                :loading="loading"
                @click="submitOrder"
                >提交订单</a-button
              >
            </p>
          </div>
        </a-card>
      </div>
    </div>
  </div>
</template>

<script>
import OneCreateAddressInfo from "@/views/order/components/OneCreateAddressInfo";
import OneSelectAddress from "@/views/order/components/OneSelectAddress";

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
  {
    title: "小计 (元)",
    dataIndex: "subtotal",
    scopedSlots: { customRender: "subtotal" },
    align: "center",
  },
];
export default {
  components: {
    OneCreateAddressInfo,
    OneSelectAddress,
  },
  data() {
    return {
      data: [],
      columns,
      // 订单备注
      remark: "",
      totalNumber: 0,
      totalMoney: 0,
      payment: 0,
      checked: true,
      loading: false,
    };
  },
  created() {
    this.getShoppingCartInfo();
  },
  methods: {
    getShoppingCartInfo() {
      this.$axios
        .get("/what/buy/" + this.$store.getters.getUser.username)
        .then((res) => {
          let drugArr = res.data.data.data;
          // console.log(drugArr);
          this.$axios
            .post("/buy/these/" + this.$store.getters.getUser.username, drugArr)
            .then((res) => {
              console.log("要买的药是这些====>", res);
              this.data = res.data.data.data;
              if (this.data.length != 0) {
                this.getInfo();
              }
            });
        });
    },
    /* 跳转到这个药的详情页面 */
    goToThisDrugPage(drugName) {
      const { href } = this.$router.resolve({
        path: "/drug/" + drugName,
      });
      window.open(href, "_blank");
    },
    /* 获取订单信息 */
    getInfo() {
      this.$axios
        .get("/get/these/" + this.$store.getters.getUser.username)
        .then((res) => {
          console.log("提交订单的信息===>", res);
          this.totalNumber = res.data.data.data.length;
          let arr = res.data.data.data;
          for (const item of arr) {
            this.totalMoney += item.price * item.amount;
          }
          this.payment = parseFloat(this.totalMoney).toFixed(2);
        });
    },
    /* 改变checked状态 */
    toggleChecked() {
      this.checked = !this.checked;
    },
    onChange(e) {
      this.checked = e.target.checked;
    },
    /* 提交订单 */
    submitOrder() {

      console.log("我要买->", this.data);
      console.log("应付现金->", this.payment);
      console.log("收货人信息->", this.$store.getters.getReceiptInfo);
      console.log("订单备注->", this.remark);
      let receiptInfo = this.$store.getters.getReceiptInfo;
      // 订单的基本信息
      let orderInfo = {
        username: receiptInfo.username,
        message: this.remark,
        payment: this.payment,
        receiptName: receiptInfo.receiptName,
        receiptAddress: receiptInfo.receiptAddress,
        detailsAddress: receiptInfo.detailsAddress,
        phone: receiptInfo.phone,
        drugs: this.data,
      };
      if (orderInfo.username == null || orderInfo.receiptAddress == null || orderInfo.receiptName == null || orderInfo.phone == null) {
        this.$message.error("请补充收货人信息！");
        return;
      }
      this.loading = true;
      console.log("orderInfo====>", orderInfo);
      this.$axios.post("/init/order", orderInfo).then((res) => {
        console.log("提交订单信息返回结果:", res);
        if (res.data.status) {
          // 如果返回true，就跳转到我的订单页面
          // 这里用replace跳转，不让用户可以回退到订单页面
          setTimeout(() => {
            this.$router.replace("/mine/order/" + receiptInfo.username);
            this.$message.success("订单提交成功！");
          }, 1000);
        } else {
          // 返回false，提交失败
          this.$message.error("订单提交失败！");
          this.loading = false;
        }
      });
    },
  },
};
</script>

<style scoped>
.order-info {
  width: 1100px;
  margin: auto;
  margin-bottom: 28px;
}

.order-info .breadcrumb {
  text-align: left;
  font-size: 16px;
  height: 56px;
  line-height: 56px;
  color: #000000a6;
}

.order-info .breadcrumb .icon-right {
  font-size: 14px;
  color: #000000a6;
}

.order-info .center {
  background-color: #fff;
  border-radius: 10px;
  display: flex;
  flex-wrap: wrap;
  color: #000000a6;
  padding: 28px 78px 50px;
}
.order-info .center .empty {
  width: 100%;
}

.order-info .order-page-second {
  width: 100%;
}
.order-info .order-page-second .ant-table-wrapper {
  width: 100%;
}
.order-info .order-page-second .ant-table {
  font-size: 15px;
}
.order-info .order-page-second .ant-table-placeholder {
  padding: 80px 16px;
  font-size: 17px;
}
.order-info .order-page-second .picture {
  width: 50px;
  height: 50px;
}

.order-info .order-page-second .drugInfo .name {
  font-size: 16px;
  font-weight: 500;
}
.order-info .order-page-second .drugInfo .standard {
  font-size: 14px;
  margin-top: 8px;
  display: block;
}
.order-info .order-page-second .price {
  font-weight: 500;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}
.order-info .order-page-second .amount {
  width: 70px;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}
.order-info .order-page-second .subtotal {
  color: #ff2c30;
  font-weight: 600;
  font-size: 17px;
  font-family: Verdana, Geneva, Tahoma, sans-serif;
}

.order-info .order-page-second .drugInfo a {
  color: #000000a6;
}
.order-info .order-page-second .drugInfo a:hover {
  color: #ff2c30;
}

.order-info .submit-orders {
  width: 100%;
  height: 250px;
}
.order-info .submit-orders .card >>> .ant-card-body {
  height: 250px;
  padding: 24px 35px;
}
.order-info .submit-orders .card >>> div {
  display: flex;
}
.order-info .submit-orders .submit-orders-left {
  flex: 1;
}
.order-info .submit-orders .submit-orders-left {
  display: flex;
  flex-wrap: wrap;
}
.order-info .submit-orders .submit-orders-left div {
  /* flex: 1; */
  width: 370px;
}
.order-info .submit-orders .submit-orders-left div span {
  display: inline-block;
  width: 100px;
  height: 36px;
}
.order-info .submit-orders .submit-orders-left .orderRemark {
  height: 150px;
}
.order-info .submit-orders .submit-orders-left .agreement {
  margin-left: 8px;
}

.order-info .submit-orders .submit-orders-right {
  flex: 1;
  display: flex;
  flex-direction: column;
}
.order-info .submit-orders .submit-orders-right p {
  text-align: right;
  margin: 0;
  flex: 1;
}
.order-info .submit-orders-right >>> button.btn_submit.ant-btn.ant-btn-danger {
  width: 158px;
  height: 50px;
  font-size: 18px;
}
</style>
