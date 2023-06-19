<template>
  <div>
    <div class="desc">
      <el-divider content-position="left">系统信息</el-divider>
      <el-descriptions :column="1" border>
        <el-descriptions-item label="版本">1.0</el-descriptions-item>
        <el-descriptions-item label="前台地址"
          >localhost:7028</el-descriptions-item
        >
        <el-descriptions-item label="后台地址"
          >localhost:8027</el-descriptions-item
        >
        <el-descriptions-item label="服务器地址"
          >localhost:9000</el-descriptions-item
        >
        <el-descriptions-item label="操作系统"> Windows </el-descriptions-item>
        <el-descriptions-item label="运行环境"> tomcat8 </el-descriptions-item>
        <el-descriptions-item label="vue版本">2.0</el-descriptions-item>
        <el-descriptions-item label="npm版本">6.14.12</el-descriptions-item>
        <el-descriptions-item label="node.js版本">v14.16.1</el-descriptions-item>
        <el-descriptions-item label="前台页面组件">Ant Design vue</el-descriptions-item>
        <el-descriptions-item label="后台页面组件">Element UI</el-descriptions-item>
        <el-descriptions-item label="上传附件限制">2M</el-descriptions-item>
        <el-descriptions-item label="MYSQL版本">5.7</el-descriptions-item>
        <1.8 label="JDK版本" >1.8</el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<style >
.desc {
  margin: 15px 0;
  padding: 12px 18px 20px;
  background-color: #fff;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.12), 0 0 4px rgba(0, 0, 0, 0.04);
  border-radius: 10px;
  min-width: 1200px;
}

.el-divider__text {
  font-size: 16px;
}

.el-descriptions-item__label{
  width: 322px;
}

.el-descriptions-item__label:hover,.el-descriptions-row:hover{
  background-color: #fafafa !important;
}
</style>
