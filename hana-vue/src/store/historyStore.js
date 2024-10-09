import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useHistoryStore = defineStore('historyStore', () => {
  const historyList = ref([]);

  const addHistory = (video) => {
    if (!historyList.value.find(item => item.id === video.id)) {
      historyList.value.push({
        ...video,
        timestamp: new Date().toLocaleString(),
      });
    }
  };

  return {
    historyList,
    addHistory
  };
});
