<template>
    <el-container class="container">
        <el-header height="auto">
            <HeaderView></HeaderView>
        </el-header>
        <el-container class="main-container">
            <el-main>
                <UserInfo></UserInfo>
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import HeaderView from './HeaderView.vue';
import UserInfo from '../components/UserInfo.vue'
</script>

<style lang="scss" scoped></style>