<script setup lang="ts">
import { gameApi } from '@/api/http';
import type { Game, } from '@/model/gameModels';
import { ref, computed, onMounted, watch } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const game = ref<Game|null>(null);

onMounted(() => {
  gameApi.getGame(props.id)
    .then(r => game.value = r);
});
</script>

<template>
    <h1 v-if="game">Running the timer for {{ game.id }}</h1>
</template>