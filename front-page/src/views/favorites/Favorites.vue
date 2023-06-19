<template>
  <div class="favorites">
    <div class="header">
      <div class="breadcrumb">
        <span>我的</span>
        <a-icon class="icon-right" type="right" />
        <span
          >收藏夹&emsp;-- 共&nbsp;<span>{{ total }}</span
          >&nbsp;条收藏</span
        >
      </div>
    </div>

    <div class="center">
      <p
        style="
          width: 100%;
          height: 38px;
          line-height: 38px;
          text-align: right;
          padding-right: 20px;
        "
      >
        <span style="float: left">收藏永不停下，购买从未开始 --></span>
        <a-popover
          v-model="visible"
          title="将收藏全部添加至购物车"
          trigger="click"
        >
          <a slot="content" @click="hide">&emsp;&emsp;&emsp;取消&emsp;</a>
          <a slot="content" @click="addToShoppingCart()">确定</a>
          <a-button
            type="link"
            style="float: left; height: 38px; line-height: 37px; padding: 0 2px"
          >
            全部添加至购物车
          </a-button>
        </a-popover>
        条件筛选：<a-select
          default-value="全部 "
          style="width: 120px"
          @change="handleChange"
        >
          <a-select-option key="7" value="week"> 最近七天 </a-select-option>
          <a-select-option key="30" value="month"> 最近一个月 </a-select-option>
          <a-select-option key="180" value="sixMonth">
            最近半年
          </a-select-option>
          <a-select-option key="" value="all"> 全部 </a-select-option>
        </a-select>
      </p>

      <a-config-provider :locale="locale" v-if="favoritesInfo.length == 0">
        <a-empty
          description="怎么是空的，快去看看本店的药品吧！"
          :image="simpleImage"
          style="
            width: 100%;
            height: 200px;
            line-height: 200px;
            color: #000000a1;
          "
        />
      </a-config-provider>

      <a-card
        v-for="(item, index) in favoritesInfo"
        size="small"
        style="
          width: 200px;
          height: 205px;
          margin-bottom: 28px;
          margin-right: 22px;
          text-align: left;
        "
        class="info-card"
        v-else
      >
        <span slot="title" @click="goToThisDrugDetails(item)">
          {{ item.drugName }}</span
        >
        <span class="remove" slot="extra" @click="removeOne(item)">移除</span>
        <p style="margin: 0 0 8px 0">
          <img
            @click="goToThisDrugDetails(item)"
            :src="item.picture"
            title="点击查看药品详情"
            alt="图片加载失败"
            width="100"
            height="80"
          />
        </p>
        <p
          style="
            margin: 0 0 8px 0;
            color: #f34e0d;
            font-size: 16px;
            font-weight: 500;
            font-family: Verdana, Geneva, Tahoma, sans-serif;
          "
        >
          ￥{{ parseFloat(item.price).toFixed(2) }}
        </p>
        <p style="margin: 0 0 8px 0">
          收藏时间：{{ splitTime(item.gmtCreate) }}
        </p>
      </a-card>

      <div class="pagination">
        <a-pagination
          v-model="current"
          :page-size-options="pageSizeOptions"
          :total="total"
          :show-total="(total) => `共 ${total} 条`"
          show-size-changer
          :page-size="pageSize"
          @showSizeChange="onShowSizeChange"
          @change="onShowCurrentChange"
          v-show="favoritesInfo.length != 0"
        >
          <template slot="buildOptionText" slot-scope="props">
            <span v-if="props.value !== '50'">{{ props.value }}条/页</span>
            <span v-if="props.value === '50'">全部</span>
          </template>
        </a-pagination>
        <div class="btn_options">
          <a-button
            style="margin-right: 18px"
            @click="showDeletePageModal"
            v-show="favoritesInfo.length != 0"
          >
            删除本页
          </a-button>
          <a-button
            @click="showDeleteAllModal"
            v-show="favoritesInfo.length != 0"
          >
            清空收藏夹
          </a-button>
        </div>
        <div>
          <!-- 删除本页的Modal -->
          <a-modal
            class="deletePageModal"
            v-model="visiblePage"
            :mask="false"
            title="删除本页"
            ok-text="确认"
            cancel-text="取消"
            @ok="hideDeletePageModal"
          >
            <p>
              是否真的要删除本页的<span style="color: #1890ff"
                >&nbsp;{{ favoritesInfo.length }}&nbsp;</span
              >条收藏记录？
            </p>
          </a-modal>
          <!-- 清空收藏夹的Modal -->
          <a-modal
            class="deletePageModal"
            v-model="visibleAll"
            :mask="false"
            title="清空收藏夹"
            ok-text="确认"
            cancel-text="取消"
            @ok="hideDeleteAllModal"
          >
            <p>
              你真的要<span style="color: red">&nbsp;清空&nbsp;</span
              >辛辛苦苦收藏的<span style="color: #1890ff"
                >&nbsp;{{ total }}&nbsp;</span
              >条记录吗？
            </p>
          </a-modal>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { Empty } from "ant-design-vue";
import zhCN from "ant-design-vue/es/locale/zh_CN";

