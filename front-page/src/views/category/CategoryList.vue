<template>
  <div class="category_list">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" />
        <router-link to="/category">全部药品分类</router-link>
      </div>
    </div>

    <div class="center">
      <a-card class="nav_card">
        <p class="title_category_list">
          药店出售药品分类
          <span class="title_fast">
            快速导航：&emsp;
            <span><a href="/category/感冒发热">感冒发热</a></span>
            <span><a href="/category/消化不良">消化不良</a></span>
            <span><a href="/category/止咳化痰">止咳化痰</a></span>
            <span><a href="/category/高血压">高血压</a></span>
            <span><a href="/category/皮肤瘙痒">皮肤瘙痒</a></span>
          </span>
        </p>
      </a-card>
      <a-config-provider :locale="locale">
        <div class="content">
          <a-table
            :columns="columns"
            :data-source="data"
            :showHeader="false"
            :pagination="false"
            :loading="loading"
            style="width: 100%"
          >
            <span
              slot="firstCategory"
              slot-scope="text"
              class="firstSort"
              @click="goToThisFirstCategory(text)"
              >{{ text }}</span
            >
            <span slot="secondCategoryList" slot-scope="text, record"
              ><span
                v-for="item in record.secondCategoryList"
                @click="goToThisSecondCategory(item)"
                ><a-tag class="tag">{{ item.secondSort }}</a-tag></span
              >
            </span>
          </a-table>
        </div>
      </a-config-provider>
    </div>
  </div>
</template>
<script>
import zhCN from "ant-design-vue/es/locale/zh_CN";
const columns = [
  {
    title: "firstCategory",
    dataIndex: "firstCategory",
    key: "firstCategory",
    scopedSlots: { customRender: "firstCategory" },
  },
  {
    title: "secondCategoryList",
    dataIndex: "secondCategoryList",
    key: "secondCategoryList",
    scopedSlots: { customRender: "secondCategoryList" },
    width: 888,
  },
];

const data = [];

export default {
  data() {
    return {
      data,
      columns,
      locale: zhCN,
      loading: true,
    };
  },
  created() {
    this.getCategoryListInfo();
  },
  methods: {
    getCategoryListInfo() {
      this.$axios.get("/front/get/category").then((res) => {
        console.log(res);
        this.data = res.data.data.frontCategories;
        this.loading = false;
      });
    },
    goToThisFirstCategory(value) {
      console.log(value);
      this.$router.push("/category/" + value);
    },
    goToThisSecondCategory(item) {
      console.log(item);
      console.log(item.id);
      console.log(item.secondSort);
      this.$router.push("/category/" + item.secondSort);
    },
  },
};
</script>

<style scoped>
@import "./style/category_list.css";
</style>