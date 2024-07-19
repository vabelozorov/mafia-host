<script setup lang="ts">
import { ref } from 'vue';

const logo = ref('maf-100-100.png');
const title = ref('Mafia Host')

</script>

<template>
    <v-app-bar rounded app>

        <v-img
            class="mx-2"
            :src="logo"
            max-height="50" 
            max-width="50"
            contain
            @click="$router.push('/')"
            :class="'cursor-pointer'"
        ></v-img>

        <v-toolbar-title class="mytitle">{{title}}</v-toolbar-title>

    </v-app-bar>
</template>    