import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useDanmakuStore = defineStore("danmaku", () => {
  const isPlaying = ref(true);
  const isDanmakuVisibility = ref(true);
  const danmakuList = ref([]);
  const danmakuCount = ref(0);

  const fecthDanmakuData = async () => {
    try {
      const response = await axios.get("/danmus/{{rid}}");
      danmakuList.value = response.data;
      console.log(danmakuList.value);
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

  const addDanmaku = async (danmaku) => {
    danmakuCount.value = danmakuList.value.length;
    const danmuData = {
      rid: danmaku.id,
      content: danmaku.content,
    };
    // 发送 POST 请求
    await axios.post("/danmus", danmuData, {
      headers: { "Content-Type": "application/json" },
    });
    danmakuList.value.push(danmaku);
  };

  const clearDanmaku = () => {
    danmakuList.value = [];
    danmakuCount.value = 0;
  };

  return {
    fecthDanmakuData,
    isPlaying,
    isDanmakuVisibility,
    danmakuList,
    danmakuCount,
    togglePlay,
    toggleDanmakuVisibility,
    addDanmaku,
    clearDanmaku,
  };
});
