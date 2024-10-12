// src/utils/axios.js
import axios from "axios";
import { useAuthStore } from "../store/auth";

const instance = axios.create({
  baseURL: "http://localhost:9901",
  timeout: 5000,
});

// 请求拦截器
instance.interceptors.request.use(
  (config) => {
    const authStore = useAuthStore();
    if (authStore.token) {
      config.headers.Authorization = `Bearer ${authStore.token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    if (error.response && error.response.status === 401) {
      // 处理未授权的情况，清除token
      const authStore = useAuthStore();
      authStore.clearToken();
      ElMessage.error('登录状态已过期，请重新登录');
      router.push('/login'); // 跳转到登录页面
    }
    return Promise.reject(error);
  }
);

export default instance;
