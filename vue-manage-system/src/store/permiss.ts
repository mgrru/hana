import { defineStore } from "pinia";
import { ref, computed } from "vue";
import { fetchUserData } from "@/api";

interface ObjectList {
  [key: string]: string[];
}

export const usePermissStore = defineStore("permiss", () => {
  const defaultList: ObjectList = {
    admin: [
      "0",
      "1",
      "11",
      "12",
      "13",
      "14",
      "15",
      "16",
      "2",
      "21",
      "22",
      "23",
      "24",
      "25",
      "26",
      "27",
      "28",
      "29",
      "291",
      "292",
      "3",
      "31",
      "32",
      "33",
      "34",
      "4",
      "41",
      "42",
      "5",
      "7",
      "6",
      "61",
      "62",
      "63",
      "64",
      "65",
      "66",
    ],
    user: ["0", "1", "11", "12", "13"],
  };

  const key = ref<string[]>([]);
  const isLoggedIn = ref(false);
  const token = ref("");

  const handleSet = (val: string[]) => {
    key.value = val;
  };

  const setToken = (newToken: string) => {
    token.value = newToken;
    localStorage.setItem("token", newToken);
    isLoggedIn.value = true;

    // 解码 token 获取角色信息
    const payload = newToken.split(".")[1]; // 获取载荷部分
    const base64Url = payload.replace(/-/g, "+").replace(/_/g, "/"); // 替换 URL 安全字符
    const decodedPayload = JSON.parse(atob(base64Url)); // Base64 解码并解析为 JSON

    // 使用 `admin` 字段判断是否为管理员
    const isAdmin = decodedPayload.admin;
    const roleName = isAdmin ? "管理员" : "用户";
    localStorage.setItem("vuems_name", roleName); // 存储用户角色信息
    const keys = defaultList[roleName == "管理员" ? "admin" : "user"];
    handleSet(keys); // 设置权限
  };

  const initializeUser = () => {
    const storedToken = localStorage.getItem("token");
    const storedRole = localStorage.getItem("vuems_name");

    if (storedToken) {
      setToken(storedToken); // 恢复 token 和登录状态
    }

    if (storedRole) {
      const keys = defaultList[storedRole === "管理员" ? "admin" : "user"];
      handleSet(keys); // 恢复权限
    }
  };

  return {
    key,
    defaultList,
    isLoggedIn,
    token,
    handleSet,
    setToken,
    initializeUser,
  };
});
