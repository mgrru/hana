import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useDanmakuStore = defineStore('danmaku', () => {
  const isPlaying = ref(true);
  const isDanmakuVisibility = ref(true);
  const danmakuList = ref([]);
  const danmakuCount = ref(0);

  const togglePlay = () => {
    isPlaying.value = !isPlaying.value;
  };

  const toggleDanmakuVisibility = () => {
    isDanmakuVisibility.value = !isDanmakuVisibility.value;
  };

  const addDanmaku = (danmaku) => {
    danmakuList.value.push(danmaku);
    danmakuCount.value = danmakuList.value.length;
  };

  const clearDanmaku = () => {
    danmakuList.value = [];
    danmakuCount.value = 0;
  };

  return {
    isPlaying,
    isDanmakuVisibility,
    danmakuList,
    danmakuCount,
    togglePlay,
    toggleDanmakuVisibility,
    addDanmaku,
    clearDanmaku
  };
});
