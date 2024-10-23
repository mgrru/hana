<!-- VideoPlayer.vue -->
<template>
    <div class="video-info-container">
        <h1 class="video-info-title">{{ animesInfo.title }}</h1>
        <div class="video-info-meta">
            <span>播放量: {{ animesInfo.views }}</span>
        </div>
        <div style="position: relative; width: 100%;">
            <!-- 视频播放器 -->
            <video ref="animeRef" width="100%" controls @play="handlePlay" @pause="handlePause">
                <source :src="url" type="video/mp4" />
            </video>
            <!-- 弹幕组件，使用自定义弹幕结构 -->
            <vue-danmaku ref="danmakuRef" v-model:danmus="danmakuList" loop :channels="5" useSlot
                style="position: absolute; top: 0; left: 0; width: 100%; height: 100%; pointer-events: none;"
                :pause="!isPlaying">
                <template v-slot:dm="{ danmu }">
                    <span>{{ danmu.user_name }}:{{ danmu.content }}</span>
                </template>
            </vue-danmaku>
        </div>
        <div>
            <!-- 控制按钮和弹幕输入 -->
            <el-button @click="togglePlay">{{ isPlaying ? '暂停' : '播放' }}</el-button>
            <el-button @click="toggleDanmakuVisibility">{{ isDanmakuVisible ? '关闭弹幕' : '显示弹幕' }}</el-button>
            <!-- <el-popover placement="bottom" title="Title" :width="200" trigger="click">
                <p>弹幕速度</p>
                <p>弹幕</p>
                <template #reference>
                    <el-button class="m-2">弹幕设置</el-button>
                </template>
            </el-popover> -->
            <el-input v-model="input" placeholder="发送弹幕" :disabled="!isDanmakuVisible">
                <template #append>
                    <el-button @click="sendDanmaku" type="primary" :disabled="!isDanmakuVisible">发送</el-button>
                </template>
            </el-input>
        </div>
        <!-- 点赞组件 -->
        <div class="video-stats">
            <ToolBar :rid="props.rid" />
        </div>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAnimeStore } from '../store/animeStore';
import { useRoute } from 'vue-router';
import { ref } from 'vue';
import { watch } from 'vue';
import { computed } from 'vue';
import { useDanmakuStore } from '../store/danmaku';
import vueDanmaku from 'vue3-danmaku'
import { storeToRefs } from 'pinia';
import ToolBar from './ToolBar.vue';


const props = defineProps({
    name: String,
    rid: Number,
});

const route = useRoute();
const danmakuStore = useDanmakuStore();
const animeStore = useAnimeStore();
const animeRef = ref(null);

const input = ref('');
const { danmakuList } = storeToRefs(danmakuStore)
const danmakuRef = ref(null);
const isPlaying = computed(() => danmakuStore.isPlaying);
const isDanmakuVisible = computed(() => danmakuStore.isDanmakuVisibility);
const { animesInfo } = storeToRefs(animeStore);
const url = computed(() => animeStore.animesInfo.url);

//弹幕相关
// 控制播放按钮
const togglePlay = () => {
    if (isPlaying.value) {
        animeRef.value.pause(); // 暂停视频
        danmakuRef.value.pause(); // 暂停弹幕
    } else {
        // 尝试播放视频，并处理可能的 NotAllowedError
        animeRef.value.play().catch(error => {
            if (error.name === 'NotAllowedError') {
                console.error('视频无法自动播放。请单击播放按钮。');
            } else {
                console.error('播放失败：', error);
            }
        });
        danmakuRef.value.play(); // 播放弹幕
    }
    danmakuStore.togglePlay(); // 更新 Pinia 状态
};
// 显示/隐藏弹幕
const toggleDanmakuVisibility = () => {
    danmakuStore.toggleDanmakuVisibility();
    if (danmakuRef.value) {
        isDanmakuVisible.value ? danmakuRef.value.show() : danmakuRef.value.hide();
    }
};
// 发送弹幕
const sendDanmaku = () => {
    if (input.value.trim()) {
        const newDanmaku = {
            rid: props.rid,
            content: input.value,
        };
        console.log('发送的弹幕:', newDanmaku);  // 打印发送的弹幕
        danmakuStore.addDanmaku(newDanmaku); // 将弹幕添加到 Pinia store
        input.value = '';
    }
};
// 监听视频播放和暂停事件，立即同步状态
const handlePlay = () => {
    danmakuStore.isPlaying = true;  // 更新 Pinia 中的状态
    danmakuRef.value.play(); // 弹幕开始播放
};
const handlePause = () => {
    danmakuStore.isPlaying = false;  // 更新 Pinia 中的状态
    danmakuRef.value.pause(); // 弹幕暂停
};


onMounted(async () => {
    // console.log(`当前rid${props.rid}`)
    if (props.name) {  // 确保 props.name 存在
        // console.log(`Video的rid${rid}`)
        // await animeStore.fetchAnimeData(props.name);
        // await danmakuStore.fetchDanmakuData(rid);
        if (animesInfo.value && animesInfo.value.url) {
            animeRef.value.load(); // 在 URL 确定后加载视频
            animeRef.value.play(); // 自动播放视频
        } else {
            console.error('视频信息未加载或无效');
        }
    }
});

// 监听播放状态变化，控制弹幕同步
watch(isPlaying, (newVal) => {
    if (animeRef.value && danmakuRef.value) {
        if (newVal) {
            animeRef.value.play();
            danmakuRef.value.play();  // 确保弹幕同步播放
        } else {
            animeRef.value.pause();
            danmakuRef.value.pause();  // 确保弹幕同步暂停
        }
    }
    console.log('弹幕列表', newVal); // 确保数据正确
});

watch([() => route.params.name, () => url.value], async ([newName, newUrl]) => {
    if (animeRef.value) {
        animeRef.value.src = '';  // 先清空当前的播放源
    }
    if (newName) {
        await animeStore.fetchAnimeData(newName);
    }
    if (animesInfo.value && animesInfo.value.url) {
        animeRef.value.src = animesInfo.value.url;  // 设置新的视频播放链接
        animeRef.value.load();  // 强制重新加载视频
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