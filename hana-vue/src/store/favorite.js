import { defineStore } from "pinia";
import { ref } from "vue";

export const useFavoriteStore = defineStore("FavoriteStore", () => {
  const favoriteList = ref([]);

  const addFavorite = (video) => {
    if (!favoriteList.value.find((item) => item.id === video.id)) {
      favoriteList.value.push({
        ...video,
        timestamp: new Date().toLocaleString(),
      });
    }
  };

  const removeFavorite = (id) => {
    favoriteList.value = favoriteList.value.filter((item) => item.id !== id);
  };

  return {
    favoriteList,
    addFavorite,
    removeFavorite,
  };
});
