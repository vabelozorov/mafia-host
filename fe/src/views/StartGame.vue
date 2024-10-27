<script setup lang="ts">
import { useGameApi } from '@/useGlobalProperties';
import routeBasedOnGameState from '@/router/routeGameState.js'

const gameApi = useGameApi();

function newGame() {

  gameApi.createGame()
    .then(id => gameApi.getGame(id))
    .then(routeBasedOnGameState)

}

</script>

<template>
  <v-row justify="center" align="center">
    <v-col cols="2">
      <v-hover v-slot="{ isHovering, props }">

        <v-btn block size="150" rounded="sm" variant="outlined" color="black" @click="newGame"
          :elevation="isHovering ? 12 : 2" v-bind="props">

          <template v-slot:prepend>
            <v-icon icon="mdi-plus" size="100"></v-icon>
          </template>

          New Game

        </v-btn>

      </v-hover>
    </v-col>
  </v-row>
</template>
