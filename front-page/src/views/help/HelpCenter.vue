<template>
  <div class="helpCenter">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /> <span>售后服务</span>
      </div>
    </div>
    <div class="center">
      <sale-service />
    </div>
  </div>
</template>

<script>
import SaleService from "@/views/drug_details/components/SaleService";

export default {
  components: {
    SaleService,
  },
};
</script>

<style lang="less" scoped>
@import url("./style/helpCenter.less");
</style>
