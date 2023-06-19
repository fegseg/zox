<template>
  <div>
    <!-- 搜索框 -->
    <a-input-search
      placeholder="请输入药品名称进行查找"
      class="search"
      @search="onSearch"
    />
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    onSearch(value) {
      console.log(value);
      if (value != "") {
        this.$router.push("/search/result/" + value).catch((err) => {});
     
      }
    },
  },
};
</script>
<style scoped>
.search {
  width: 100%;
}
.search >>> .ant-input {
  height: 38px;
  border-radius: 8px;
}
</style>