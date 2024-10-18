<template>
    <div>
        <TableSearch :query="query" :options="searchOpt" :search="handleSearch" />
        <div class="container">

            <TableCustom :columns="columns" :tableData="tableData" :total="page.total" :viewFunc="handleView"
                :page-change="changePage" :editFunc="handleEdit" :deactivateFunc="handleDeactivate">
                <template #process="{ rows }">
                    <el-tag type="success" v-if="rows.process">通过</el-tag>
                    <el-tag type="danger" v-else>未通过</el-tag>
                </template>
                <template #cover="{ rows }">
                    <el-image style="width: 100px; height: 100px" :src="rows.cover" fit="cover" lazy>
                        <template #error>
                            <div class="image-slot">
                                <el-icon><icon-picture /></el-icon>
                            </div>
                        </template>
                    </el-image>
                </template>
            </TableCustom>
        </div>
        <el-dialog title="审核" v-model="visible" width="700px" destroy-on-close :close-on-click-modal="false"
            @close="closeDialog">
            <TableEdit :form-data="rowData" :options="options" :update="updateStatus"
                :animeOperator="isAnimeOperator" />

        </el-dialog>
        <el-dialog title="查看详情" v-model="visible1" width="700px" destroy-on-close>
            <TableDetail :data="viewData">
                <template #process="{ rows }">
                    <el-tag type="success" v-if="rows.process">通过</el-tag>
                    <el-tag type="danger" v-else>未通过</el-tag>
                </template>
                <template #cover="{ rows }">
                    <el-image style="width: 100px; height: 100px" :src="rows.cover" fit="cover" lazy />
                </template>
            </TableDetail>
        </el-dialog>
    </div>
</template>

<script setup lang="ts" name="system-anime">
import { ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import { Anime } from '@/types/anime';
import { fetchAnimeData, fetchSearchData, deactivateAnime, approveAnime, rejectAnime } from '@/api';
import TableCustom from '@/components/table-custom.vue';
import TableDetail from '@/components/table-detail.vue';
import RolePermission from './role-permission.vue'
import { CirclePlusFilled } from '@element-plus/icons-vue';
import { FormOption, FormOptionList } from '@/types/form-option';
import axios from 'axios';
import { Picture as IconPicture } from '@element-plus/icons-vue'

// 查询相关
const query = reactive({
    name: '',
});
const searchOpt = ref<FormOptionList[]>([
    { type: 'input', label: '动漫名称：', prop: 'name' }
])
const handleSearch = async () => {
    try {
        const response = await fetchSearchData(query.name)
        ElMessage.error('搜索成功');
        // 将返回的数据赋值给表格数据
        tableData.value = JSON.parse(response.data);
        // page.total = response.data.length;  // 更新总条数
    } catch (error) {
        ElMessage.error('搜索失败');
    }
    changePage(1);
};

// 表格相关
let columns = ref([
    { prop: 'id', label: '动漫ID' },  // 确保表格中有正确的id
    { prop: 'name', label: '动漫名称' },
    { prop: 'cover', label: '动漫封面' },
    { prop: 'episode', label: '动漫集数' },
    { prop: 'process', label: '审核状态' },
    // { prop: 'permissions', label: '权限管理' },
    { prop: 'animeOperator', label: '操作', width: 250 },
])
const page = reactive({
    index: 1,
    size: 10,
    total: 0,
})
const tableData = ref<Anime[]>([]);
const getData = async () => {
    const res = await fetchAnimeData()
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
        { type: 'switch', label: '动漫状态', prop: 'process', required: true, activeText: '已通过', inactiveText: '未通过' },
    ]
})
const visible = ref(false);
const isAnimeOperator = ref();
const rowData = ref<Partial<Anime>>({});
const handleEdit = (row: Anime) => {
    rowData.value = { ...row };
    isAnimeOperator.value = true;
    visible.value = true;
};

const updateStatus = async (newForm) => {
    console.log(`当前动漫id：${newForm.id}`)
    try {
        newForm.process ? approveAnime(newForm.id) : rejectAnime(newForm.id);
        ElMessage(newForm.process ? '通过审核' : '不通过审核');
        getData();
        closeDialog();
    } catch (error) {
        ElMessage.error('操作失败');
    }
};

const closeDialog = () => {
    visible.value = false;
    isAnimeOperator.value = false;
    rowData.value = {};
};

// 查看详情弹窗相关
const visible1 = ref(false);
const viewData = ref({
    row: {},
    list: [],
    column: 2
});
const handleView = (row: Anime) => {
    viewData.value.row = { ...row }
    viewData.value.list = [
        {
            prop: 'id',
            label: '动漫ID',
        },
        {
            prop: 'name',
            label: '动漫名称',
        },
        {
            prop: 'cover',
            label: '动漫封面',
        },
        {
            prop: 'title',
            label: '动漫标题',
        },
        {
            prop: 'process',
            label: '审核状态',
        },
        {
            prop: 'episode_name',
            label: '动漫集数',
        },
        {
            prop: 'url',
            label: '动漫链接',
        },
        {
            prop: 'likes',
            label: '点赞数',
        },
        {
            prop: 'views',
            label: '浏览数',
        },
    ]
    visible1.value = true;
};

// 下架相关
const handleDeactivate = async (row: Anime) => {
    try {
        await deactivateAnime(row.id)
        ElMessage.success('下架成功');
        getData();
    } catch (error) {
        ElMessage.error('下架失败', error);
    }
};

</script>

<style scoped></style>