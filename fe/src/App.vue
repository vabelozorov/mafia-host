<script setup lang="ts">
import AppBar from '@/components/AppBar.vue'


</script>

<template>

     <v-app>
      <AppBar />

      <v-main class="d-flex align-center justify-center" >
           <RouterView />
      </v-main>

    </v-app>
</template>