<template>
  <div class="new_drugs">
    <div class="title">
      <h2>最 新 药 品</h2>
      <p><router-link to="/category">更多</router-link></p>
    </div>
    <div class="drugs">
      <a-card hoverable class="card" v-for="drug in drugs">
        <img
          slot="cover"
          alt="example"
          :src="drug.picture"
          @click="goToDetails(drug.name)"
          title="点击查看详情"
        />
        <div class="afterImg">
          <p class="name">
            <span @click="goToDetails(drug.name)">{{ drug.name }}</span>
          </p>
          <p class="price">￥{{ parseFloat(drug.price).toFixed(2) }}</p>
          <p class="desc" title="详情请查看说明书">
            作用：{{ drug.description }}
          </p>
        </div>
      </a-card>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      drugs: [],
    };
  },
  created() {
    this.getNewDrugs();
  },
  methods: {
    getNewDrugs() {
      this.$axios.get("/home/new/drugs").then((res) => {
        this.drugs = res.data.data.data.records;
      });
    },
    goToDetails(name) {
      console.log(name);
      const { href } = this.$router.resolve({
        path: "/drug/" + name,
      });
      window.open(href, "_blank");
    },
  },
};
</script>

<style lang="less" scoped>
@import "../style/new_drugs.less";
</style>
 