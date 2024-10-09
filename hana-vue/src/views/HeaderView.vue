<template>
    <div class="header">
        <div class="header-bar">
            <div class="left-entry">
                <ul>
                    <li><a href="/">首页</a></li>
                    <li><router-link to="/video">番剧</router-link></li>
                    <li><a href="#">漫画</a></li>
                    <li><router-link to="/video">视频</router-link></li>
                </ul>
            </div>
            <div class="center-search-container">
                <!-- 搜索框组件 -->
                <Search></Search>
            </div>
            <div class="right-entry">
                <ul>
                    <!-- 登录成功显示用户的头像 -->
                    <!-- <li v-if="userStore.isLoggedIn">
                        <el-avatar src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    </li> -->
                    <!-- 没有登录显示登录按钮 -->
                    <li class="login-avatar">
                        <Login></Login>
                    </li>
                    <li class="icon-above-text">
                        <router-link to="/favorite">收藏</router-link>
                    </li>
                    <li>
                        <router-link to="/history">历史</router-link>
                    </li>
                    <li><router-link to="/">用户</router-link></li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import Login from "../components/Login.vue";
import Search from "../components/Search.vue";
import { useUserStore } from '../store/userStore';


const userStore = useUserStore(); // 访问登录状态

</script>

<style scoped>
.header {
    width: 100%;
    background: #ffffff;
    z-index: 1;
    align-items: center;
}

.header-bar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    background-color: #f9f9f9;
    border-bottom: 1px solid #ebebeb;
    position: -webkit-sticky;
    position: sticky;
    top: 0px;
}

.left-entry ul,
.right-entry ul {
    display: flex;
    list-style: none;
    padding: 0;
    margin: 0;
    gap: 20px;
}

.left-entry li,
.right-entry li {
    position: relative;
}

.left-entry li a,
.right-entry li a {
    text-decoration: none;
    color: #333;
    font-size: 16px;
    font-weight: bold;
    padding: 5px 10px;
    transition: color 0.3s, background-color 0.3s;
}

.left-entry li a:hover,
.right-entry li a:hover {
    color: #409eff;
    background-color: #f0f8ff;
    border-radius: 5px;
}

.center-search-container {
    display: flex;
    align-items: center;
    border-radius: 4px;
    background-color: #fff;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    padding: 0 10px;
    transition: box-shadow 0.3s;
}

/* 响应式布局 */
@media (max-width: 768px) {
    .right-entry ul {
        flex-direction: column;
        align-items: flex-start;
        gap: 10px;
    }

    .right-entry li {
        flex-direction: row;
        gap: 10px;
    }

    .right-entry .el-avatar {
        width: 40px;
        height: 40px;
    }

    .right-entry a {
        font-size: 14px;
    }
}
</style>
