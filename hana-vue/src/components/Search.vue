<template>
    <div class="-container">
        <el-input style="width: 240px" placeholder="请输入搜索内容" v-model="searchName">
            <template #append>
                <el-button type="primary" :icon="Search" @click="handleSearch">搜索</el-button>
            </template>
        </el-input>
    </div>
</template>

<script lang="ts" setup>
import { Search } from '@element-plus/icons-vue';
import { ElMessage } from 'element-plus';
import { useAnimeStore } from '../store/animeStore';
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const animeStore = useAnimeStore();
const searchName = ref('');

const handleSearch = async () => {
    if (!searchName.value.trim()) {
        ElMessage.warning('请输入搜索内容');
        return;
    }
    // 跳转到视频展示页面，并传递搜索关键词
    router.push({ name: 'searchList', query: { name: searchName.value } });
};

</script>

<style lang="scss" scoped>
.el-input {
    border: none;
    box-shadow: none;
}

.search-icon {
    margin-left: 8px;
    cursor: pointer;
    color: #333;
    transition: color 0.3s;
}

.search-icon:hover {
    color: #409eff;
}
</style>