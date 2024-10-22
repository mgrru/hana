// src/store/msgStore.js
import { defineStore } from "pinia";
import { ref, watch } from "vue";
import axios from "../utils/axios";
import { useUserStore } from "./userStore"; // 引入 userStore
import { ElMessage } from "element-plus";

export const useMsgStore = defineStore("msgStore", () => {
  const messages = ref([]);
  const loading = ref(false);
  const error = ref(null);
  const userStore = useUserStore(); // 使用 userStore
  const userId = ref(null);

  // 监听 userStore 的变化，当用户信息加载完成时设置 userId
  //   if (userStore.userInfo.id) {
  //     userId.value = userStore.userInfo.id; // 获取当前用户的 userId
  //   }

  // 监听 userStore 的变化，当用户信息加载完成时设置 userId
  watch(
    () => userStore.userInfo.id, // 监听 userStore.userInfo.id 的变化
    (newId) => {
      // 当 userInfo.id 改变时，执行该回调
      if (newId) {
        // 如果新的 ID 有值（即不为 null 或 undefined）
        userId.value = newId; // 将 userId 设置为新的 userInfo.id
        fetchMessages(); // 然后调用 fetchMessages 获取消息
      }
    },
    { immediate: true } // immediate: true 表示初始监听时立即执行回调
  );

  // 获取消息列表
  const fetchMessages = async () => {
    if (!userId.value) {
      console.warn("userId 未定义，无法获取消息");
      return;
    }

    loading.value = true;
    error.value = null;
    try {
      const response = await axios.get("/msg");
      //烦死的===不能用
      messages.value = JSON.parse(response.data).filter(
        (msg) => msg.recipient == userId.value
      );
      // console.log(response.data);
    } catch (err) {
      error.value = err.message;
    } finally {
      loading.value = false;
    }
  };

  // 发送消息
  const sendMessage = async (recipientId, content) => {
    try {
      await axios.post("/msg", {
        recipient: recipientId,
        content,
      });
      ElMessage({
        message: "发送成功！！",
        type: "success",
      });
      // 成功发送后，刷新消息列表
      await fetchMessages();
    } catch (err) {
      error.value = err.message;
    }
  };

  return {
    messages,
    loading,
    error,
    fetchMessages,
    sendMessage,
    userId, // 当前用户ID
  };
});