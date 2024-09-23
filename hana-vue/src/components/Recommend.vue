<template>
    <!-- 轮播图 -->
    <el-carousel :interval="5000" arrow="always" class="carousel">
        <el-carousel-item v-for="(video, index) in carouselVideos" :key="index">
            <div class="carousel-item">
                <img :src="video.cover" :alt="video.title" class="carousel-image" />
                <div class="carousel-info">
                    <h3>{{ video.title }}</h3>
                    <p>{{ video.description }}</p>
                    <p>Views: {{ video.views }}</p>
                </div>
            </div>
        </el-carousel-item>
    </el-carousel>
</template>

<script setup>
import { onMounted } from 'vue';
import { useCarouselStore } from '../store/carouselStore';
import { ElCarousel, ElCarouselItem } from 'element-plus';

const carouselStore = useCarouselStore();
const { carouselVideos, fetchCarouselVideos } = carouselStore;

onMounted(() => {
    fetchCarouselVideos();
});
</script>

<style scoped>
.carousel-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background-color: #f5f5f5;
}

.carousel-image {
    width: 100%;
    height: auto;
}

.carousel-info {
    text-align: center;
    padding: 10px;
}

h3 {
    margin: 10px 0;
}
</style>