import axios, {
  AxiosInstance,
  AxiosError,
  AxiosResponse,
  InternalAxiosRequestConfig,
} from "axios";
import { useAuthStore } from "../store/auth";

const service: AxiosInstance = axios.create({
  baseURL: "http://localhost:9901",
  timeout: 5000,
});

service.interceptors.request.use(
  (config: InternalAxiosRequestConfig) => {
    // const authStore = useAuthStore();
    // if (authStore.token) {
    //   config.headers.Authorization = `Bearer ${authStore.token}`;
    // }
    return config;
  },
  (error: AxiosError) => {
    console.log(error);
    return Promise.reject();
  }
);

service.interceptors.response.use(
  (response: AxiosResponse) => {
    return response;
  },
  (error: AxiosError) => {
    console.log(error);
    return Promise.reject();
  }
  // (error) => {
  //     if (error.response && error.response.status === 401) {
  //       // 处理未授权的情况，清除token
  //       const authStore = useAuthStore();
  //       authStore.clearToken();
  //       ElMessage.error('登录状态已过期，请重新登录');
  //       router.push('/login'); // 跳转到登录页面
  //     }
  //     return Promise.reject(error);
  //   }
);

export default service;
