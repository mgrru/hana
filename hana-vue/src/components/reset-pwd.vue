<template>
    <div class="login-bg">
        <div class="login-container">
            <div class="reset-title">重置密码</div>
            <p class="reset-text">输入你的邮箱以获取验证码，设置新密码</p>
            <el-form :model="param" :rules="rules" ref="reset" size="large">

                <!-- 原密码输入框 -->
                <el-form-item prop="oldPassword">
                    <el-input type="password" placeholder="原密码" v-model="param.oldPassword"
                        @keyup.enter="submitForm(reset)">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 新密码输入框 -->
                <el-form-item prop="newPassword">
                    <el-input type="password" placeholder="新密码" v-model="param.newPassword"
                        @keyup.enter="submitForm(reset)">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 邮箱输入框，右侧添加发送验证码按钮 -->
                <el-form-item prop="email">
                    <el-input v-model="param.email" placeholder="邮箱">
                        <template #prepend>
                            <el-icon>
                                <Message />
                            </el-icon>
                        </template>
                        <template #append>
                            <el-button type="primary" @click="sendVerificationCode" :loading="isSubmitting">
                                {{ isCodeSent ? '已发送' : '发送验证码' }}
                            </el-button>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 邮箱验证码输入框 -->
                <el-form-item prop="verificationCode">
                    <el-input v-model="param.verificationCode" placeholder="输入邮箱验证码">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 提交和重置按钮 -->
                <div class="button-group">
                    <el-button class="login-btn" type="primary" size="large" @click="submitForm(reset)"
                        :loading="isSubmitting">提交</el-button>
                    <el-button class="reset-btn" type="warning" size="large" @click="resetForm">重置</el-button>
                </div>

                <p class="login-text"><el-link type="primary" @click="$router.push('/login')">返回登录</el-link></p>
            </el-form>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import axios from '../utils/axios';
import { useRouter } from 'vue-router';
import { useUserStore } from '../store/userStore'; // 引入 userStore

const router = useRouter();
const isSubmitting = ref(false);
const param = ref({
    oldPassword: '',
    newPassword: '',
    email: '',
    verificationCode: ''
});

const userStore = useUserStore(); // 获取 userStore
const userEmail = userStore.userInfo.email; // 获取当前用户的邮箱

const rules = {
    oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    newPassword: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        {
            pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
            message: '请输入正确的邮箱格式',
            trigger: 'blur',
        },
    ],
    verificationCode: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
};

const reset = ref();
const isCodeSent = ref(false);

// 重置表单
const resetForm = () => {
    param.value = {
        oldPassword: '',
        newPassword: '',
        email: '',
        verificationCode: ''
    };
    isCodeSent.value = false;
};

// 发送验证码并检查邮箱是否与当前用户相同
const sendVerificationCode = async () => {
    if (isSubmitting.value || isCodeSent.value) return; // 如果验证码已发送，直接返回
    isSubmitting.value = true;

    // 检查用户是否输入邮箱
    if (!param.value.email) {
        ElMessage.error('请先输入邮箱');
        isSubmitting.value = false;
        return;
    }

    // 检查输入的邮箱是否与当前用户的邮箱一致
    if (!userEmail) {
        ElMessage.error('当前用户没有邮箱，请先设置邮箱');
        isSubmitting.value = false;
        return;
    }
    if (param.value.email !== userEmail) {
        ElMessage.error('输入的邮箱与当前用户信息中的邮箱不匹配');
        isSubmitting.value = false;
        return;
    }

    try {
        const response = await axios.post('/users/verify/email', { email: param.value.email });
        if (response.status === 200) {
            ElMessage.success('验证码已发送，请注意查收');
            isCodeSent.value = true;
        } else {
            ElMessage.error('验证码发送失败，请重试。');
        }
    } catch (error) {
        ElMessage.error('网络错误，发送验证码失败');
    } finally {
        isSubmitting.value = false;
    }
};

const submitForm = async () => {
    if (isSubmitting.value) return;
    isSubmitting.value = true;

    if (!param.value.verificationCode) {
        ElMessage.error('请输入验证码');
        isSubmitting.value = false;
        return;
    }

    const resetData = {
        pass: param.value.oldPassword,
        new_pass: param.value.newPassword,
        code: param.value.verificationCode,
    };

    try {
        const response = await axios.put('/users/pass', resetData);
        if (response.status === 200) {
            ElMessage.success('密码重置成功');
            router.push('/login');
        } else {
            ElMessage.error('密码重置失败，请重试。');
        }
    } catch (error) {
        if (error.response) {
            if (error.response.status === 400) {
                ElMessage.error('验证码无效或已过期');
            } else if (error.response.status === 401) {
                ElMessage.error('原密码错误');
            } else {
                ElMessage.error('密码重置失败，请稍后重试');
            }
        } else {
            ElMessage.error('网络错误，请检查网络连接');
        }
    } finally {
        isSubmitting.value = false;
    }
};

</script>

<style scoped>
.login-bg {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100vh;
    background: bisque;
}

.reset-title {
    text-align: center;
    font-size: 22px;
    color: #333;
    font-weight: bold;
    margin-bottom: 10px;
}

.reset-text {
    text-align: center;
    font-size: 14px;
    color: #787878;
    margin-bottom: 40px;
}

.login-container {
    width: 450px;
    border-radius: 5px;
    background: #fff;
    padding: 40px 50px 50px;
    box-sizing: border-box;
}

.button-group {
    display: flex;
    justify-content: space-between;
}

.login-btn,
.reset-btn {
    width: 48%;
}

.login-text {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-top: 20px;
    font-size: 14px;
    color: #333;
}
</style>
