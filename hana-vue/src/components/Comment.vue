<!-- src/components/Comment.vue -->
<template>
    <div class="comment">
        <div class="comment-content">
            <p>{{ comment.username }}: {{ comment.content }}</p>
            <p class="meta">
                <span class="time">{{ comment.createdAt }}</span>
                <span class="likes" @click="toggleLike(comment.id)">
                    {{ comment.likes }} {{ comment.liked ? '取消点赞' : '点赞' }}
                </span>
                <span class="reply" @click="showReplyInput(comment.id)">回复</span>

                <!-- 只有评论人自己或视频发布者能删除评论 -->
                <button v-if="comment.isOwner || comment.isPublisher" class="delete"
                    @click="deleteComment(comment.id)">删除</button>
            </p>
        </div>

        <!-- 回复输入框 -->
        <div v-if="comment.showReplyInput" class="reply-input">
            <input v-model="replyContent" placeholder="回复..." @keyup.enter="submitReply(comment.id)" />
        </div>

        <!-- 子评论折叠按钮 -->
        <div class="replies" v-if="comment.replies.length">
            <p class="toggle-replies" @click="toggleReplies(comment.id)">
                {{ comment.showReplies ? '收起回复' : '查看回复 (' + calculateTotalReplies(comment) + ')' }}
            </p>
        </div>

        <!-- 递归渲染子评论 -->
        <div v-if="comment.showReplies" class="nested-replies">
            <Comment v-for="reply in comment.replies" :key="reply.id" :comment="reply" @reply="submitReply"
                @toggle-like="toggleLike" />
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useCommentStore } from '../store/comment';
import Comment from '../components/Comment.vue';

const commentStore = useCommentStore();
const replyContent = ref('');

// 点赞功能
const toggleLike = (commentId) => {
    commentStore.toggleLike(commentId);
};

// 显示/隐藏回复输入框
const showReplyInput = (commentId) => {
    commentStore.showReplyInput(commentId);
};

// 提交回复功能
const submitReply = (commentId, parentCommentId = null) => {
    if (replyContent.value.trim()) {
        commentStore.addReply(parentCommentId || commentId, replyContent.value);
        replyContent.value = '';
    }
};

// 折叠/展开子评论
const toggleReplies = (commentId) => {
    commentStore.toggleReplies(commentId);
};

// 删除评论功能
const deleteComment = (commentId) => {
    commentStore.removeComment(commentId);
};

// 传递的评论数据
defineProps({
    comment: {
        type: Object,
        required: true,

    },
});

// 注册子组件用于递归渲染
defineExpose({
    submitReply,
    toggleLike,
    deleteComment,
});

// 递归计算所有评论及其子评论的总数量
const calculateTotalReplies = (comment) => {
    let total = comment.replies ? comment.replies.length : 0;
    if (comment.replies) {
        for (const reply of comment.replies) {
            total += calculateTotalReplies(reply);
        }
    }
    return total;
};

</script>

<style scoped>
.comment {
    margin-bottom: 20px;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.comment-content {
    padding: 5px;
}

.meta {
    display: flex;
    justify-content: space-between;
    font-size: 0.85em;
    color: #555;
}

.likes,
.reply {
    cursor: pointer;
    color: #42b983;
}

.reply-input input {
    width: 100%;
    padding: 5px;
    margin-top: 5px;
    border: 1px solid #ddd;
    border-radius: 3px;
}

.nested-replies {
    margin-left: 20px;
    margin-top: 10px;
}

.toggle-replies {
    cursor: pointer;
    color: #42b983;
    font-size: 0.9em;
}
</style>