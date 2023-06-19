<template>
  <div class="shopping-cart">
    <div class="header">
      <div class="breadcrumb">
        <span>我的</span>
        <a-icon class="icon-right" type="right" />
        <span>购物车</span>
      </div>
    </div>

    <div class="center">
      <!-- 购物车表格 -->
      <a-table
        :row-selection="rowSelection"
        :columns="columns"
        :data-source="data"
        :pagination="false"
        :loading="loading"
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
        <span slot="amountTitle"
          >数量
          <a-tooltip
            ><a-icon type="question-circle" />
            <template slot="title"> 最大数量为200 </template>
          </a-tooltip></span
        >
        <span slot="amount" slot-scope="text, record">
          <a-input-number
            id="inputNumber"
            v-model="record.amount"
            :min="1"
            :max="200"
            class="amount"
            @change="onChange(record.amount, record.key)"
          />
        </span>
        <!-- 小计 -->
        <span slot="subtotal" slot-scope="text, record" class="subtotal"
          >￥{{ parseFloat(record.amount * record.price).toFixed(2) }}</span
        >
        <span slot="operate" slot-scope="text, record" class="operate">
          <a-popconfirm
            title="确定要删除吗？"
            ok-text="确定"
            okType="primary"
            cancel-text="点错了，取消"
            @confirm="handleDelete(record.key)"
          >
            <a-icon
              slot="icon"
              type="question-circle-o"
              style="color: #1890ff"
            />
            <a href="javascript:;">&emsp;删除&emsp;</a>
          </a-popconfirm>
        </span>
      </a-table>

      <a-card style="width: 100%" v-show="data.length != 0">
        <div>
          <a-modal
            v-model="visible"
            title="提示"
            @ok="handleOk"
            cancelText="取消"
            okText="确定"
            class="batchDelete-shoppingCart"
          >
            <p>确定要删除选中的 {{ checkedKeys.length }} 条购物车记录？</p>
          </a-modal>
        </div>
        <span class="batchDelete">
          <a href="javascript:;" @click="batchDelete()">批量删除</a>
        </span>

        <div class="total-btn">
          <a-button type="danger" size="large" @click="buy()">
            立即结算
          </a-button>
        </div>
        <div class="total-span">
          <span class="number"
            >共&ensp;<span
              style="
                color: #f34e0d;
                font-size: 16px;
                font-family: Verdana, Geneva, Tahoma, sans-serif;
              "
              >{{ checkedKeys.length }}</span
            >&ensp;件商品</span
          >&emsp;
          <span
            >总计 ( 不含运费 ) ：<span class="totalMoney"
              >￥{{
                totalMoney != 0 ? parseFloat(totalMoney).toFixed(2) : 0
              }}</span
            ></span
          >
        </div>
      </a-card>
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
    slots: { title: "amountTitle" },
    scopedSlots: { customRender: "amount" },
    align: "center",
  },
  {
    title: "小计 (元)",
    dataIndex: "subtotal",
    scopedSlots: { customRender: "subtotal" },
    align: "center",
  },
  {
    title: "操作",
    dataIndex: "operate",
    scopedSlots: { customRender: "operate" },
    align: "center",
  },
];
export default {
  components: {},
  data() {
    return {
      data: [],
      columns,
      totalMoney: 0,
      checkedKeys: [],
      visible: false,
      drugInfos: [],
      loading: true,
    };
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys, selectedRows) => {
          console.log(
            `selectedRowKeys: ${selectedRowKeys}`,
            "selectedRows: ",
            selectedRows
          );
          let drugInfos = [];
          for (const item of selectedRows) {
            let drugInfo = {
              drugName: "",
              amount: "",
            };
            // console.log("item:", item);
            drugInfo.drugName = item.drugName;
            drugInfo.amount = item.amount;
            drugInfos.push(drugInfo);
            // console.log("xxxx:", drugInfos);
          }
          // console.log("drugInfos===>", drugInfos);
          this.drugInfos = drugInfos;
          // console.log("this.drugInfos===>", this.drugInfos);
          let shoppingCartIds = [];
          for (const row of selectedRows) {
            shoppingCartIds.push(row.key);
          }
          this.checkedKeys = shoppingCartIds;
          // console.log("购物车ids====>", this.checkedKeys);
          let formData = new FormData();
          formData.append("shoppingCartIds", shoppingCartIds);
          this.$axios.post("/shopping/account", formData).then((res) => {
            this.totalMoney = res.data.data.totalMoney;
          });
        },
        getCheckboxProps: (record) => ({
          props: {
            disabled: record.name === "Disabled User", // Column configuration not to be checked
            name: record.name,
          },
        }),
      };
    },
  },
  methods: {
    /* 根据账号获取购物车信息 */
    getShoppingCartInfo() {
      this.$axios
        .get("/shopping/info/" + this.$store.getters.getUser.username,{
         headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          this.data = res.data.data.data;
          this.loading = false;
        });
    },
    /* 跳转到这个药的详情页面 */
    goToThisDrugPage(drugName) {
      const { href } = this.$router.resolve({
        path: "/drug/" + drugName,
      });
      window.open(href, "_blank");
    },
    /* 买药 */
    buy() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          let username = this.$store.getters.getUser.username;
          if (this.drugInfos.length != 0) {
            this.$axios
              .post("/buy/drugs/" + username, this.drugInfos)
              .then((res) => {
                // console.log(res);
              });
            this.$router.push("/order/confirm").catch((err) => {});
          } else {
            this.$message.warning("请先选择要结算的药品！");
          }
        } else {
          this.$message.warning("请先登录！");
        }
      } else {
        this.$message.warning("请先登录！");
      }
    },
    /* 单个删除 */
    handleDelete(key) {
      // console.log("删除" + key);
      this.$axios.get("/shopping/delete/one/" + key).then((res) => {
        console.log(res);
        if (res.data.status) {
          this.$message.success("删除成功！");
          this.$router.go(0)
        }
      });
    },
    /* 改变数量 */
    onChange(value, key) {
      this.$axios
        .post("/shopping/update/amount/" + key + "/" + value)
        .then((res) => {
          // console.log(res);
          let formData = new FormData();
          formData.append("shoppingCartIds", this.checkedKeys);
          this.$axios.post("/shopping/account", formData).then((res) => {
            this.totalMoney = res.data.data.totalMoney;
          });
        });
    },
    /* 批量删除 */
    batchDelete() {
      console.log(this.checkedKeys);
      if (this.checkedKeys.length == 0) {
        this.$message.warning("请先选择要删除的药品！");
      } else {
        this.visible = true;
      }
    },
    handleOk(e) {
      // console.log(e);
      let formData = new FormData();
      formData.append("ids", this.checkedKeys);
      this.$axios.post("/shopping/batchDelete", formData).then((res) => {
        if (res.data.status) {
          this.getShoppingCartInfo();
          this.$message.success(
            "已成功删除选中的 " + this.checkedKeys.length + " 条购物车信息！"
          );
        }
      });
      this.visible = false;
    },
  },
  created() {
    this.getShoppingCartInfo();
  },
};
</script>

<style>
@import "./style/shopping_cart.css";
</style>
