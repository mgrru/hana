/*
 * @Author: tangzhengtao 1399159010@qq.com
 * @Date: 2024-09-23 08:43:57
 * @LastEditors: tangzhengtao 1399159010@qq.com
 * @LastEditTime: 2024-10-22 22:01:06
 * @FilePath: \vue3-app1\src\store\toolBarStore.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { ElMessage } from "element-plus";
import { useFavoriteStore } from "./favorite";

export const useToolBarStore = defineStore("toolBarStore", () => {
  const favorites = ref(0); // 收藏数
  const favoriteStore = useFavoriteStore();
  const isFavorited = ref(false); // 是否收藏
  

  // 收藏切换
  const toggleFavorite = async (rid) => {
    try {
      //取消收藏
      if (isFavorited.value) {
        await favoriteStore.removeFavorite(rid);
        favorites.value--;
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


  return {
    isFavorited,
    toggleFavorite,
  };
});
