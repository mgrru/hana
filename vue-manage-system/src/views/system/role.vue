<template>
    <div>
        <!-- <TableSearch :query="query" :options="searchOpt" :search="handleSearch" /> -->
        <div class="container">

            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :delFunc="handleDelete" :page-change="changePage" :editFunc="handleEdit">
                <template #toolbarBtn>
                    <el-button type="warning" :icon="CirclePlusFilled" @click="visible = true">新增</el-button>
                </template>
                <template #status="{ rows }">
                    <el-tag type="success" v-if="rows.status">启用</el-tag>
                    <el-tag type="danger" v-else>禁用</el-tag>
                </template>
                <template #permissions="{ rows }">
                    <el-button type="primary" size="small" plain @click="handlePermission(rows)">管理</el-button>
                </template>
            </TableCustom>
        </div>
        <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
            :close-on-click-modal="false" @close="closeDialog">
            <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" :add="addData" />
        </el-dialog>
        <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
            <TableDetail :data="viewData">
                <template #status="{ rows }">
                    <el-tag type="success" v-if="rows.status">启用</el-tag>
                    <el-tag type="danger" v-else>禁用</el-tag>
                </template>
            </TableDetail>
        </el-dialog>
        <el-dialog title="权限管理" v-model="visible2" width="500px" destroy-on-close>
            <RolePermission :permiss-options="permissOptions" />
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="system-role">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Role } from '@/types/role';
import { fetchRoleData, addRole, updateRole, deleteRole } from '@/api';
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
//     { type: 'input', label: '角色名称：', prop: 'name' }
// ])
// const handleSearch = () => {
//     changePage(1);
// };

// 表格相关
let columns = ref([
    { prop: 'id', label: '角色ID' },  // 确保表格中有正确的id
    { prop: 'name', label: '角色名称' },
    // { prop: 'key', label: '角色标识' },
    // { prop: 'status', label: '状态' },
    { prop: 'permissions', label: '权限管理' },
    { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})
const tableData = ref<Role[]>([]);
const getData = async () => {
    const res = await fetchRoleData()
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
        { type: 'input', label: '角色ID', prop: 'id', required: true },
        { type: 'input', label: '角色名称', prop: 'name', required: true },
        // { type: 'input', label: '角色标识', prop: 'key', required: true },
        // { type: 'switch', label: '状态', prop: 'status', required: false, activeText: '启用', inactiveText: '禁用' },
    ]
})
const visible = ref(false);
const isEdit = ref(false);
// const rowData = ref({});
const rowData = ref<Partial<Role>>({});
const handleEdit = (row: Role) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};
const updateData = async (newForm) => {
    console.log(`更新role`)
    const rid = newForm.value.id; // 获取ID
    // 构建数据对象，符合接口要求
    const roleData = {
        name: newForm.value.name,
    };
    await updateRole(rid, roleData)
    ElMessage.success('更新成功');
    getData();
    closeDialog();
    ElMessage.error('更新失败');
};


const addData = async (newForm) => {
    console.log(`修改role`)
    // 构建数据对象，符合接口要求
    const roleData = {
        name: newForm.value.name,
    };
    try {
        await addRole(roleData)
        ElMessage.success('更新成功');
        getData();
        closeDialog();
    } catch (error) {
        ElMessage.error('更新失败');
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
const handleView = (row: Role) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'id',
            label: '角色ID',
        },
        {
            prop: 'name',
            label: '角色名称',
        },
    ]
    visible1.value = true;
};

// 删除相关
const handleDelete = async (row: Role) => {
    try {
        await deleteRole(row.id)
        ElMessage.success('删除成功');
        getData();
    } catch (error) {
        ElMessage.error('删除失败', error);
    }
};



// 权限管理弹窗相关
const visible2 = ref(false);
const permissOptions = ref({})
const handlePermission = (row: Role) => {
    visible2.value = true;
    permissOptions.value = {
        id: row.id,
        // permiss: row.permiss
    };
}
</script>

<style scoped></style>