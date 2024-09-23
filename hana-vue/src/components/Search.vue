<template>
    <div class="-container">
        <el-input style="width: 240px" placeholder="Please input" />
        <el-icon class="search-icon">
            <Search />
        </el-icon>
    </div>
</template>

<script lang="ts" setup>

</script>

<style lang="scss" scoped>
.el-input {
    border: none;
    box-shadow: none;
}

.search-icon {
    margin-left: 8px;
    cursor: pointer;
    color: #333;
    transition: color 0.3s;
}

.search-icon:hover {
    color: #409eff;
}
</style>