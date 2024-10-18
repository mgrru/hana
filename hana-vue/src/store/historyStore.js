import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useHistoryStore = defineStore("historyStore", () => {
  const historyList = ref([]);

  const fetchHistoryAnimes = async () => {
    try {
      const response = await axios.get("/history");
      console.log("接口响应数据:", response.data);
      historyList.value = response.data; // 假设接口返回的是数组
    } catch (error) {
      console.error("获取历史视频列表失败：", error);
    }
  };

  const addHistory = async (anime) => {
    try {
      // 发送添加历史记录的请求
      console.log(`当前发送的动漫id:${anime.id}`);
      await axios.post(`/history/${anime.id}`, anime);
      historyList.value.push(anime); // 更新本地记录
    } catch (error) {
      console.error("添加历史记录失败：", error);
    }
  };

  const deleteHistory = async (rid) => {
    try {
      await axios.delete(`/history/${rid}`);
      historyList.value = historyList.value.filter((item) => item.id !== rid);
    } catch (error) {
      console.error("删除历史记录失败：", error);
    }
  };

  return {
    fetchHistoryAnimes,
    historyList,
    addHistory,
    deleteHistory,
  };
});
