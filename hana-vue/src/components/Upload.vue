<template>
    <el-header height="auto">
        <Header />
    </el-header>
    <div class="resource-upload">
        <h2>上传动漫资源</h2>
        <el-form ref="formRef" :model="uploadForm" label-width="120px">
            <el-form-item label="标题">
                <el-input v-model="uploadForm.title" placeholder="请输入标题" />
            </el-form-item>

            <el-form-item label="视频类型">
                <el-select v-model="uploadForm.type" placeholder="请选择视频类型" size="large" style="width: 240px">
                    <el-option v-for="item in uploadOptions" :key="item.value" :label="item.label"
                        :value="item.value" />
                </el-select>
            </el-form-item>

            <el-form-item label="板块名称">
                <el-select v-model="uploadForm.sid" placeholder="请选择板块" size="large" style="width: 240px">
                    <el-option v-for="item in sectionOptions" :key="item.value" :label="item.label"
                        :value="item.value" />
                </el-select>
            </el-form-item>

            <el-form-item label="视频名称">
                <el-input v-model="uploadForm.name" placeholder="请输入视频名称" />
            </el-form-item>

            <el-form-item label="集数">
                <span>{{ uploadForm.episode }}</span> <!-- 显示默认值 1 -->
            </el-form-item>


            <el-form-item label="集数名称">
                <el-input v-model="uploadForm.episode_name" placeholder="请输入集数名称" />
            </el-form-item>

            <el-form-item label="选择视频文件">
                <el-upload :show-file-list="false" :before-upload="handleVideoChange" drag auto-upload="false">
                    <video v-if="videoFileList" :src="videoFileList" />
                    <i v-else class="el-icon-upload"></i>
                    <div class="el-upload__text">将视频拖到此处，或点击上传</div>
                    <div class="el-upload__tip">只能上传视频文件</div>
                </el-upload>
            </el-form-item>

            <el-form-item label="选择封面">
                <el-upload :show-file-list="false" :before-upload="handleCoverChange" drag auto-upload="false">
                    <img v-if="coverFileList" :src="coverFileList" />
                    <i v-else class="el-icon-upload"></i>
                    <div class="el-upload__text">将封面拖到此处，或点击上传</div>
                    <div class="el-upload__tip">只能上传图片文件</div>
                </el-upload>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm" :loading="uploadStore.uploadStatus">提交</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'
import { useUploadStore } from '../store/uploadStore';
const isSubmitting = ref(false);
const uploadForm = ref({
    title: '',
    type: '',
    name: '',
    episode: 1,
    episode_name: '',
    sid: null, // 新增板块 sid
    videoFile: null,
    coverFile: null,
})

const videoFileList = ref([])
const coverFileList = ref([])

const uploadOptions = [
    { value: '动画', label: '动画' },
    { value: '漫画', label: '漫画' },
    { value: '其他', label: '其他' },
]

// 板块选项（sections）
const sectionOptions = [
    { value: 1, label: '日漫' },
    { value: 2, label: '国漫' },
    { value: 3, label: '其他' },
]

// 检查并获取视频文件
const handleVideoChange = (file) => {
    const isVideo = file.type.startsWith('video/')
    if (!isVideo) {
        ElMessage.error('只能上传视频文件')
        return false
    }
    uploadForm.value.videoFile = file
    videoFileList.value = URL.createObjectURL(file)
    return false
}

// 检查并获取封面文件
const handleCoverChange = (file) => {
    const isImage = file.type.startsWith('image/')
    if (!isImage) {
        ElMessage.error('只能上传图片文件')
        return false
    }
    uploadForm.value.coverFile = file
    coverFileList.value = URL.createObjectURL(file)
    return false
}

const uploadStore = useUploadStore();

const submitForm = async () => {


    await uploadStore.submitForm(uploadForm.value);
    if (uploadStore.uploadStatus) {
        // 通知父组件上传成功
        emit('upload-success');
    }

};
</script>

<style scoped>
.resource-upload {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #dcdfe6;
    border-radius: 4px;
    background-color: #f9f9f9;
}

h2 {
    text-align: center;
    margin-bottom: 20px;
}

.el-upload__text {
    color: #409eff;
}

.el-upload__tip {
    font-size: 12px;
    color: #909399;
}
</style>
