<template>
    <div id="app">
        <el-container class="container">
            <el-header>
                <HeaderView></HeaderView>
            </el-header>
            <el-main>
                <AnimeContainer :name="name" :rid="rid" :key="rid"></AnimeContainer>
            </el-main>
        </el-container>
    </div>
</template>

<script setup>
import { ref, watch, onMounted } from 'vue';
import { useAnimeStore } from '../store/animeStore';
import HeaderView from './HeaderView.vue';
import AnimeContainer from './AnimeContainerView.vue';
import { useRoute } from 'vue-router';


const route = useRoute();
const name = ref(route.params.name);
const animeStore = useAnimeStore();
const rid = ref(null); // 用于存储视频ID

onMounted(async () => {
    await animeStore.fetchAnimeData(name.value);
    rid.value = animeStore.animesInfo.id; // 从store中获取视频ID
    console.log('rid:', rid.value);
});

watch(() => route.params, async (newParams) => {
    name.value = newParams.name;
    await animeStore.fetchAnimeData(name.value);
    rid.value = animeStore.animesInfo.id; // 更新视频ID
});
</script>


<style lang="scss">
.main-container {
    padding: 0 20px;
}

.el-main {
    padding: 0 2px;
}
</style>