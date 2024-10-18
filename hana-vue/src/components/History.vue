<template>
    <div class="history-page">
        <h2>历史播放记录</h2>
        <ul v-if="historyList.length > 0" class="history-list">
            <li class="history-item" v-for="anime in historyList" :key="anime.id">
                <router-link :to="'/anime/' + anime.name" class="link">
                    <img :src="anime.cover" alt="Anime" class="anime" />
                    <div class="anime-info">
                        <p class="anime-title">{{ anime.title }}</p>
                    </div>
                </router-link>
                <el-button @click="handDelete(anime.id)" size="mini" type="danger">删除</el-button>
            </li>
        </ul>
        <p v-else class="no-history">暂无历史视频</p>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { storeToRefs } from 'pinia';
import { useHistoryStore } from '../store/historyStore';

const historyStore = useHistoryStore();
const { historyList, fetchHistoryAnimes, deleteHistory } = storeToRefs(historyStore);

onMounted(() => {
    fetchHistoryAnimes(); // 获取历史记录
});

// 删除历史记录方法
const handDelete = async (rid) => {
    try {
        await deleteHistory(rid); // 调用 store 中的删除方法
        fetchHistoryAnimes(); // 重新获取历史记录
    } catch (error) {
        console.error('删除历史记录失败:', error);
    }
};
</script>

<style scoped>
.history-page {
    padding: 20px;
    max-width: 900px;
    margin: 0 auto;
    background-color: #f9f9f9;
    /* 增加背景色 */
    border-radius: 10px;
    /* 圆角 */
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    /* 阴影效果 */
}

.history-list {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.history-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    /* 减小底部间距 */
    padding: 15px;
    /* 增加内边距 */
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, background-color 0.2s;
    /* 增加背景色过渡 */
}

.history-item:hover {
    transform: translateY(-3px);
    background-color: #f0f8ff;
    /* 鼠标悬停时背景色变化 */
}

.link {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
}

.anime {
    width: 100px;
    height: 56px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 15px;
    transition: transform 0.3s;
    /* 添加图片缩放效果 */
}

.anime:hover {
    transform: scale(1.05);
    /* 鼠标悬停时放大效果 */
}

.anime-info {
    flex: 1;
}

.anime-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin: 0;
}

.no-history {
    text-align: center;
    font-size: 18px;
    color: #777;
    /* 调整暂无历史视频的颜色 */
}
</style>
