<!--
 * @Author: tangzhengtao 1399159010@qq.com
 * @Date: 2024-10-08 10:55:40
 * @LastEditors: tangzhengtao 1399159010@qq.com
 * @LastEditTime: 2024-10-22 22:09:18
 * @FilePath: \vue3-app1\src\components\History.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
    <div class="history-container">
        <h2>历史播放记录</h2>
        <div v-if="historyList.length > 0" v-for="item in sortedHistoryList" :key="item.id" class="history-item">
            <router-link :to="'/animes/' + item.name" class="link">
                <img :src="item.cover" alt="cover" class="cover-image" />
                <div class="anime-info">
                    <h3>{{ item.title }}</h3>
                    <p>{{ item.views }} 次观看 | {{ item.likes }} 次点赞</p>
                    <p class="timestamp">观看时间: {{ new Date(item.time).toLocaleString() }}</p>
                </div>
            </router-link>
            <button @click="deleteHistory(item.id)">删除</button>
        </div>
        <p v-else>没有历史记录。</p>
    </div>
</template>


<script setup>
import { onMounted, computed } from 'vue';
import { useHistoryStore } from '../store/historyStore';
import { storeToRefs } from 'pinia';


const historyStore = useHistoryStore();
const { historyList } = storeToRefs(historyStore);

// 对历史记录按时间倒序排序
const sortedHistoryList = computed(() => {
    return historyList.value.slice().sort((a, b) => new Date(b.time) - new Date(a.time));
});

onMounted(() => {
    historyStore.fetchHistory(); // 获取历史记录
});

const deleteHistory = (id) => {
    historyStore.deleteHistoryItem(id); // 删除历史记录
};
</script>

<style scoped>
.history-container {
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

.history-item {
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

.history-item:hover {
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

.timestamp {
    font-size: 0.85em;
    color: #999;
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
    .history-item {
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
