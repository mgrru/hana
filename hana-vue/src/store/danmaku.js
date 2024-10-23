import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useDanmakuStore = defineStore("danmaku", () => {
  const isPlaying = ref(true);
  const isDanmakuVisibility = ref(true);
  const danmakuList = ref([]);
  const danmakuCount = ref(0);

  const fetchDanmakuData = async (rid) => {
    try {
      const response = await axios.get(`/danmus/${rid}`);

      danmakuList.value = JSON.parse(response.data);
      console.log(response.data);
    } catch (error) {
      console.error("获取弹幕列表失败：", error);
    }
  };

  const togglePlay = () => {
    isPlaying.value = !isPlaying.value;
  };

  const toggleDanmakuVisibility = () => {
    isDanmakuVisibility.value = !isDanmakuVisibility.value;
  };

  const addDanmaku = async (newDanmaku) => {
    danmakuCount.value = danmakuList.value.length;

    // 发送 POST 请求
    await axios.post(`/danmus`, newDanmaku, {
      headers: { "Content-Type": "application/json" },
    });
    danmakuList.value.push(newDanmaku);
  };

  const removeDanmu = async (id) => {
    try {
      const response = await axios.delete(`/danmus/${id}`);
      console.log("删除评论响应:", response); // 调试信息

      // 检查状态码
      if (response.status === 200) {
        return true; // 返回删除成功的标志
      } else {
        throw new Error(response.data.message || "删除评论失败"); // 后端返回非成功的状态
      }
    } catch (error) {
      throw new Error(
        error.response ? error.response.data.message : error.message
      ); // 抛出错误让调用方处理
    }
  };

  const clearDanmaku = () => {
    danmakuList.value = [];
    danmakuCount.value = 0;
  };

  return {
    fetchDanmakuData,
    isPlaying,
    isDanmakuVisibility,
    danmakuList,
    danmakuCount,
    togglePlay,
    toggleDanmakuVisibility,
    addDanmaku,
    clearDanmaku,
    removeDanmu,
  };
});
