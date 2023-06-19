<template>
  <div class="yjx-drug">
    <div class="yjx-home-floor2">
      <HealthTips />
      <Carousel class="carousel" />
      <time-line />
    </div>

    <div class="yjx-home-floor3">
      <a-card
        class="big-card"
        style="width: 1200px"
        title="常 用 药 品 推 荐"
        :tab-list="tabList"
        :active-tab-key="key"
        @tabChange="(key) => onTabChange(key, 'key')"
      >

      
      <span slot="extra"  class="more"
          ><router-link to="/websocket">问诊</router-link></span
          >

        <span slot="extra" class="more"
          ><router-link to="/category">更多</router-link></span
          >
        <div class="drug-allCard">
          <a-card
            hoverable
            style="width: 270px"
            class="drug-card"
            v-for="item in drugInfoList"
          >
            <img
              slot="cover"
              alt="图片加载失败"
              :src="item.picture"
              @click="goToDetails(item.name)"
            />
            <template slot="actions" class="ant-card-actions">
              <div
                @click="changeDrugStatus(item)"
                v-if="!nameArr.includes(item.name)"
              >
                <a-icon key="star" type="star" />&nbsp;收藏
              </div>
              <div @click="changeDrugStatus(item)" v-else>
                <a-icon type="star" theme="filled" />&nbsp;已收藏
              </div>
              <div @click="goToDetails(item.name)">
                <a-icon key="shopping" type="shopping" />&nbsp;购买
              </div>
              <div @click="goToDetails(item.name)">
                <a-icon key="profile" type="profile" />&nbsp;详情
              </div>
            </template>
            <a-card-meta
              :title="item.name"
              description=""
              @click="goToDetails(item.name)"
            >
            </a-card-meta>
            <span
              style="
                color: #f34e0d;
                font-size: 18px;
                display: inline-block;
                margin-top: 8px;
                font-family: Verdana, Geneva, Tahoma, sans-serif;
              "
              >￥{{ parseFloat(item.price).toFixed(2) }}</span
            >
          </a-card>
        </div>
      </a-card>
    </div>

    <NewDrugs />
    <HotTips />
  </div>
</template>

<script>
import Carousel from "@/components/Carousel.vue";
import HealthTips from "@/components/HealthTips.vue";
import TimeLine from "@/components/TimeLine.vue";
import HotTips from "@/views/home/components/HotTips.vue";
import NewDrugs from "@/views/home/components/NewDrugs.vue";

export default {
  components: { Carousel, HealthTips, TimeLine, HotTips, NewDrugs },
  name: "Drug",
  data() {
    return {
      tabList: [
        {
          key: "0",
          tab: "感冒发热",
          // scopedSlots: { tab: "customRender" },
        },
        {
          key: "1",
          tab: "肠胃疾病",
        },
        {
          key: "2",
          tab: "呼吸道类",
        },
        {
          key: "3",
          tab: "心脑血管",
        },
        {
          key: "4",
          tab: "皮肤科药",
        },
      ],
      key: "0",
      drugInfoList: [],
      nameArr: [],
    };
  },
  methods: {
    111(){
      window.open("", "_self")
    },
    onTabChange(key, type) {
      // console.log(key, type);
      // console.log(this.tabList[key].tab);
      this[type] = key;
      // console.log(this[type]);
      this.getDrugInfoList();
    },
    /* 获取药品列表 */
    getDrugInfoList() {
      let currentPage = 1;
      let pageSize = 8;
      this.$axios
        .get(
          "/search2/with/" +
            currentPage +
            "/" +
            pageSize +
            "/" +
            this.tabList[this.key].tab
        )
        .then((res) => {
          // console.log(res);
          this.drugInfoList = res.data.data.data.records;
          // console.log(_this.drugInfoList);
        });
    },
    /* 获取当前用户的收藏 */
    getThisUserFavorites() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          this.$axios
            .get("/get/collect/name/" + this.$store.getters.getUser.username)
            .then((res) => {
              this.nameArr = res.data.data.data;
            });
        }
      }
    },
    /* 改变药品的收藏状态 */
    changeDrugStatus(item) {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          let collectInfo = {
            username: this.$store.getters.getUser.username,
            drugName: item.name,
            picture: item.picture,
            price: item.price,
          };
          this.$axios.post("/drug/collect", collectInfo).then((res) => {
            // console.log(res);
            this.getDrugInfoList();
            this.getThisUserFavorites();
            if (res.data.message === "收藏成功！") {
              this.$message.success(res.data.message);
            } else {
              return null;
            }
          });
          // console.log(this.drug);
        } else {
          this.$message.warning("请先登录！");
        }
      } else {
        this.$message.warning("请先登录！");
      }
    },
    /* 跳转到详情页 */
    goToDetails(name) {
      // console.log(name);
      const { href } = this.$router.resolve({
        path: "/drug/" + name,
      });
      window.open(href, "_blank");
    },
  },
  created() {
    this.getDrugInfoList();
    this.getThisUserFavorites();
  },
};
</script>

<style>
@import "./style/drug.css";
</style>
