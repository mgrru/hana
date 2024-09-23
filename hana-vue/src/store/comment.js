// src/store/comment.js
import { defineStore } from 'pinia';
import { fetchComments, sendComment, deleteComment } from '../api/comment.js';

export const useCommentStore = defineStore('comment', {
  state: () => ({
    comments: [],
    loading: false
  }),
  actions: {
    async loadComments() {
      this.loading = true;
      try {
        const response = await fetchComments();
        // 初始化评论
        this.comments = response.data.map(comment => ({
          ...comment,
          showReplies: false,
          showReplyInput: false,
          replies: comment.replies.map(reply => ({
            ...reply,
            showReplies: false,
            showReplyInput: false
          }))
        }));
      } catch (error) {
        console.error('获取评论失败', error);
      } finally {
        this.loading = false;
      }
    },
    // 发送评论
    async sendComment(content, rid) {
      try {
        const response = await sendComment(content, rid);
        if (response.success) {
          this.loadComments();  // 重新加载评论
        }
      } catch (error) {
        console.error('发送评论失败', error);
      }
    },

    // 删除评论
    async removeComment(commentId) {
      try {
        const response = await deleteComment(commentId);
        if (response.success) {
          this.loadComments();  // 重新加载评论
        }
      } catch (error) {
        console.error('删除评论失败', error);
      }
    },
    // 点赞
    toggleLike(commentId) {
      const comment = this.findCommentById(commentId);
      if (comment) {
        comment.liked = !comment.liked;
        comment.likes += comment.liked ? 1 : -1;
      }
    },
    // 显示/隐藏回复输入框
    showReplyInput(commentId) {
      const comment = this.findCommentById(commentId);
      if (comment) {
        comment.showReplyInput = !comment.showReplyInput;
      }
    },
    // 添加回复
    addReply(parentCommentId, content) {
      const parentComment = this.findCommentById(parentCommentId);
      if (parentComment) {
        const newReply = {
          id: Date.now().toString(),
          content,
          username: 'CurrentUser',
          createdAt: new Date().toLocaleString(),
          likes: 0,
          liked: false,
          replies: [],
          showReplies: false,
          showReplyInput: false
        };
        parentComment.replies.push(newReply);
        parentComment.showReplyInput = false;
      }
    },
    // 折叠/展开子评论
    toggleReplies(commentId) {
      const comment = this.findCommentById(commentId);
      if (comment) {
        comment.showReplies = !comment.showReplies;
      }
    },

    // 递归查找评论（通过 ID 查找评论或回复）
    findCommentById(commentId, comments = this.comments) {
      for (const comment of comments) {
        if (comment.id === commentId) return comment;
        const foundInReplies = this.findCommentById(commentId, comment.replies);
        if (foundInReplies) return foundInReplies;
      }
      return null;
    }
  }
});
