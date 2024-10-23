/*
 * @Author: tangzhengtao 1399159010@qq.com
 * @Date: 2024-10-08 10:42:32
 * @LastEditors: tangzhengtao 1399159010@qq.com
 * @LastEditTime: 2024-10-22 20:34:08
 * @FilePath: \vue3-app1\src\store\historyStore.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
// store/historyStore.js
import { ref } from "vue";
import { defineStore } from "pinia";
import axios from "../utils/axios";

export const useHistoryStore = defineStore("history", () => {
  const historyList = ref([]);

  const fetchHistory = async () => {
    try {
      const response = await axios.get(`/history`);
      console.log("获取的历史视频消息", response.data);

      // 提取并去重
      const newHistory = JSON.parse(response.data).map((item) => ({
        id: item.resource.id,
        cover: item.resource.cover,
        name: item.resource.name,
        title: item.resource.title,
        likes: item.resource.likes,
        views: item.resource.views,
        time: item.time, // 根据需要保留时间信息
      }));

      // 根据 id 去重
      historyList.value = [];
      newHistory.forEach((item) => {
        const exists = historyList.value.some((h) => h.id === item.id);
        if (!exists) {
          historyList.value.push(item);
        }
      });
    } catch (error) {
      console.error("获取历史记录失败：", error);
    }
  };

  const addHistoryItem = async (rid) => {
    console.log("添加历史记录");
    try {
      const existsIndex = historyList.value.findIndex(
        (item) => item.id === rid
      );

      if (existsIndex === -1) {
        await axios.post(`/history/${rid}`); // 调用后端接口添加历史记录
        // 如果是新的记录，推入历史列表
        const newRecord = {
          id: rid,
          time: new Date().toISOString(), // 设置当前时间
        };
        historyList.value.push(newRecord);
      } else {
        // 如果已存在，更新其时间
        historyList.value[existsIndex].time = new Date().toISOString();
        console.log("历史记录已存在，更新时间。");
      }
    } catch (error) {
      console.error("添加历史记录失败：", error);
    }
  };

  const deleteHistoryItem = async (rid) => {
    try {
      await axios.delete(`/history/${rid}`); // 删除历史记录
      historyList.value = historyList.value.filter((item) => item.id !== rid); // 更新本地状态
    } catch (error) {
      console.error("删除历史记录失败：", error);
    }
  };

  return {
    historyList,
    fetchHistory,
    addHistoryItem,
    deleteHistoryItem,
  };
});
