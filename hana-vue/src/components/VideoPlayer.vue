<template>
    <div class="video-info-container">
        <h1 class="video-info-title">{{ title }}</h1>

        <div class="video-info-meta">
            <span>播放量: {{ views }}</span>
            <span>弹幕数: {{ danmakuCount }}</span>
            <span>发布时间: {{ publishDate }}</span>
        </div>


        <div style="position: relative; width: 100%;">
            <!-- 视频播放器 -->
            <video ref="videoRef" width="100%" controls @play="handlePlay" @pause="handlePause">
                <source :src="videoSrc" type="video/mp4" />
            </video>

            <!-- 弹幕组件，使用自定义弹幕结构 -->
            <vue-danmaku ref="danmakuRef" v-model:danmus="danmakuList" loop :channels="5" useSlot
                style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none;"
                :pause="!isPlaying">
                <template v-slot:dm="{ danmu }">
                    <span>{{ danmu.content }}</span>
                </template>
            </vue-danmaku>
        </div>

        <div>
            <!-- 控制按钮和弹幕输入 -->
            <el-button @click="togglePlay">{{ isPlaying ? '暂停' : '播放' }}</el-button>
            <el-button @click="toggleDanmakuVisibility">{{ isDanmakuVisible ? '关闭弹幕' : '显示弹幕' }}</el-button>
            <el-popover placement="bottom" title="Title" :width="200" trigger="click">
                <p>弹幕速度</p>
                <p>弹幕</p>
                <template #reference>
                    <el-button class="m-2">弹幕设置</el-button>
                </template>
            </el-popover>
            <el-input v-model="input" placeholder="发送弹幕" :disabled="!isDanmakuVisible" />
            <el-button @click="sendDanmaku" type="primary" :disabled="!isDanmakuVisible">发送</el-button>
        </div>

        <!-- 点赞组件 -->
        <div class="video-stats">
            <like-button />
        </div>

        <div class="video-desc-container">
            <p>{{ description }}</p>
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useVideoStore } from '../store/animeStore';
import { useRoute } from 'vue-router';
import { ref } from 'vue';
import { watch } from 'vue';
import { computed } from 'vue';
import { useDanmakuStore } from '../store/danmaku';
import vueDanmaku from 'vue3-danmaku'
import { generateRandomDanmaku } from '../mock/mockData';
import { storeToRefs } from 'pinia';
import LikeButton from './LikeButton.vue';
//测试视频
// import videoSrc from '../videos/barca1.mp4'


const store = useDanmakuStore();
const input = ref('');
const danmakuList = computed(() => store.danmakuList);
const videoRef = ref(null);
const danmakuRef = ref(null);
const danmakuCount = computed(() => store.danmakuList.length);
// console.log("弹幕数：" + danmakuCount.value);

const videoStore = useVideoStore();
const route = useRoute();
const videoId = ref(route.params.id);

const isPlaying = computed(() => store.isPlaying);
const isDanmakuVisible = computed(() => store.isDanmakuVisibility);

// 控制播放按钮
const togglePlay = () => {
    if (isPlaying.value) {
        videoRef.value.pause(); // 暂停视频
        danmakuRef.value.pause(); // 暂停弹幕
    } else {
        // 尝试播放视频，并处理可能的 NotAllowedError
        videoRef.value.play().catch(error => {
            if (error.name === 'NotAllowedError') {
                console.error('Video cannot be played automatically. Please click the play button.');
            } else {
                console.error('Playback failed:', error);
            }
        });
        danmakuRef.value.play(); // 播放弹幕
    }
    store.togglePlay(); // 更新 Pinia 状态
};

// 显示/隐藏弹幕
const toggleDanmakuVisibility = () => {
    store.toggleDanmakuVisibility();
    if (danmakuRef.value) {
        isDanmakuVisible.value ? danmakuRef.value.show() : danmakuRef.value.hide();
    }
};

