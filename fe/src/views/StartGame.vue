<script setup lang="ts">
import { useHttp } from '@/useGlobalProperties';
import type { Game } from '@/model/gameModels'
import routeForState from '@/router/routeGameState.js'

const http = useHttp();

function newGame() {

  http.createGame(undefined)
    .then(r => r.id)
    .then(id => http.getGame({ queries: { id: id } }))
    .then(r => ({ id: r.id, state: r.state } as Game ))
    .then(routeForState)

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
