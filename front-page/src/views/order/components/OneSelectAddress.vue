<template>
  <div id="one-select-address">
    <div class="empty" v-if="noAddressInfo">
      <a-empty
        image=""
        :image-style="{
          height: '60px',
        }"
      >
        <p slot="description">还没有保存地址信息，左上角添加地址</p>
      </a-empty>
    </div>
    <div class="existAddress" v-else>
      <a-card style="border: none">
        <a-card-grid
          :hoverable="false"
          :style="checked == index ? defaultCss : ''"
          v-for="(item, index) in data"
          @click="changeAddress(item, index)"
        >
          <div class="all-info">
            <p style="font-weight: 600">
              {{ showAddress(item.receiptAddress) }}
            </p>
            <a-divider />
            <p>{{ item.detailsAddress }}</p>
            <p>收货人：{{ item.receiptName }}</p>
            <p>联系方式：{{ item.phone }}</p>
          </div>
          <!-- ✔ -->
          <p style="text-align: right">
            <span
              v-show="item.isDefault == 1 ? true : false"
              class="default_address"
              >默认地址</span
            >
            <span
              v-show="setToDefault == index ? true : false"
              class="set_default_address"
              @click="setThisToDefault(item, index)"
              >设为默认</span
            >

            <OneUpdateAddressInfo
              :item="item"
              @click.native="transfer(item)"
              @fatherMethod="getAddressInfo()"
            >
              <span class="update"></span
            ></OneUpdateAddressInfo>
          </p>
        </a-card-grid>
      </a-card>
    </div>
  </div>
</template>

<script>
import OneUpdateAddressInfo from "@/views/order/components/OneUpdateAddressInfo";
export default {
  components: {
    OneUpdateAddressInfo,
  },
  data() {
    return {
      data: [],
      noAddressInfo: true,
      defaultCss: "border: 7px dashed #216efdb7; box-sizing: border-box;",
      checked: 0,
      setToDefault: -1,
      item: {},
    };
  },
  methods: {
    getAddressInfo() {
      this.$axios
        .get(
          "/query/shoppingAddressInfo/" + this.$store.getters.getUser.username
        )
        .then((res) => {
          // console.log(res);
          this.data = res.data.data.data;
          for (const getInfo of res.data.data.data) {
            if (getInfo.isDefault == 1) {
              this.$store.commit("SET_RECEIPTINFO", getInfo);
            }
          }
          this.noAddressInfo = !res.data.status;
          this.checked = 0;
          this.setToDefault = -1;
        });
    },
    showAddress(address) {
      let arr = address.split("/");
      let str = "";
      for (const item of arr) {
        str += item;
      }
      return str;
    },
    changeAddress(item, index) {
      // console.log(item);
      // console.log(index);
      this.$store.commit("SET_RECEIPTINFO", item);
      this.checked = index;
      item.isDefault == 1
        ? (this.setToDefault = -1)
        : (this.setToDefault = index);
    },
    setThisToDefault(item, index) {
      // console.log(item.username, item.id);
      this.$axios
        .get("/set/default/" + item.username + "/" + item.id, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          // console.log(res);
          this.$message.success(res.data.message);
          this.getAddressInfo();
        });
    },
    transfer(item) {
      this.item = item;
    },
  },
  created() {
    this.getAddressInfo();
  },
};
</script>

<style scoped>
#one-select-address >>> .ant-card-body {
  padding: 0 0 0 9px;
}
#one-select-address >>> .ant-card-grid {
  padding: 5px 8px 8px 8px;
  border: 7px dashed rgba(255, 255, 255, 0);
  border-radius: 5px;
  width: 24%;
  height: 160px;
  text-align: center;
  margin-right: 6px;
  margin-bottom: 5px;
}
#one-select-address >>> .ant-card-grid:hover {
  border: 7px dashed #216efdb7;
  cursor: pointer;
}
#one-select-address .all-info {
  height: 112px;
}
#one-select-address >>> .ant-card-grid p {
  text-align: left;
  margin: 0;
  font-size: 13px;
}
#one-select-address >>> .ant-card-grid p .update {
  color: #cc9977;
  font-size: 14px;
}

#one-select-address >>> .ant-divider {
  margin: 5px 0;
}
#one-select-address .default_address {
  float: left;
  color: #fff;
  background: rgba(231, 145, 33, 0.842);
  padding: 2px 5px;
  border-radius: 5px;
}
#one-select-address .set_default_address {
  float: left;
  color: rgba(119, 115, 115, 1);
  background: rgb(245, 245, 245);
  padding: 2px 5px;
  border-radius: 5px;
}
#one-select-address .set_default_address:hover {
  color: red;
}
</style>
