<template>
    <div class="user-management-page">

        <el-tabs tab-position="left" v-model="activeName" class="user-tabs">
            <el-tab-pane name="label1" label="我的资料" class="user-tabpane">
                <el-descriptions title="我的资料" direction="vertical" :column="3" border style="margin-top: 20px">
                    <el-descriptions-item label="用户名">{{ userStore.userInfo.name }}</el-descriptions-item>
                    <el-descriptions-item label="账号状态">
                        <el-tag type="success" v-if="userStore.userInfo.is_ban === '否'">正常</el-tag>
                        <el-tag type="danger" v-else>禁用</el-tag>
                    </el-descriptions-item>
                    <el-descriptions-item label="年龄">{{ userStore.userInfo.age }}</el-descriptions-item>
                    <el-descriptions-item label="电话">{{ userStore.userInfo.phone }}</el-descriptions-item>
                    <el-descriptions-item label="邮箱">{{ userStore.userInfo.email }}</el-descriptions-item>
                </el-descriptions>
            </el-tab-pane>

            <el-tab-pane name="label2" label="消息中心" class="user-tabpane">
                <msg />
            </el-tab-pane>

            <el-tab-pane name="label3" label="修改密码" class="user-tabpane">
                <reset-pwd />
            </el-tab-pane>

            <el-tab-pane name="label4" label="修改资料" class="user-tabpane">
                <el-form :model="userStore.tempUserInfo" ref="userFormRef" :rules="rules" label-width="100px"
                    class="user-form">
                    <el-form-item label="用户名" prop="name">
                        <el-input v-model="userStore.tempUserInfo.name" placeholder="请输入新用户名" />
                    </el-form-item>
                    <el-form-item label="年龄" prop="age">
                        <el-input v-model="userStore.tempUserInfo.age" placeholder="请输入新年龄" />
                    </el-form-item>
                    <el-form-item label="电话" prop="phone">
                        <el-input v-model="userStore.tempUserInfo.phone" placeholder="请输入新电话" />
                    </el-form-item>
                    <el-form-item label="邮箱" prop="email">
                        <el-input v-model="userStore.tempUserInfo.email" placeholder="请输入新邮箱" />
                    </el-form-item>
                    <el-button type="primary" @click="onSubmit">更新信息</el-button>
                </el-form>
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script setup>
import { onMounted,ref } from 'vue';
import resetPwd from './reset-pwd.vue';
import Msg from './Msg.vue';
import { useUserStore } from '../store/userStore';
const userFormRef = ref(null); // 正确定义 ref
const userStore = useUserStore();

const rules = {
    name: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
    age: [{ required: true, message: '请输入年龄', trigger: 'blur' }],
    phone: [
        { required: true, message: '请输入电话', trigger: 'blur' },
        { pattern: /^[0-9]{11}$/, message: '请输入11位数字的电话', trigger: 'blur' },
    ],
    email: [
        { required: true, message: '请输入邮箱', trigger: 'blur' },
        { pattern: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/, message: '请输入正确的邮箱格式', trigger: 'blur' },
    ],
};

// 表单提交处理
const onSubmit = () => {
    if (!userFormRef.value) {
        console.log('表单引用未正确初始化');
        return;
    }
    userFormRef.value.validate((valid) => {
        if (valid) {
            userStore.updateUserInfo();
        } else {
            console.log('表单验证失败');
        }
    });
};


onMounted(() => {
    userStore.fetchUserInfo();
    console.log(userFormRef.value); // 检查 userFormRef 是否正确
});
</script>

<style scoped>
.user-management-page {
    padding: 30px;
    max-width: 800px;
    margin: 0 auto;
    background-color: #fff;
    border-radius: 10px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
}

h1 {
    text-align: center;
    font-size: 24px;
    color: #2c3e50;
    margin-bottom: 30px;
}

.user-tabs {
    background-color: #f6f6f6;
    border-radius: 8px;
}

.user-tabpane {
    padding: 20px;
}

.user-form {
    background-color: #f9f9f9;
    border-radius: 8px;
    padding: 20px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.el-input {
    width: 100%;
}

.el-button {
    margin-top: 20px;
}

.el-descriptions {
    background-color: #fefefe;
    border-radius: 10px;
    padding: 20px;
}

.el-tag {
    font-size: 14px;
}
</style>
