<template>
  <div class="game-day-view">
    <div v-if="game" class="pa-4">
      <h1 class="text-h4 mb-12 text-center">Day {{ game.getLastRoundNumber() }}</h1>
      
      <div class="game-layout">
        <PlayerOvalLayout
          :players="game.players"
          :game="{ roles: game.roles }"
          :readOnly="true"
        />
      </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from 'vue';
import { gameApi } from '@/api/http';
import { Game } from '@/model/gameModels';
import routeBasedOnGameState from '@/router/routeGameState';
import PlayerOvalLayout from '@/components/PlayerOvalLayout.vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const game = ref<Game | null>(null);

// Fetch game data
const fetchGame = async () => {
  gameApi.getGame(props.id)
      .then(r => game.value = r);
};

// Lifecycle hooks
onMounted(() => {
  fetchGame();
});

onBeforeUnmount(() => {

});
</script>

<style scoped>
.game-day-view {
  max-width: 1400px;
  margin: 0 auto;
  padding: 16px;
}

.game-layout {
  margin: 32px 0;
}
</style>