export default {
  data() {
    return {
      locale: zhCN,
      favoritesInfo: [],
      checked: false,
      pageSizeOptions: ["10", "20", "30", "40", "50"],
      current: 1,
      pageSize: 10,
      total: 0,
      visible: false,
      visiblePage: false,
      visibleAll: false,
      time: "",
    };
  },
  beforeCreate() {
    this.simpleImage = Empty.PRESENTED_IMAGE_SIMPLE;
  },
  created() {
    this.getFavoritesInfo();
  },
  methods: {
    /* 将收藏全部添加至购物车的提示 */
    hide() {
      this.visible = false;
    },
    /* 将收藏全部添加至购物车 */
    addToShoppingCart() {
      if (this.total != 0) {
        let formData = new FormData();
        formData.append("username", this.$store.getters.getUser.username);
        this.$axios
          .post("/add/to/shoppingCart", formData, {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          })
          .then((res) => {
            console.log(res);
            if (res.data.status) {
              this.getFavoritesInfo();
              this.$message.success("操作成功，已成功将全部收藏添加至购物车！");
            }
          });
      } else {
        this.$message.warning("您的收藏夹现在是空的！");
      }
    },
    /* 条件筛选 */
    handleChange(value, key) {
      console.log(value);
      console.log(key.data.key);
      this.current = 1;
      this.pageSize = 10;
      this.time = key.data.key;
      this.getFavoritesInfo();
    },
    /* 获取收藏夹的信息 */
    getFavoritesInfo() {
      this.$axios
        .get(
          "/get/collect/info/" +
            this.$store.getters.getUser.username +
            "/" +
            this.current +
            "/" +
            this.pageSize +
            "/" +
            this.time,
          {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          }
        )
        .then((res) => {
          console.log(res);
          this.favoritesInfo = res.data.data.data.records;
          this.total = res.data.data.data.total;
        });
    },
    /* 把返回的时间分割，只显示年-月-日 */
    splitTime(time) {
      let arr = time.split(" ");
      return arr[0];
    },
    /* 跳转到对应药品的详情页 */
    goToThisDrugDetails(item) {
      console.log(item.drugName);
      this.$router.push("/drug/" + item.drugName);
    },
    /* 删除单个 */
    removeOne(item) {
      console.log("移除", item.drugName, item.id);
      this.$axios
        .get("/delete/favorite/" + item.id, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          this.getFavoritesInfo();
        });
    },
    /* 切换页数 */
    onShowCurrentChange(current) {
      this.current = current;
      this.getFavoritesInfo();
      console.log(current);
    },
    /* 切换每一页的数量 */
    onShowSizeChange(current, pageSize) {
      this.pageSize = pageSize;
      if (this.total - pageSize < 0) {
        this.current = 1;
      }
      this.getFavoritesInfo();
      console.log(current, pageSize);
    },
    showDeletePageModal() {
      this.visiblePage = true;
    },
    hideDeletePageModal() {
      this.visiblePage = false;
      console.log("点击了确认");
      let ids = [];
      for (const item of this.favoritesInfo) {
        ids.push(item.id);
      }
      console.log(ids);
      this.$axios
        .post(
          "/favorite/batch/delete/" + this.$store.getters.getUser.username,
          ids,
          {
            headers: {
              Authorization: localStorage.getItem("token"),
            },
          }
        )
        .then((res) => {
          console.log(res);
          if (res.data.status) {
            this.getFavoritesInfo();
            this.$message.success("本页记录成功删除！");
          } else {
            this.$message.warning("服务器被外星人劫持了，请稍后再试。");
          }
        });
    },
    showDeleteAllModal() {
      this.visibleAll = true;
    },
    hideDeleteAllModal() {
      this.visibleAll = false;
      console.log("点击了确认");
      let formData = new FormData();
      formData.append("username", this.$store.getters.getUser.username);
      this.$axios
        .post("/favorite/all/delete", formData, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          console.log(res);
          if (res.data.status) {
            this.getFavoritesInfo();
            this.$message.success("已清空收藏夹！");
          } else {
            this.$message.warning("服务器被外星人劫持了，请稍后再试。");
          }
        });
    },
  },
};
</script>

<style scoped>
.favorites {
  width: 1200px;
  margin: auto;
  margin-bottom: 28px;
}
.favorites .breadcrumb {
  text-align: left;
  font-size: 16px;
  height: 56px;
  line-height: 56px;
  color: #000000a6;
}
.favorites .breadcrumb .icon-right {
  font-size: 14px;
  color: #000000a6;
}
.favorites .breadcrumb a {
  color: #000000a6;
}
.favorites .breadcrumb a:hover {
  color: #1890ff;
}

.favorites .center {
  min-height: 400px;
  background-color: #fff;
  border-radius: 10px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  color: #000000a6;
  padding: 28px 38px 38px 52px;
}
.favorites .center .info-card:hover {
  cursor: pointer;
}
.favorites .center .remove:hover {
  color: #1890ff;
}
.favorites .center >>> .ant-card-head-title {
  color: #000000a6;
  font-weight: 530;
}
.favorites .center >>> .ant-card-head-title:hover {
  color: #f34e0d;
}

.favorites .center .pagination {
  width: 100%;
  text-align: left;
}
.favorites .center .pagination .btn_options {
  width: 100%;
  height: 32px;
  line-height: 32px;
  text-align: right;
  margin-top: -32px;
  padding-right: 20px;
}

.deletePageModal >>> .ant-modal-content {
  width: 398px;
  margin-left: 86px;
  margin-top: 66px;
}
</style>