<template>
    <el-popover placement="bottom" :width="400" trigger="hover">
        <template #reference>
            <span @click="navigateToLogin">
                <el-avatar :src="authStore.isLoggedIn ? '' : ''" style="background-color: lightskyblue;">
                    {{ authStore.isLoggedIn ? '帅哥' : '登录' }}
                </el-avatar>
            </span>
        </template>

        <template v-if="authStore.isLoggedIn">
            <div class="user-popover">
                <strong>{{ userStore.userInfo.name }}</strong>
                <el-button type="primary" @click="navigateToUserInfo">用户管理</el-button>
                <el-button type="primary" @click="handleLogout">退出登录</el-button>
            </div>
        </template>

        <template v-else>
            <div class="login-popover">
                <el-button class="login-button" @click="navigateToLogin">立即登录</el-button>
            </div>
        </template>
    </el-popover>
</template>

<script setup>
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/userStore';
import { useAuthStore } from '../store/auth';
import { ref, watch } from 'vue';

const authStore = useAuthStore();
const userStore = useUserStore();
const router = useRouter();

const navigateToLogin = () => {
    router.push({ name: 'Login' }); // 替换为你的登录页面路由名称
};
const navigateToUserInfo = () => {
    router.push({ name: 'UserInfo' }); // 替换为你的登录页面路由名称
};

const handleLogout = () => {
    console.log('Logout function triggered');
    authStore.clearToken();
    alert('退出登录成功！');
};
</script>

<style scoped>
.user-popover {
    padding: 15px;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    text-align: center;
}

.user-popover p {
    font-size: 18px;
    font-weight: bold;
    color: #333;
    margin-bottom: 10px;
}

.user-popover button {
    margin-top: 5px;
    width: 100%;
}
</style>