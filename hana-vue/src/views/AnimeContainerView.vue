<template>
    <div class="video-container">
        <div class="left-container">
            <div class="video-player-container">
                <VideoPlayer :name="props.name" :rid="props.rid" :key="props.rid"></VideoPlayer>
            </div>
            <!-- 评论组件 -->
            <div class="comment-section">
                <CommentList :rid="props.rid" :key="props.rid" />
            </div>
        </div>
        <div class="right-container">
            <!-- 用户信息内容 -->
            <!-- <div class="user-info-container">

                <p>视频发布者：{{ animesInfo.uid }}</p>

            </div> -->
            <!-- 用户组件 -->
            <!-- 弹幕列表组件 -->
            <div class="danmaku-list">
                <h3>弹幕列表</h3>
                <el-scrollbar style="height: 400px;">
                    <div v-for="item in danmakuList" :key="item.id" class="danmaku-item">
                        <el-card :body-style="{ padding: '10px' }">
                            <div>{{ item.user_name }}:{{ item.content }}</div>
                            <button class="delete" @click="deleteDanmu(item.id)">删除</button>
                        </el-card>

                    </div>
                    <p v-if="danmakuList.length === 0">还没有弹幕，快来说些什么吧！</p>
                </el-scrollbar>


            </div>
            <!-- 视频选集组件 -->

            <!-- 视频推荐 -->
            <div>
                <p>推荐视频</p>
                <AnimeList :animesList="recommendAnimesList" />
            </div>
        </div>
    </div>
</template>


<script setup>
import VideoPlayer from '../components/VideoPlayer.vue';
import { useDanmakuStore } from '../store/danmaku.js';
import CommentList from '../components/CommentList.vue';

import { defineProps, onMounted } from 'vue';
import { useAnimeStore } from '../store/animeStore.js';
import { storeToRefs } from 'pinia';
import AnimeList from './AnimeList.vue';
import { ElMessage, ElMessageBox } from "element-plus"; // 引入 Element Plus 消息组件

const animeStore = useAnimeStore();
const { animesInfo } = storeToRefs(animeStore)

const { recommendAnimesList } = storeToRefs(animeStore);

const danmakuStore = useDanmakuStore();
// const danmakuList = danmakuStore.danmakuList;
const { danmakuList } = storeToRefs(danmakuStore)

const props = defineProps({
    name: String,
    rid: Number,
});


const deleteDanmu = (danmuId) => {
    console.log(`调用删除按钮`)
    ElMessageBox.confirm(
        '此操作将永久删除该弹幕, 是否继续?',
        '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }
    )
        .then(async () => {
            // 确认后执行删除操作
            await danmakuStore.removeDanmu(danmuId);

            // 删除成功提示
            ElMessage({
                message: '评论删除成功',
                type: 'success',
                duration: 2000,
            });
            await danmakuStore.fetchDanmakuData(props.rid); // 删除成功后重新加载评论
        })
        .catch(() => {
            // 用户取消操作
            ElMessage({
                type: 'info',
                message: '已取消删除',
                duration: 1500,
            });
        });
};

console.log('AnimeContainer 接收到的 rid:', props.rid);

onMounted(async () => {

    try {
        await Promise.all([
            animeStore.fetchRecommendAnimes(),
            danmakuStore.fetchDanmakuData(props.rid),
        ]);
    } catch (error) {
        console.error("获取视频列表失败：", error);
    }
    console.log('AnimeContainerView 接收的name:', props.name);
});
</script>

<style lang="scss">
.video-container {
    display: flex;
    flex-wrap: wrap;
    margin: 20px;
    padding: 20px;
    background: linear-gradient(135deg, #74ebd5, #acb6e5);
    border-radius: 15px;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
}

.left-container,
.right-container {
    box-sizing: border-box;
    padding: 20px;
    border-radius: 15px;
}

.left-container {
    flex: 3;
    margin-right: 20px;
}

.video-player-container {
    background-color: #ffffff;
    border-radius: 15px;
    padding: 15px;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

.comment-section {
    margin-top: 20px;
    background-color: #ffffff;
    border-radius: 15px;
    padding: 20px;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
    position: relative;
    /* 为遮罩层提供相对定位 */
}

.mask {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(255, 255, 255, 0.8);
    /* 半透明背景 */
    display: flex;
    justify-content: center;
    align-items: center;
}

.right-container {
    flex: 1;
    display: flex;
    flex-direction: column;
    background-color: #f9f9f9;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

.user-info-container {
    background-color: #ffffff;
    border-radius: 15px;
    padding: 20px;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
    margin-bottom: 20px;
}

.el-collapse {
    width: 100%;
    border-radius: 15px;
    background-color: #ffffff;
    box-shadow: 0 5px 20px rgba(0, 0, 0, 0.05);
}

.danmu-list {
    padding: 15px;
    background-color: #f7f7f7;
    border-radius: 10px;
}

@media (max-width: 768px) {
    .video-container {
        flex-direction: column;
        margin: 10px;
        padding: 15px;
    }

    .left-container {
        margin-right: 0;
        margin-bottom: 20px;
    }

    .right-container {
        margin-bottom: 20px;
    }
}
</style>
