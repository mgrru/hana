<!-- EpisodeSelector.vue -->
<template>
    <div class="episode-selector">
        <el-button v-for="episode in episodes" :key="episode.id" @click="onEpisodeClick(episode.title)">
            {{ episode.title }}
        </el-button>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

const props = defineProps({
    episodes: {
        type: Array,
        required: true,
    }
});

const emit = defineEmits(['select-episode']);

const onEpisodeClick = (episode_name) => {
    emit('select-episode', episode_name);
};
</script>

<style scoped>
.episode-selector {
    display: flex;
    gap: 10px;
    margin-top: 20px;
}

.episode-selector .el-button {
    flex: 1;
}
</style>