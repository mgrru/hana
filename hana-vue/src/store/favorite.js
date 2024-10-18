import { defineStore } from "pinia";
import { ref } from "vue";

export const useFavoriteStore = defineStore("FavoriteStore", () => {
  const favoriteList = ref([]);

  const fetchFavoriteAnimes = async () => {
    try {
      const response = await axios.get("/favorites");
      console.log("接口响应数据:", response.data); // 检查接口返回的数据结构
      favoriteList.value = JSON.parse(response.data);
    } catch (error) {
      console.error("获取收藏视频列表失败：", error);
    }
  };

  const addFavorite = async (video) => {
    try {
      // 发送添加历史记录的请求
      await axios.post(`/history/${anime.id}`, anime);
      historyList.value.push(anime); // 更新本地记录
    } catch (error) {
      console.error("添加历史记录失败：", error);
    }
  };

  const removeFavorite = (id) => {
    favoriteList.value = favoriteList.value.filter((item) => item.id !== id);
  };

  return {
    fetchFavoriteAnimes,
    favoriteList,
    addFavorite,
    removeFavorite,
  };
});
