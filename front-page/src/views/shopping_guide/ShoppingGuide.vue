<template>
  <div class="shoppingGuide">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /> <span>购物指南</span>
      </div>
    </div>
    <div class="center">
      <h2 class="title">购物指南</h2>
      <a-timeline>
        <a-timeline-item>
          <h2>1. 选择想要购买的药品，点击“购买”或“详情”。</h2>
          <br />
          <img src="./img/1.png" alt="" width="1000" height="400" />
        </a-timeline-item>
        <a-timeline-item
          ><h2>
            2.
            参考医师叮嘱，查看药品的详细信息，确认无误后，先选择购买数量，再点击“立即购买”。
          </h2>
          <br />
          <img src="./img/2.png" alt="" width="1000" height="500"
        /></a-timeline-item>
        <a-timeline-item
          ><h2>3. 填写收货信息，若已填写过，直接选择收货地址即可。</h2>
          <br />
          <img src="./img/3.png" alt="" width="900" height="250"
        /></a-timeline-item>
        <a-timeline-item
          ><h2>4. 再次确认购买药品的信息是否有误。</h2>
          <br />
          <img src="./img/4.png" alt="" width="900" height="200"
        /></a-timeline-item>
        <a-timeline-item
          ><h2>5. 备注信息（选填），提交订单。</h2>
          <br />
          <img src="./img/5.png" alt="" width="900" height="350"
        /></a-timeline-item>
        <a-timeline-item
          ><h2>6. 交易完成，生成交易订单，可在“我的订单”里查看订单相关信息。</h2>
          <br />
          <img src="./img/6.png" alt="" width="1100" height="250"
        /></a-timeline-item>
      </a-timeline>
    </div>
  </div>
</template>

<script>
export default {};
</script>

<style lang="less" scoped>
@import url("./style/shoppingGuide.less");
</style>
