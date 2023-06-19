<template>
  <div class="introduce">
    <div class="top">
      <Search />
    </div>
    <div class="bottom">
      <p class="header"><span class="title">限时优惠</span></p>
      <div class="drugList">
        <div>
          <div class="left">
            <img
              src="http://img.800pharm.com/images//20160427/20160427142822_418.jpg"
              alt=""
              width="138"
              height="118"
            />
          </div>
          <div class="right">
            <p class="drugName">布洛芬缓释胶囊</p>
            <p>原：<span>￥100.00</span></p>
            <p>优惠：<span>8折</span></p>
            <p>现：<span>￥80.00</span></p>
          </div>
        </div>
        <div>
          <div class="left">
            <img
              src="http://img.800pharm.com/images//20160427/20160427142822_418.jpg"
              alt=""
              width="138"
              height="118"
            />
          </div>
          <div class="right">
            <p class="drugName">布洛芬缓释胶囊</p>
            <p>原：<span>￥100.00</span></p>
            <p>优惠：<span>8折</span></p>
            <p>现：<span>￥80.00</span></p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Search from "@/components/Search";
export default {
  components: { Search },
  data() {
    return {};
  },
};
</script>

<style scoped>
.introduce {
  width: 271px;
  height: 371px;
}
.introduce .top {
  height: 46px;
}

.introduce .bottom {
  height: 324px;
  background-color: #fff;
  border-radius: 8px;
}

.introduce .bottom .header {
  height: 36px;
  line-height: 36px;
  text-align: left;
  margin: 0;
}

.introduce .bottom .header .title {
  margin-left: 15px;
  font-size: 16px;
}

.introduce .bottom .drugList {
  display: flex;
  align-content: flex-start;
  flex-wrap: wrap;
  justify-content: space-evenly;
}

.introduce .bottom .drugList > div {
  width: 92%;
  height: 125px;
  margin: 11px 0 0 0;
  text-align: left;
  border: 1px solid #e8e8e8;
  border-radius: 5px;
  display: flex;
}

.introduce .bottom .drugList > div .left img {
  cursor: pointer;
}

.introduce .bottom .drugList > div .right p {
  margin: 0;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 26px;
  line-height: 26px;
  font-size: 13px;
}

.introduce .bottom .drugList > div .drugName {
  cursor: pointer;
}

.introduce .bottom .drugList > div .drugName:hover{
  color: #f34e0d;
}

.introduce .bottom .drugList > div .right p span {
  font-family: Verdana, Geneva, Tahoma, sans-serif;
  color: #f34e0d;
}
</style>
