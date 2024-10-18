<template>
    <div>
        <!-- <TableSearch :query="query" :options="searchOpt" :search="handleSearch" /> -->
        <div class="container">
            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :delFunc="handleDelete" :page-change="changePage" :editFunc="handleEdit">
                <template #toolbarBtn>
                    <el-button type="warning" :icon="CirclePlusFilled" @click="visible = true">新增</el-button>
                </template>
                <template #role.name="{ rows }">
                    {{ rows.role.name ? rows.role.name : '暂无角色' }}
                </template>
                <template #ban="{ rows }">
                    <el-tag type="success" v-if="rows.ban">是</el-tag>
                    <el-tag type="danger" v-else>否</el-tag>
                </template>
                <!-- <template #permissions="{ rows }">
                    <el-button type="primary" size="small" plain @click="handlePermission(rows)">管理</el-button>
                </template> -->
            </TableCustom>

        </div>
        <el-dialog :title="isEdit ? '编辑' : '新增'" v-model="visible" width="700px" destroy-on-close
            :close-on-click-modal="false" @close="closeDialog">
            <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
        </el-dialog>
        <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
            <TableDetail :data="viewData"></TableDetail>
        </el-dialog>
        <!-- <el-dialog title="权限管理" v-model="visible2" width="500px" destroy-on-close>
            <RolePermission :permiss-options="permissOptions" />
        </el-dialog> -->
    </div>
</template>

<script setup lang="ts" name="system-user">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { User } from '@/types/user';
import { fetchUserData } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import RolePermission from './role-permission.vue'
import axios from 'axios';

// // 查询相关
// const query = reactive({
//     name: '',
// });
// const searchOpt = ref<FormOptionList[]>([
//     { type: 'input', label: '用户名：', prop: 'name' }
// ])
// const handleSearch = () => {
//     changePage(1);
// };

// 表格相关
let columns = ref([
    { prop: 'id', label: '用户ID' },  // 确保表格中有正确的id
    { prop: 'name', label: '用户名' },  // 确保表格中有正确的id
    { prop: 'role.name', label: '角色' },
    { prop: 'ban', label: '状态' },
    { prop: 'permissions', label: '权限管理' },
    { prop: 'operator', label: '操作', width: 250 },
])
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})
const tableData = ref<User[]>([]);
const getData = async () => {
    const res = await fetchUserData()
    // 检查是否接收到正确的数据
    // console.log(res.data);  // 输出返回数据，检查 role 是否包含 name 属性
    tableData.value = JSON.parse(res.data);
    // page.total = res.data.pageTotal;
};
getData();

const changePage = (val: number) => {
    page.index = val;
    getData();
};

// 新增/编辑弹窗相关
let options = ref<FormOption>({
    labelWidth: '100px',
    span: 12,
    list: [
        { type: 'input', label: '用户名', prop: 'name', required: true },
        { type: 'input', label: '手机号', prop: 'phone', required: true },
        // { type: 'input', label: '密码', prop: 'password', required: true },
        { type: 'input', label: '邮箱', prop: 'email', required: true },
        { type: 'input', label: '角色', prop: 'role.name', required: true },
        { type: 'switch', label: '状态', prop: 'status', required: true, activeText: '启用', inactiveText: '禁用' },
    ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref({});
const handleEdit = (row: User) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};
const updateData = () => {
    closeDialog();
    getData();
};

const closeDialog = () => {
    visible.value = false;
    isEdit.value = false;
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
    row: {},
    list: []
});
const handleView = (row: User) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'id',
            label: '用户ID',
        },
        {
            prop: 'account',
            label: '用户账号',
        },
        // {
        //     prop: 'password',
        //     label: '密码',
        // },
        {
            prop: 'age',
            label: '年齡',
        },
        {
            prop: 'email',
            label: '邮箱',
        },
        {
            prop: 'phone',
            label: '电话',
        },
        {
            prop: 'role.name',
            label: '角色',
        },
        {
            prop: 'ban',
            label: '角色状态',
        },
    ]
    visible1.value = true;
};

// 删除相关
const handleDelete = async (row: User) => {
    try {
        await axios.delete(`/users/${row.id}`);  // 发送删除请求到后端
        ElMessage.success('删除成功');
        getData();  // 删除成功后刷新表格数据
    } catch (error) {
        ElMessage.error('删除失败');
        console.error('删除时出错:', error);
    }
};


// 权限管理弹窗相关
// const visible2 = ref(false);
// const permissOptions = ref({})
// const handlePermission = (row: User) => {
//     visible2.value = true;
//     permissOptions.value = {
//         id: row.id,
//         permiss: row.permiss
//     };
// }
</script>

<style scoped></style>