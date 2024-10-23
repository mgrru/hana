<template>
    <div class="login-bg">
        <div class="login-container">
            <div class="login-header">
                <!-- <img class="logo mr10" src="../../assets/img/logo.svg" alt="" /> -->
                <div class="login-title">后台管理系统</div>
            </div>
            <el-form :model="param" :rules="rules" ref="login" size="large">
                <el-form-item prop="username">
                    <el-input v-model="param.username" placeholder="用户名">
                        <template #prepend>
                            <el-icon>
                                <User />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="密码" v-model="param.password"
                        @keyup.enter="submitForm(login)">
                        <template #prepend>
                            <el-icon>
                                <Lock />
                            </el-icon>
                        </template>
                    </el-input>
                </el-form-item>
                <div class="pwd-tips">
                    <el-checkbox class="pwd-checkbox" v-model="checked" label="记住密码" />
                    <el-link type="primary" @click="$router.push('/reset-pwd')">忘记密码</el-link>
                </div>
                <el-button class="login-btn" type="primary" size="large" @click="submitForm(login)">登录</el-button>
                <p class="login-text">
                    没有账号？<el-link type="primary" @click="$router.push('/register')">立即注册</el-link>
                </p>
            </el-form>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue';
import { useTabsStore } from '@/store/tabs';
import { usePermissStore } from '@/store/permiss';
import { fetchUserData, loginRequest } from '@/api';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import type { FormInstance, FormRules } from 'element-plus';
interface LoginInfo {
    username: string;
    password: string;
}
const permissStore = usePermissStore();
const lgStr = localStorage.getItem('login-param');
const defParam = lgStr ? JSON.parse(lgStr) : null;
const checked = ref(lgStr ? true : false);
const router = useRouter();
const param = reactive<LoginInfo>({
    username: defParam ? defParam.username : '',
    password: defParam ? defParam.password : '',
});
const rules: FormRules = {
    username: [
        {
            required: true,
            message: '请输入用户名',
            trigger: 'blur',
        },
    ],
    password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
};
const permiss = usePermissStore();
const login = ref<FormInstance>();
const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return;

    formEl.validate(async (valid: boolean) => {
        if (valid) {
            try {
                const tableData = {
                    account: param.username,
                    pass: param.password
                };

                // 发送登录请求
                const response = await loginRequest(tableData);

                // 成功处理
                if (response.status === 200) {
                    const token = response.data.replace(/^Bearer\s/, ''); // 获取 token
                    permissStore.setToken(token);  // 保存 token
                    ElMessage.success('登录成功');
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
                console.error('登录请求失败:', error);

                if (error.response.status === 477) {
                    ElMessage.error(`错误 ${error.response.data || '请求失败，请重试。'}`);
                } else if (error.response.status === 666) {
                    ElMessage.error(`错误 ${error.response.status}: 用户名或密码错误，请重试！`);
                } else {
                    // 其他错误
                    ElMessage.error('登录请求失败，请检查网络或服务器。');
                }
            }
        } else {
            ElMessage.error('登录失败');
            return false;
        }
    });
};


const tabs = useTabsStore();
tabs.clearTabs();
</script>

<style scoped>
.login-bg {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100vh;
    background: beige center/cover no-repeat;
}

.login-header {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 40px;
}

.logo {
    width: 35px;
}

.login-title {
    font-size: 22px;
    color: #333;
    font-weight: bold;
}

.login-container {
    width: 450px;
    border-radius: 5px;
    background: #fff;
    padding: 40px 50px 50px;
    box-sizing: border-box;
}

.pwd-tips {
    display: flex;
    justify-content: space-between;
    align-items: center;
    font-size: 14px;
    margin: -10px 0 10px;
    color: #787878;
}

.pwd-checkbox {
    height: auto;
}

.login-btn {
    display: block;
    width: 100%;
}

.login-tips {
    font-size: 12px;
    color: #999;
}

.login-text {
    display: flex;
    align-items: center;
    margin-top: 20px;
    font-size: 14px;
    color: #787878;
}
</style>
