<template>
    <div class="user-management-page">
        <h1>用户管理</h1>
        <el-descriptions title="你的信息" direction="vertical" :column="3" border style="margin-top: 20px">
            <el-descriptions-item label="用户名">{{ userStore.userInfo.name }}</el-descriptions-item>
            <el-descriptions-item label="是否封禁">{{ userStore.userInfo.is_ban }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ userStore.userInfo.age }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ userStore.userInfo.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.userInfo.email }}</el-descriptions-item>
        </el-descriptions>
        <h2>修改你的信息</h2>
        <el-form :model="userStore.tempUserInfo" ref="userFormRef" label-width="100px" class="user-form">
            <el-form-item label="用户名" prop="name" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                <el-input v-model="userStore.tempUserInfo.name" placeholder="请输入新用户名" />
            </el-form-item>
            <el-form-item label="年龄" prop="age" :rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
                <el-input v-model="userStore.tempUserInfo.age" placeholder="请输入新年龄" />
            </el-form-item>
            <el-form-item label="电话" prop="phone" :rules="[{ required: true, message: '请输入电话', trigger: 'blur' }]">
                <el-input v-model="userStore.tempUserInfo.phone" placeholder="请输入新电话" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' }]">
                <el-input v-model="userStore.tempUserInfo.email" placeholder="请输入新邮箱" />
            </el-form-item>
            <el-button type="primary" @click="userStore.updateUserInfo">更新信息</el-button>
        </el-form>
    </div>
</template>

<script setup>
import { onMounted } from 'vue';

import { useUserStore } from '../store/userStore';

const userStore = useUserStore();


// 在组件挂载时调用 fetchUserInfo 获取用户信息
onMounted(() => {
    userStore.fetchUserInfo();
});
</script>

<style scoped>
.user-management-page {
    padding: 20px;
    max-width: 600px;
    margin: 0 auto;
}

.user-form {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    color: #333;
}
</style>
