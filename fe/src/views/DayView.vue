<script setup lang="ts">
import { gameApi } from '@/api/http';
import { Game } from '@/model/gameModels';
import routeBasedOnGameState from '@/router/routeGameState';
import { ref, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const game = ref<Game|null>(null);

onMounted(() => {
  gameApi.getGame(props.id)
    .then(r => {
      game.value = r;

    });
});

</script>

<template>
    <h1 v-if="game">Game ID {{ game.id }}, day {{ game.getLastRoundNumber() }}</h1>
</template>