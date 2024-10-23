<template>
    <div id="app">
        <div class="main-content">
            <div>
                <div v-if="animesList && animesList.length > 0" class="anime-item" v-for="anime in animesList"
                    :key="anime.id">
                    <router-link :to="'/animes/' + anime.name" @click="addToHistory(anime.id)">
                        <img :src="anime.cover" alt="封面" loading="lazy">
                        <p>{{ anime.name }}</p>
                    </router-link>

                </div>
                <p v-else>没有找到视频。</p> <!-- 处理没有数据的情况 -->
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
import { useHistoryStore } from '../store/historyStore';
const props = defineProps({
    animesList: {
        type: Array,
        required: true,
    },
});
const historyStore = useHistoryStore();
// 添加到历史记录的方法
const addToHistory = (rid) => {
    historyStore.addHistoryItem(rid); // 只传递 rid
};

</script>

<style scoped>
#app {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    padding: 20px;
    min-height: 100vh;
    box-sizing: border-box;
}

.main-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    /* 自适应列布局 */
    grid-auto-rows: minmax(250px, auto);
    /* 动态调整行高，最小250px */
    gap: 20px;
    /* 网格间距 */
    width: 100%;
    max-width: 1200px;
    padding: 20px;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease-in-out, transform 0.3s;
}

.anime-item {
    background: linear-gradient(to right, #f9f9f9, #e6e9ef);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    text-align: center;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

.anime-item img {
    width: 100%;
    height: 160px;
    object-fit: cover;
    border-radius: 10px;
}

.anime-item p {
    font-size: 18px;
    color: #475669;
    margin-top: 10px;
    font-weight: bold;
    letter-spacing: 0.5px;
}

.anime-item:hover {
    transform: translateY(-10px) scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

@media (max-width: 1000px) {
    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        /* 中等屏幕列布局 */
        grid-auto-rows: minmax(220px, auto);
        /* 调整行高 */
    }

    .anime-item img {
        height: 140px;
    }
}

@media (max-width: 768px) {
    #app {
        flex-direction: column;
        align-items: center;
    }

    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        /* 小屏幕列布局 */
        grid-auto-rows: minmax(200px, auto);
        /* 调整行高 */
    }

    .anime-item img {
        height: 130px;
    }
}
</style>