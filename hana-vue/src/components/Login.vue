<template>
    <el-popover placement="bottom" :width="400" trigger="hover">
        <template #reference>
            <span @click="dialogLoginVisible = true">
                <el-avatar style="background-color: lightskyblue;">
                    登录
                </el-avatar>
            </span>
        </template>
        <div class="login-popover">
            <p>登录后你可以：</p>
            <ul>
                <li>免费看高清视频</li>
                <li>多端同步播放记录</li>
                <li>发表弹幕/评论</li>
                <li>热门番剧影视看不停</li>
            </ul>
            <div class="login-popover-a">
                <el-button class="login-button" @click="dialogLoginVisible = true">立即登录</el-button>
                <div>
                    <span>首次使用?</span>
                    <span class="register-link" @click="dialogLoginVisible = true">点我注册</span>
                </div>
            </div>
        </div>
    </el-popover>

    <!-- 登录对话框 -->
    <el-dialog v-model="dialogLoginVisible" width="500" class="custom-dialog">
        <div class="login">
            <h1>登录</h1>
            <el-form ref="ruleFormRef" label-width="auto" class="demo-ruleForm" status-icon>
                <el-form-item label="账号" prop="userName">
                    <el-input placeholder="请输入账号" />
                </el-form-item>
                <el-form-item label="密码" prop="userPass" autocomplete="off">
                    <el-input placeholder="请输入密码" show-password />
                </el-form-item>
                <div class="login-button-group">
                    <el-button class="custom-button" @click="">注册</el-button>
                    <el-button type="primary" class="custom-button" @click="handleLogin">登录</el-button>
                </div>
            </el-form>
        </div>
    </el-dialog>
</template>

<script setup>
import { ref } from 'vue';
import { useUserStore } from '../store/userStore';

const dialogLoginVisible = ref(false);
const userStore = useUserStore();

const handleLogin = async () => {
    const userData = {
        username: 'username', // 从表单获取
        password: 'password'  // 从表单获取
    };
    await userStore.login(userData);
    dialogLoginVisible.value = false;
};
</script>

<style scoped>
.login-popover {
    padding: 15px;
    text-align: left;
    background-color: #ffffff;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.login-popover p {
    font-size: 16px;
    margin-bottom: 10px;
    font-weight: 500;
    color: #333;
}

.login-popover ul {
    list-style-type: none;
    padding: 0;
    margin: 0 0 10px;
}

.login-popover li {
    margin-bottom: 5px;
    font-size: 14px;
    color: #666;
}

.login-popover-a {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
}

.login-button {
    width: 100%;
    background-image: linear-gradient(45deg, #4caf50, #81c784);
    color: white;
    border: none;
    border-radius: 20px;
    padding: 10px 0;
    transition: all 0.3s ease;
}

.login-button:hover {
    background-image: linear-gradient(45deg, #43a047, #66bb6a);
    box-shadow: 0 4px 12px rgba(67, 160, 71, 0.4);
}

.custom-dialog {
    border-radius: 12px;
    padding: 20px;
    background-color: #f9f9f9;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

.login {
    text-align: center;
}

h1 {
    font-size: 24px;
    color: #333;
    margin-bottom: 20px;
}

.demo-ruleForm {
    max-width: 300px;
    margin: 0 auto;
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

.register-link {
    color: #409eff;
    cursor: pointer;
}

.register-link:hover {
    text-decoration: underline;
}

.el-avatar {
    width: 50px;
    height: 50px;
    border-radius: 50%;
    background-color: lightskyblue;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 18px;
    color: white;
    cursor: pointer;
    transition: all 0.3s ease;
}

.el-avatar:hover {
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
</style>