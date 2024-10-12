<template>
    <div id="app">
        <div class="main-content">
            <!-- 模拟生成视频项 -->
            <div class="anime-item" v-for="anime in animesList" :key="animes.id" ">
                <router-link :to="'/animes/' + anime.name">
                <img :src="anime.cover" alt="封面">
                <p>{{ anime.name }}</p>
                </router-link>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAnimeStore } from '../store/animeStore';
import { useHistoryStore } from '../store/historyStore';
import { storeToRefs } from 'pinia';

const aninmStore = useAnimeStore();
// 获取视频列表
// const { videoList } = storeToRefs(videoStore);  // 使用 storeToRefs 保持响应式
// 历史仓库
// const historyStore = useHistoryStore();

onMounted(() => {
    aninmStore.fetchAllAnimes();
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
    /* 居中对齐主内容 */
    background: linear-gradient(to right, #8ce4eb, #b0eaff);
    /* 渐变背景 */
    padding: 20px;
    min-height: 100vh;
    /* 设置最小高度为视口高度 */
    box-sizing: border-box;
    /* 包含padding和border在总宽度内 */
}

.main-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    /* 自适应列宽 */
    grid-gap: 15px;
    width: 100%;
    max-width: 1200px;
    padding: 15px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 添加阴影 */
    border-radius: 10px;
    /* 圆角 */
    background-color: #fff;
    /* 内容区域背景色 */
    transition: box-shadow 0.3s ease-in-out;
    /* 阴影过渡 */
}

.anime-item {
    background-color: #d3dce6;
    align-items: center;
    justify-content: center;
    color: #475669;
    font-size: 18px;
    height: 200px;
    border-radius: 8px;
    transition: all 0.3s ease-in-out;
    /* 平滑过渡 */
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    /* 添加阴影 */
    display: inline-block;
    margin: 10px;
    text-align: center;
}

.anime-item img {
    width: 200px;
    height: 100px;
    object-fit: cover;
}

.anime-item:hover {
    transform: translateY(-5px) scale(1.05);
    /* 鼠标悬停时上升效果 */
    box-shadow: 0 6px 10px rgba(0, 0, 0, 0.2);
    /* 鼠标悬停时加深阴影 */
}

/* 响应式布局 */
@media (max-width: 1000px) {
    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
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
    }
}
</style>