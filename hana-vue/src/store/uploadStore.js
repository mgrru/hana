import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";
import { ElMessage } from "element-plus";

export const useUploadStore = defineStore("uploadStore", () => {
  // 视频列表
  const animes = ref([]);

  // 上传状态
  const uploadStatus = ref(false);

  // 获取用户稿件数据
  const fetchAnimes = async () => {
    try {
      const response = await axios.get("/users/animes");
      animes.value = JSON.parse(response.data);
    } catch (error) {
      ElMessage.error("获取视频列表失败");
    }
  };

  // 提交上传表单
  const submitForm = async (uploadForm) => {
    const formData = new FormData();
    formData.append("title", uploadForm.title);
    formData.append("type", uploadForm.type);
    formData.append("name", uploadForm.name);
    formData.append("episode", uploadForm.episode);
    formData.append("episode_name", uploadForm.episode_name);
    formData.append("sid", uploadForm.sid);
    formData.append("resources", uploadForm.videoFile);
    formData.append("cover", uploadForm.coverFile);

    try {
      await axios.post("http://localhost:9901/upload", formData, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      ElMessage.success("提交成功");
      uploadStatus.value = true; // 标记为上传成功
    } catch (error) {
      ElMessage.error("上传失败，请稍后重试");
      uploadStatus.value = false; // 上传失败
    }
  };

  return {
    animes,
    fetchAnimes,
    submitForm,
    uploadStatus,
  };
});
