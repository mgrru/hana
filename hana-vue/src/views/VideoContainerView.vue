<template>
    <div class="video-container">
        <div class="left-container">
            <div class="video-player-container">
                <VideoPlayer :videoId="videoId"></VideoPlayer>
            </div>
            <!-- 评论组件 -->

            <div class="comment-section">
                <!-- <div v-if="visibleComments.length">
                    <Comment v-for="comment in visibleComments" :key="comment.id" :comment="comment" />
                </div>
                <p v-if="!userStore.isLoggedIn && comments.length > maxVisibleComments">
                    还有更多评论，<a @click="commentToLogin">登录以查看全部</a>
                </p>
                <p v-if="comments.length === 0">还没有评论，快来抢沙发吧！</p> -->

                <!-- 遮罩层 -->
                <!-- <div v-if="!userStore.isLoggedIn && comments.length > maxVisibleComments" class="mask">
                    <button class="login-btn" @click="commentToLogin">登录以查看更多评论</button>
                </div> -->
                <CommentList />
            </div>
        </div>
        <div class="right-container">
            <div class="user-info-container">
                <!-- 用户信息内容 -->
                <p>视频发布人信息</p>
            </div>
            <!-- 用户组件 -->
            <!-- 弹幕组件 -->
            <div class="danmaku-list">
                <h3>弹幕列表</h3>
                <el-scrollbar style="height: 400px;">
                    <div v-for="item in danmakuList" :key="item.id" class="danmaku-item">
                        <el-card :body-style="{ padding: '10px' }">
                            <div>{{ item.content }}</div>
                            <div class="meta">{{ item.timestamp }}</div>
                        </el-card>
                    </div>
                </el-scrollbar>
            </div>
            <!-- 视频选集组件 -->
        </div>
    </div>
</template>


<script setup>
import { computed } from 'vue';
import VideoPlayer from '../components/VideoPlayer.vue';
import { useDanmakuStore } from '../store/danmaku.js';
import CommentList from '../components/CommentList.vue';

const danmakuStore = useDanmakuStore();
const danmakuList = computed(() => danmakuStore.danmakuList);

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
