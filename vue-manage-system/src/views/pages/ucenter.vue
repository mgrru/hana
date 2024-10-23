<template>
    <div>
        <div class="user-container">
            <el-card class="user-profile" shadow="hover" :body-style="{ padding: '0px' }">
                <div class="user-profile-bg"></div>
                <div class="user-avatar-wrap">
                    <el-avatar class="user-avatar" :size="120" :src="avatarImg" />
                </div>
                <div class="user-info">
                    <div class="info-name">{{ userInfo.name }}</div>
                    <div class="info-desc">
                        <span>{{ userInfo.account }}</span>
                        <el-divider direction="vertical" />
                        <el-link href="https://github.com/mgrru/hana" target="_blank">github.com/mgrru/hana</el-link>
                    </div>
                    <div class="info-desc">{{ userInfo.role.name }}</div>
                    <div class="info-icon">
                        <a href="https://github.com/lin-xin" target="_blank"> <i class="el-icon-lx-github-fill"></i></a>
                        <i class="el-icon-lx-qq-fill"></i>
                        <i class="el-icon-lx-facebook-fill"></i>
                        <i class="el-icon-lx-twitter-fill"></i>
                    </div>
                </div>
                <div class="user-footer">
                    <div class="user-footer-item">
                        <el-statistic title="Follower" :value="1800" />
                    </div>
                    <div class="user-footer-item">
                        <el-statistic title="Following" :value="666" />
                    </div>
                    <div class="user-footer-item">
                        <el-statistic title="Total Post" :value="888" />
                    </div>
                </div>
            </el-card>
            <el-card class="user-content" shadow="hover"
                :body-style="{ padding: '20px 50px', height: '100%', boxSizing: 'border-box' }">
                <el-tabs tab-position="left" v-model="activeName">
                    <!-- <el-tab-pane name="label1" label="消息通知" class="user-tabpane">
                        <TabsComp />
                    </el-tab-pane>
                    <el-tab-pane name="label2" label="我的头像" class="user-tabpane">
                        <div class="crop-wrap" v-if="activeName === 'label2'">
                            <vueCropper ref="cropper" :img="imgSrc" :autoCrop="true" :centerBox="true" :full="true"
                                mode="contain">
                            </vueCropper>
                        </div>
                        <el-button class="crop-demo-btn" type="primary">选择图片
                            <input class="crop-input" type="file" name="image" accept="image/*" @change="setImage" />
                        </el-button>
                        <el-button type="success" @click="saveAvatar">上传并保存</el-button>
                    </el-tab-pane> -->
                    <el-tab-pane name="label3" label="修改密码" class="user-tabpane">
                        <el-form :model="form" :rules="rules" ref="register" size="large">
                            <!-- 原密码输入框 -->
                            <el-form-item prop="old_pass">
                                <el-input type="password" placeholder="原密码" v-model="form.oldPassword"
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
                                <el-input type="password" placeholder="新密码" v-model="form.newPassword"
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
                                <el-input v-model="form.email" placeholder="邮箱">
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
                                <el-input v-model="form.verificationCode" placeholder="输入邮箱验证码">
                                    <template #prepend>
                                        <el-icon>
                                            <Lock />
                                        </el-icon>
                                    </template>
                                </el-input>
                            </el-form-item>

                            <el-button class="login-btn" type="primary" size="large" @click="submitForm(reset)"
                                :loading="isSubmitting">提交</el-button>
                            <el-button class="reset-btn" type="warning" size="large" @click="resetForm">重置</el-button>

                            <p class="login-text"><el-link type="primary" @click="$router.push('/login')">返回登录</el-link>
                            </p>
                        </el-form>
                    </el-tab-pane>
                    <el-tab-pane label="待开发" name="lable4">
                    </el-tab-pane>
                </el-tabs>
            </el-card>
        </div>
    </div>
</template>

<script setup lang="ts" name="ucenter">
import { onMounted, reactive, ref } from 'vue';
import { VueCropper } from 'vue-cropper';
import 'vue-cropper/dist/index.css';
import avatar from '@/assets/img/img.jpg';
import TabsComp from '../element/tabs.vue';
import { ElMessage, type FormInstance, type FormRules } from 'element-plus';
import { verifyEmailRequest, resetPass, fetchUser } from '@/api';
import { stringifyQuery } from 'vue-router';
import { useRouter } from 'vue-router';

const router = useRouter();

const register = ref<FormInstance>();
const userInfo = ref({
    id: "",
    account: "",
    name: "",
    is_ban: "",
    age: "",
    phone: "",
    email: "",
    role: {
        id: 0,
        name: "",
    },
});

const getUserInfo = async () => {
    const response = await fetchUser();
    userInfo.value = JSON.parse(response.data)
}

