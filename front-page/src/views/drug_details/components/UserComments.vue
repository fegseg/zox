<template>
  <div class="userComments">
    <div class="none" v-if="data.length == 0">
      <a-empty description="暂无评论" />
    </div>
    <div class="commentsList" v-else>
      <a-list
        class="comment-list"
        :header="`共 ${data.length} 条评价`"
        item-layout="horizontal"
        :data-source="data"
      >
        <a-list-item slot="renderItem" slot-scope="item, index">
          <a-comment
            :author="item.username"
            :avatar="item.avatar"
          >
            <p slot="content">
              {{ item.message }}
            </p>
            <a-tooltip slot="datetime" :title="item.datetime">
              <span>{{ item.gmtCreate }}</span>
            </a-tooltip>
          </a-comment>
          <a-rate :default-value="item.rate" disabled />
        </a-list-item>
      </a-list>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      data: [],
    };
  },
  created() {
    this.getCommentsByDrug();
  },
  methods: {
    getCommentsByDrug() {
      let name = this.$route.params.name;
      let formData = new FormData();
      formData.append("drugName", name);
      this.$axios.post("/comments/drug/has", formData).then((res) => {
        console.log("评价信息=>", res);
        this.data = res.data.data.responseBody;
      });
    },
  },
};
</script>

<style scoped>
.userComments >>> span.ant-comment-content-author-name {
  font-size: 16px;
}
.userComments >>> span.ant-comment-content-author-time {
  font-size: 14px;
}
.userComments >>> img {
  width: 50px;
  height: 50px;
}
.userComments >>> p {
  white-space: inherit;
  text-align: left;
}
.userComments >>> .ant-list-header {
  text-align: right;
}

</style>
