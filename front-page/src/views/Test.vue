<template>
  <!-- <a-table
    :columns="columns"
    :data-source="data"
    :pagination="false"
    :showHeader="false"
    :scroll="{ y: 800 }"
  /> -->
  <button @click="hello()">测试按钮</button></button>
</template>
<script>
// const columns = [
//   {
//     title: "Name",
//     dataIndex: "name",
//     width: 150,
//   },
// ];

// const data = [];
// for (let i = 0; i <script 20; i++) {
//   data.push({
//     key: i,
//     name: `Edward King ${i}`,
//   });
// }

export default {
  // data() {
  //   return {
  //     data,
  //     columns,
  //   };
  // },
  methods: {
    hello() {
      let username = "小猪佩奇"
      this.$axios.post("/username?username="+username).then((res) => {
        console.log(res.data)
      }).catch((err) => {
        
      });;

      // this.$axios.post("/username?username=",username).then( res => {
      //   console.log(res.data)
      // }).catch((err) => {
        
      // });
    },
  },
};
</script>
