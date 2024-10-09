<template>
    <div id="app">
        <el-container class="container">
            <el-header>
                <HeaderView></HeaderView>
            </el-header>
            <el-main>
                <VideoContainer :videoId="videoId"></VideoContainer>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useVideoStore } from '../store/videoStore';
import HeaderView from './HeaderView.vue';
import VideoContainer from './VideoContainerView.vue';
import { useRoute } from 'vue-router';
const videoStore = useVideoStore();
const route = useRoute();
const videoId = ref(route.params.id);
console.log("通过 route 获取的 ID:", videoId.value);
// 初始加载视频数据
onMounted(async () => {
    await videoStore.fetchVideoData(videoId.value);
});

// 监听 videoId 变化
watch(() => route.params.id, async (newId) => {
    videoId.value = newId;
    await videoStore.fetchVideoData(newId);
});
</script>


<style lang="scss">
.el-main {
    padding: 0;
}

.main-container {
    padding: 0 20px;
}

.el-main {
    padding: 0 2px;
}
</style>