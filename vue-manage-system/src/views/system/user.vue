<template>
    <div>
        <!-- <TableSearch :query="query" :options="searchOpt" :search="handleSearch" /> -->
        <div class="container">
            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :page-change="changePage" :editFunc="handleEdit">
                <template #role.name="{ rows }">
                    {{ rows.role.name ? rows.role.name : '暂无角色' }}
                </template>
                <template #ban="{ rows }">
                    <el-tag type="danger" v-if="rows.ban">禁用</el-tag>
                    <el-tag type="success" v-else>正常</el-tag>
                </template>
            </TableCustom>

        </div>
        <el-dialog title="编辑" v-model="visible" width="700px" destroy-on-close :close-on-click-modal="false"
            @close="closeDialog">
            <TableEdit :form-data="rowData" :options="options" :edit="isEdit" :update="updateData" />
        </el-dialog>
        <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
            <TableDetail :data="viewData">
                <template #ban="{ rows }">
                    <el-tag type="danger" v-if="rows.ban">禁用</el-tag>
                    <el-tag type="success" v-else>正常</el-tag>
                </template>
            </TableDetail>
        </el-dialog>
        
    </div>
</template>

<script setup lang="ts" name="system-user">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { User } from '@/types/user';
import { fetchUserData, unbanUser, banUser, updateUserRole } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import TableSearch from '@/components/table-search.vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import RolePermission from './role-permission.vue'

// 表格相关
let columns = ref([
    { prop: 'id', label: '用户ID' },  // 确保表格中有正确的id
    { prop: 'name', label: '用户名' },  // 确保表格中有正确的id
    { prop: 'account', label: '用户账号' },
    { prop: 'role.name', label: '角色' },
    { prop: 'ban', label: '状态' },
    // { prop: 'permissions', label: '权限管理' },
    { prop: 'userOperator', label: '操作', width: 250 },
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
        {
            type: 'switch', label: '状态', prop: 'ban', required: true, activeText: '禁用', inactiveText: '启用',
            style: '--el-switch-on-color:#ff4949 ; --el-switch-off-color: #13ce66',
        },
        {
            type: 'roleSelect', label: '用户角色', prop: 'role.id', required: true,
            opts: [
                { label: '管理员', value: '1' },
                { label: '普通用户', value: '2' }
            ],
            disabled: false
        },
    ]
})
const visible = ref(false);
const isEdit = ref(false);
const rowData = ref<Partial<User>>({});
const handleEdit = (row: User) => {
    rowData.value = { ...row };
    isEdit.value = true;
    visible.value = true;
};
const updateData = async (newForm) => {
    console.log('New Form Data:', newForm.value); // 添加这一行
    closeDialog();
    try {
        // 先执行禁用或解封操作
        if (newForm.value.ban) {
            await banUser(newForm.value.id);
            ElMessage.success('用户已禁用');
        } else {
            await unbanUser(newForm.value.id);
            ElMessage.success('用户已解封');
        }

        // 更新用户角色
        await updateUserRole(newForm.value.id, newForm.value.role.id);
        ElMessage.success('用户角色已更新');

        // 刷新数据
        getData();
    } catch (error) {
        ElMessage.error('操作失败: ' + (error.message || '未知错误'));
    } finally {
        closeDialog();
    }
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



</script>

<style scoped></style>