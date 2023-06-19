<template>
  <div id="myInfo">
    <div class="header">
      <div class="breadcrumb">
        <router-link :to="{ path: '/' }">
          <span style="font-weight: normal">首页</span></router-link
        ><a-icon class="icon-right" type="right" /> <span>我的</span
        ><a-icon class="icon-right" type="right" />
        <span>个人资料</span>
      </div>
    </div>

    <div class="myInfo-center">
      <p class="revise">
        <UpdateMyInfo @fatherMethod="getMyInfo()" />
      </p>

      <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
        <img alt="头像-大图" style="width: 100%" :src="previewImage" />
      </a-modal>
      <div class="desc">
        <a-descriptions title="个人资料" :column="1" bordered>
          <a-descriptions-item label="头像">
            <a
              style="display: inline-block; margin-left: 98px"
              href="javascript:;"
              @click="showModal"
              ><img
                class="avatar"
                :src="userInfo.avatar"
                alt="头像"
                title="点击查看大图"
                width="88"
                height="88"
            /></a>
            <a-upload
              name="file"
              :action="
                $store.state.imgBaseUrl +
                'upload/avatar/' +
                this.$route.params.username
              "
              @change="handleChange"
              class="upload-avatar"
            >
              <a-button type="link" class="update-avatar">
                <a-icon type="upload" /><span style="font-size: 14px">
                  更换头像</span
                >
              </a-button>
            </a-upload>
          </a-descriptions-item>
          <a-descriptions-item label="账号">
            <span
              >{{ userInfo.username }}
              <a-tag
                :color="userInfo.auth == 4 ? 'blue' : 'orange'"
                style="font-size: 14px"
              >
                {{ userInfo.auth == 4 ? "普通用户" : "会员" }}
              </a-tag></span
            >
          </a-descriptions-item>
          <a-descriptions-item label="昵称">
            <span>{{ userInfo.nickName }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="姓名">
            <span> {{ userInfo.realName }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="性别">
            <span> {{ userInfo.gender }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="生日"
            ><span>{{ userInfo.birthday }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="年龄"
            ><span>{{ age }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="星座"
            ><span>{{ constellation }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="手机号码">
            <span>{{ userInfo.phone }}</span>
          </a-descriptions-item>
          <a-descriptions-item>
            <span slot="label" style="font-size: 14px">
              &emsp;邮箱地址
              <a-tooltip title="注册时已经绑定，暂不支持修改">
                <a-icon type="question-circle-o" />
              </a-tooltip>
            </span>
            <span>{{ userInfo.email }}</span>
          </a-descriptions-item>
          <a-descriptions-item label="所在地区">
            <span> {{ userInfo.address }}</span>
          </a-descriptions-item>
        </a-descriptions>
      </div>
    </div>
  </div>
</template>

<script>
import UpdateMyInfo from "@/views/mine/components/UpdateMyInfo";
export default {
  components: { UpdateMyInfo },
  data() {
    return {
      userInfo: {},
      age: "",
      constellation: "",
      previewVisible: false,
      previewImage: "",
    };
  },
  methods: {
    handleChange(info) {
      if (info.file.status === "done") {
        // console.log(info.file);
        // console.log(info.file.response.data.data);
        let userInfo = this.$store.getters.getUser;
        userInfo.avatar = info.file.response.data.data.avatar;
        this.$store.commit("SET_USERINFO", userInfo);
        this.$message.success(`头像更换成功！`);
        const { href } = this.$router.resolve({
          name: "我的资料",
          path: "/mine/info/" + this.$route.params.username,
        });
        window.open(href, "_self");
      } else if (info.file.status === "error") {
        this.$message.error(`上传失败！`);
      }
    },
    handleCancel() {
      this.previewVisible = false;
    },
    showModal() {
      this.previewVisible = true;
      this.previewImage = this.userInfo.avatar;
    },
    getMyInfo() {
      let username = this.$route.params.username;
      // console.log(username);
      this.$axios
        .get("/front/user/info/" + username, {
          headers: {
            Authorization: localStorage.getItem("token"),
          },
        })
        .then((res) => {
          // console.log(res);
          this.userInfo = res.data.data.data;
          if (
            res.data.data.data.avatar == "" ||
            res.data.data.data.avatar == null
          ) {
            this.userInfo.avatar =
              "https://cube.elemecdn.com/9/c2/f0ee8a3c7c9638a54940382568c9dpng.png";
          } else {
            this.userInfo.avatar = res.data.data.data.avatar;
          }
          this.age = this.getAge(res.data.data.data.birthday);
          this.constellation = this.getConstellation(
            res.data.data.data.birthday
          );
          this.formatAddress(res.data.data.data.address);
          // console.log(this.userInfo);
        });
    },
    formatAddress(address) {
      if (address != null && address != "") {
        let addressArr = address.split("/");
        let str = "";
        for (const item of addressArr) {
          str += item;
        }
        this.userInfo.address = str;
      } else {
        return "";
      }
    },
    /* 计算年龄 */
    getAge(birthday) {
      if (birthday == null || birthday == "") {
        return "";
      } else {
        let date = new Date();
        // 得到当前时间
        let yearNow = date.getFullYear();
        let monthNow = date.getMonth() + 1;
        let dayNow = date.getDate();
        // 把生日分成年月日
        if (birthday != "" || birthday != null) {
          let str = birthday.split("-");

          let yearBirth = str[0];
          let monthBirth = str[1];
          let dayBirth = str[2];
          if (monthNow < monthBirth) {
            let age = yearNow - yearBirth - 1;
            return age;
          } else if (monthNow > monthBirth) {
            let age = yearNow - yearBirth;
            return age;
          } else {
            if (dayNow < dayBirth) {
              let age = yearNow - yearBirth - 1;
              return age;
            } else {
              let age = yearNow - yearBirth;
              return age;
            }
          }
        }
      }
    },
    /* 判断星座 */
    getConstellation(birthday) {
      // 把生日分成年月日
      if (null != birthday && birthday != "") {
        let str = birthday.split("-");

        let monthBirth = str[1];
        let dayBirth = str[2];

        if (monthBirth == 1) {
          return dayBirth < 21 ? "摩羯座" : "水瓶座";
        } else if (monthBirth == 2) {
          return dayBirth < 20 ? "水瓶座" : "双鱼座";
        } else if (monthBirth == 3) {
          return dayBirth < 21 ? "双鱼座" : "白羊座";
        } else if (monthBirth == 4) {
          return dayBirth < 21 ? "白羊座" : "金牛座";
        } else if (monthBirth == 5) {
          return dayBirth < 22 ? "金牛座" : "双子座";
        } else if (monthBirth == 6) {
          return dayBirth < 22 ? "双子座" : "巨蟹座";
        } else if (monthBirth == 7) {
          return dayBirth < 23 ? "巨蟹座" : "狮子座";
        } else if (monthBirth == 8) {
          return dayBirth < 24 ? "狮子座" : "处女座";
        } else if (monthBirth == 9) {
          return dayBirth < 24 ? "处女座" : "天秤座";
        } else if (monthBirth == 10) {
          return dayBirth < 24 ? "天秤座" : "天蝎座";
        } else if (monthBirth == 11) {
          return dayBirth < 23 ? "天蝎座" : "射手座";
        } else if (monthBirth == 12) {
          return dayBirth < 22 ? "射手座" : "摩羯座";
        }
      } else {
        return "";
      }
    },
  },
  created() {
    this.getMyInfo();
  },
};
</script>

<style>
@import "./style/myInfo.css";
</style>
