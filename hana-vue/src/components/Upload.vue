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
                <el-upload :file-list="videoFileList" :before-upload="handleVideoChange" drag>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将视频拖到此处，或点击上传</div>
                    <div class="el-upload__tip">只能上传视频文件</div>
                </el-upload>
            </el-form-item>

            <el-form-item label="选择封面">
                <el-upload :file-list="coverFileList" :before-upload="handleCoverChange" drag>
                    <i class="el-icon-upload"></i>
                    <div class="el-upload__text">将封面拖到此处，或点击上传</div>
                    <div class="el-upload__tip">只能上传图片文件</div>
                </el-upload>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" @click="submitForm">提交</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import axios from '../utils/axios'

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
    { value: 1, label: '日漫' },   // 对应 sid=1
    { value: 2, label: '国漫' },   // 对应 sid=1
    { value: 3, label: '其他' }, // 对应 sid=3
]

// 检查并获取视频文件
const handleVideoChange = (file) => {
    const isVideo = file.type.startsWith('video/')
    if (!isVideo) {
        ElMessage.error('只能上传视频文件')
        return false
    }
    uploadForm.value.videoFile = file
    return true
}

// 检查并获取封面文件
const handleCoverChange = (file) => {
    const isImage = file.type.startsWith('image/')
    if (!isImage) {
        ElMessage.error('只能上传图片文件')
        return false
    }
    uploadForm.value.coverFile = file
    return true
}

// 提交表单
const submitForm = async () => {
    if (!uploadForm.value.videoFile) {
        ElMessage.error('请先选择视频文件')
        return
    }
    if (!uploadForm.value.coverFile) {
        ElMessage.error('请先选择封面文件')
        return
    }
    if (!uploadForm.value.sid) {
        ElMessage.error('请选择板块')
        return
    }

    // 创建 FormData 对象
    const formData = new FormData()
    formData.append('title', uploadForm.value.title)
    formData.append('type', uploadForm.value.type)
    formData.append('name', uploadForm.value.name)
    formData.append('episode', uploadForm.value.episode)
    formData.append('episode_name', uploadForm.value.episode_name)
    formData.append('sid', uploadForm.value.sid) // 添加 sid 参数
    formData.append('resources', uploadForm.value.videoFile)
    formData.append('cover', uploadForm.value.coverFile)

    try {
        // 发送 POST 请求
        await axios.post('/upload', formData, {
            headers: { 'Content-Type': 'multipart/form-data' },
        })

        ElMessage.success('提交成功')

    } catch (error) {
        ElMessage.error('上传失败，请稍后重试')
    }
}
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
