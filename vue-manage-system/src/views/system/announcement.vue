<template>
    <div>
        <!-- <TableSearch :query="query" :options="searchOpt" :search="handleSearch" /> -->
        <div class="container">

            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :page-change="changePage" :editFunc="handleEdit" :delFunc="handleDelete">
                <template #toolbarBtn>
                    <el-button type="warning" :icon="CirclePlusFilled" @click="visible = true">新增</el-button>
                </template>
            </TableCustom>
        </div>
        <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
            :close-on-click-modal="false" @close="closeDialog">
            <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" :add="addData" />
        </el-dialog>
        <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
            <TableDetail :data="viewData">

            </TableDetail>
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="system-annoucement">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Section } from '@/types/section';
import { Announcement } from '@/types/annoucement';
import { fetchAnnouncementsData, addAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import tableAdd from '@/components/table-add.vue';
import RolePermission from './role-permission.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import axios from 'axios';

// // 查询相关
// const query = reactive({
//     name: '',
// });
// const searchOpt = ref<FormOptionList[]>([
//     { type: 'input', label: '公告名称：', prop: 'name' }
// ])
// const handleSearch = () => {
//     changePage(1);
// };

// 表格相关
let columns = ref([
    { prop: 'id', label: '公告ID' },  // 确保表格中有正确的id
    { prop: 'title', label: '公告标题' },
    { prop: 'content', label: '公告内容' },
    { prop: 'time', label: '发布时间' },
    { prop: 'operator', label: '操作', width: 250 },

])
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})
const tableData = ref<Announcement[]>([]);
const getData = async () => {
    const res = await fetchAnnouncementsData()
    // 假设 res.data 是一个数组，每个对象都有一个 time 字段
    const announcements = JSON.parse(res.data);

    announcements.forEach(item => {
        // 将 ISO 时间转换为 Date 对象
        const dateObj = new Date(item.time);

        // 将 Date 对象格式化为本地时间字符串
        item.formattedTime = dateObj.toLocaleString(); // 或者使用其他格式化方法
    });

    // 将处理过的数据赋值给 tableData
    tableData.value = announcements;
    // page.total = res.data.pageTotal;
};
getData();
const changePage = (val: number) => {
    page.index = val;
    getData();
};

// 新增/编辑弹窗相关
const options = ref<FormOption>({
    labelWidth: '100px',
    span: 24,
    list: [
        { type: 'input', label: '公告ID', prop: 'id', required: true },
        { type: 'input', label: '公告标题', prop: 'title', required: true },
        { type: 'input', label: '公告内容', prop: 'content', required: true },
        { type: 'date', label: '发布时间', prop: 'time', required: true },
    ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref<Partial<Announcement>>({});


const handleEdit = (row: Announcement) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};



const updateData = async (newForm) => {
    console.log(`更新公告`)
    console.log(`新更新的公告值为${newForm.value}`)
    const rid = newForm.value.id; // 获取公告ID
    // 构建公告数据对象，符合接口要求
    const announcementData = {
        title: newForm.value.title,   // 公告标题
        content: newForm.value.content, // 公告内容
        time: new Date().toISOString()  // 生成当前的 ISO 时间字符串
    };
    try {

        // 调用 updateAnnouncement，传递公告ID和公告数据
        await updateAnnouncement(rid, announcementData);
        ElMessage.success('更新成功'); // 更新成功的提示
        getData(); // 刷新数据
        closeDialog(); // 关闭对话框
    } catch (error) {
        ElMessage.error('更新失败'); // 更新失败的提示
    }
};


const addData = async (newForm) => {
    console.log(`修改`)
    // 构建公告数据对象，符合接口要求
    const announcementData = {
        title: newForm.value.title,   // 公告标题
        content: newForm.value.content, // 公告内容
        time: new Date().toISOString()  // 生成当前的 ISO 时间字符串
    };
    try {
        // 调用 addAnnouncement，传递公告ID和公告数据
        await addAnnouncement(announcementData);
        ElMessage.success('更新成功'); // 添加成功的提示
        getData(); // 刷新数据
        closeDialog(); // 关闭对话框
    } catch (error) {
        ElMessage.error('更新失败'); // 添加失败的提示
    }
};

const closeDialog = () => {
    visible.value = false;
    isEdit.value = false;
    rowData.value = {};
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
    row: {},
    list: [],
    column: 1
});
const handleView = (row: Announcement) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'id',
            label: '公告ID',
        },
        {
            prop: 'title',
            label: '公告标题',
        },
        {
            prop: 'content',
            label: '公告内容',
        },
        {
            prop: 'time',
            label: '发布时间',
        },
    ]
    visible1.value = true;
};

// 删除相关
const handleDelete = async (row: Announcement) => {
    try {
        await deleteAnnouncement(row.id)
        ElMessage.success('删除成功');
        getData();
    } catch (error) {
        ElMessage.error('删除失败', error);
    }
};




</script>

<style scoped></style>