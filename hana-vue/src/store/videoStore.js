import { defineStore } from "pinia";
import { ref } from "vue";
import Mock from "mockjs";
import axios from "axios";

export const useVideoStore = defineStore("videoStore", () => {
  const videoList = ref([]);
  const videoSrc = ref("");
  const title = ref("");
  const description = ref("");
  const views = ref(0);
  const likes = ref(0);
  const stars = ref(0);
  const danmaku = ref(0);
  const publishDate = ref("");
  const shares = ref(0);

  // 获取视频列表
  const fetchVideoList = async () => {
    try {
        const response = await axios.get('/videoList');
        console.log("获取的视频列表响应:", response.data); // 检查返回的数据结构
        if (response.data && response.data.list) {
            videoList.value = response.data.list; // 确保赋值给 videoList
        } else {
            console.error("获取视频列表失败，数据格式不正确:", response.data);
        }
    } catch (error) {
        console.error("获取视频列表失败：", error);
    }
};


  // 获取视频数据
  const fetchVideoData = async (videoId) => {
    try {
      const response = await axios.get(`/videos/${videoId}`);
      videoSrc.value = response.data.videoSrc;
      title.value = response.data.title;
      description.value = response.data.description;
      views.value = response.data.views;
      likes.value = response.data.likes;
      stars.value = response.data.stars;
      danmaku.value = response.data.danmaku;
      publishDate.value = response.data.publishDate;
      shares.value = response.data.shares;
    } catch (error) {
      console.error("获取视频数据失败：",error);
    }
  };

  return {
    videoList,
    videoSrc,
    title,
    description,
    views,
    likes,
    stars,
    danmaku,
    publishDate,
    shares,
    fetchVideoList,
    fetchVideoData,
  };
});
