<template>
  <!-- 推荐药品组件 -->
  <div class="health-tips-drugs-recommend">
    <a-card>
      <a-divider orientation="left"><h3>药品推荐</h3></a-divider>
      <div class="drugs">
        <div class="drug" v-for="item in recommendInfo">
          <img
            :src="item.picture"
            width="100"
            height="100"
            title="点击查看详情"
            @click="goToDrugPage(item.name)"
          />
          <p class="drug_name">
            <span @click="goToDrugPage(item.name)"> {{ item.name }}</span>
          </p>
          <p class="drug_price">￥{{ parseFloat(item.price).toFixed(2) }}</p>
        </div>
      </div>
    </a-card>
  </div>
</template>

<script>
export default {
  props: ["item"],
  data() {
    return {
      // 接收药品信息的集合
      recommendInfo: [],
    };
  },
  created() {
    this.getSameRecommend();
    console.log("怎么回事", this.$props.item);
  },
  methods: {
    /* 根据分类获取推荐同类的药品 */
    getSameRecommend() {
      this.$axios.get("/health/tips/drugs/" + this.$props.item).then((res) => {
        this.recommendInfo = res.data.data.data.records;
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
};
</script>

<style lang="less" scoped>
.health-tips-drugs-recommend {
  margin-top: 38px;

  .ant-card-bordered {
    border-radius: 10px;
  }
  .drugs {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: flex-start;

    .drug {
      width: 20%;

      a {
        color: #000000a6;
      }

      a:hover {
        color: coral;
      }

      img,
      p {
        margin-bottom: 13px;
      }

      img {
        cursor: pointer;
      }

      .drug_name {
        font-size: 16px;
        cursor: pointer;
      }
      .drug_name:hover {
        color: #f34e0d;
      }

      .drug_price {
        font-size: 18px;
        color: #f34e0d;
        font-family: Verdana, Geneva, Tahoma, sans-serif;;
      }
    }
  }
}
</style>
