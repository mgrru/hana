<template>
    <el-container class="manuscript-container">
        <el-header class="header">
            <Header />
        </el-header>
        <el-container class="content">
            <el-main>
                <div>
                    <AnimeList :animesList="searchAnimesList" />
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import Header from './HeaderView.vue';
import { onMounted } from 'vue';
import { useAnimeStore } from '../store/animeStore';
import { storeToRefs } from 'pinia';
import AnimeList from './AnimeList.vue';

const animeStore = useAnimeStore();
const { searchAnimesList } = storeToRefs(animeStore)
const searchName = route.query.name || '';

// 页面加载时并行获取视频列表
onMounted(async () => {
    console.log(route.query); // 确认是否有 `name` 参数
    try {
        await Promise.all([
            animeStore.fetchSearchAnimes(searchName)
        ]);
    } catch (error) {
        console.error("获取视频列表失败：", error);
    }
});
</script>
