/*
 * @Author: tangzhengtao 1399159010@qq.com
 * @Date: 2024-10-09 10:48:54
 * @LastEditors: tangzhengtao 1399159010@qq.com
 * @LastEditTime: 2024-10-22 22:04:27
 * @FilePath: \vue3-app1\src\store\favorite.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { ElMessage } from "element-plus";

export const useFavoriteStore = defineStore("favoriteStore", () => {
  const favoriteList = ref([]); // 收藏列表

  // 获取收藏列表
  const fetchFavorites = async () => {
    try {
      const response = await axios.get("/favorites");
      favoriteList.value = JSON.parse(response.data);
    } catch (error) {
      console.error("获取收藏列表失败：", error);
    }
  };

  // 取消收藏
  const removeFavorite = async (rid) => {
    try {
      await axios.delete(`/favorites/${rid}`);
      favoriteList.value = favoriteList.value.filter((item) => item.id !== rid);
      ElMessage({
        message: "取消收藏成功！",
        type: "success",
      });
    } catch (error) {
      console.error("取消收藏失败：", error);
    }
  };

  return {
    favoriteList,
    fetchFavorites,
    removeFavorite,
  };
});
