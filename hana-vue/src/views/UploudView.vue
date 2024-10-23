<template>
    <el-container class="manuscript-container">
        <el-header class="header">
            <Header />
        </el-header>

        <el-container class="content">
            <el-main>
                <!-- 面包屑导航 -->
                <el-breadcrumb separator="/">
                    <el-breadcrumb-item @click="currentPage = 'manuscripts'">
                        <span style="cursor: pointer;">我的稿件</span>
                    </el-breadcrumb-item>
                    <el-breadcrumb-item @click="currentPage = 'upload'">
                        <span style="cursor: pointer;">上传资源</span>
                    </el-breadcrumb-item>
                </el-breadcrumb>

                <!-- 切换内容显示区域 -->
                <div v-if="currentPage === 'manuscripts'" class="video-list">
                    <div class="filter-bar">
                        <el-select v-model="selectedArea" placeholder="全部板块" size="small" @change="fetchAnimes">
                            <el-option label="全部板块" value=""></el-option>
                            <el-option label="国漫" value="国漫"></el-option>
                            <el-option label="日漫" value="日漫"></el-option>
                            <el-option label="其他" value="其他"></el-option>
                        </el-select>
                        <el-select v-model="sortOrder" placeholder="排序方式" size="small" @change="fetchAnimes">
                            <el-option label="播放量排序" value="views"></el-option>
                            <el-option label="点赞数排序" value="likes"></el-option>
                        </el-select>
                        <el-select v-model="process" placeholder="审核状态" size="small" @change="fetchAnimes">
                            <el-option label="已通过" value="true"></el-option>
                            <el-option label="未通过" value="false"></el-option>
                        </el-select>
                    </div>

                    <ul class="anime-list">
                        <li v-for="anime in filteredAnimes" :key="anime.id" class="anime-item">
                            <router-link :to="'/animes/' + anime.name" class="link">
                                <img :src="anime.cover" alt="cover" class="cover-image" />
                                <div class="anime-info">
                                    <h3>{{ anime.title }}</h3>
                                    <p>{{ anime.views }} 次观看 | {{ anime.likes }} 次点赞</p>
                                </div>
                            </router-link>
                            <div class="action-buttons">
                                <el-button type="link" @click="handleEdit(anime.id)">编辑</el-button>
                                <el-button type="link" @click="handleAnalytics(anime.id)">数据</el-button>
                            </div>
                        </li>
                    </ul>
                </div>

                <div v-if="currentPage === 'upload'">
                    <Upload @upload-success="handleUploadSuccess" />
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useUploadStore } from '../store/uploadStore';
import Header from './HeaderView.vue';
import Upload from '../components/Upload.vue';

const currentPage = ref('manuscripts');

// 获取 store 实例
const uploadStore = useUploadStore();

// 筛选条件
const selectedArea = ref('');
const sortOrder = ref('views');
const process = ref('');
const sortDirection = ref('desc'); // 默认降序

// 监听 selectedArea 的变化
watch(selectedArea, (newVal) => {
    if (newVal === '') {
        sortOrder.value = 'views';
        process.value = '';
        sortDirection.value = 'desc';
    }
});

// 计算过滤后的动漫列表
const filteredAnimes = computed(() => {
    let filtered = uploadStore.animes;

    if (selectedArea.value) {
        filtered = filtered.filter(anime => anime.type === selectedArea.value);
    }

    if (process.value) {
        filtered = filtered.filter(anime => String(anime.process) === process.value);
    }

    filtered = filtered.sort((a, b) =>
        sortDirection.value === 'asc'
            ? a[sortOrder.value] - b[sortOrder.value]
            : b[sortOrder.value] - a[sortOrder.value]
    );

    return filtered;
});

// 页面加载时获取稿件数据
onMounted(uploadStore.fetchAnimes);

// 上传成功后跳转到“我的稿件”页面
const handleUploadSuccess = () => {
    currentPage.value = 'manuscripts';
    uploadStore.fetchAnimes();
};

// 操作按钮点击事件
const handleEdit = (id) => {
    console.log('编辑稿件: ' + id);
};

const handleAnalytics = (id) => {
    console.log('查看数据: ' + id);
};
</script>

<style scoped>
/* 布局样式 */
.content {
    width: 100%;
    padding: 20px;

    align-items: center;
}

.el-breadcrumb {
    display: flex;
    justify-content: flex-start;
    padding: 10px;
    background-color: #f9f9f9;
    border-radius: 8px;
}

.el-breadcrumb-item {
    cursor: pointer;
    font-size: 16px;
}

.el-breadcrumb-item:hover {
    text-decoration: underline;
}

.anime-list {
    list-style: none;
    padding: 0;
    margin: 0;
    width: 80%;
    max-width: 1200px;
}

.anime-item {
    display: flex;
    align-items: center;
    margin-bottom: 15px;
    padding: 20px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    transition: transform 0.2s, background-color 0.2s;
}

.anime-item:hover {
    transform: translateY(-3px);
    background-color: #f0f8ff;
}

.link {
    display: flex;
    align-items: center;
    text-decoration: none;
    color: inherit;
    flex-grow: 1;
}

.cover-image {
    width: 120px;
    height: 80px;
    border-radius: 8px;
    object-fit: cover;
    margin-right: 20px;
}

.anime-info {
    flex-grow: 1;
}

.anime-info h3 {
    font-size: 18px;
    margin-bottom: 10px;
}

.anime-info p {
    color: #909399;
    margin-bottom: 10px;
}

.action-buttons {
    display: flex;
    gap: 10px;
}

/* 筛选功能的样式 */
.filter-bar {
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
    gap: 10px;
    margin-bottom: 20px;
}

.filter-bar .el-select {
    flex-grow: 1;
    min-width: 150px;
}

/* 自适应布局 */
@media (max-width: 768px) {
    .anime-item {
        flex-direction: column;
        align-items: flex-start;
    }

    .cover-image {
        width: 100%;
        height: auto;
        margin-right: 0;
        margin-bottom: 10px;
    }

    .anime-info {
        width: 100%;
    }
}
</style>