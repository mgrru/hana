import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { ElMessage } from "element-plus";
import { useFavoriteStore } from "./favorite";

export const useToolBarStore = defineStore("toolBarStore", () => {
  // const likes = ref(0); // 点赞数
  // const isLiked = ref(false); // 是否点赞
  const favorites = ref(0); // 收藏数
  const favoriteStore = useFavoriteStore();
  const isFavorited = ref(false); // 是否收藏
  // const shares = ref(0); // 分享数
  // const isShared = ref(false); // 是否分享
  // const rid = ref(0);

  // 点赞切换
  // const toggleLike = async (rid) => {
  //   try {
  //     if (isLiked.value) {
  //       await axios.delete(`/likes/${rid}`);
  //       likes.value--;
  //     } else {
  //       await axios.post(`/likes`, { rid });
  //       likes.value++;
  //       ElMessage({
  //         message: "点赞成功！",
  //         type: "success",
  //       });
  //     }
  //     isLiked.value = !isLiked.value;
  //   } catch (error) {
  //     console.error("点赞操作失败：", error);
  //   }
  // };

  // 收藏切换
  const toggleFavorite = async (rid) => {
    try {
      //取消收藏
      if (isFavorited.value) {
        await axios.delete(`/favorites/${rid}`);
        favorites.value--;
        ElMessage({
          message: "取消收藏成功！",
          type: "success",
        });
      } else {
        //添加收藏
        await axios.post(`/favorites/${rid}`);
        favorites.value++;
        ElMessage({
          message: "收藏成功！",
          type: "success",
        });
      }
      isFavorited.value = !isFavorited.value;
    } catch (error) {
      console.error("收藏操作失败：", error);
    }
  };

  // 分享切换
  // const toggleShare = async (rid) => {
  //   try {
  //     if (isShared.value) {
  //       await axios.delete(`/shares/${rid}`);
  //       shares.value--;
  //     } else {
  //       await axios.post(`/shares`, { rid });
  //       shares.value++;
  //       ElMessage({
  //         message: "分享成功！",
  //         type: "success",
  //       });
  //     }
  //     isShared.value = !isShared.value;
  //   } catch (error) {
  //     console.error("分享操作失败：", error);
  //   }
  // };

  // 获取点赞数
  // const fetchLikes = async (rid) => {
  //   try {
  //     const response = await axios.get(`/likes/${rid}`);
  //     likes.value = response.data.likes;
  //     isLiked.value = response.data.isLiked;
  //   } catch (error) {
  //     console.error("获取点赞数失败：", error);
  //   }
  // };

  // 获取收藏数
  // const fetchFavorites = async (rid) => {
  //   try {
  //     const response = await axios.get(`/favorites`);
  //     favorites.value = response.data.favorites;
  //     isFavorited.value = response.data.isFavorited;
  //   } catch (error) {
  //     console.error("获取收藏数失败：", error);
  //   }
  // };

  // 获取分享数
  // const fetchShares = async (rid) => {
  //   try {
  //     const response = await axios.get(`/shares/${rid}`);
  //     shares.value = response.data.shares;
  //     isShared.value = response.data.isShared;
  //   } catch (error) {
  //     console.error("获取分享数失败：", error);
  //   }
  // };

  return {
    // likes,
    // isLiked,
    // favorites,
    isFavorited,
    // shares,
    // isShared,
    // toggleLike,
    toggleFavorite,
    // toggleShare,
    // fetchLikes,
    // fetchFavorites,
    // fetchShares,
  };
});
