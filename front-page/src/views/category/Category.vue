<template>
  <div class="category">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" />
        <router-link to="/category">全部药品分类</router-link>
        <a-icon class="icon-right" type="right" />
        <span>{{ this.$route.params.categoryName }}</span>
      </div>
    </div>

    <div class="center">
      <div class="sequence">
        <a-radio-group
          class="btn_sequence"
          default-value="a"
          button-style="outline"
        >
          <a-radio-button value="a"> 默认 </a-radio-button>
          <a-radio-button value="b"> 销量 </a-radio-button>
          <a-radio-button value="c"> 上架时间 </a-radio-button>
        </a-radio-group>
        <span class="total_top"
          >共 {{ total }} 种
          <span style="font-weight: bold">{{
            this.$route.params.categoryName
          }}</span>
          相关的药品</span
        >
        <span class="recommend">药品推荐：</span>
      </div>

      <div style="width: 100%" v-if="loading">
        <a-spin tip="加载中..."> </a-spin>
      </div>

      <div class="left" v-if="!loading">
        <div style="width: 100%" v-if="drugsList == ''">
          本店暂未上架该类药品，可以联系本店，我们将尽快处理该问题！
        </div>
        <a-card
          class="drug"
          hoverable
          style="width: 212px; height: 288px"
          v-for="item in drugsList"
        >
          <img
            :src="item.picture"
            alt="加载失败，刷新页面试试"
            title="点击查看详情"
            @click="goToDrugPage(item.name)"
          />
          <div class="afterImg">
            <p class="title" @click="goToDrugPage(item.name)">
              {{ item.name }}
            </p>
            <p class="price">￥{{ parseFloat(item.price).toFixed(2) }}/盒</p>
            <p class="standard">{{ item.standard }}</p>
            <p class="desc">
              {{ item.description }}
            </p>
          </div>
        </a-card>

        <div class="pagination" v-if="drugsList != ''">
          <a-config-provider :locale="locale">
            <a-pagination
              show-quick-jumper
              :defaultPageSize="20"
              :default-current="current"
              :total="total"
              @change="onChange"
            />
          </a-config-provider>
        </div>
      </div>
      <div class="right" v-if="!loading">
        <div class="drug" v-for="item in recommendDrugs">
          <img
            :src="item.picture"
            alt="加载失败，刷新页面试试"
            title="点击查看详情"
            @click="goToDrugPage(item.name)"
          />
          <div class="afterImg">
            <p class="title" @click="goToDrugPage(item.name)">
              {{ item.name }}
            </p>
            <p class="price">￥{{ parseFloat(item.price).toFixed(2) }}/盒</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import zhCN from "ant-design-vue/es/locale/zh_CN";
export default {
  data() {
    return {
      drugsList: [],
      recommendDrugs: [],
      locale: zhCN,
      current: 1,
      total: 0,
      loading: true,
    };
  },
  beforeCreate() {
    document.title = this.$route.params.categoryName;
  },
  created() {
    this.getDrugsList();
  },
  watch: {
    current: "getDrugsList",
  },
  methods: {
    getDrugsList() {
      let formData = new FormData();
      formData.append("categoryName", this.$route.params.categoryName);
      formData.append("current", this.current);
      this.$axios.post("/get/category/drugs", formData).then((res) => {
        if (res != "" && res != null) {
          this.loading = false;
        }
        console.log(res)
        this.drugsList = res.data.data.data.records;
        this.total = res.data.data.data.total;
        let tempArr = res.data.data.data.records;
        for (let i = 0; i < 2; i++) {
          this.recommendDrugs.push(
            tempArr[Math.floor(Math.random() * tempArr.length)]
          );
        }
      });
    },
    /* 跳转到相对应的药品页面 */
    goToDrugPage(name) {
      const { href } = this.$router.resolve({
        path: "/drug/" + name,
      });
      // 打开一个新的标签页
      window.open(href, "_blank");
    },
    onChange(pageNumber) {
      console.log("Page: ", pageNumber);
      this.current = pageNumber
    },
  },
};
</script>

<style scoped>
@import "./style/category.css";
</style>
