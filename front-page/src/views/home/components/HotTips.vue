<template>
  <div class="question">
    <div class="hot">
      <h2>热 点 资 讯</h2>
      <a-collapse accordion v-for="(item, index) in hotHealthTips">
        <a-collapse-panel :key="index + 1" :header="item.title">
          <p class="contents" v-html="item.contents"></p>
          <div class="foot">
            <span class="read">阅读次数：{{ item.readNumber }}</span>
            <a
              class="details"
              :href="'/health/tips/details/' + item.username + '/' + item.title"
              @click="addReadNumber(item)"
              >详情</a
            >
          </div>
        </a-collapse-panel>
      </a-collapse>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      hotHealthTips: [],
    };
  },
  created() {
    this.getHotHealthTips();
  },
  methods: {
    /* 获取热点资讯 */
    getHotHealthTips() {
      this.$axios.post("/health/tips/hot").then((res) => {
        console.log("热点资讯===>", res);
        this.hotHealthTips = res.data.data.responseBody.records;
      });
    },
    /* 阅览次数加1 */
    addReadNumber(item) {
      console.log(item);
      let formData = new FormData();
      formData.append("id", item.id);
      this.$axios.post("/health/tips/readNumber/add", formData);
    },
  },
};
</script>

<style scoped>
.question {
  width: 1200px;
  margin: auto;
  margin-top: 28px;
}

.question .hot {
  border-radius: 10px;
  background-color: #fff;
  padding: 0 28px 28px 28px;
}
.question .hot >>>.ant-collapse{
  margin-bottom: 5px;
}
.question .hot h2 {
  color: #000000a6;
  height: 58px;
  line-height: 58px;
  margin-bottom: 18px;
  border-bottom: #dedede 1px solid;
  text-align: left;
}

.question .hot >>> .ant-collapse-header {
  text-align: left;
  background-color: #fff;
}

.question .hot .contents {
  text-align: left;
  line-height: 28px;
  display: -webkit-box;
  -webkit-line-clamp: 5;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.question .hot .foot {
  height: 28px;
  line-height: 28px;
}

.question .hot .foot .read {
  float: left;
}
.question .hot .foot .details {
  float: right;
  color: #000000a6;
}
.question .hot .foot .details:hover {
  color: #ff2c30;
}
</style>
