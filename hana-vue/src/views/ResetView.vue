<template>
    <el-container class="manuscript-container">
        <el-header class="header">
            <Header />
        </el-header>
        <el-container class="content">
            <el-main>
                <resetPwd />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import Header from './HeaderView.vue';
import resetPwd from '../components/reset-pwd.vue';
</script>
