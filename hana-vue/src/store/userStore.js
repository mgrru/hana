// store/userStore.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useUserStore = defineStore("user", () => {
  const userInfo = ref({
    id: "",
    account: "string",
    name: "",
    is_ban: "",
    age: "",
    phone: "",
    email: "",
    role: {
      id: 0,
      name: "string",
    },
  });

  // 清除用户信息
  const clearUserInfo = () => {
    userInfo.value = {
      id: "",
      account: "",
      name: "",
      is_ban: "",
      age: "",
      phone: "",
      email: "",
      role: {
        id: 0,
        name: "",
      },
    };
  };

  const tempUserInfo = ref({ ...userInfo.value }); // 临时变量，用于修改

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await axios.get("/users"); // 调用接口获取用户信息
      const data = response.data;
      console.log("响应的数据为", response.data);
      const userData = JSON.parse(data);
      userInfo.value = {
        id: userData.id,
        name: userData.name,
        account: userData.account,
        is_ban: userData.ban ? "是" : "否",
        age: userData.age,
        phone: userData.phone,
        email: userData.email,
        role_name: userData.role.name,
      };
      // 初始化临时变量
      tempUserInfo.value = { ...userInfo.value };
    } catch (error) {
      console.error("获取用户信息失败:", error);
      alert("获取用户信息失败，请重试。");
    }
  };

  const updateUserInfo = async () => {
    const updatedInfo = {
      name: tempUserInfo.value.name,
      age: tempUserInfo.value.age,
      phone: tempUserInfo.value.phone,
      email: tempUserInfo.value.email,
    };

    console.log("要更新的信息:", updatedInfo);

    try {
      const response = await axios.put(`/users`, updatedInfo);

      // 打印响应数据，查看实际的响应结构
      console.log("更新响应:", response.data);

      // 更新成功
      userInfo.value = { ...tempUserInfo.value }; // 更新用户信息
      alert("用户信息更新成功！");
      fetchUserInfo();
    } catch (error) {
      console.error("更新用户信息失败:", error);
      alert("更新失败，请重试。");
    }
  };

  return {
    userInfo,
    tempUserInfo,
    fetchUserInfo,
    updateUserInfo,
    clearUserInfo,
  };
});
