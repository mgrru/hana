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
                    <el-button type="primary" class="custom-button" @click="handleLogin"
                        :loading="isSubmitting">登录</el-button>
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
                    <el-button type="primary" class="custom-button" @click="handleRegister"
                        :loading="isSubmitting">注册</el-button>
                </div>

                <p v-if="errorMessage">{{ errorMessage }}</p>
            </el-form>
            <div class="pwd-tips">
                <el-checkbox class="pwd-checkbox" v-model="checked" label="记住密码" />
                <el-link type="primary" @click="$router.push('/reset-pwd')">忘记密码</el-link>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import axios from '../utils/axios';
import { useRouter } from 'vue-router';
import HeaderView from './HeaderView.vue';
import { useAuthStore } from '../store/auth';
import { ElMessage } from 'element-plus';

const authStore = useAuthStore();
const isLogin = ref(true);  // 控制显示登录还是注册
const isSubmitting = ref(false);
const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(lgStr ? true : false);
const loginForm = ref({
    account: defParam ? defParam.account : '',
    pass: defParam ? defParam.pass : '',
});
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
const handleLogin = async () => {
    if (isSubmitting.value) return; // 防止重复点击
    isSubmitting.value = true;

    try {
        const response = await axios.post('/login', loginForm.value);

        if (response.status === 200) {
            const token = response.data.replace(/^Bearer\s/, ''); // 获取 token
            authStore.setToken(token);  // 保存 token
            ElMessage.success('登录成功！');
            clearLoginForm();
            router.push('/');  // 跳转到首页
            if (checked.value) {
                localStorage.setItem('login-param', JSON.stringify(param)); // 记住账号信息
            } else {
                localStorage.removeItem('login-param'); // 不保存账号信息
            }
        } else {
            ElMessage.error('登录请求失败，请重试。');
        }
    } catch (error) {
        handleLoginError(error);
    } finally {
        isSubmitting.value = false; // 恢复提交状态
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
    if (isSubmitting.value) return; // 防止重复点击
    isSubmitting.value = true;

    const formattedRegisterForm = {
        account: registerForm.value.account.trim(),
        pass: registerForm.value.pass.trim(),
    };

    try {
        const response = await axios.post('/register', formattedRegisterForm);
        ElMessage.success(response.data.msg || '注册成功，请登录！');
        clearRegisterForm();
        toggleForm(); // 切换到登录表单
    } catch (error) {
        ElMessage.error(error.response?.data?.msg || '注册请求失败，请重试。');
    } finally {
        isSubmitting.value = false; // 恢复提交状态
    }
};

// 处理登录错误
const handleLoginError = (error) => {
    console.error('登录请求失败:', error);
    if (error.response.status === 477) {
        ElMessage.error(`${error.response.data || '请求失败，请重试。'}`);
    } else if (error.response.status === 666) {
        ElMessage.error(`错误 ${error.response.status}:用户名或密码错误，请重试！`);
    } else {
        ElMessage.error('登录请求失败，请检查网络或服务器。');
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
    padding: 40px 30px;
    /* 调整内边距 */
    border-radius: 10px;
    /* 圆角更加明显 */
    box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
    /* 增强阴影效果 */
    width: 400px;
    transition: box-shadow 0.3s ease;
    /* 平滑的阴影过渡 */
}

.login-register-container:hover {
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
    /* 悬停时更强的阴影效果 */
}

h1 {
    font-size: 26px;
    /* 增加标题字体大小 */
    margin-bottom: 20px;
    color: #333;
    text-align: center;
    /* 居中对齐 */
}

.login-button-group {
    display: flex;
    justify-content: space-between;
    margin-top: 20px;
}

.custom-button {
    width: 120px;
    border-radius: 25px;
    transition: background-color 0.3s ease, transform 0.3s ease;
    /* 添加背景色变化 */
}

.custom-button:hover {
    transform: scale(1.05);
    background-color: #e0e0e0;
    /* 悬停时更改背景颜色 */
}

.pwd-tips {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    margin-top: 10px;
    /* 调整上边距 */
    color: #787878;
}

.pwd-checkbox {
    margin-right: 5px;
    /* 添加右边距 */
}
</style>
