<template>
  <div id="health_tips_categorys">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /><a
          href="/health/tips/categorys"
          >健康资讯</a
        >
        <a-icon class="icon-right" type="right" /><a
          :href="'/health/tips/' + this.$route.params.category"
          >{{ this.$route.params.category }}</a
        >&emsp;共 <span style="font-size: 18px">{{ total }}</span> 篇
      </div>
    </div>

    <div class="center">
      <a-divider orientation="left" style="margin: 10px 0 0 0"
        ><h3>{{ this.$route.params.category }}</h3></a-divider
      >
      <div class="second-floor">
        <div class="list" v-for="item in healthTips">
          <h2 class="title">
            <a
              :href="'/health/tips/details/' + item.username + '/' + item.title"
              @click="addReadNumber(item)"
              >{{ item.title }}</a
            >
          </h2>
          <div class="contents" v-html="item.contents"></div>
          <p>
            <span class="time">发布时间：{{ item.gmtCreate }}</span>
            <span class="origin">{{ item.origin }}</span>
            <a
              :href="'/health/tips/details/' + item.username + '/' + item.title"
              class="details"
              title="点击查看详情"
              @click="addReadNumber(item)"
              >详情</a
            >
          </p>
        </div>

        <a-pagination
          v-model="current"
          :total="total"
          show-less-items
          style="margin: 28px 0"
        />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      current: 1,
      healthTips: [],
      total: 0,
    };
  },
  beforeCreate() {
    document.title = "健康资讯-" + this.$route.params.category;
  },
  created() {
    this.getHealthTipsByCategory();
  },
  watch: {
    current: "getHealthTipsByCategory",
  },
  methods: {
    /* 获取全部的文章 */
    getHealthTipsByCategory() {
      this.$axios
        .get("/health/tips/" + this.$route.params.category + "/" + this.current)
        .then((res) => {
          console.log("分类文章--->", res);
          this.healthTips = res.data.data.data.records;
          this.total = res.data.data.data.total;
        });
    },
     /* 阅览次数加1 */
    addReadNumber(item) {
      let formData = new FormData();
      formData.append("id", item.id);
      this.$axios.post("/health/tips/readNumber/add", formData);
    },
  },
};
</script>

<style lang="less" scoped>
@import "./style/one_category_health_tips.less";
</style>