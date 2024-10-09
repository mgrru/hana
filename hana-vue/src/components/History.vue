<template>
    <div class="history-page">
        <h2>历史播放记录</h2>
        <ul v-if="historyList.length > 0" class="history-list">
            <li class="history-item" v-for="video in historyList" :key="video.id">
                <router-link :to="'/video/' + video.id" class="link">
                    <img :src="video.thumbnail" alt="Thumbnail" class="thumbnail" />
                    <div class="video-info">
                        <p class="video-title">{{ video.title }}</p>
                        <p class="video-timestamp">播放时间: {{ video.timestamp }}</p>
                    </div>
                </router-link>
            </li>
        </ul>
        <p v-else class="no-favorites">暂无收藏视频</p>
    </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useHistoryStore } from '../store/historyStore';

const historyStore = useHistoryStore();
const { historyList } = storeToRefs(historyStore);
</script>

<style scoped>
.history-page {
    padding: 20px;
    max-width: 900px;
    margin: 0 auto;
}

.history-list {
    list-style-type: none;
    padding: 0;
    margin: 0;
}

.history-item {
    display: flex;
    align-items: center;
    margin-bottom: 20px;
    padding: 10px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;
}

.history-item:hover {
    transform: translateY(-3px);
}

.link {
    display: flex;
    align-items: center;
    text-decoration: none;
    /* 去掉链接下划线 */
    color: inherit;
    /* 继承文本颜色 */
}

.thumbnail {
    width: 100px;
    height: 56px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 15px;
}

.video-info {
    flex: 1;
}

.video-title {
    font-size: 16px;
    font-weight: bold;
    color: #333;
    margin: 0;
}

.video-timestamp {
    font-size: 14px;
    color: #888;
    margin-top: 5px;
}
</style>
