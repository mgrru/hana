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

<script setup lang="ts" name="system-section">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Section } from '@/types/section';
import { fetchSectionsData, addSection, updateSectiont, deleteSection } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import axios from 'axios';

// // 查询相关
// const query = reactive({
//     name: '',
// });
// const searchOpt = ref<FormOptionList[]>([
//     { type: 'input', label: '板块名称：', prop: 'name' }
// ])
// const handleSearch = () => {
//     changePage(1);
// };

// 表格相关
let columns = ref([
    { prop: 'id', label: '板块ID' },  // 确保表格中有正确的id
    { prop: 'name', label: '板块名称' },
    { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})
const tableData = ref<Section[]>([]);
const getData = async () => {
    const res = await fetchSectionsData()
    console.log(res.data)
    tableData.value = JSON.parse(res.data);
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
        { type: 'input', label: '板块ID', prop: 'id', required: true },
        { type: 'input', label: '板块名称', prop: 'name', required: true },
    ]
})
// const addOptions = ref<FormOption>({
//     labelWidth: '100px',
//     span: 24,
//     list: [
//         { type: 'input', label: '板块ID', prop: 'id', required: true },
//         { type: 'input', label: '板块名称', prop: 'name', required: true },
//     ]
// })
const visible = ref(false);
const isEdit = ref(false);
// const rowData = ref({});
const rowData = ref<Partial<Section>>({});
const handleEdit = (row: Section) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};

//更新函数
const updateData = async (newForm) => {
    console.log(`更新`)

    const rid = newForm.value.id; // 获取板块ID 应该是row.id
    // 构建板块数据对象，符合接口要求
    const sectionData = { name: newForm.value.name };
    try {
        // 调用 updateSectiont，传递板块ID板块数据
        await updateSectiont(rid, sectionData);
        ElMessage.success('更新成功'); // 更新成功的提示
        getData(); // 刷新数据
        closeDialog(); // 关闭对话框
    } catch (error) {
        ElMessage.error('更新失败'); // 更新失败的提示
    }
};

//新增数据函数
const addData = async (newForm) => {
    console.log(`修改`)
    // 构建板块数据对象，匹配接口要求
    const sectionData = { name: newForm.value.name };
    try {
        // 调用 addSection，传递板块数据
        await addSection(sectionData);
        ElMessage.success('添加成功'); // 添加成功的提示
        getData(); // 刷新数据
        closeDialog(); // 关闭对话框
    } catch (error) {
        ElMessage.error('添加失败'); // 添加失败的提示
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
const handleView = (row: Section) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'id',
            label: '板块ID',
        },
        {
            prop: 'name',
            label: '板块名称',
        },
    ]
    visible1.value = true;
};

// 删除相关
const handleDelete = async (row: Section) => {
    try {
        await deleteSection(row.id)
        ElMessage.success('删除成功');
        getData();
    } catch (error) {
        ElMessage.error('删除失败');
    }
};




</script>

<style scoped></style>