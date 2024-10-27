<template>
    <div class="position-relative mx-auto" :style="containerStyle">
      <template v-for="(player, index) in players" :key="index">
        <div
          :style="{
            position: 'absolute',
            left: `${getPosition(index, players.length).x}px`,
            top: `${getPosition(index, players.length).y}px`,
            transform: 'translate(-50%, -50%)',
            zIndex: hoveredIndex === index ? 2 : 1
          }"
          @mouseenter="hoveredIndex = index"
          @mouseleave="hoveredIndex = null"
        >
          <v-card
            :color="getTeamColor(player.role)"
            class="player-card"
            width="150"
            @click="!readOnly && onPlayerClick(index)"
            :class="{
              'selected-for-swap': selectedPlayerIndex === index,
              'readonly': readOnly
            }"
          >
            <div class="position-number">{{ player.position + 1 }}</div>
            <v-card-text>
              <v-text-field
                v-model="player.name"
                label="Name"
                variant="outlined"
                density="compact"
                :readonly="readOnly"
              ></v-text-field>
              
              <v-select
                v-if="!readOnly"
                :model-value="player.role"
                :items="availableRoles(player)"
                label="Role"
                variant="outlined"
                density="compact"
                item-title="name"
                item-value="name"
                @update:model-value="(newRole) => updatePlayerRole(index, newRole)"
              ></v-select>
              <v-text-field
                v-else
                :model-value="player.role"
                label="Role"
                variant="outlined"
                density="compact"
                readonly
              ></v-text-field>
            </v-card-text>
          </v-card>
        </div>
      </template>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ref, computed } from 'vue';
  import type { Game, Player } from '@/model/gameModels';
  
// Update the type to only include what we need from Game
interface GameInfo {
  roles: {
    name: string;
    team: "black" | "red";
    count: number;
  }[];
}

const props = defineProps<{
  players: Player[];
  game: GameInfo | null;  // Changed from Game to GameInfo
  readOnly?: boolean;
  selectedPlayerIndex?: number | null;
}>();
  const emit = defineEmits<{
    'update:players': [players: Player[]];
    'player-click': [index: number];
    'update-role': [playerIndex: number, newRole: string | null];
  }>();
  
  // Container dimensions
  const CONTAINER_WIDTH = 1000;
  const CONTAINER_HEIGHT = 600;
  const X_RADIUS = 400;
  const Y_RADIUS = 250;
  
  const containerStyle = {
    width: `${CONTAINER_WIDTH}px`,
    height: `${CONTAINER_HEIGHT}px`,
    position: 'relative' as const
  };
  
  const hoveredIndex = ref<number | null>(null);
  
  const getPosition = (index: number, total: number) => {
    const angle = (index / total) * 2 * Math.PI;
    const adjustedAngle = angle - Math.PI / 2;
    return {
      x: CONTAINER_WIDTH / 2 + X_RADIUS * Math.cos(adjustedAngle),
      y: CONTAINER_HEIGHT / 2 + Y_RADIUS * Math.sin(adjustedAngle)
    };
  };
  
  const getTeamColor = (roleName: string | null): string => {
    if (!roleName || !props.game) return 'grey-lighten-3';
    const role = props.game.roles.find(r => r.name === roleName);
    return role?.team === 'red' ? 'red-lighten-4' : 'grey-darken-3';
  };
  
  const availableRoles = (player: Player) => {
    if (!props.game) return [];
    return props.game.roles;
  };
  
  const onPlayerClick = (index: number) => {
    emit('player-click', index);
  };
  
  const updatePlayerRole = (playerIndex: number, newRole: string | null) => {
    emit('update-role', playerIndex, newRole);
  };
  </script>
  
  <style scoped>
  .player-card {
    transition: all 0.3s ease;
    cursor: pointer;
    position: relative;
  }
  
  .player-card:hover:not(.readonly) {
    transform: scale(1.05);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  }
  
  .readonly {
    cursor: default;
  }
  
  .selected-for-swap {
    border: 2px solid #1976D2;
    box-shadow: 0 0 8px rgba(25, 118, 210, 0.5);
  }
  
  .position-number {
    position: absolute;
    top: -12px;
    left: -12px;
    background-color: #1976D2;
    color: white;
    border-radius: 50%;
    width: 24px;
    height: 24px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    font-size: 14px;
    z-index: 1;
  }
  </style>