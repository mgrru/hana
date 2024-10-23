<template>
    <div class="favorite-container">
        <h2>我的收藏</h2>
        <div v-if="favoriteList.length > 0" v-for="item in favoriteList" :key="item.id" class="favorite-item">
            <router-link :to="'/animes/' + item.name" class="link">
                <img :src="item.cover" alt="cover" class="cover-image" />
                <div class="anime-info">
                    <h3>{{ item.title }}</h3>
                    <p>{{ item.views }} 次观看 | {{ item.likes }} 次点赞</p>
                </div>
            </router-link>
            <button @click="removeFavorite(item.id)">取消收藏</button>
        </div>
        <p v-else>暂无收藏记录。</p>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useFavoriteStore } from '../store/favorite';
import { storeToRefs } from 'pinia';

const favoriteStore = useFavoriteStore();
const { favoriteList } = storeToRefs(favoriteStore);

onMounted(() => {
    favoriteStore.fetchFavorites(); // 获取收藏列表
});

const removeFavorite = (rid) => {
    favoriteStore.removeFavorite(rid); // 取消收藏
};
</script>

<style scoped>
.favorite-container {
    padding: 20px;
    max-width: 800px;
    margin: 0 auto;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

h2 {
    text-align: center;
    margin-bottom: 20px;
    font-size: 24px;
    color: #333;
}

.favorite-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 15px;
    margin-bottom: 15px;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s ease-in-out;
}

.favorite-item:hover {
    transform: translateY(-5px);
}

.link {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
}

.cover-image {
    width: 120px;
    height: 120px;
    margin-right: 20px;
    border-radius: 10px;
    object-fit: cover;
}

.anime-info {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.anime-info h3 {
    font-size: 18px;
    margin-bottom: 8px;
    color: #333;
}

.anime-info p {
    font-size: 14px;
    color: #666;
}

button {
    background-color: #ff4d4f;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    transition: background-color 0.3s;
}

button:hover {
    background-color: #e43d3f;
}

@media (max-width: 600px) {
    .favorite-item {
        flex-direction: column;
        align-items: flex-start;
    }

    .cover-image {
        width: 100%;
        height: auto;
        margin-bottom: 15px;
    }

    button {
        align-self: flex-end;
        width: 100%;
        text-align: center;
    }
}
</style>