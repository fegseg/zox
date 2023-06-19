<template>
  <div class="searchResult">
    <div class="header">
      <div class="breadcrumb">
        <a-icon type="arrow-left" class="arrow" @click="goBack()" />
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal" @click="goBack()">
            返回</span
          ></router-link
        >
        <span style="font-weight: normal" class="title"> 搜索结果</span>
      </div>
    </div>

    <div class="center">
      <div class="sequence">
        <span class="total_top"
          >您搜索的关键字是：<span
            style="color: #f34e0d; font-size: 18px; margin-right: 28px"
            >{{ $route.params.value }}</span
          >
          共为您找到
          <span style="color: #f34e0d">{{ total }}</span> 种相关的药品</span
        >
      </div>

      <div style="width: 100%" v-if="loading">
        <a-spin tip="加载中..."> </a-spin>
      </div>

      <div class="res" v-if="!loading">
        <div style="width: 100%;line-height:128px;font-size:16px" v-if="drugsList == ''">
          换个关键字试试吧！
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
              :defaultPageSize="10"
              :default-current="current"
              :total="total"
              @change="onChange"
            />
          </a-config-provider>
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
      locale: zhCN,
      current: 1,
      total: 0,
      loading: true,
    };
  },
  created() {
    this.getDrugsList();
  },
  methods: {
    goBack() {
      this.$router.go(-1); //返回上一页
    },
    getDrugsList() {
      let formData = new FormData();
      formData.append("drugName", this.$route.params.value);
      formData.append("current", this.current);
      this.$axios.post("/search/drug/name", formData).then((res) => {
        console.log("搜索结果===>", res);
        // this.$store.commit("SET_SEARCHRESULT", res.data.data.responseBody);
        this.drugsList = res.data.data.responseBody.records;
        this.total = res.data.data.responseBody.total;
        this.loading = false;
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
      console.log("current", this.current);
      this.current = pageNumber;
      this.getDrugsList();
    },
  },
};
</script>

<style scoped>
@import "./style/SearchResult.css";
</style>
