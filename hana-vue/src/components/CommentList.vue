<template>
    <div class="comment-list">
        <h2>评论区</h2>
        <!-- 发布评论区 -->
        <div class="reply-input">
            <el-input v-model="sendContent" placeholder="发布评论" @keyup.enter="submitComment()">
                <template #append>
                    <el-button type="primary" @click="submitComment()" :loading="loading">发布</el-button>
                </template>
            </el-input>
        </div>
        <!-- 评论列表部分 -->
        <div v-loading="commentStore.loading">
            <div v-for="comment in visibleComments" :key="comment.id" class="comment-item">
                <p>{{ comment.user_name }}: {{ comment.content }}</p>
                <p class="meta">
                    <span class="time">{{ new Date(comment.time).toLocaleString() }}</span>
                    <button class="delete" @click="deleteComment(comment.id, props.rid)">删除</button>
                </p>
            </div>
        </div>
        <!-- 显示遮罩层，如果未登录并且评论数量超过限制 -->
        <div v-if="!authStore.isLoggedIn && comments.length > maxVisibleComments" class="mask">
            <button class="login-btn" @click="commentToLogin">登录以查看更多评论</button>
        </div>
        <!-- 显示没有评论的提示 -->
        <p v-if="comments.length === 0 && !commentStore.loading">还没有评论，快来抢沙发吧！</p>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue';
import { useCommentStore } from '../store/comment';
import { useAuthStore } from '../store/auth';
import { useRouter } from 'vue-router';
import { ElMessage, ElMessageBox } from "element-plus"; // 引入 Element Plus 消息组件

const commentStore = useCommentStore();
const authStore = useAuthStore();
const router = useRouter();

const comments = computed(() => commentStore.comments);
const sendContent = ref('');
const maxVisibleComments = 3;
const loading = ref(false); // 加载状态

const props = defineProps({
    rid: Number,
});

const visibleComments = computed(() => {
    return authStore.isLoggedIn || comments.value.length <= maxVisibleComments
        ? comments.value
        : comments.value.slice(0, maxVisibleComments);
});

const submitComment = async () => {
    // 检查评论内容是否为空
    if (!sendContent.value.trim()) {
        ElMessage({
            message: '评论内容不能为空',
            type: 'warning',
            duration: 2000,
        });
        return;
    }

    const contentData = {
        rid: props.rid,
        content: sendContent.value,
    };

    try {
        // 禁用提交按钮，防止重复提交
        loading.value = true;

        // 调用发送评论的操作，并传递 rid
        await commentStore.sendCommentAction(contentData);

        // 清空输入框
        sendContent.value = '';

        // 提示评论提交成功
        ElMessage({
            message: '评论提交成功',
            type: 'success',
            duration: 2000,
        });

    } catch (error) {
        // 提示评论提交失败
        ElMessage({
            message: `评论提交失败: ${error.message || '未知错误'}`,
            type: 'error',
            duration: 3000,
        });
    } finally {
        // 恢复提交按钮
        loading.value = false;
    }
};


const commentToLogin = () => {
    router.push({ name: 'Login' });
};

const deleteComment = (commentId, rid) => {
    ElMessageBox.confirm(
        '此操作将永久删除该评论, 是否继续?',
        '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
    }
    )
        .then(async () => {
            // 确认后执行删除操作
            await commentStore.removeComment(commentId, rid);

            // 删除成功提示
            ElMessage({
                message: '评论删除成功',
                type: 'success',
                duration: 2000,
            });
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

// 使用 watch 监听 rid 的变化
watch(() => props.rid, (newRid) => {
    if (newRid) {
        loadComments(newRid);
    }
});

// 定义加载评论的函数
const loadComments = async (rid) => {
    if (loading.value) return; // 如果正在加载则返回
    loading.value = true;
    try {
        await commentStore.loadComments(rid); // 调用加载评论的 action
    } catch (error) {
        console.error("加载评论失败：", error);
    } finally {
        loading.value = false;
    }
};

// 在 mounted 钩子中初始化评论
onMounted(() => {
    if (props.rid) {
        loadComments(props.rid);
    }
});
</script>


<style scoped>
.comment-list {
    position: relative;
    padding: 16px;
    border: 1px solid #eaeaea;
    border-radius: 8px;
    background-color: #f9f9f9;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.reply-input {
    display: flex;
    margin-bottom: 16px;
}

.reply-input input {
    flex: 1;
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 4px;
    outline: none;
    transition: border-color 0.3s;
}

.reply-input input:focus {
    border-color: #42b983;
}

.comment-item {
    padding: 12px;
    border-bottom: 1px solid #eaeaea;
}

.comment-item:last-child {
    border-bottom: none;
}

.meta {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 8px;
}

.time {
    font-size: 12px;
    color: #888;
}

.delete {
    background: none;
    border: none;
    color: #ff4d4f;
    cursor: pointer;
    font-size: 14px;
    transition: color 0.3s;
}

.delete:hover {
    text-decoration: underline;
    color: #c0392b;
}

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
    padding: 8px 16px;
    background-color: #42b983;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.login-btn:hover {
    background-color: #36a373;
}
</style>
