<!-- src/components/Comment.vue -->
<template>
    <div class="comment">
        <div class="comment-content">

        </div>
    </div>
</template>

<script setup>
import { useCommentStore } from '../store/comment';

const commentStore = useCommentStore();

// 删除评论功能
const deleteComment = (rid) => {
    commentStore.removeComment(rid);
};

// 传递的评论数据
defineProps({
    comment: {
        type: Object,
        required: true,
    },
    rid: {
        type: Number,
        required: true,
    }
});

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