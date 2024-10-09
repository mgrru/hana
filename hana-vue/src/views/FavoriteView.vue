<template>
    <el-container class="container">
        <el-header height="auto">
            <HeaderView></HeaderView>
        </el-header>
        <el-container class="main-container">
            <el-main>
                <Favorite />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup>
import HeaderView from './HeaderView.vue';
import Favorite from '../components/Favorite.vue';
</script>

<style lang="scss" scoped></style>