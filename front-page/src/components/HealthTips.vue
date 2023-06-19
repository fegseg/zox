<template>
  <div>
    <a-card title="健康资讯" class="yjx-home-news-card">
      <a slot="extra" href="/health/tips/categorys">更多</a>
      <p v-for="item in healthTips">
        <a
          :href="'/health/tips/details/' + item.username + '/' + item.title"
          :title="item.title"
          @click="addReadNumber(item)"
          >{{ item.title }}</a
        >
      </p>
    </a-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      healthTips: [],
    };
  },
  created() {
    this.getHealthTips();
  },
  methods: {
    /* 获取健康资讯 */
    getHealthTips() {
      this.$axios.get("/get/home/health/tips").then((res) => {
        this.healthTips = res.data.data.data;
        console.log("健康小贴士==>", res);
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

<style>
.yjx-home-news-card {
  text-align: left;
  /* margin: 0 18px 0 0; */
  width: 260px;
  height: 371px;
  border-radius: 10px;
  padding: auto;
}
.yjx-home-news-card .ant-card-head-title {
  color: #000000a6;
}

.yjx-home-news-card .ant-card-extra a {
  color: #000000a6;
}

.yjx-home-news-card .ant-card-extra a:hover {
  color: coral;
}

.yjx-home-news-card p a {
  color: #000000a6;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.yjx-home-news-card p a:hover {
  color: coral;
}
</style>
