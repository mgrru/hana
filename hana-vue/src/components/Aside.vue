<template>
    <!-- 侧边工具栏 -->
    <div class="aside-right">
        <div class="buttons-container">
            <el-button icon="Refresh" class="refresh-button" @click="refreshVideoList">换一换</el-button>

            <el-dropdown placement="top-start" class="more-button">
                <el-button>
                    <el-icon>
                        <MoreFilled />
                    </el-icon>
                </el-button>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item>
                            <el-button>
                                <el-icon>
                                    <QuestionFilled />
                                </el-icon>
                                官方客服
                            </el-button>
                        </el-dropdown-item>
                        <el-dropdown-item>
                            <el-button>反馈</el-button>
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>

            <el-button icon="ArrowUp" class="back-to-top-button" @click="scrollToTop">回到顶部</el-button>
        </div>
    </div>
</template>

<script setup>
import { useVideoStore } from '../store/videoStore';
const videoStore = useVideoStore();

const refreshVideoList = async () => {
    await videoStore.fetchVideoList();
};

const scrollToTop = () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth', // 平滑滚动
    });
};

</script>

<style lang="scss" scoped>
.aside-right {
    width: 70px;
    background-color: #c2df80;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
    padding: 20px 10px;
    border-radius: 15px;
    box-shadow: 0 6px 12px rgba(0, 0, 0, 0.1);
    position: fixed;
    top: 20%;
    right: 20px;
    z-index: 1000;
    transition: box-shadow 0.3s, transform 0.3s;
}

.aside-right:hover {
    box-shadow: 0 10px 20px rgba(0, 0, 0, 0.15);
    transform: translateY(-5px);
}

.buttons-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 20px;
}

.refresh-button,
.back-to-top-button,
.more-button {
    width: 68px;
    height: 68px;
    display: flex;
    flex-direction: column;
    gap: 5px;
    justify-content: center;
    align-items: center;
    border-radius: 50%;
    background-color: #409eff;
    color: #fff;
    transition: background-color 0.3s, transform 0.3s;
}

.refresh-button:hover,
.back-to-top-button:hover,
.more-button:hover {
    background-color: #66b1ff;
    transform: translateY(-3px);
}

.el-button {
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.el-button:active {
    box-shadow: none;
    transform: translateY(2px);
}

.el-icon {
    font-size: 24px; // 调整图标的大小
}

.refresh-button span,
.back-to-top-button span {
    font-size: 12px; // 调整文字的大小
}
</style>