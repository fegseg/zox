<template>
  <div class="myOrder">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /> <span><a>我的</a></span
        ><a-icon class="icon-right" type="right" />
        <span><a>交易订单</a></span>
      </div>
    </div>

    <div class="center">
      <div class="order">
        <a-card
          style="width: 100%; text-align: left"
          :bordered="false"
          :tab-list="tabListNoTitle"
          :active-tab-key="noTitleKey"
          @tabChange="(key) => onTabChange(key, 'noTitleKey')"
        >
          <div class="header">
            <table>
              <tr>
                <th width="80px">
                  <a-checkbox @change="onChange" :disabled="activeKey != 4" />
                  全选
                </th>
                <th width="400px">药品</th>
                <th>单价</th>
                <th>数量</th>
                <th>小计</th>
                <th>商品操作</th>
                <th>交易状态</th>
                <th>交易操作</th>
              </tr>
            </table>
          </div>
          <div class="btn_page" v-if="orderInfo.length != 0">
            <a-button @click="goToBeforePage()" :disabled="isBefore"
              >上一页</a-button
            >
            <a-button
              @click="goToNextPage()"
              :disabled="isNext"
              class="nextPage"
              >下一页</a-button
            >
          </div>
          <div class="empty" v-if="orderInfo.length == 0">空空如也！！！</div>
          <!-- <div v-if="noTitleKey === '1'"> -->
          <div class="waitSend" v-for="(order, index) in orderInfo">
            <div class="row1">
              <a-checkbox
                @change="onChange"
                @click="selectItem(order.orderId, index)"
                :disabled="order.status != 4"
              />
              <span class="create_time">{{ splitTime(order.gmtCreate) }}</span
              ><span class="orderId">订单编号：{{ order.orderId }}</span>
              <span class="home"><a href="/">mucの药店</a></span>
              <span class="call"
                ><a title="遇到无法解决的问题，可以拨打电话咨询客服。"
                  >联系客服：95598</a
                ></span
              >

              <a-popconfirm
                title="确定要删除这个订单吗？"
                ok-text="确定"
                cancel-text="取消"
                @confirm="deleteThisOrder(order.orderId)"
              >
                <span class="delete" title="删除" v-if="order.status == 4"
                  ><a-icon
                    type="delete"
                    theme="twoTone"
                    two-tone-color="#999999"
                /></span>
              </a-popconfirm>
            </div>
            <div class="row2">
              <div class="drug">
                <div class="drug_item" v-for="drug in order.drugs">
                  <img
                    :src="drug.picture"
                    alt="加载失败"
                    title="查看详情"
                    width="96"
                    height="80"
                    @click="goToThisDrugPage(drug.drugName)"
                  />
                  <div class="baseInfo">
                    <p>
                      <span
                        @click="goToThisDrugPage(drug.drugName)"
                        class="drugName"
                        >{{ drug.drugName }}</span
                      >
                    </p>
                    <p>规格：{{ drug.standard }}</p>
                  </div>
                  <div class="price">
                    ￥{{ parseFloat(drug.price).toFixed(2) }}
                  </div>
                  <div class="amount">{{ drug.amount }}</div>
                  <div class="subtotal">
                    ￥{{ parseFloat(drug.amount * drug.price).toFixed(2) }}
                  </div>
                </div>
              </div>
              <div class="drugs_options">
                <div>
                  <p>
                    <a
                      v-if="order.status != 4 && order.complaint == 0"
                      @click="showModal(order)"
                      >我要投诉</a
                    >
                    <span v-if="order.complaint == 1" style="color: red"
                      >投诉成功</span
                    >
                    <a-modal
                      v-model="visible"
                      title="投诉"
                      :mask="false"
                      @ok="handleOk"
                      @cancel="handleCancel"
                      okText="确认"
                      cancelText="取消"
                    >
                      <p>订单编号：{{ order.orderId }}</p>
                      <a-textarea
                        placeholder="请填写投诉理由"
                        v-model="complaintReason"
                        :maxLength="100"
                        :autoSize="{ minRows: 4, maxRows: 7 }"
                      />
                    </a-modal>
                  </p>
                  <p v-if="order.status != 1">
                    <a href="#">申请售后</a>
                  </p>
                </div>
              </div>
              <div class="trading_status">
                <div>
                  <span v-if="order.status == 1">等待发货</span>
                  <span v-else-if="order.status == 2">等待收货</span>
                  <span
                    v-else-if="order.status == 3 || order.status == 4"
                    style="color: #67c23a"
                    >交易成功</span
                  >
                  <br />
                  <span
                    class="orderDetails"
                    @click="goToOrderDetailsPage(order)"
                    >订单详情</span
                  >
                </div>
              </div>
              <div class="trading_options">
                <div>
                  <p v-if="order.status == 2">
                    <a @click="confirmReceipt(order)">确认收货</a>
                  </p>
                  <p v-if="order.status != 4"><a>申请退货</a></p>
                  <p v-if="order.status == 3">
                    <a @click="goToComments(order)">我要评价</a>
                  </p>
                  <!-- <p v-if="order.status == 3"><a href="#">申请开票</a></p> -->
                </div>
              </div>
            </div>
            <div class="row3">
              实付款(总金额)：<span
                >￥{{ parseFloat(order.payment).toFixed(2) }}</span
              >
            </div>
          </div>

          <a-pagination
            v-model="current"
            :total="total"
            show-less-items
            style="float: right; margin-right: -24px"
            v-if="orderInfo.length != 0"
          />
        </a-card>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 订单信息
      orderInfo: [],
      // 当前页
      current: 1,
      // 总数
      total: 0,
      // 是否禁用前一页
      isBefore: true,
      // 是否禁用下一页
      isNext: true,
      // 标签页list
      tabListNoTitle: [
        {
          key: "1",
          tab: "待发货",
        },
        {
          key: "2",
          tab: "待收货",
        },
        {
          key: "3",
          tab: "待评价",
        },
        {
          key: "4",
          tab: "已完成",
        },
      ],
      // 无标题的标签的默认key
      noTitleKey: "1",
      // 当前活跃的标签key
      activeKey: "1",
      // 多选框选中
      selectedItems: [],
      // 填写投诉理由的提示是否可见
      visible: false,
      // 被投诉的订单id
      complaintId: "",
      // 投诉理由
      complaintReason: "",
    };
  },
  created() {
    this.getOrderInfo();
  },
  watch: {
    activeKey: "getOrderInfo",
    current: "getOrderInfo",
  },
  methods: {
    /* 获取待发货的订单信息 */
    getOrderInfo() {
      let username = this.$store.getters.getUser.username;
      this.$axios
        .get("/order/" + username + "/" + this.current + "/" + this.activeKey, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          this.orderInfo = res.data.data.data.orderList;
          this.total = res.data.data.data.total;
          // 我在请求数据时对总数做一个判断，然后是否判断禁用下一页按钮
          // 总数小于等于10，直接禁用上一页和下一页
          if (res.data.data.data.total <= 10) {
            this.isNext = true;
            this.isBefore = true;
          } else {
            // 总数大于10的情况
            // 判断请求数据时，是第几页，如果不是第一页，就让第一页disabled = false
            if (this.current > 1) {
              this.isBefore = false;
            }
            // 如果总页数等于当前页且当前页不等于1，就禁用下一页
            if (
              this.current == Math.ceil(res.data.data.data.total / 10) &&
              this.current != 1
            ) {
              this.isNext = true;
            } else {
              // 否则，就启用下一页
              this.isNext = false;
            }
          }
          console.log("total====>", res.data.data.data.total);
          console.log("orderList===>", res.data.data.data.orderList);
          // console.log(res);
          // console.log(this.isNext);
          // console.log(this.total);
        });
    },
    /* 改变标签 */
    onTabChange(key, type) {
      console.log(key, type);
      this.activeKey = key;
      this.current = 1;
      this[type] = key;
    },
    /* 分割时间，只显示年月日 */
    splitTime(time) {
      let arr = time.split(" ");
      return arr[0];
    },
    /* 复选框选中 */
    onChange(e) {
      console.log(`checked = ${e.target.checked}`);
      console.log(e);
      if (e.target.checked) {
      }
    },
    selectItem(orderId, index) {
      // console.log("选中的订单编号", orderId);
      // console.log("index==>", index);
    },
    /* 上一页，做一些逻辑判断是否第一页 */
    goToBeforePage() {
      this.current--;
      if (this.current <= 1) {
        this.current = 1;
        this.isBefore = true;
      }
    },
    /* 下一页，判断到底没 */
    goToNextPage() {
      this.current++;
      this.isBefore = false;
      // 因为没切换一次标签都会请求数据，所以我在请求数据时对总数做一个判断，然后是否判断禁用下一页按钮
    },
    /* 前往订单详情页 */
    goToOrderDetailsPage(order) {
      console.log(order);
      const { href } = this.$router.resolve({
        path: "/mine/order/details/" + order.orderId,
      });
      window.open(href, "_blank");
    },
    /* 跳转到这个药的详情页面 */
    goToThisDrugPage(drugName) {
      const { href } = this.$router.resolve({
        path: "/drug/" + drugName,
      });
      window.open(href, "_blank");
    },
    showModal(order) {
      this.visible = true;
      this.complaintId = order.orderId;
      console.log("投诉id===>", this.complaintId);
    },
    handleOk(e) {
      // console.log("e--->",e);
      this.complaint(this.complaintId, this.complaintReason);
      this.visible = false;
      this.complaintReason = "";
      this.complaintId = "";
    },
    handleCancel() {
      this.visible = false;
      this.complaintReason = "";
      this.complaintId = "";
    },
    /* 对订单进行一个投诉 */
    complaint(complaintId, complaintReason) {
      if (complaintReason == "" || complaintReason == null) {
        this.$message.info("请先填写投诉理由！");
        return "";
      }
      console.log("投诉=>", complaintId);
      let formData = new FormData();
      formData.append("orderId", complaintId);
      formData.append("reason", complaintReason);
      this.$axios
        .post("/user/complaint", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          console.log(res);
          this.getOrderInfo();
          this.$message.success("投诉成功！");
        });
    },
    /* 确认收货操作 */
    confirmReceipt(order) {
      console.log("订单编号", order.orderId, "确认收货");
      let formData = new FormData();
      formData.append("orderId", order.orderId);
      this.$axios
        .post("/user/order/confirm", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          console.log(res);
          this.getOrderInfo();
          this.$message.success("收货成功！");
        });
    },
    /* 前往评论页面 */
    goToComments(order) {
      // console.log(order)
      this.$router.push("/order/comments/" + order.orderId);
    },
    /* 删除这个订单，只能删除已完成的订单 */
    deleteThisOrder(orderId) {
      console.log("要删除的订单=>", orderId);
      let formData = new FormData();
      formData.append("orderId", orderId);
      this.$axios
        .post("/user/delete/order", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          if (res.data.data.responseBody) {
            this.activeKey = 4;
            this.getOrderInfo();
            this.$message.success("删除成功！");
          } else {
            this.$message.success("操作失败！");
          }
        });
    },
  },
};
</script>

<style lang="less" scoped>
@import "./style/myOrder.less";
</style>
