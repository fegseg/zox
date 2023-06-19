<template>
  <!-- 推荐药品组件 -->
  <div class="same-recommend">
    <a-card title="同类药品推荐">
      <a-card-grid
        v-for="item in recommendInfo"
        style="width: 25%; text-align: center"
      >
        <!-- 药品图片 -->
        <img
          @click="goToDrugPage(item.name)"
          :src="item.picture"
          alt="药品图片"
          width="200"
          height="150"
        />
        <!-- 药品标题 -->
        <span class="title" @click="goToDrugPage(item.name)"
          >{{ item.name }}
        </span>
        <!-- 药品价格 -->
        <p style="color: #f34e0d">
          ￥<span
            style="
              font-size: 16px;
              font-family: Verdana, Geneva, Tahoma, sans-serif;
            "
            >{{ parseFloat(item.price).toFixed(2) }}</span
          >
        </p>
      </a-card-grid>
    </a-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 接收药品信息的集合
      recommendInfo: [],
    };
  },
  methods: {
    /* 根据药名获取推荐同类的药品 */
    getSameRecommend() {
      let name = this.$route.params.name;
      console.log("参数", name);
      const _this = this;
      this.$axios.get("/medicine/recommend/" + name).then((res) => {
        _this.recommendInfo = res.data.data.data.records;
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
  },
  created() {
    this.getSameRecommend();
  },
};
</script>

<style>
.same-recommend {
  margin-top: 38px;
  font-weight: 500;
}
.same-recommend .ant-card-head {
  text-align: left;
  background-color: #fafafa;
  color: #707070;
  font-weight: bold;
}

.same-recommend img {
  cursor: pointer;
}

.same-recommend .title {
  display: block;
  margin: 9px 0 5px;
  color: #707070;
  cursor: pointer;
}

.same-recommend .title:hover {
  color: #ff2c30;
}
.same-recommend p {
  margin: 0;
}
</style>
