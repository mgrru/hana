<template>
	<el-descriptions :title="title" :column="column" border>
		<el-descriptions-item v-for="item in list" :span="item.span">
			<template #label> {{ item.label }} </template>

			<slot :name="item.prop" :rows="row">
				{{ item.value || row[item.prop] || getNestedValue(row, item.prop) }}
			</slot>
		</el-descriptions-item>
	</el-descriptions>
</template>

<script lang="ts" setup>
// 定义 getNestedValue  函数
const getNestedValue = (obj, path) => {
	return path.split('.').reduce((acc, part) => acc && acc[part], obj);
};
const props = defineProps({
	data: {
		type: Object,
		required: true,
	}
});
const { row, title, column = 2, list } = props.data;

</script>
