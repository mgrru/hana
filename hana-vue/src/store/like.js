import { defineStore } from "pinia";
import axios from "axios";

export const useLikeStore = defineStore('like',{
    state:()=>({
        likes:0,//点赞数
        isLiked: false,//是否点赞点赞就不能再点赞
        favorites: 0,       // 收藏数
        isFavorited: false, // 是否收藏
        shares: 0,          // 分享数
        isShared:false,      //是否分享
    }),

    actions: {
        // 点赞切换
        async toggleLike(rid) {
            try {
                if (this.isLiked) {
                    await axios.delete(`/likes/${rid}`);
                    this.likes--;
                } else {
                    await axios.post(`/likes`, { rid });
                    this.likes++;
                }
                this.isLiked = !this.isLiked;
            } catch (error) {
                console.error("点赞操作失败：", error);
            }
        },

        // 收藏切换
        async toggleFavorite(rid) {
            try {
                if (this.isFavorited) {
                    await axios.delete(`/favorites/${rid}`);
                    this.favorites--;
                } else {
                    await axios.post(`/favorites`, { rid });
                    this.favorites++;
                }
                this.isFavorited = !this.isFavorited;
            } catch (error) {
                console.error("收藏操作失败：", error);
            }
        },

        
        // 分享切换
        async toggleShare(rid) {
            try {
                if (this.isShared) {
                    await axios.delete(`/shares/${rid}`);
                    this.shares--;
                } else {
                    await axios.post(`/shares`, { rid });
                    this.shares++;
                }
                this.isShared = !this.isShared;
            } catch (error) {
                console.error("分享操作失败：", error);
            }
        },


        // 获取点赞数
        async fetchLikes(rid) {
            try {
                const response = await axios.get(`/likes/${rid}`);
                this.likes = response.data.likes;
                this.isLiked = response.data.isLiked;
            } catch (error) {
                console.error("获取点赞数失败：", error);
            }
        },

        // 获取收藏数
        async fetchFavorites(rid) {
            try {
                const response = await axios.get(`/favorites/${rid}`);
                this.favorites = response.data.favorites;
                this.isFavorited = response.data.isFavorited;
            } catch (error) {
                console.error("获取收藏数失败：", error);
            }
        },

        // 获取分享数
        async fetchShares(rid) {
            try {
                const response = await axios.get(`/shares/${rid}`);
                this.shares = response.data.shares;
                this.isShared = response.data.isShared;
            } catch (error) {
                console.error("获取分享数失败：", error);
            }
        },
    }
})