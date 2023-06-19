<template>
  <div class="drug-details">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" />
        <router-link :to="'/category/' + drug.sort">{{ drug.sort }}</router-link
        ><a-icon class="icon-right" type="right" />
        <span><a>{{ drug.name }}</a></span>
      </div>
    </div>

    <div class="center">
      <div class="left">
        <EnlargePicture class="drug" />
        <div class="star-report">
          <a-button
            type="link"
            size="small"
            class="btn-star"
            @click="changeDrugStatus"
            v-if="!starStatus"
            ><a-icon key="star" type="star" />&nbsp; 收藏药品
          </a-button>
          <a-button
            type="link"
            size="small"
            class="btn-star"
            @click="changeDrugStatus"
            v-else
            ><a-icon type="star" theme="filled" />&nbsp; 取消收藏
          </a-button>
          <a-button type="link" size="small" class="btn-report">举报 </a-button>
        </div>
        <p>
          <span style="color: red">温馨提示：</span
          >图片均为本网站对原品的真实拍摄，仅供参考；如遇新包装上市可能存在上新滞后，请以实物为准。
        </p>
      </div>
      <div class="right">
        <h2>
          {{ drug.name }}
        </h2>
        <p>适用症状： {{ drug.sort }}</p>
        <p>规&emsp;格：{{ drug.standard }}</p>
        <p>产品编号：{{ drug.productNumber }}</p>
        <p>
          批准文号：{{ drug.approvalNumber
          }}<a
            style="color: #59a3db"
            href="http://app1.nmpa.gov.cn/data_nmpa/face3/base.jsp?tableId=25&tableName=TABLE25&title=%B9%FA%B2%FA%D2%A9%C6%B7&bcId=152904713761213296322795806604"
            >（国家食药总局查询）</a
          >
        </p>
        <p>
          生产厂家：<span style="color: #59a3db">{{ drug.manufacturer }}</span>
        </p>
        <p>
          价&emsp;格：<span
            style="
              color: #f34e0d;
              font-size: 18px;
              font-family: Verdana, Geneva, Tahoma, sans-serif;
            "
            >￥<strong style="font-size: 22px">{{
              parseFloat(drug.price).toFixed(2)
            }}</strong></span
          >
        </p>
        <p>
          数量：
          <a-input-number
            id="inputNumber"
            v-model="value"
            :min="1"
            :max="200"
            @change="onChange"
            class="amount"
          />
        </p>

        <a-button size="large" class="btn" @click="addToShoppingCart()">
          <a-icon key="shoppingCart" type="shopping-cart" />&nbsp; 加入购物车
        </a-button>
        <a-button type="danger" size="large" class="btn" @click="buy()"
          ><a-icon key="shopping" type="shopping" />&nbsp; 立即购买
        </a-button>
        <p class="hint">
          <a-icon type="exclamation-circle" />&nbsp;请仔细阅读<u
            >{{ drug.name }}说明书</u
          >并按照说明使用或在医生与药师指导下购买和使用
        </p>
        <p class="promise">
          本店承诺：
          <span><i>正</i>确保正品</span>
          <span><i>专</i>专业药师</span>
          <span><i>付</i>货到付款</span>
          <span><i>退</i>七天无理由退换</span>
        </p>
      </div>

      <div class="bottom">
        <a-card
          style="width: 100%"
          :tab-list="tabListNoTitle"
          :active-tab-key="noTitleKey"
          @tabChange="(key) => onTabChange(key, 'noTitleKey')"
        >
          <!-- 商品介绍 -->
          <div v-if="noTitleKey === 'introduce'">
            <Introduce :drugName="this.$route.params.name" />
          </div>
          <!-- 药品说明书 -->
          <div v-else-if="noTitleKey === 'synopsis'">
            <SynopsisInfo :drug="drug" />
          </div>
          <!-- 用户评价 -->
          <div v-else-if="noTitleKey === 'evaluation'">
            <UserComments />
          </div>
          <!-- 售后服务 -->
          <div v-else="noTitleKey === 'service'">
            <sale-service />
          </div>
        </a-card>

        <!-- 推荐药品组件 -->
        <sameRecommend />
      </div>
    </div>
  </div>
</template>
<script>
import EnlargePicture from "@/components/EnlargePicture";
import Introduce from "@/views/drug_details/components/Introduce";
import SynopsisInfo from "@/views/drug_details/components/SynopsisInfo";
import SaleService from "@/views/drug_details/components/SaleService";
import SameRecommend from "@/views/drug_details/components/SameRecommend";
import UserComments from "@/views/drug_details/components/UserComments";

