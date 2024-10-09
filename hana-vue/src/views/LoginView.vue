<template>
    <HeaderView />
    <div class="login-register-page">
        <div class="login-register-container">
            <h1>{{ isLogin ? '登录' : '注册' }}</h1>

            <!-- 登录表单 -->
            <el-form v-if="isLogin" :model="loginForm" ref="loginFormRef" label-width="auto" class="demo-ruleForm"
                status-icon>
                <el-form-item label="账号" prop="account"
                    :rules="[{ required: true, message: '请输入账号', trigger: 'blur' }]">
                    <el-input v-model="loginForm.account" placeholder="请输入账号" />
                </el-form-item>
                <el-form-item label="密码" prop="pass" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
                    <el-input v-model="loginForm.pass" placeholder="请输入密码" show-password />
                </el-form-item>
                <div class="login-button-group">
                    <el-button class="custom-button" @click="toggleForm">去注册</el-button>
                    <el-button type="primary" class="custom-button" @click="handleLogin">登录</el-button>
                </div>
                <p v-if="errorMessage">{{ errorMessage }}</p>
            </el-form>

            <!-- 注册表单 -->
            <el-form v-else :model="registerForm" ref="registerFormRef" label-width="auto" class="demo-ruleForm"
                status-icon>
                <el-form-item label="账号" prop="account"
                    :rules="[{ required: true, message: '请输入账号', trigger: 'blur' }]">
                    <el-input v-model="registerForm.account" placeholder="请输入账号" />
                </el-form-item>
                <el-form-item label="密码" prop="pass" :rules="[{ required: true, message: '请输入密码', trigger: 'blur' }]">
                    <el-input v-model="registerForm.pass" placeholder="请输入密码" show-password />
                </el-form-item>
                <el-form-item label="用户名" prop="name" :rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
                    <el-input v-model="registerForm.name" placeholder="请输入用户名" />
                </el-form-item>
                <el-form-item label="年龄" prop="age" :rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
                    <el-input v-model="registerForm.age" placeholder="请输入年龄" />
                </el-form-item>
                <el-form-item label="电话" prop="phone" :rules="[{ required: true, message: '请输入电话', trigger: 'blur' }]">
                    <el-input v-model="registerForm.phone" placeholder="请输入电话" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email" :rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' }]">
                    <el-input v-model="registerForm.email" placeholder="请输入邮箱" />
                </el-form-item>
                <div class="login-button-group">
                    <el-button class="custom-button" @click="toggleForm">去登录</el-button>
                    <el-button type="primary" class="custom-button" @click="handleRegister">注册</el-button>
                </div>
                <p v-if="errorMessage">{{ errorMessage }}</p>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '../store/userStore';
import axios from 'axios';
import { useRouter } from 'vue-router';
import HeaderView from './HeaderView.vue';

const isLogin = ref(true);  // 控制显示登录还是注册
const userStore = useUserStore();
const loginForm = ref({ account: '', pass: '' });
const registerForm = ref({
    account: '',
    pass: '',
    name: '',
    age: '',
    phone: '',
    email: ''
});
const errorMessage = ref('');
const router = useRouter();

// 切换表单
const toggleForm = () => {
    isLogin.value = !isLogin.value;
};

// 登录处理函数
const handleLogin = async () => {
    try {
        const response = await axios.post('/api/login', loginForm.value);
        if (response.data.success) {
            userStore.setToken(response.data.token);
            userStore.setUserInfo(response.data.userInfo);
            localStorage.setItem('token', response.data.token);
            alert('登录成功！');
            router.push('/');
        } else {
            errorMessage.value = response.data.message;
        }
    } catch (error) {
        errorMessage.value = '登录请求失败，请重试。';
    }
};

// 注册处理函数
const handleRegister = async () => {
    try {
        const response = await axios.post('/api/register', registerForm.value);
        if (response.data.success) {
            alert('注册成功，请登录！');
            toggleForm();  // 注册成功后切换到登录表单
        } else {
            errorMessage.value = response.data.message;
        }
    } catch (error) {
        errorMessage.value = '注册请求失败，请重试。';
    }
};
</script>

<style scoped>
.login-register-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f5f5f5;
}

.login-register-container {
    background-color: #fff;
    padding: 40px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    width: 400px;
}

h1 {
    font-size: 24px;
    margin-bottom: 20px;
    color: #333;
}

.login-button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

.custom-button {
    width: 120px;
    border-radius: 25px;
    transition: all 0.3s ease;
}

.custom-button:hover {
    transform: scale(1.05);
}
</style>