import { defineStore } from "pinia";
import { ref } from "vue";
import Mock from "mockjs";
import axios from "axios";

export const useAnimeStore = defineStore("animeStore", () => {
  const animeUrl = ref("");
  const name = ref("");
  const cover = ref("");
  // const description = ref("");
  // const views = ref(0);
  // const likes = ref(0);
  const episode_name = ref("");
  const stars = ref(0);
  const danmaku = ref(0);
  // const publishDate = ref("");
  // const shares = ref(0);

  // 获取视频列表
  const fetchAllAnimes = async () => {
    try {
      // const response = await axios.get("/videoList");
      const response = await axios.get("/animes");
      animeUrl.value = response.data.url;
      name.value = response.data.name;
      cover.value = response.data.cover;
      console.log("获取的视频列表响应:", response.data); // 检查返回的数据结构
    } catch (error) {
      console.error("获取视频列表失败：", error);
    }
  };

  // 获取视频数据
  const fetchAnimeData = async (name, episode_name) => {
    try {
      // const response = await axios.get(`/videos/${videoId}`);
      const response = await axios.get(`/animes/${name}/${episode_name}`);
      animeUrl.value = response.data.url;
      name.value = response.data.name;
      // description.value = response.data.description;
      // views.value = response.data.views;
      // likes.value = response.data.likes;
      episode_name.value = response.data.episode_name;
      stars.value = response.data.stars;
      danmaku.value = response.data.danmaku;
      // publishDate.value = response.data.publishDate;
      // shares.value = response.data.shares;
    } catch (error) {
      console.error("获取视频数据失败：", error);
    }
  };

  return {
    animeUrl,
    name,
    episode_name,
    stars,
    danmaku,
    fetchAllAnimes,
    fetchAnimeData,
  };
});
