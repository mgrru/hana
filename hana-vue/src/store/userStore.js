// store/userStore.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useUserStore = defineStore("user", () => {
  // const token = ref("");
  // const isLoggedIn = ref(false);
  // const userInfo = ref([]);

  // const fetchUserInfo = async () =>{
  //   try {
  //     const response = await axios.get('/user/info'); // 替换为你的用户信息接口
  //     userInfo = response.data; // 更新用户信息
  // } catch (error) {
  //     console.error('获取用户信息失败:', error);
  // }
  // }
  // 设置 token
  // const setToken = (newToken) => {
  //   token.value = newToken;
  //   isLoggedIn.value = true; // 登录状态设置为 true
  //   localStorage.setItem("token", newToken); // 保存 token 到 localStorage
  // };

  // 清除 token
  // const clearToken = () => {
  //   token.value = "";
  //   isLoggedIn.value = false; // 登录状态设置为 false
  //   userInfo.value = {}; // 清空用户信息
  //   localStorage.removeItem("token"); // 从 localStorage 中移除 token
  //   localStorage.removeItem("userInfo"); // 清除 localStorage 中的用户信息
  // };

  // const setUserInfo = (info) => {
  //   userInfo.value = info; // 设置用户信息
  //   localStorage.setItem("userInfo", JSON.stringify(info)); // 保存到 localStorage
  // };

  // const initializeUser = () => {
  //   const storedToken = localStorage.getItem("token");
  //   const storedUserInfo = localStorage.getItem("userInfo");
  //   if (storedToken) {
  //     setToken(storedToken); // 恢复 token 和登录状态
  //     // 你可以在这里添加获取用户信息的逻辑
  //     userInfo.value = JSON.parse(storedUserInfo); // 从 localStorage 加载用户信息
  //   }
  // };

  const userInfo = ref({
    name: "",
    is_ban: "",
    age: "",
    phone: "",
    email: "",
  });

  const tempUserInfo = ref({ ...userInfo.value }); // 临时变量，用于修改

  // 获取用户信息
  const fetchUserInfo = async () => {
    try {
      const response = await axios.get("/users"); // 调用接口获取用户信息
      const data = response.data;

      if (data.code === 200) {
        const userData = JSON.parse(data.data);
        userInfo.value = {
          name: userData.name,
          is_ban: userData.ban ? "是" : "否",
          age: userData.age,
          phone: userData.phone,
          email: userData.email,
        };
        // 初始化临时变量
        tempUserInfo.value = { ...userInfo.value };
      } else {
        console.log("获取用户信息失败");
      }
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

      // 如果后端没有返回 message 字段，就根据返回的 code 判断是否成功
      if (response.data.code === 200) {
        // 更新成功
        userInfo.value = { ...tempUserInfo.value }; // 更新用户信息
        alert("用户信息更新成功！");
        fetchUserInfo();
      } else {
        // 使用 msg 字段或者提供默认错误消息
        alert(response.data.msg || "更新失败，请重试。");
      }
    } catch (error) {
      console.error("更新用户信息失败:", error);
      alert("更新失败，请重试。");
    }
  };

  return {
    // userInfo,
    // fetchUserInfo,
    // token,
    // isLoggedIn,
    // userInfo,
    // clearToken,
    // setUserInfo,
    // initializeUser,
    userInfo,
    tempUserInfo,
    fetchUserInfo,
    updateUserInfo,
  };
});
