<template>
    <div>
        <el-carousel v-if="!loading && !error" :interval="5000" arrow="always">
            <el-carousel-item v-for="video in carouselVideos" :key="video.id">
                <img :src="video.cover" :alt="video.title" />
            </el-carousel-item>
        </el-carousel>
        <div v-if="loading">加载中...</div>
        <div v-if="error" class="error-message">{{ error }}</div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useCarouselStore } from '../store/carouselStore';
import { ref } from 'vue'

const loading = ref(true);
const error = ref(null); // 用于存储错误信息
const carouselStore = useCarouselStore();
const { carouselVideos, fetchCarouselVideos } = carouselStore;

onMounted(async () => {
    loading.value = true; // 开始加载
    error.value = null; // 清除之前的错误信息
    try {
        await fetchCarouselVideos(); // 等待获取数据
    } catch (err) {
        error.value = '获取轮播图信息失败！'; // 设置错误信息
        console.error(err); // 打印错误
    } finally {
        loading.value = false; // 加载结束
    }
});
</script>

<style scoped>
.el-carousel__item h3 {
    color: #475669;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
    text-align: center;
}

.el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
}

.el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
}
</style>