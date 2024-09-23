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

<script setup lang="ts">
import { ref, watch, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { useVideoStore } from '../store/videoStore';
import HeaderView from './HeaderView.vue';
import VideoContainer from './VideoContainerView.vue';

const videoStore = useVideoStore();
const route = useRoute();
const videoId = ref(route.params.id);

// 初始加载数据
onMounted(async () => {
    await videoStore.fetchVideoData(videoId.value);
});

// 监听路由变化，动态加载新视频数据
watch(() => route.params.id, async (newId) => {
    videoId.value = newId;
    await videoStore.fetchVideoData(videoId.value);
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