import { defineStore } from "pinia";
import axios from "axios";

export const useCarouselStore = defineStore('carousel',{
    state:()=>({
        carouselVideos:[]
    }),

    actions:{
        async fetchCarouselVideos(){
            try {
                const response = await axios.get('/api/carouselVideos');
                console.log('完整返回的数据:', response); // 打印完整的 response 对象
                console.log('返回的数据:', response.data); // 打印 response.data
                this.carouselVideos = response.data;
            } catch (error) {
                console.error('获取轮播图信息失败！',error)
            }
        }
    }
}
)
