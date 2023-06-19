<template>
<div>
<!-- 走马灯 -->
  <a-carousel autoplay class="yjx-home-carousel">
    <div v-for="picture in carouselData">
      <img
        class="img"
        :src="picture.path"
        width="658"
        height="370"
      />
    </div>
      
    </a-spin>
  </a-carousel>
</div>
</template>
<script>
export default {
  data() {
    return {
      carouselData: [],
    };
  },
  methods: {
    getCarouselData() {
      const _this = this;
      this.$axios.get("/query/enable/carousels").then((res) => {
        _this.carouselData = res.data.data.data;
      });
    },
  },
  created() {
    this.getCarouselData();
  },
};
</script>

<style>
.yjx-home-carousel {
  width: 658px;
  height: 370px;
  margin: 1px 6px 0 6px;
  border-radius: 10px;
  overflow: hidden;
}
.yjx-home-carousel:hover {
  cursor: pointer;
}
.yjx-home-carousel .img {
  border-radius: 10px;
}
/* .yjx-home-carousel .img:hover {
  cursor: pointer;
} */
</style>