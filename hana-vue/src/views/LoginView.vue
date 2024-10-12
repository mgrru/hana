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
import axios from '../utils/axios';
import { useRouter } from 'vue-router';
import HeaderView from './HeaderView.vue';
import { useAuthStore } from '../store/auth';
import { ElMessage } from 'element-plus';

const userStore = useUserStore();
const authStore = useAuthStore();
const isLogin = ref(true);  // 控制显示登录还是注册
const loginForm = ref({ account: '', pass: '' });
const registerForm = ref({
    account: '',
    pass: ''
});


const errorMessage = ref('');
const router = useRouter();

// 切换表单
const toggleForm = () => {
    console.log('切换到登录表单');
    isLogin.value = !isLogin.value;
};

// 登录处理函数
// const handleLogin = async () => {
//     try {
//         const response = await axios.post('/login', loginForm.value);

//         const token = response.data.data;

//         authStore.setToken(token); // 保存token
//         // console,log(authStore.isLogin.value)
//         ElMessage.success('登录成功！');
//         clearLoginForm();
//         // 跳转到首页或其他页面
//         router.push('/');
//     } catch (error) {
//         errorMessage.value = '登录请求失败，请重试。';
//     }
// };
const handleLogin = async () => {
    try {
        const response = await axios.post('/login', loginForm.value);

        // 检查后端返回的 code 是否为 200
        if (response.data.code === 200) {
            const token = response.data.data;  // 获取 token

            authStore.setToken(token);  // 保存 token
            await userStore.fetchUserInfo();

            ElMessage.success('登录成功！');
            clearLoginForm();
            router.push('/');  // 跳转到首页
        } else {
            // 登录失败，显示后端返回的错误信息
            ElMessage.error(response.data.msg || '账号或密码错误');
        }
    } catch (error) {
        console.error('登录请求失败:', error);
        ElMessage.error('登录请求失败，请重试。');
    }
};


// 清空登录表单
const clearLoginForm = () => {
    loginForm.value = {
        account: '',
        pass: ''
    };
};
// 注册处理函数
const handleRegister = async () => {

    // 确保所有输入字段去除空格
    const formattedRegisterForm = {
        account: registerForm.value.account.trim(),
        pass: registerForm.value.pass.trim(),
    };

    try {
        const response = await axios.post('/register', formattedRegisterForm);

        console.log('注册响应:', response.data);
        // 使用 nextTick 来确保 DOM 完成更新：
        ElMessage.success(response.data.msg || '注册成功，请登录！');
        clearRegisterForm();
        toggleForm(); // 切换到登录表单
    } catch (error) {
        ElMessage.error('注册请求失败，请重试。');
        ElMessage.error(error.response?.data?.msg || '注册请求失败，请重试。');
    }

};

// 清空表单
const clearRegisterForm = () => {
    console.log('清空注册表单');
    registerForm.value = {
        account: '',
        pass: '',
    };
    console.log('表单已清空:', registerForm.value); // 打印清空后的表单
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