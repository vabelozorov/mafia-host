<script setup lang="ts">
import { gameApi } from '@/api/http';
import type { Game } from '@/model/gameModels';
import routeBasedOnGameState from '@/router/routeGameState';
import { ref, onMounted, onBeforeUnmount } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const game = ref<Game|null>(null);
const timeRemaining = ref(0);
const timerInterval = ref<number | null>(null);
const isTimerRunning = ref(false);

onMounted(() => {
  gameApi.getGame(props.id)
    .then(r => {
      game.value = r;
      timeRemaining.value = r.firstNightDuration;
    });
});

const startTimer = () => {
  if (!game.value || isTimerRunning.value) return;
  
  isTimerRunning.value = true;
  timeRemaining.value = game.value.firstNightDuration;
  
  timerInterval.value = window.setInterval(() => {
    if (timeRemaining.value > 0) {
      timeRemaining.value--;
    } else {
      stopTimer();
    }
  }, 1000);
};

const stopTimer = () => {
  if (timerInterval.value) {
    clearInterval(timerInterval.value);
    timerInterval.value = null;
  }
  isTimerRunning.value = false;
};

// Cleanup on component unmount
onBeforeUnmount(() => {
  stopTimer();
});

const skipToFirstDay = () => {
  gameApi.nextDay(props.id)
    .then(id => gameApi.getGame(id))
    .then(routeBasedOnGameState)
  ;
};
</script>

<template>
  <v-container v-if="game" class="text-center">
    <v-card class="mx-auto" max-width="400">
      <v-card-title class="text-h4 pb-4">
        First Night
      </v-card-title>

      <v-card-text>
        <div class="text-h2 font-weight-bold mb-6">
          {{ timeRemaining }}
        </div>

        <v-row justify="center" class="mt-4">
          <v-col cols="12" sm="6">
            <v-btn
              color="primary"
              block
              :disabled="isTimerRunning"
              @click="startTimer"
            >
              Start Timer
            </v-btn>
          </v-col>
          <v-col cols="12" sm="6">
            <v-btn
              color="secondary"
              block
              @click="skipToFirstDay"
            >
              Skip to First Day
            </v-btn>
          </v-col>
        </v-row>
      </v-card-text>
    </v-card>
  </v-container>
</template>