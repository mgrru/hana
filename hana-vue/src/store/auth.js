// src/store/auth.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore("auth", () => {
  const token = ref("");
  const isLoggedIn = ref(false);
  const router = useRouter();
  // 保存token
  const setToken = (newToken) => {
    token.value = newToken;
    localStorage.setItem("token", newToken);
    isLoggedIn.value = true; // 登录状态设置为 true
    console.log(isLoggedIn.value);
  };

  // 清除token
  const clearToken = () => {
    console.log("clearToken function triggered");
    token.value = "";
    isLoggedIn.value = false; // 登录状态设置为 false
    localStorage.removeItem("token"); // 从 localStorage 中移除 token
    // 确认函数是否执行到了这里
    console.log("Before router.push('/login')");
    // 执行退出登录后的操作，比如跳转到登录页面
    router.push("/login"); // 假设你的登录页面路径是 /login
  };

  const initializeUser = () => {
    const storedToken = localStorage.getItem("token");
    if (storedToken) {
      setToken(storedToken); // 恢复 token 和登录状态
    }
  };

  return {
    token,
    isLoggedIn,
    setToken,
    clearToken,
    initializeUser,
  };
});
