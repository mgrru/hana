<template>
    <el-container class="container">
        <el-header height="auto">
            <HeaderView></HeaderView>
        </el-header>
        <el-container class="main-container">
            <el-main>
                <Announcements />
                <div>
                    <AnimeList :animesList="popularAnimesList" />
                </div>
                <div>
                    <AnimeList :animesList="recommendAnimesList" />
                </div>
                <div>
                    <AnimeList :animesList="animesList" />
                </div>
                <!-- <Section /> -->
            </el-main>
            <el-aside width="70px">
                <Aside></Aside>
            </el-aside>
        </el-container>
    </el-container>
</template>


<script setup>
import Announcements from '../components/Announcements.vue';
import HeaderView from './HeaderView.vue';
import Aside from '../components/Aside.vue';
import { onMounted } from 'vue';
import Section from '../components/Section.vue';
import { useAnimeStore } from '../store/animeStore';
import { storeToRefs } from 'pinia';
import AnimeList from './AnimeList.vue';

const animeStore = useAnimeStore();
const { popularAnimesList, recommendAnimesList, animesList } = storeToRefs(animeStore);

// 页面加载时并行获取视频列表
onMounted(async () => {
    try {
        await Promise.all([
            animeStore.fetchPopularAnimes(),
            animeStore.fetchRecommendAnimes(),
            animeStore.fetchAllAnimes(),
        ]);
    } catch (error) {
        console.error("获取视频列表失败：", error);
    }
});
</script>

<style scoped>
.container {
    margin: 0;
    padding: 0;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    /* 让 header 和 main-container 垂直排列 */
}


.main-container {
    padding: 0 5px;
}

.el-main {
    padding: 0 2px;
}
</style>