<template>
    <div>
        <button @click="toggleLike">
            点赞数{{ animesInfo.likes }}
        </button>
        <button @click="toggleFavorite">
            {{ animesInfo.isFavorited ? '取消收藏' : '收藏' }}:{{ animesInfo.favorites }}
        </button>

    </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useToolBarStore } from '../store/toolBarStore';
import { storeToRefs } from 'pinia';
import { useAnimeStore } from '../store/animeStore.js';

const toolBarStore = useToolBarStore();
const animeStore = useAnimeStore();

const { animesInfo } = storeToRefs(animeStore);

const props = defineProps({
    name: String,
    rid: Number,
});
const toggleFavorite = () => {
    toolBarStore.toggleFavorite(props.rid);
};

onMounted(() => {
    animeStore.fetchAnimeData
});
</script>

<style scoped>
button {
    background-color: #42b983;
    color: white;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-right: 10px;
}
</style>
