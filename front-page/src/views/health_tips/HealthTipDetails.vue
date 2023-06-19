<template>
  <div id="health_tips_details">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span> </router-link
        ><a-icon class="icon-right" type="right" />
        <a href="/health/tips/categorys"><span>健康资讯</span></a
        ><a-icon class="icon-right" type="right" />
        <a :href="'/health/tips/' + article.category"
          ><span>{{ article.category }}</span></a
        ><a-icon class="icon-right" type="right" />
        <span>文章详情</span>
      </div>
    </div>

    <div class="center">
      <div class="article">
        <h1 class="title">{{ article.title }}</h1>
        <p class="author_origin">
          <span>来源：{{ article.origin }}</span>
          <span v-show="article.origin === '原创'"
            >作者：{{ article.author }}</span
          >
          <span v-show="article.origin === '转载'"
            >原文链接：<a :href="article.reprintLink" target="_blank">{{
              article.reprintLink
            }}</a></span
          >
          <span v-show="article.origin === '原创'"
            >发布时间：{{ article.gmtCreate }}</span
          >
        </p>
        <div v-html="article.contents" class="contents"></div>
        <p class="reprint_time">
          <span v-show="article.origin === '转载'"
            >转载时间：{{ article.gmtCreate }}</span
          >
          <span class="likes">阅览次数：{{article.readNumber}}</span>
        </p>
      </div>

      <div class="right">
        <TipsRecommend />
      </div>
    </div>

    <DrugsRecommend :item="article.category" v-if="article.category != null" />
  </div>
</template>

<script>
import TipsRecommend from "@/views/health_tips/components/TipsRecommend";
import DrugsRecommend from "@/views/health_tips/components/DrugsRecommend";
export default {
  components: {
    TipsRecommend,
    DrugsRecommend,
  },
  data() {
    return {
      article: {},
    };
  },
  beforeCreate() {
    document.title = "健康小贴士 - " + this.$route.params.title;
  },
  created() {
    this.getDetailsContent();
  },
  methods: {
    /* 根据管理员用户名和文章确定唯一文章获取文章详情 */
    getDetailsContent() {
      this.$axios
        .get(
          "/health/tips/get/details/" +
            this.$route.params.username +
            "/" +
            this.$route.params.title
        )
        .then((res) => {
          console.log("详细内容", res);
          this.article = res.data.data.data;
        });
    },
  },
};
</script>

<style lang="less" scoped>
@import "./style/health_tips_details.less";
</style>