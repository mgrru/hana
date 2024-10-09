<template>
    <el-container>
        <el-header>后台管理系统</el-header>
        <el-aside width="200px">菜单栏</el-aside>
        <el-main>
            <router-view></router-view> <!-- 显示具体模块 -->
        </el-main>
    </el-container>
</template>