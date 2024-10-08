<template>
    <div class="comment-list">
        <h2>评论区</h2>

        <!-- 加载状态 -->
        <div v-if="commentStore.loading">加载中...</div>

        <!-- 评论列表部分 -->
        <div v-else>
            <div v-for="comment in visibleComments" :key="comment.id" class="comment-item">
                <Comment :comment="comment" :rid="rid" />
            </div>
        </div>

        <!-- 显示遮罩层，如果未登录并且评论数量超过限制 -->
        <div v-if="!userStore.isLoggedIn && comments.length > maxVisibleComments" class="mask">
            <button class="login-btn" @click="redirectToLogin">登录以查看更多评论</button>
        </div>

        <!-- 显示没有评论的提示 -->
        <p v-if="comments.length === 0 && !commentStore.loading">还没有评论，快来抢沙发吧！</p>
    </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import { useCommentStore } from '../store/comment';
import { useUserStore } from '../store/userStore';
import Comment from './Comment.vue';
import { useRoute, useRouter } from 'vue-router';


const commentStore = useCommentStore();
const userStore = useUserStore();
const route = useRoute();
const router = useRouter();

// 限制最多显示的评论数量，未登录时限制为3条
const maxVisibleComments = 3;

// 获取视频 ID (rid)
const rid = ref(null);

// 动态获取视频 ID
watchEffect(async () => {
    rid.value = route.params.id;
    console.log("当前 rid:", rid.value);
    if (rid.value) {
        await commentStore.loadComments(rid.value);
        console.log('获取到的评论数据:', comments.value); // 调试信息
    } else {
        console.error('缺少视频 ID (rid)');
    }
});

// 获取评论列表
const comments = computed(() => commentStore.comments);

// 计算可见的评论，未登录状态下最多显示 `maxVisibleComments` 条
const visibleComments = computed(() => {
    return userStore.isLoggedIn || comments.value.length <= maxVisibleComments
        ? comments.value
        : comments.value.slice(0, maxVisibleComments);
});


// 获取评论数据
// onMounted(async () => {
//     if (rid.value) {
//         await commentStore.loadComments(rid.value);
//         console.log('获取到的评论数据:', comments.value); // 调试信息
//     } else {
//         console.error('缺少视频 ID (rid)');
//     }
// });

// 跳转到登录页
const redirectToLogin = () => {
    router.push({ name: 'Login' });  // 确保你的路由中定义了 Login 页面
};

</script>

<style scoped>
.comment-list {
    position: relative;
}

/* 遮罩层渐变效果 */
.mask {
    position: absolute;
    bottom: 0;
    left: 0;
    right: 0;
    height: 100px;
    background: linear-gradient(to top, white, rgba(255, 255, 255, 0));
    display: flex;
    justify-content: center;
    align-items: center;
}

.login-btn {
    position: relative;
    z-index: 2;
    background-color: #42b983;
    color: white;
    border: none;
    padding: 8px 16px;
    border-radius: 4px;
    cursor: pointer;
}
</style>