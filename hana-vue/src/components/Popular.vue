<template>
    <div id="app">
        <div class="main-content">
            <div>
                <p>热门</p>
                <div v-if="popularAnimesList && popularAnimesList.length > 0" class="anime-item"
                    v-for="popularAnime in popularAnimesList" :key="popularAnime.id">
                    <router-link :to="'/animes/' + popularAnime.name">
                        <img :src="popularAnime.cover" alt="热门封面">
                        <p>{{ popularAnime.name }}</p>
                    </router-link>
                </div>
                <p v-else>没有找到热门视频。</p> <!-- 处理没有数据的情况 -->
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useAnimeStore } from '../store/animeStore.js';
import { useHistoryStore } from '../store/historyStore';
import { storeToRefs } from 'pinia';

const animeStore = useAnimeStore();
const { popularAnimesList } = storeToRefs(animeStore);

// 页面加载时获取视频列表
onMounted(async () => {
    try {
        await animeStore.fetchPopularAnimes();
        // console.log("视频页获取的所有推荐视频列表:", popularAnimesList.value);  // 检查数据是否被正确加载
        // console.log("获取的推荐视频列表数:", popularAnimesList.value.length);  // 检查数据是否被正确加载
    } catch (error) {
        console.error("获取视频列表失败：", error);
    }
});


// 记录点击历史
// const recordHistory = (video) => {
//     historyStore.addHistory(video); // 将视频信息添加到历史记录中
// };

</script>

<style scoped>
#app {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    /* 垂直方向对齐更灵活 */
    background: linear-gradient(135deg, #8ce4eb 0%, #b0eaff 100%);
    padding: 20px;
    min-height: 100vh;
    box-sizing: border-box;
}

.main-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    /* 自适应列布局 */
    grid-gap: 20px;
    /* 增加间距 */
    width: 100%;
    max-width: 1200px;
    padding: 20px;
    /* 增加内边距 */
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    /* 加强阴影效果 */
    transition: box-shadow 0.3s ease-in-out, transform 0.3s;
}

.anime-item {
    background: linear-gradient(to right, #f9f9f9, #e6e9ef);
    /* 添加渐变背景 */
    display: flex;
    flex-direction: column;
    /* 内容垂直排列 */
    align-items: center;
    justify-content: space-between;
    text-align: center;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
    height: 250px;
    /* 增加视频项高度 */
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
    /* 增强文字突出 */
    letter-spacing: 0.5px;
    /* 增加字间距 */
}

.anime-item:hover {
    transform: translateY(-10px) scale(1.05);
    /* 增加更显著的交互效果 */
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

@media (max-width: 1000px) {
    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        /* 调整列数 */
    }

    .anime-item {
        height: 220px;
        /* 减少高度 */
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
        width: 100%;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        /* 进一步缩小列宽 */
    }

    .anime-item {
        height: 200px;
    }

    .anime-item img {
        height: 130px;
    }
}
</style>