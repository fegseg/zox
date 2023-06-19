// 转了ES6会报错
// module.exports = { devServer: { port: 8000 } }
module.exports = {
  devServer: {
    port: 7028
  },
  //publicPath默认是./，它会假设应用被部署到一个域名的根目录上，当被部署到一个子路径时，需要修改为: /子路径/
  publicPath: '/',
  /* 输出文件目录，在npm run build时，生成的目录名称为dist */
  outputDir:'dist'
}
