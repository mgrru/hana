<template>
    <el-container class="container">
        <el-header height="auto">
            <HeaderView></HeaderView>
        </el-header>
        <el-container class="main-container">
            <el-main>
                <Recommend></Recommend>
                <VideoList></VideoList>
            </el-main>
            <el-aside width="70px">
                <Aside></Aside>
            </el-aside>
        </el-container>
    </el-container>



</template>


<script setup lang="ts">
import Recommend from '../components/Recommend.vue';
import VideoList from './VideoList.vue';
import HeaderView from './HeaderView.vue';
import Aside from '../components/Aside.vue';
</script>

<style scoped>
.container {
    margin: 0;
    padding: 0;
    background: #ffffff;
    display: flex;
    flex-direction: column;
    /* 让 header 和 main-container 垂直排列 */
}


.main-container {
    padding: 0 20px;
}

.el-main {
    padding: 0 2px;
}
</style>