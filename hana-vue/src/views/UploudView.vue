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
                                <el-button type="text" @click="handleEdit(anime.id)">编辑</el-button>
                                <el-button type="text" @click="handleAnalytics(anime.id)">数据</el-button>
                            </div>
                        </li>
                    </ul>
                </div>

                <div v-if="currentPage === 'upload'">
                    <Upload />
                </div>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import Header from './HeaderView.vue';
import { ElMessage } from 'element-plus';
import axios from '../utils/axios';
import Upload from '../components/Upload.vue';

// 当前页面状态
const currentPage = ref('manuscripts');

// 视频列表数据
const animes = ref([]);

// 筛选条件
// 筛选条件
const selectedArea = ref('');
const sortOrder = ref('views');
const process = ref('');
const sortDirection = ref('desc'); // 默认降序

// 监听 selectedArea 的变化，当选择 "全部板块" 时重置其他选项
watch(selectedArea, (newVal) => {
    if (newVal === '') { // 当选中 "全部板块" 时
        sortOrder.value = 'views';  // 重置排序方式为播放量
        process.value = '';         // 重置审核状态为默认值（全部）
        sortDirection.value = 'desc'; // 重置排序方向为降序
    }
});

// 获取用户稿件数据
const fetchAnimes = async () => {
    try {
        const response = await axios.get('/users/animes');
        animes.value = JSON.parse(response.data);
    } catch (error) {
        ElMessage.error('获取视频列表失败');
    }
};

const filteredAnimes = computed(() => {
    let filtered = animes.value;

    // 根据板块筛选
    if (selectedArea.value) {
        filtered = filtered.filter(anime => anime.type === selectedArea.value);
    }

    // 根据审核状态筛选
    if (process.value) {
        filtered = filtered.filter(anime => String(anime.process) === process.value);
    }

    // 根据排序方式和方向排序
    filtered = filtered.sort((a, b) =>
        sortDirection.value === 'asc'
            ? a[sortOrder.value] - b[sortOrder.value]
            : b[sortOrder.value] - a[sortOrder.value]
    );

    return filtered;
});

// 操作按钮点击事件
const handleEdit = (id) => {
    ElMessage.info('编辑稿件: ' + id);
};

const handleAnalytics = (id) => {
    ElMessage.info('查看数据: ' + id);
};

// 组件挂载后获取数据
onMounted(fetchAnimes);
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
