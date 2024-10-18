<template>
    <div id="app">
        <div class="main-content">
            <div>
                <div v-if="animesList && animesList.length > 0" class="anime-item" v-for="anime in animesList"
                    :key="anime.id">
                    <router-link :to="'/animes/' + anime.name" @click="recordHistory(anime)">
                        <img :src="anime.cover" alt="封面" loading="lazy">
                        <p>{{ anime.name }}</p>
                    </router-link>
                </div>
                <p v-else>没有找到视频。</p> <!-- 处理没有数据的情况 -->
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps } from 'vue';
import { useHistoryStore } from '../store/historyStore';

const props = defineProps({
    animesList: {
        type: Array,
        required: true,
    },
});
// import { ref, onMounted, computed } from 'vue';
// import { useAnimeStore } from '../store/animeStore.js';

// import { storeToRefs } from 'pinia';
// import axios from "../utils/axios";


// const animeStore = useAnimeStore();

// const { animesList } = storeToRefs(animeStore);

//另一种写法不用pinia
// const fetchAListData = async () => {
//     try {
//         const response = await axios.get('/animes') // 请求接口 /sections
//         // console.log('data:', response.data) // 第一个打印 - 确保获取到的原始数据正确
//         // aList.value = response.data
//         aList.value = JSON.parse(response.data)
//         console.log('aList value:', aList.value) // 第三个打印 - 确保 sections 数据已经正确赋值
//     } catch (error) {
//         console.error('获取 section 数据失败:', error)
//     }
// }


// 历史仓库
const historyStore = useHistoryStore();

// 记录点击历史
const recordHistory = (anime) => {
    historyStore.addHistory(anime); // 将视频信息添加到历史记录中
};

</script>

<style scoped>
#app {
    display: flex;
    justify-content: center;
    align-items: flex-start;
    background: linear-gradient(135deg, #8ce4eb 0%, #b0eaff 100%);
    padding: 20px;
    min-height: 100vh;
    box-sizing: border-box;
}

.main-content {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    /* 自适应列布局 */
    grid-auto-rows: minmax(250px, auto);
    /* 动态调整行高，最小250px */
    gap: 20px;
    /* 网格间距 */
    width: 100%;
    max-width: 1200px;
    padding: 20px;
    background-color: #fff;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
    transition: box-shadow 0.3s ease-in-out, transform 0.3s;
}

.anime-item {
    background: linear-gradient(to right, #f9f9f9, #e6e9ef);
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    text-align: center;
    padding: 15px;
    border-radius: 12px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease-in-out, box-shadow 0.3s ease-in-out;
}

.anime-item img {
    width: 100%;
    height: 160px;
    object-fit: cover;
    border-radius: 10px;
}

.anime-item p {
    font-size: 18px;
    color: #475669;
    margin-top: 10px;
    font-weight: bold;
    letter-spacing: 0.5px;
}

.anime-item:hover {
    transform: translateY(-10px) scale(1.05);
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

@media (max-width: 1000px) {
    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
        /* 中等屏幕列布局 */
        grid-auto-rows: minmax(220px, auto);
        /* 调整行高 */
    }

    .anime-item img {
        height: 140px;
    }
}

@media (max-width: 768px) {
    #app {
        flex-direction: column;
        align-items: center;
    }

    .main-content {
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        /* 小屏幕列布局 */
        grid-auto-rows: minmax(200px, auto);
        /* 调整行高 */
    }

    .anime-item img {
        height: 130px;
    }
}
</style>