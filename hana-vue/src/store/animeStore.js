// useAnimeStore,js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { ElMessage } from "element-plus";

export const useAnimeStore = defineStore("animeStore", () => {
  const animesList = ref([]); // 用于保存视频列表数据
  const popularAnimesList = ref([]);
  const recommendAnimesList = ref([]);
  const animesInfo = ref({}); // 用于保存视频列表数据

  const fetchAllAnimes = async () => {
    try {
      // 每次请求前先清空数组，防止数据重复
      animesList.value = []; // 这里要使用 .value 来操作 ref
      const response = await axios.get("/animes");
      console.log("接口响应数据:", response.data); // 检查接口返回的数据结构
      animesList.value = JSON.parse(response.data);
    } catch (error) {
      console.error("获取所以视频列表失败：", error);
    }
  };

  const fetchPopularAnimes = async () => {
    try {
      const response = await axios.get("/animes/popular");
      console.log("popular接口响应数据:", response.data); // 检查接口返回的数据结构
      popularAnimesList.value = JSON.parse(response.data);
    } catch (error) {
      console.error("获取热门视频列表失败：", error);
    }
  };

  const fetchRecommendAnimes = async () => {
    try {
      const response = await axios.get("/animes/recommend");
      // console.log("recommend接口响应数据:", response.data); // 检查接口返回的数据结构
      recommendAnimesList.value = JSON.parse(response.data);
    } catch (error) {
      console.error("获取推荐视频列表失败：", error);
    }
  };

  // 获取视频数据
  const fetchAnimeData = async (name) => {
    try {
      const response = await axios.get(`/animes/${name}`);
      console.log("该视频页的视频响应数据为:", response.data);
      const animesData = JSON.parse(response.data).find((data) => data.process);
      animesInfo.value = { ...animesData };
    } catch (error) {
      console.error("获取视频数据失败：", error);
    }
  };

  const fetchEpisodeUrl = async (name, episode_name) => {
    try {
      const response = await axios.get(`/animes/${name}/${episode_name}`);
      const data = JSON.parse(response.data); // 假设返回的结构是 { url: "视频链接" }
      animeUrl.value = data.url;
    } catch (error) {
      console.error("获取选集视频链接失败：", error);
    }
  };

  // 上传动漫视频
  const uploadAnime = async (formData) => {
    try {
      const response = await axios.post("/upload", formData, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });
      if (response.code === 200) {
        ElMessage.success("动漫视频上传成功");
      } else {
        ElMessage.error("上传失败");
      }
    } catch (error) {
      console.error("动漫视频上传出错", error);
      ElMessage.error("上传失败，请稍后重试");
    }
  };

  return {
    animesList,
    popularAnimesList,
    recommendAnimesList,
    animesInfo,
    fetchAllAnimes,
    fetchAnimeData,
    fetchPopularAnimes,
    fetchRecommendAnimes,
    fetchEpisodeUrl,
    uploadAnime,
  };
});
