<template>
    <div>
        <button @click="toggleLike">
            <!-- {{ isLiked ? '取消点赞' : '点赞' }} ({{ likes }}) -->
            点赞数{{ animesInfo.likes }}
        </button>
        <button @click="toggleFavorite">
            {{ isFavorited ? '取消收藏' : '收藏' }}
        </button>
        <!-- <button @click="toggleShare">
            {{ isShared ? ' 取消分享' : '分享' }}({{ shares }})
        </button> -->
        <!-- <el-button type="warning" :icon="Star" circle /> -->
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useToolBarStore } from '../store/toolBarStore';
import { storeToRefs } from 'pinia';
import { useAnimeStore } from '../store/animeStore.js';

const toolBarStore = useToolBarStore();
// const { isLiked, favorites, isFavorited, shares, isShared } = storeToRefs(likeStore);
const animeStore = useAnimeStore();

const { animesInfo } = storeToRefs(animeStore);

// const rid = 123; // 动漫ID

// const toggleLike = () => {
//     likeStore.toggleLike(rid);
// };

const toggleFavorite = () => {
    toolBarStore.toggleFavorite(rid);
};

// const toggleShare = () => {
//     likeStore.toggleShare(rid);
// };

onMounted(() => {
    animeStore.fetchAnimeData
    // likeStore.fetchLikes(rid);
    // likeStore.fetchFavorites(rid);
    // likeStore.fetchShares(rid);
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