const name = localStorage.getItem('vuems_name');
const form = reactive({
    email: '',
    newPassword: '',
    oldPassword: '',
    verificationCode: '',
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

// 重置表单
const resetForm = () => {
    form.email = '';
    form.newPassword = '';
    form.oldPassword = '';
    form.verificationCode = '';
    isCodeSent.value = false; // 重置验证码状态
    ElMessage.success('表单已重置');
};


const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl || (isSubmitting.value)) return;
    isSubmitting.value = true;



    formEl.validate(async (valid: boolean) => {
        if (valid) {
            ElMessage.success('密码重置成功');

            if (!form.verificationCode) {
                ElMessage.error('请输入验证码');
                isSubmitting.value = false;
                return;
            }

            // 调用重置密码的API
            const resetData = {
                pass: form.oldPassword,
                new_pass: form.newPassword,
                code: form.verificationCode,
            }


            try {
                const response = await resetPass(resetData)
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

        } else {
            return false;
        }
    });
};

const activeName = ref('label1');
const isSubmitting = ref(false);
const reset = ref();
const isCodeSent = ref(false);
const userEmail = userInfo.value.email; // 获取当前用户的邮箱

const avatarImg = ref(avatar);
const imgSrc = ref(avatar);
const cropImg = ref('');
const cropper: any = ref();

const setImage = (e: any) => {
    const file = e.target.files[0];
    if (!file.type.includes('image/')) {
        return;
    }
    const reader = new FileReader();
    reader.onload = (event: any) => {
        imgSrc.value = event.target.result;
        cropper.value && cropper.value.replace(event.target.result);
    };
    reader.readAsDataURL(file);
};

const sendVerificationCode = async () => {
    if (isSubmitting.value || isCodeSent.value) return; // 如果验证码已发送，直接返回
    isSubmitting.value = true;

    // 检查用户是否输入邮箱
    if (!form.email) {
        ElMessage.error('请先输入邮箱');
        isSubmitting.value = false;
        return;
    }

    // 检查输入的邮箱是否与当前用户的邮箱一致
    const userEmail = userInfo.value.email; // 从 userInfo 中获取用户邮箱
    if (!userEmail) {
        ElMessage.error('当前用户没有邮箱，请先设置邮箱');
        isSubmitting.value = false;
        return;
    }
    if (form.email !== userEmail) {
        ElMessage.error('输入的邮箱与当前用户信息中的邮箱不匹配');
        isSubmitting.value = false;
        return;
    }

    const codeData = {
        email: form.email,
    };

    try {
        const response = await verifyEmailRequest(codeData);
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


const cropImage = () => {
    cropImg.value = cropper.value?.getCroppedCanvas().toDataURL();
};

const saveAvatar = () => {
    avatarImg.value = cropImg.value;
};

onMounted(getUserInfo)
</script>

<style scoped>
.user-container {
    display: flex;
}

.user-profile {
    position: relative;
}

.user-profile-bg {
    width: 100%;
    height: 200px;
    background-image: url('../../assets/img/ucenter-bg.jpg');
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
}

.user-profile {
    width: 500px;
    margin-right: 20px;
    flex: 0 0 auto;
    align-self: flex-start;
}

.user-avatar-wrap {
    position: absolute;
    top: 135px;
    width: 100%;
    text-align: center;
}

.user-avatar {
    border: 5px solid #fff;
    border-radius: 50%;
    overflow: hidden;
    box-shadow: 0 7px 12px 0 rgba(62, 57, 107, 0.16);
}

.user-info {
    text-align: center;
    padding: 80px 0 30px;
}

.info-name {
    margin: 0 0 20px;
    font-size: 22px;
    font-weight: 500;
    color: #373a3c;
}

.info-desc {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-bottom: 5px;
}

.info-desc,
.info-desc a {
    font-size: 18px;
    color: #55595c;
}

.info-icon {
    margin-top: 10px;
}

.info-icon i {
    font-size: 30px;
    margin: 0 10px;
    color: #343434;
}

.user-content {
    flex: 1;
}

.user-tabpane {
    padding: 20px;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.el-form {
    width: 100%;
    max-width: 400px;
}

.el-form-item {
    width: 100%;
}

.el-input {
    width: 100%;
}

.el-input__inner {
    border-radius: 8px;
    padding: 12px 15px;
    box-shadow: inset 0 1px 2px rgba(0, 0, 0, 0.1);
    transition: border-color 0.3s, box-shadow 0.3s;
}

.el-input__inner:focus {
    border-color: #409eff;
    box-shadow: 0 0 5px rgba(64, 158, 255, 0.5);
}

.el-button {
    width: 100%;
    height: 45px;
    margin: 10px 0;
    border-radius: 8px;
    transition: background-color 0.3s, box-shadow 0.3s;
}

.el-button--primary {
    background-color: #409eff;
}

.el-button--primary:hover {
    background-color: #66b1ff;
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.login-btn {
    margin-top: 20px;
}

.login-text {
    margin-top: 10px;
    text-align: center;
}

.crop-wrap {
    width: 400px;
    height: 300px;
    margin: 20px 0;
    border: 1px solid #dcdfe6;
    border-radius: 8px;
}

.crop-demo-btn {
    width: 100%;
    position: relative;
    border-radius: 8px;
}

.crop-input {
    position: absolute;
    width: 100%;
    height: 100%;
    opacity: 0;
    cursor: pointer;
}

.w500 {
    width: 500px;
}

.user-footer {
    display: flex;
    border-top: 1px solid rgba(83, 70, 134, 0.1);
}

.user-footer-item {
    padding: 20px 0;
    width: 33.3333333333%;
    text-align: center;
}

.user-footer>div+div {
    border-left: 1px solid rgba(83, 70, 134, 0.1);
}
</style>

<style>
.el-tabs.el-tabs--left {
    height: 100%;
}
</style>