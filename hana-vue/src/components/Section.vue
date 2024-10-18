<template>
    <el-tabs style="height: 200px" class="demo-tabs">
        <!-- 动态渲染 section -->
        <el-tab-pane v-for="section in sections" :key="section.id" :label="section.name">
        </el-tab-pane>
    </el-tabs>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from '../utils/axios'

const sections = ref([])  // 用于存储 section 数据

// 获取 section 数据
const fetchSectionData = async () => {
    try {
        const response = await axios.get('/sections') // 请求接口 /sections
        console.log('data:', response.data) // 第一个打印 - 确保获取到的原始数据正确

        // 解析字符串数据
        const parsedData = JSON.parse(response.data)
        console.log('raw data:', parsedData) // 第二个打印 - 确保字符串正确解析成数组

        // 将解析后的数据赋值给 sections
        sections.value = parsedData
        console.log('sections value:', sections.value) // 第三个打印 - 确保 sections 数据已经正确赋值
    } catch (error) {
        console.error('获取 section 数据失败:', error)
    }
}

// 页面加载时调用获取数据
onMounted(() => {
    fetchSectionData()
})
</script>

<style>
.demo-tabs>.el-tabs__content {
    padding: 32px;
    color: #6b778c;
    font-size: 32px;
    font-weight: 600;
}

.el-tabs--right .el-tabs__content,
.el-tabs--left .el-tabs__content {
    height: 100%;
}
</style>
