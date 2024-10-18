// stores/announcements.js
import { defineStore } from "pinia";
import { ref } from "vue";
import axios from "../utils/axios";

export const useAnnouncementsStore = defineStore("announcements", () => {
  const announcements = ref([]);
  const isLoading = ref(false);
  const error = ref(null);

  const fetchAnnouncements = async () => {
    isLoading.value = true;
    try {
      const response = await axios.get("/announcements");
      announcements.value = JSON.parse(response.data);
      console.log("公告响应：" + response.data);
    } catch (err) {
      error.value = err.message;
    } finally {
      isLoading.value = false;
    }
  };

  return { announcements, isLoading, error, fetchAnnouncements };
});
