
import { defineStore } from 'pinia';

export const useDanmakuStore = defineStore('danmaku', {
  state: () => ({
    isPlaying: true,
    isDanmakuVisibility: true,
    danmakuList: [] , // 弹幕列表
    danmakuCount: 0   // 弹幕数量
  }),
  actions: {
    // 切换播放状态
    togglePlay() {
      this.isPlaying = !this.isPlaying;
    },
    // 切换弹幕可见性
    toggleDanmakuVisibility() {
      this.isDanmakuVisibility = !this.isDanmakuVisibility;
    },
    // 添加新弹幕
    addDanmaku(danmaku) {
      this.danmakuList.push(danmaku);
      this.danmakuCount = this.danmakuList.length;// 每次添加弹幕后更新弹幕数
    },
    // 清空弹幕列表
    clearDanmaku() {
      this.danmakuList = [];
      this.danmakuCount = 0;
    }
  }
});
