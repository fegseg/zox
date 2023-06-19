<template>
  <div class="synopsis">
    <h2>（{{ drug.name }}）说明书</h2>
    <p style="color: #ff5c4d !important">
      温馨提示：部分商品说明书更换频繁，请以商品实物为准。
    </p>
    <a-descriptions :column="1" bordered>
      <a-descriptions-item label="【药品名称】">
        {{ synopsis.name }}
      </a-descriptions-item>
      <a-descriptions-item label="【主要成分】">
        {{ synopsis.mainIngredients }}
      </a-descriptions-item>
      <a-descriptions-item label="【性 状】">
        {{ synopsis.traits }}
      </a-descriptions-item>
      <a-descriptions-item label="【适应症/主治功能】">
        {{ synopsis.indications }}
      </a-descriptions-item>
      <a-descriptions-item label="【规格型号】">
        {{ synopsis.standard }}
      </a-descriptions-item>
      <a-descriptions-item label="【用法用量】">
        {{ synopsis.dosage }}
      </a-descriptions-item>
      <a-descriptions-item label="【不良反应】">
        {{ synopsis.adverseReactions }}
      </a-descriptions-item>
      <a-descriptions-item label="【禁 忌】">
        {{ synopsis.taboo }}
      </a-descriptions-item>
      <a-descriptions-item label="【注意事项】">
        {{ synopsis.precautions }}
      </a-descriptions-item>
      <a-descriptions-item label="【药物互相作用】">
        {{ synopsis.interaction }}
      </a-descriptions-item>
      <a-descriptions-item label="【儿童用药】">
        {{ synopsis.childrenMedication }}
      </a-descriptions-item>
      <a-descriptions-item label="【老年患者用药】">
        {{ synopsis.oldMedication }}
      </a-descriptions-item>
      <a-descriptions-item label="【孕妇及哺乳期用药】">
        {{ synopsis.pregnancyMedication }}
      </a-descriptions-item>
      <a-descriptions-item label="【药物过量】">
        {{ synopsis.overdose }}
      </a-descriptions-item>
      <a-descriptions-item label="【药理毒理】">
        {{ synopsis.pharmacology }}
      </a-descriptions-item>
      <a-descriptions-item label="【药代动力学】">
        {{ synopsis.pharmacokinetics }}
      </a-descriptions-item>
      <a-descriptions-item label="【贮 藏】">
        {{ synopsis.storage }}
      </a-descriptions-item>
      <a-descriptions-item label="【包 装】">
        {{ synopsis.box }}
      </a-descriptions-item>
      <a-descriptions-item label="【有效期】">
        {{ synopsis.validPeriod }}
      </a-descriptions-item>
      <a-descriptions-item label="【执行标准】">
        {{ synopsis.executiveStandard }}
      </a-descriptions-item>
      <a-descriptions-item label="【批准文号】">
        {{ synopsis.approvalNumber }}
      </a-descriptions-item>
      <a-descriptions-item label="【生产厂家】">
        {{ synopsis.manufacturer }}
      </a-descriptions-item>
    </a-descriptions>
  </div>
</template>

<script>
export default {
  props: ["drug"],
  data() {
    return {
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
      const _this = this;
      this.$axios.get("/synopsis/info/" + this.$props.drug.id).then((res) => {
        _this.synopsis = res.data.data.data;
      });
    },
  },
};
</script>

<style scoped>
.synopsis >>> .ant-descriptions-item-label {
  width: 200px;
}
.synopsis >>> .ant-descriptions-item-content {
  text-align: left;
}
</style>