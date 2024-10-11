<template>
    <div>
        <el-tree class="mgb10" ref="tree" :data="data" node-key="id" default-expand-all show-checkbox
            :default-checked-keys="checkedKeys" />
        <el-button type="primary" @click="onSubmit">保存权限</el-button>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElTree } from 'element-plus';
import { menuData } from '@/components/menu';

const props = defineProps({
    permissOptions: {
        type: Object,
        required: true,
    },
});

const menuObj = ref({});
// const data = menuData.map((item) => {
//     if (item.children) {
//         menuObj.value[item.id] = item.children.map((sub) => sub.id);
//     }
//     return {
//         id: item.id,
//         label: item.title,
//         children: item.children?.map((child) => {
//             return {
//                 id: child.id,
//                 label: child.title,
//             };
//         }),
//     };
// });

const getTreeData = (data: any[]) => {
    return data.map((item) => {
        const obj: any = {
            id: item.id,
            label: item.title,
        };
        if (item.children) {
            menuObj.value[item.id] = item.children.map((sub: any) => sub.id);
            obj.children = getTreeData(item.children);
        }
        return obj;
    });
};
const data = getTreeData(menuData);
const checkData = (data: string[]) => {
    return data.filter((item) => {
        return !menuObj.value[item] || data.toString().includes(menuObj.value[item].toString());
    });
};
// 获取当前权限
// const checkedKeys = ref<string[]>(checkData(props.permissOptions.permiss));
// const checkedKeys = ref<string[]>(props.permissOptions.permiss.map((p: any) => p.id));
// 直接使用权限 ID 作为 checkedKeys
const checkedKeys = ref<string[]>([]);

if (props.permissOptions?.permiss && Array.isArray(props.permissOptions.permiss)) {
    checkedKeys.value = props.permissOptions.permiss.map((p: any) => p.id);
} else {
    console.warn('permissOptions.permiss is not an array or is undefined.');
}

// 保存权限
const tree = ref<InstanceType<typeof ElTree>>();
// const onSubmit = () => {
//     // 获取选中的权限
//     const keys = [...tree.value!.getCheckedKeys(false), ...tree.value!.getHalfCheckedKeys()] as number[];
//     console.log(keys);
// };
const onSubmit = () => {
    // 获取所有选中的节点，包括完全勾选和部分勾选的
    const checkedKeys = tree.value?.getCheckedKeys(false) || [];  // 完全勾选的节点
    const halfCheckedKeys = tree.value?.getHalfCheckedKeys() || [];  // 半勾选的节点

    // 合并完全勾选和半勾选的节点 ID
    const keys = [...checkedKeys, ...halfCheckedKeys] as number[];

    console.log('保存的权限 ID:', keys);
    // 在这里可以进行后续保存逻辑，例如通过 API 提交
};

</script>

<style scoped></style>
