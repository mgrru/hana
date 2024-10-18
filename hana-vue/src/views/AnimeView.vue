<template>
    <div id="app">
        <el-container class="container">
            <el-header>
                <HeaderView></HeaderView>
            </el-header>
            <el-main>
                <AnimeContainer :name="name"></AnimeContainer>
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

// 初始加载视频数据
onMounted(async () => {
    console.log('当前name:', name.value);
});

// 监听 name 变化
watch(() => route.params, async (newParams) => {
    name.value = newParams.name;
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