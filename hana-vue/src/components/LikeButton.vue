<template>
    <div>
        <button @click="toggleLike">
            {{ isLiked ? '取消点赞' : '点赞' }} ({{ likes }})
        </button>
        <button @click="toggleFavorite">
            {{ isFavorited ? '取消收藏' : '收藏' }} ({{ favorites }})
        </button>
        <button @click="toggleShare">
            {{ isShared ? ' 取消分享' : '分享' }}({{ shares }})
        </button>
        <!-- <el-button type="warning" :icon="Star" circle /> -->
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useLikeStore } from '../store/like.js';
import { storeToRefs } from 'pinia';
import { Star } from '@element-plus/icons-vue'

const likeStore = useLikeStore();
const { likes, isLiked, favorites, isFavorited, shares, isShared } = storeToRefs(likeStore);

const rid = 123; // 动漫ID

const toggleLike = () => {
    likeStore.toggleLike(rid);
};

const toggleFavorite = () => {
    likeStore.toggleFavorite(rid);
};

const toggleShare = () => {
    likeStore.toggleShare(rid);
};

onMounted(() => {
    likeStore.fetchLikes(rid);
    likeStore.fetchFavorites(rid);
    likeStore.fetchShares(rid);
});
</script>

<style scoped>
button {
    background-color: #42b983;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
}
</style>
