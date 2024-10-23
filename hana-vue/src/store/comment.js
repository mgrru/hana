import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { useUserStore } from "./userStore"; // 导入用户状态管理
import { ElMessage, ElMessageBox } from "element-plus"; // 引入 ElMessage 和 ElMessageBox

export const useCommentStore = defineStore("comment", () => {
  const comments = ref([]);
  const loading = ref(false);
  const userStore = useUserStore(); // 获取用户 store

  // 获取评论列表
  const loadComments = async (rid) => {
    loading.value = true;
    const response = await axios.get(`/comments/${rid}`);
    try {
      // 处理接口数据结构
      comments.value = JSON.parse(response.data);
    } catch (error) {
      console.error(
        "获取评论失败",
        error.response ? error.response.data : error.message
      );
    } finally {
      loading.value = false;
    }
  };

  const sendCommentAction = async (contentData) => {
    try {
      const response = await axios.post(`/comments`, contentData);
      console.log("发送评论响应:", response.data); // 调试信息

      // 如果后端返回错误状态码，手动抛出错误
      if (response.status !== 200) {
        throw new Error(response.data.message || "发送评论失败");
      }

      // 调用 loadComments 重新加载评论，并传递 rid
      await loadComments(contentData.rid);

      return response.data;
    } catch (error) {
      // 这里抛出错误，以便在 submitComment 中捕获
      throw new Error(
        error.response ? error.response.data.message : error.message
      );
    }
  };

  const removeComment = async (id, rid) => {
    try {
      const response = await axios.delete(`/comments/${id}`);
      console.log("删除评论响应:", response); // 调试信息

      // 检查状态码
      if (response.status === 200) {
        await loadComments(rid); // 删除成功后重新加载评论
        return true; // 返回删除成功的标志
      } else {
        throw new Error(response.data.message || "删除评论失败"); // 后端返回非成功的状态
      }
    } catch (error) {
      throw new Error(
        error.response ? error.response.data.message : error.message
      ); // 抛出错误让调用方处理
    }
  };

  return {
    comments,
    loading,
    loadComments,
    sendCommentAction,
    removeComment,
  };
});
