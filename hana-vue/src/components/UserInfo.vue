<template>
    <div class="user-management-page">
        <h1>用户管理</h1>
        <el-descriptions title="你的信息" direction="vertical" border style="margin-top: 20px">
            <el-descriptions-item :rowspan="2" :width="140" label="头像" align="center">
                <el-image style="width: 100px; height: 100px" :src="userStore.userInfo.avatar" />
            </el-descriptions-item>
            <el-descriptions-item label="昵称">{{ userStore.userInfo.userName }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ userStore.userInfo.age }}</el-descriptions-item>
            <el-descriptions-item label="电话">{{ userStore.userInfo.phone }}</el-descriptions-item>
            <el-descriptions-item label="邮箱">{{ userStore.userInfo.email }}</el-descriptions-item>
        </el-descriptions>
        <el-form :model="userInfo" ref="userFormRef" label-width="100px" class="user-form">
            <el-form-item label="用户名" prop="name" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                <el-input v-model="userInfo.name" placeholder="请输入新用户名" />
            </el-form-item>
            <el-form-item label="年龄" prop="age" :rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
                <el-input v-model="userInfo.age" placeholder="请输入新年龄" />
            </el-form-item>
            <el-form-item label="电话" prop="phone" :rules="[{ required: true, message: '请输入电话', trigger: 'blur' }]">
                <el-input v-model="userInfo.phone" placeholder="请输入新电话" />
            </el-form-item>
            <el-form-item label="邮箱" prop="email" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' }]">
                <el-input v-model="userInfo.email" placeholder="请输入新邮箱" />
            </el-form-item>
            <el-button type="primary" @click="updateUserInfo">更新信息</el-button>
        </el-form>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useUserStore } from '../store/userStore';
import axios from 'axios';
import { useRoute } from 'vue-router';

const userStore = useUserStore();
const userInfo = ref({
    name: '',
    age: '',
    phone: '',
    email: ''
});

const route = useRoute();

onMounted(() => {
    userInfo.value = {
        name: userStore.userInfo.userName || '',
        age: userStore.userInfo.age || '',
        phone: userStore.userInfo.phone || '',
        email: userStore.userInfo.email || ''
    };
});

console.log(userStore.userInfo); // 查看 userStore 中的数据是否正确


const updateUserInfo = async () => {
    const userId = route.params.id;
    try {
        const response = await axios.put(`/users/${userId}`, userInfo.value);
        if (response.data.success) {
            alert('用户信息更新成功！');
        } else {
            alert(response.data.message);
        }
    } catch (error) {
        console.error('更新用户信息失败:', error);
        alert('更新失败，请重试。');
    }
};
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
