<template>
    <div class="login-bg">
        <div class="login-container">
            <div class="reset-title">重置密码</div>
            <p class="reset-text">输入你的邮箱以获取验证码，设置新密码</p>
            <el-form :model="param" :rules="rules" ref="register" size="large">

                <!-- 原密码输入框 -->
                <el-form-item prop="old_pass">
                    <el-input type="password" placeholder="原密码" v-model="param.oldPassword"
                        @keyup.enter="submitForm(register)">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 新密码输入框 -->
                <el-form-item prop="new_pass">
                    <el-input type="password" placeholder="新密码" v-model="param.newPassword"
                        @keyup.enter="submitForm(register)">
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
                            <el-button type="primary" @click="sendVerificationCode">发送验证码</el-button>
                        </template>
                    </el-input>
                </el-form-item>

                <!-- 邮箱验证码输入框 -->
                <el-form-item prop="verification_code">
                    <el-input v-model="param.verificationCode" placeholder="输入邮箱验证码">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>

                <el-button class="login-btn" type="primary" size="large" @click="submitForm(register)">提交</el-button>
                <p class="login-text"><el-link type="primary" @click="$router.push('/login')">返回登录</el-link></p>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { verifyEmailRequest, resetPass } from '@/api';

const param = ref({
    oldPassword: '',
    newPassword: '',
    email: '',
    verificationCode: ''
});

const rules: FormRules = {
    old_pass: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
    new_pass: [{ required: true, message: '请输入新密码', trigger: 'blur' }],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        {
            pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
            message: '请输入正确的邮箱格式',
            trigger: 'blur',
        },
    ],
    verification_code: [{ required: true, message: '请输入验证码', trigger: 'blur' }]
};

const register = ref<FormInstance>();

const sendVerificationCode = async () => {
    if (!param.value.email) {
        ElMessage.error('请先输入邮箱');
        return;
    }
    // 调用发送验证码API
    const codeData = {
        email: param.value.email
    }
    await verifyEmailRequest(codeData);
    ElMessage.success('验证码已发送，请注意查收');
};

const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;
    formEl.validate(async (valid: boolean) => {
        if (valid) {
            ElMessage.success('密码重置成功');
            // 调用重置密码的API
            const resetData = {
                pass: param.value.oldPassword,
                new_pass: param.value.newPassword,
                code: param.value.verificationCode,
            }
            await resetPass(resetData)
        } else {
            return false;
        }
    });
};
</script>

<style scoped>
.login-bg {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100vh;
    background: url(../../assets/img/login-bg.jpg) center/cover no-repeat;
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

.login-btn {
    display: block;
    width: 100%;
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
