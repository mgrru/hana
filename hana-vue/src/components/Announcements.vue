<template>
    <div class="announcement-container">
        <h2>公告</h2>
        <div v-if="isLoading" class="loading">加载中...</div>
        <div v-if="error" class="error">{{ error }}</div>
        <ul v-else class="announcement-list">
            <li v-for="announcement in announcements" :key="announcement.id" class="announcement-item">
                <h3>{{ announcement.title }}</h3>
                <p>{{ announcement.content }}</p>
                <small>{{ new Date(announcement.time).toLocaleString() }}</small>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { storeToRefs } from 'pinia';
import { useAnnouncementsStore } from '../store/announcements';
import { onMounted } from 'vue';

const announcementsStore = useAnnouncementsStore();
const { announcements, isLoading, error } = storeToRefs(announcementsStore);

const loadAnnouncements = () => {
    announcementsStore.fetchAnnouncements();
    console.log("加载后的公告数据:", announcements.value); // 确认store中的数据
};

// 自动加载公告
onMounted(() => {
    console.log("组件已挂载，开始加载公告");
    loadAnnouncements();
});
</script>

<style scoped>
.announcement-container {
    max-width: 800px;
    margin: 20px auto;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}

h2 {
    font-size: 24px;
    color: #333;
    text-align: center;
    margin-bottom: 20px;
    font-weight: bold;
}

.loading {
    text-align: center;
    color: #666;
    font-size: 18px;
}

.error {
    text-align: center;
    color: red;
    font-size: 18px;
}

.announcement-list {
    list-style: none;
    padding: 0;
    margin: 0;
}

.announcement-item {
    border-bottom: 1px solid #e0e0e0;
    padding: 20px;
    transition: background-color 0.3s ease;
}

.announcement-item:hover {
    background-color: #f9f9f9;
}

h3 {
    font-size: 20px;
    color: #333;
    margin: 0;
    margin-bottom: 10px;
}

p {
    font-size: 16px;
    color: #666;
    margin: 0 0 10px;
}

small {
    color: #999;
    font-size: 14px;
    display: block;
}

.announcement-item:last-child {
    border-bottom: none;
}
</style>