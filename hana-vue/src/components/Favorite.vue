<template>
    <div class="favorite-page">
        <h2></h2>
        <ul v-if="favoriteList.length > 0" class="favorite-list" title="我的收藏">
            <li v-for="favorite in favoriteList" :key="favorite.id" class="favorite-item">
                <img :src="favorite.cover" alt="Cover" class="cover" />
                <div class="favorite-info">
                    <p class="title">{{ favorite.title }}</p>
                </div>
            </li>
        </ul>
        <p v-else class="no-favorites">暂无收藏视频</p>
    </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useFavoriteStore } from '../store/favorite';

// 获取收藏列表
const favoriteStore = useFavoriteStore();
const { favoriteList } = storeToRefs(favoriteStore);
</script>

<style scoped>
.favorite-page {
    padding: 20px;
    max-width: 900px;
    margin: 0 auto;
}

.favorite-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}

.favorite-item {
    display: flex;
    align-items: center;
    background-color: #f9f9f9;
    padding: 10px;
    border-radius: 8px;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;
}

.favorite-item:hover {
    transform: scale(1.02);
}

.cover {
    width: 120px;
    height: 90px;
    border-radius: 4px;
    margin-right: 15px;
}

.favorite-info {
    display: flex;
    flex-direction: column;
}

.title {
    font-size: 18px;
    font-weight: 600;
    color: #333;
}


.no-favorites {
    text-align: center;
    font-size: 18px;
    color: #666;
}
</style>