<template>
    <div class="login-container">
        <h2>登录</h2>
        <form @submit.prevent="login">
            <div>
                <label for="username">用户名</label>
                <input v-model="username" id="username" type="text" required />
            </div>
            <div>
                <label for="password">密码</label>
                <input v-model="password" id="password" type="password" required />
            </div>
            <button type="submit">登录</button>
        </form>
        <p v-if="error">{{ error }}</p>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import axios from '../utils/axios';
import { useAuthStore } from '../store/auth';

const username = ref('');
const password = ref('');
const error = ref('');
const authStore = useAuthStore();
const router = useRouter();

const login = async () => {
    try {
        const response = await axios.post('/login', {
            username: username.value,
            password: password.value,
        });
        const token = response.data.token;

        authStore.setToken(token); // 保存token

        // 跳转到首页或其他页面
        router.push('/');
    } catch (err) {
        error.value = '登录失败，请检查用户名和密码';
    }
};
</script>

<style scoped>
.login-container {
    width: 300px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    border-radius: 10px;
}
</style>