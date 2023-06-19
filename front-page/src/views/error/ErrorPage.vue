<template>
  <a-result title="当前页面正在开发中，先回去看看其他的页面吧！">
    <template #extra>
      <a-button @click="goBack"> 返回上一个页面 </a-button>
      <a-button type="primary">
        <a href="/">前往首页</a>
      </a-button>
    </template>
  </a-result>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    goBack() {
      this.$router.go(-1); //返回上一层
    },
  },
};
</script>