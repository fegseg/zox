<template>
  <div class="introduce">
    <a-descriptions :column="1" bordered>
      <a-descriptions-item label="【产品名称】">
        {{ drug.name }}
      </a-descriptions-item>
      <a-descriptions-item label="【产品规格】">
        {{ synopsis.standard }}
      </a-descriptions-item>
      <a-descriptions-item label="【有效日期】">
        {{ synopsis.validPeriod }}
      </a-descriptions-item>
      <a-descriptions-item label="【主要原料】">
        {{ synopsis.mainIngredients }}
      </a-descriptions-item>
      <a-descriptions-item label="【主要作用】">
        {{ synopsis.indications }}
      </a-descriptions-item>
      <a-descriptions-item label="【用法用量】">
        {{ synopsis.dosage }}
      </a-descriptions-item>
      <a-descriptions-item label="【生产企业】">
        {{ synopsis.manufacturer }}
      </a-descriptions-item>
      <a-descriptions-item label="【注意事项】">
        {{ synopsis.precautions }}
      </a-descriptions-item>
    </a-descriptions>
  </div>
</template>

<script>
export default {
  props: ["drugName"],
  data() {
    return {
      drug:{},
      // 说明书信息
      synopsis: {},
    };
  },
  created() {
    this.getSynopsisInfo();
  },
  methods: {
    /* 根据药品id获取说明书信息 */
    getSynopsisInfo() {
      this.$axios
        .get("/medicine/info/name/" + this.$props.drugName)
        .then((res) => {
          this.drug = res.data.data.data
          let drugInfo = res.data.data.data;
          this.$axios
            .get("/synopsis/info/" + drugInfo.id)
            .then((res) => {
              this.synopsis = res.data.data.data;
            });
        });
    },
  },
};
</script>

<style scoped>
.introduce >>> .ant-descriptions-item-label {
  width: 200px;
}
.introduce >>> .ant-descriptions-item-content {
  text-align: left;
}
</style>