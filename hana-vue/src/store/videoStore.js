import { defineStore } from "pinia";
import Mock from "mockjs";

export const useVideoStore = defineStore("videoStore", {
  state: () => ({
    videoList: [], // 用于保存视频列表数据
    videoSrc: "",
    title: "",
    description: "",
    views: 0,
    likes: 0,
    stars: 0,
    danmaku: 0,
    publishDate: "",
    shares: 0,
  }),
  actions: {
    async fetchVideoList() {
      // 模拟生成 48 个视频项
      const data = Mock.mock({
        "list|48": [
          {
            "id|+1": 1,
            thumbnail: '@image("200x100", "#50B347", "#FFF", "Video")', // 随机生成缩略图
            title: "@ctitle(10, 20)", // 随机生成标题
          },
        ],
      });
      console.log("Fetched video list: ", data.list); // 打印出数据
      this.videoList = data.list;
    },
    async fetchVideoData(videoId) {
      // 根据视频 ID 模拟生成视频数据
      const data = Mock.mock({
        videoSrc: new URL("@/assets/videos/barca1.mp4", import.meta.url).href, // 使用静态资源路径
        title: "@ctitle(10, 20)",
        description: "@cparagraph(1, 3)",
        "views|1000-100000": 1,
        "likes|100-10000": 1,
        "stars|100-5000": 1,
        "danmaku|100-5000": 1,
        publishDate: '@date("yyyy-MM-dd")',
        "shares|10-500": 1,
      });

      this.videoSrc = data.videoSrc;
      this.title = data.title;
      this.description = data.description;
      this.views = data.views;
      this.likes = data.likes;
      this.stars = data.stars;
      this.danmaku = data.danmaku;
      this.publishDate = data.publishDate;
      this.shares = data.shares;
    },
  },
});
