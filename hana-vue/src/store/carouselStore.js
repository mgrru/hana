import { defineStore } from 'pinia';
import { ref } from 'vue';
import axios from 'axios';

export const useCarouselStore = defineStore('carousel', () => {
  const carouselVideos = ref([]);

  const fetchCarouselVideos = async () => {
    try {
      const response = await axios.get('/api/carouselVideos');
      console.log('完整返回的数据:', response);
      console.log('返回的数据:', response.data);
      carouselVideos.value = response.data.videos;
    } catch (error) {
      console.error('获取轮播图信息失败！', error);
    }
  };

  return {
    carouselVideos,
    fetchCarouselVideos
  };
});
