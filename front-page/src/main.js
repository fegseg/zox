// 启用vue调试工具
devtools: 1

import Vue from 'vue'
import App from './App.vue'

// router 路由跳转
import router from './router'

// vueX 状态共享
import store from './store'

// axios 发送请求
import axios from 'axios'
import './network/axios'
Vue.prototype.$axios = axios


// ant design vue 按需引用
import {
  FormModel, Avatar, Input, Select, DatePicker, Switch, Checkbox, Divider,
  Radio, Button, Layout, Menu, Icon, Carousel, Card, Upload, Tooltip, Form,
  BackTop, PageHeader, InputNumber, Descriptions, Badge, Empty, Spin, Dropdown,
  Row, Col, message, notification, Table, Popconfirm, Pagination, Modal, Cascader,
  Tag, Result, List, ConfigProvider, TimePicker, Transfer, Calendar, Popover, Drawer,
  Steps,Collapse,Timeline,Rate,Comment
} from 'ant-design-vue';
Vue.use(FormModel);
Vue.use(Rate);
Vue.use(Comment);
Vue.use(Collapse);
Vue.use(Timeline);
Vue.use(Drawer);
Vue.use(Popover);
Vue.use(Calendar);
Vue.use(Transfer);
Vue.use(Input);
Vue.use(Select);
Vue.use(DatePicker);
Vue.use(Switch);
Vue.use(Checkbox);
Vue.use(Radio);
Vue.use(Button);
Vue.use(Layout);
Vue.use(Menu);
Vue.use(Icon);
Vue.use(Carousel);
Vue.use(Card);
Vue.use(Avatar);
Vue.use(Upload);
Vue.use(Tooltip);
Vue.use(BackTop);
Vue.use(PageHeader);
Vue.use(InputNumber);
Vue.use(Descriptions);
Vue.use(Badge);
Vue.use(Empty);
Vue.use(Divider);
Vue.use(Form);
Vue.use(Spin);
Vue.use(Dropdown);
Vue.use(Row);
Vue.use(Col);
Vue.use(Table);
Vue.use(Popconfirm);
Vue.use(Pagination);
Vue.use(Modal);
Vue.use(Cascader);
Vue.use(Tag);
Vue.use(Result);
Vue.use(List);
Vue.use(ConfigProvider);
Vue.use(TimePicker);
Vue.use(Steps);
// 需要在Vue的原型链增加这两个属性
Vue.prototype.$message = message;
Vue.prototype.$notification = notification;
// message配置过期时间，最大数量
message.config({
  duration: 2,
  maxCount: 3,
});



Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
