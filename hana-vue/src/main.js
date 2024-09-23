import { createApp } from 'vue'
import './style.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import videojs from 'video.js'
import "video.js/dist/video-js.css";
import router from './router'
import { createPinia } from 'pinia'
import { useUserInfoStore } from './store/user';
import axios from 'axios'
import '../src/mock/mockData'



const pinia = createPinia()
const app=createApp(App)
app.config.globalProperties.$video = videojs;
app.config.globalProperties.$axios = axios; // 注册全局的 axios 实例

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
  }

app.use(pinia)

// 可以在这里初始化 userInfo
const userInfoStore = useUserInfoStore();
userInfoStore.setUserInfo({ id: 1, username: 'zhangsan' });

app.use(ElementPlus)
app.use(router)

app.mount('#app')