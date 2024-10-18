// src/store/auth.js
import { defineStore } from "pinia";
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useUserStore } from "./userStore"; // 引入 userStore
// import { useMsgStore } from "./msgStore"; // 引入 msgStore

export const useAuthStore = defineStore("auth", () => {
  const token = ref("");
  const isLoggedIn = ref(false);
  const router = useRouter();
  const userStore = useUserStore(); // 使用 userStore

  // 保存token
  const setToken = async (newToken) => {
    token.value = newToken;
    localStorage.setItem("token", newToken);
    isLoggedIn.value = true; // 登录状态设置为 true
    console.log("当前是否登录：" + isLoggedIn.value);

    // 获取用户信息并存储在 userStore
    try {
      await userStore.fetchUserInfo(); // 假设 fetchUserInfo 调用了用户信息的 API
    } catch (error) {
      console.error("获取用户信息失败:", error);
    }
  };

  // 清除token
  const clearToken = () => {
    console.log("调用函数clearToken");
    token.value = "";
    isLoggedIn.value = false; // 登录状态设置为 false
    localStorage.removeItem("token"); // 从 localStorage 中移除 token

    // 清除 userStore 中的用户信息
    userStore.clearUserInfo(); // 需要在 userStore 中定义 clearUserInfo 函数

    // 执行退出登录后的操作，比如跳转到登录页面
    router.push("/login");
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