export default {
  components: {
    EnlargePicture,
    Introduce,
    SynopsisInfo,
    SaleService,
    SameRecommend,
    UserComments,
  },
  data() {
    return {
      // 展示的标准图
      imgUrl: "",
      // 购买数量
      value: 1,
      // 药品信息
      drug: {},
      // 收藏状态
      starStatus: false,
      // 标题
      tabListNoTitle: [
        {
          key: "introduce",
          tab: "商品介绍",
        },
        {
          key: "synopsis",
          tab: "说明书",
        },
        {
          key: "evaluation",
          tab: "用户评价",
        },
        {
          key: "service",
          tab: "售后服务",
        },
      ],
      noTitleKey: "introduce",
    };
  },
  beforeCreate() {
    document.title = this.$route.params.name;
  },
  created() {
    this.getInfo();
  },
  methods: {
    /* 根据id获取药物的信息在购物页面展示 */
    getInfo() {
      const name = this.$route.params.name;
      console.log(name);
      const _this = this;
      this.$axios.get("/medicine/info/name/" + name).then((res) => {
        console.log(res);
        let drugInfo = res.data.data.data;
        _this.drug = res.data.data.data;
        _this.imgUrl = drugInfo.picture;
        if (this.$store.getters.getUser != null) {
          _this.getDrugCollectDrugStatus(
            _this.$store.getters.getUser.username,
            drugInfo.name
          );
        }
      });
    },
    /* 获取药品的收藏状态 */
    getDrugCollectDrugStatus(username, drugName) {
      this.$axios
        .get("/get/collect/status/" + username + "/" + drugName)
        .then((res) => {
          console.log("status", res);
          this.starStatus = res.data.data.status == 1 ? true : false;
        });
    },
    /* 改变药品的收藏状态 */
    changeDrugStatus() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          let collectInfo = {
            username: this.$store.getters.getUser.username,
            drugName: this.drug.name,
            picture: this.drug.picture,
            price: this.drug.price,
          };
          this.$axios.post("/drug/collect", collectInfo).then((res) => {
            console.log(res);
            this.getInfo();
            if (res.data.message === "收藏成功！") {
              this.$message.success(res.data.message);
            } else {
              return null;
            }
          });
          console.log(this.drug);
        } else {
          this.$message.warning("请先登录！");
        }
      } else {
        this.$message.warning("请先登录！");
      }
    },
    /* 填加药品到购物车 */
    addToShoppingCart() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          this.$axios
            .get("/has/user/" + this.$store.getters.getUser.username)
            .then((res) => {
              console.log(res.data.data.isLogin);
              if (res.data.data.isLogin) {
                let drugName = this.$route.params.name;
                let username = this.$store.getters.getUser.username;
                this.$axios
                  .post(
                    "/shopping/add/" +
                      drugName +
                      "/" +
                      username +
                      "/" +
                      this.value
                  )
                  .then((res) => {
                    console.log(res);
                    if (res.data.message === "添加成功！") {
                      this.$message.success(
                        res.data.message + drugName + "+" + this.value
                      );
                    } else {
                      this.$message.warning(res.data.message);
                      this.value = res.data.data.data;
                    }
                  })
                  .catch((err) => {
                    console.log("不能添加了");
                  });
              } else {
                this.$message.warning("登录成功以后才能添加药品到购物车哦！");
              }
            });
        } else {
          this.$message.warning("请先登录！");
        }
      } else {
        this.$message.warning("请先登录！");
      }
    },
    /* 买药 */
    buy() {
      if (this.$store.getters.getUser != null) {
        if (this.$store.getters.getUser.username != null) {
          let username = this.$store.getters.getUser.username;
          let drugInfo = {
            drugName: this.$route.params.name,
            amount: this.value,
          };
          let drugs = [];
          drugs.push(drugInfo);
          console.log(drugs);
          this.$axios.post("/buy/drugs/" + username, drugs).then((res) => {
            console.log(res);
          });
          this.$router.push("/order/confirm").catch((err) => {});
        } else {
          this.$message.warning("请先登录！");
        }
      } else {
        this.$message.warning("请先登录！");
      }
    },

    /* 数量框 */
    onChange(value) {
      console.log("changed", value);
    },
    /* 切换标签 */
    onTabChange(key, type) {
      console.log(key, type);
      this[type] = key;
    },
  },
};
</script>

<style>
@import "./style/drugDetails.css";
</style>
