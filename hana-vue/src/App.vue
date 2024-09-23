<script setup>
</script>

<template>
  <!-- keep-alive来缓存页面，避免重复加载 -->
  <router-view v-slot="{ Component }">
    <keep-alive>
      <component :is="Component" />
    </keep-alive>
  </router-view>
</template>

<style scoped></style>
