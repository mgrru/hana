// store/userStore.js
import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const token = ref("");
  const isLoggedIn = ref(false);
  const userInfo = ref({
    avatar: "",
    userName: "",
    age: "",
    phone: "",
    email: "",
  }); // 默认包含 avatar 属性
  const dialogLoginVisible = ref(false); // 用于控制登录对话框

  // 设置 token
  const setToken = (newToken) => {
    token.value = newToken;
    isLoggedIn.value = true; // 登录状态设置为 true
    localStorage.setItem("token", newToken); // 保存 token 到 localStorage
  };

  // 清除 token
  const clearToken = () => {
    token.value = "";
    isLoggedIn.value = false; // 登录状态设置为 false
    userInfo.value = {}; // 清空用户信息
    localStorage.removeItem("token"); // 从 localStorage 中移除 token
  };

  const setUserInfo = (info) => {
    userInfo.value = info; // 设置用户信息
  };

  const initializeUser = () => {
    const storedToken = localStorage.getItem("token");
    if (storedToken) {
      setToken(storedToken); // 恢复 token 和登录状态
      // 你可以在这里添加获取用户信息的逻辑
    }
  };

  return {
    token,
    isLoggedIn,
    userInfo,
    dialogLoginVisible,
    setToken,
    clearToken,
    setUserInfo,
    initializeUser,
  };
});
