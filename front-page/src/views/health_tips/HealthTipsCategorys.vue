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
          href="/health/tips/categorys"
          >全部</a
        >
      </div>
    </div>

    <div class="center">
      <div class="first-floor">
        <div class="article_sort" v-for="item in firstSort">
          <a :href="'/health/tips/' + item.category" class="category">{{
            item.category
          }}</a>
          <!-- <span>|</span> -->
        </div>
      </div>
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
      firstSort: [],
      healthTips: [],
      total: 0,
    };
  },
  created() {
    this.getAllFirstSort();
    this.getAllHealthTips();
  },
  watch: {
    current: "getAllHealthTips",
  },
  methods: {
    /* 获取药品的一级分类，全部的一级分类 */
    getAllFirstSort() {
      this.$axios.get("/get/all/first/category").then((res) => {
        console.log("一级分类--->", res);
        this.firstSort = res.data.data.data;
      });
    },
    /* 获取全部的文章 */
    getAllHealthTips() {
      this.$axios.get("/all/health/tips/" + this.current).then((res) => {
        console.log("全部文章--->", res);
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
@import "./style/health_tips_categorys.less";
</style>