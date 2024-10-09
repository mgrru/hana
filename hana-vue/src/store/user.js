// 引入 Pinia 的 `defineStore` 函数，用于定义一个 Pinia store。
import { defineStore } from 'pinia';

// 定义一个名为 `useUserInfoStore` 的 Pinia store。
// 这个函数返回一个 store 实例，可以在组件中使用。
export const useUserInfoStore = defineStore('userInfo', {
  // 定义 store 的状态（state）
  state: () => ({

    // 存储点赞状态
    likedComments: {}
  }),

  // 定义 store 的动作（actions）
  actions: {
    // `setUserInfo` 方法用于更新 `userInfo` 状态。
    setUserInfo(userInfo) {
      // 更新 `userInfo` 状态
      this.userInfo = userInfo;
    },
    
    // `toggleLike` 方法用于更新点赞状态
    toggleLike(commentId) {
      // 切换点赞状态
      if (this.likedComments[commentId]) {
        delete this.likedComments[commentId];
      } else {
        this.likedComments[commentId] = true;
      }
    }
  },


  // 定义 store 的 getter（获取器）
  getters: {
    getLikedComments() {
      return this.likedComments;
    }
  }
});

export default useUserInfoStore;