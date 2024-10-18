import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "axios";

export const useCommentStore = defineStore("comment", () => {
  const comments = ref([]);
  const loading = ref(false);

  // 获取评论列表
  const loadComments = async (rid) => {
    loading.value = true;
    const response = await axios.get(`/comments/${rid}`);
    console.log("API响应:", response.data); // 调试信息
    try {
      // const response = await axios.get(`/comments/${rid}`);
      // const response = await axios.get(`/comments/${rid}`);
      // console.log("API响应:", response.data); // 调试信息
      // comments.value = response.data.data.map((comment) => ({
      //   ...comment,
      //   showReplies: false,
      //   showReplyInput: false,
      //   replies: comment.replies.map((reply) => ({
      //     ...reply,
      //     showReplies: true,
      //     showReplyInput: false,
      //   })),
      // }));
      // 处理接口数据结构
      comments.value = JSON.parse(response.data).map((comment) => ({
        id: comment.id,
        username: comment.user_name, // 接口中的 user_name
        content: comment.content,
        time: comment.time, // 接口中的 time
        // likes: 0, // 后端未返回点赞数，前端默认 0
        // liked: false,
        // showReplies: false,
        // showReplyInput: false,
        // replies: [], // 假设后端暂未提供子评论，初始化为空
      }));
    } catch (error) {
      console.error(
        "获取评论失败",
        error.response ? error.response.data : error.message
      );
    } finally {
      loading.value = false;
    }
  };

  // 发送评论
  // const sendCommentAction = async (content, rid) => {
  //   try {
  //     const response = await axios.post("/comments", { content, rid });
  //     console.log("发送评论响应:", response.data); // 调试信息
  //     if (response.data.success) {
  //       await loadComments(rid);
  //     } else {
  //       console.error("发送评论失败:", response.data.message);
  //     }
  //   } catch (error) {
  //     console.error(
  //       "发送评论失败",
  //       error.response ? error.response.data : error.message
  //     );
  //   }
  // };
  const sendCommentAction = async (content, rid) => {
    try {
      const response = await axios.post(`/comments/${rid}`, { content });
      console.log("发送评论响应:", response.data); // 调试信息
      await loadComments(rid); // 重新加载评论
      console.error("发送评论失败:", response.data.message);
    } catch (error) {
      console.error(
        "发送评论失败",
        error.response ? error.response.data : error.message
      );
    }
  };

  // 删除评论
  // const removeComment = async (commentId, rid) => {
  //   try {
  //     const response = await axios.delete(`/comments/${commentId}`);
  //     console.log("删除评论响应:", response.data); // 调试信息
  //     if (response.data.success) {
  //       await loadComments(rid);
  //     } else {
  //       console.error("删除评论失败:", response.data.message);
  //     }
  //   } catch (error) {
  //     console.error(
  //       "删除评论失败",
  //       error.response ? error.response.data : error.message
  //     );
  //   }
  // };
  // 有子评论的功能
  // const removeComment = async (commentId, rid) => {
  //   try {
  //     const response = await axios.delete(`/comments/${commentId}`);
  //     console.log("删除评论响应:", response.data); // 调试信息
  //     if (response.data.success) {
  //       await loadComments(rid); // 重新加载评论
  //     } else {
  //       console.error("删除评论失败:", response.data.message);
  //     }
  //   } catch (error) {
  //     console.error(
  //       "删除评论失败",
  //       error.response ? error.response.data : error.message
  //     );
  //   }
  // };
  const removeComment = async (rid) => {
    try {
      const response = await axios.delete(`/comments/${rid}`);
      console.log("删除评论响应:", response.data); // 调试信息
      if (response.data.success) {
        await loadComments(rid); // 重新加载评论
      } else {
        console.error("删除评论失败:", response.data.message);
      }
    } catch (error) {
      console.error(
        "删除评论失败",
        error.response ? error.response.data : error.message
      );
    }
  };

  // 点赞/取消点赞
  // const toggleLike = (commentId) => {
  //   const comment = findCommentById(commentId);
  //   if (comment) {
  //     comment.liked = !comment.liked;
  //     comment.likes += comment.liked ? 1 : -1;
  //   }
  // };

  // 显示/隐藏回复输入框
  const showReplyInput = (commentId) => {
    const comment = findCommentById(commentId);
    if (comment) {
      comment.showReplyInput = !comment.showReplyInput;
    }
  };

  // 添加回复
  const addReply = (parentCommentId, content) => {
    const parentComment = findCommentById(parentCommentId);
    if (parentComment) {
      const newReply = {
        id: Date.now().toString(),
        content,
        username: "CurrentUser",
        createdAt: new Date().toLocaleString(),
        likes: 0,
        liked: false,
        replies: [],
        showReplies: false,
        showReplyInput: false,
      };
      parentComment.replies.push(newReply);
      parentComment.showReplyInput = false;
    }
  };

  // 显示/隐藏子回复
  const toggleReplies = (commentId) => {
    const comment = findCommentById(commentId);
    if (comment) {
      comment.showReplies = !comment.showReplies;
    }
  };

  // 根据ID查找评论
  const findCommentById = (commentId, commentList = comments.value) => {
    for (const comment of commentList) {
      if (comment.id === commentId) return comment;
      const foundInReplies = findCommentById(commentId, comment.replies);
      if (foundInReplies) return foundInReplies;
    }
    return null;
  };

  return {
    comments,
    loading,
    loadComments,
    sendComment: sendCommentAction,
    removeComment,
    toggleLike,
    showReplyInput,
    addReply,
    toggleReplies,
    findCommentById,
  };
});