//发送弹幕
const sendDanmaku = () => {
    if (input.value.trim()) {
        const newDanmaku = {
            id: Date.now(),
            content: input.value,
            timestamp: new Date().toLocaleString(),  // 使用本地时间格式
            color: '#FF0000' // 默认颜色
        };
        console.log('发送的弹幕:', newDanmaku);  // 打印发送的弹幕
        store.addDanmaku(newDanmaku); // 将弹幕添加到 Pinia store
        input.value = '';
    }
};


// 监听视频播放和暂停事件，立即同步状态
const handlePlay = () => {
    store.isPlaying = true;  // 更新 Pinia 中的状态
    danmakuRef.value.play(); // 弹幕开始播放
};

const handlePause = () => {
    store.isPlaying = false;  // 更新 Pinia 中的状态
    danmakuRef.value.pause(); // 弹幕暂停
};

// 监听播放状态变化，控制弹幕同步
watch(isPlaying, (newVal) => {
    if (videoRef.value && danmakuRef.value) {
        if (newVal) {
            videoRef.value.play();
            danmakuRef.value.play();  // 确保弹幕同步播放
        } else {
            videoRef.value.pause();
            danmakuRef.value.pause();  // 确保弹幕同步暂停
        }
    }
    console.log('Danmaku List:', newVal); // 确保数据正确
});



onMounted(async () => {
    if (videoId.value) {
        videoStore.fetchVideoData(videoId.value); // 根据视频 ID 加载数据
        console.log('加载视频的Id为：', videoId.value);
        console.log('获取的视频数据:', videoStore.title); // 检查标题是否已更新
    }
    // 初始化弹幕列表
    const initialDanmakuList = generateRandomDanmaku(10);
    store.danmakuList = initialDanmakuList;

    // 页面加载时不自动播放视频和弹幕，等待用户点击播放
    if (videoRef.value && danmakuRef.value) {
        videoRef.value.pause(); // 暂停视频
        danmakuRef.value.pause(); // 暂停弹幕
    }
    console.log('组件挂载时的弹幕列表:', danmakuList.value);  // 打印初始弹幕列表
});

const { videoSrc, title, description, views, likes, stars, danmaku, publishDate, shares } = storeToRefs(videoStore);
// const { title, description, views, likes, stars, danmaku, publishDate, shares } = videoStore;

// 监听视频 ID 变化，当用户点击不同视频时自动更新
watch(() => route.params.id, async (newId) => {
    if (newId) {
        videoId.value = newId;
        await videoStore.fetchVideoData(videoId.value); // 根据新ID拉取视频数据
    }
});
</script>

<style scoped>
.video-info-container {
    width: auto;
    max-width: 720px;
    margin: 20px auto;
    padding: 20px;
    background-color: #f9f9f9;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.video-info-title {
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 15px;
    color: #333;
}

.video-info-meta {
    display: flex;
    justify-content: space-between;
    margin-bottom: 15px;
    font-size: 14px;
    color: #888;
}

.video-info-meta span {
    margin-right: 10px;
}

.video-player {
    width: 100%;
    height: auto;
    border-radius: 10px;
    margin-bottom: 20px;
}

.video-stats {
    display: flex;
    justify-content: space-around;
    margin-bottom: 20px;
    font-size: 16px;
    color: #666;
}

.video-desc-container {
    background-color: #ffffff;
    padding: 15px;
    border-radius: 10px;
    box-shadow: inset 0 2px 4px rgba(0, 0, 0, 0.05);
    font-size: 14px;
    color: #444;
    line-height: 1.6;
}

.video-tag-container {
    margin-top: 20px;
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
}

@media (max-width: 768px) {
    .video-info-meta {
        flex-direction: column;
        align-items: flex-start;
    }

    .video-stats {
        flex-direction: column;
        align-items: flex-start;
    }

    .video-tag-container {
        justify-content: flex-start;
    }
}


.el-collapse {
    border: 1px solid red;
    /* 给弹幕列表加个边框，方便调试 */
}

.danmaku-list {
    margin-top: 20px;
}

.danmaku-item {
    margin-bottom: 10px;
}

.meta {
    font-size: 12px;
    color: #999;
}
</style>