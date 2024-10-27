<template>
  <v-container class="fill-height">
    <v-row justify="center" align="center">
      <v-col cols="12">
        <v-card class="pa-4">
          <v-card-title class="text-center text-h5 mb-12">
            Assign Roles to Players
          </v-card-title>

          <div v-if="game" class="mb-12 position-relative">
            <!-- Center Buttons - Absolutely positioned over the oval layout -->
            <div
              class="center-buttons"
            >
              <v-btn
                color="primary"
                variant="outlined"
                :disabled="!allRolesUnassigned"
                @click="assignRandomRoles"
              >
                Random Roles
              </v-btn>
              <v-btn
                color="error"
                variant="outlined"
                :disabled="allRolesUnassigned"
                @click="clearAllRoles"
              >
                Clear Roles
              </v-btn>
            </div>

            <player-oval-layout
              :players="players"
              :game="{roles: game.roles}"
              :selected-player-index="selectedPlayerIndex"
              @player-click="selectPlayerForSwap"
              @update-role="updateRole"
            />
            
          </div>

          <v-card-actions class="justify-center mt-4">
            <v-btn
              color="primary"
              :disabled="!isValidConfiguration"
              @click="submitConfiguration"
            >
              Start First Night
            </v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>

    <v-snackbar
      v-model="snackbar.show"
      :color="snackbar.color"
      :timeout="3000"
    >
      {{ snackbar.text }}
      
      <template v-slot:actions>
        <v-btn
          color="white"
          variant="text"
          @click="snackbar.show = false"
        >
          Close
        </v-btn>
      </template>
    </v-snackbar>
  </v-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, watch } from 'vue';
import { gameApi } from '@/api/http';
import type { Game, Player } from '@/model/gameModels';
import routeBasedOnGameState from '@/router/routeGameState';
import PlayerOvalLayout from '@/components/PlayerOvalLayout.vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

const game = ref<Game | null>(null);
const players = ref<Player[]>([]);
const selectedPlayerIndex = ref<number | null>(null);
const remainingRoles = ref<Record<string, number>>({});

const totalPlayers = computed(() => {
  if (!game.value) return 0;
  return game.value.roles.reduce((sum, role) => sum + role.count, 0);
});

// Initialize players and remaining roles when game data is loaded
watch(game, (newGame) => {
  if (newGame) {
    remainingRoles.value = Object.fromEntries(
      newGame.roles.map(role => [role.name, role.count])
    );

    players.value = Array(totalPlayers.value)
      .fill(undefined)
      .map((_, index) => ({
        name: '',
        role: null,
        position: index
      }));
  }
}, { immediate: true });

const snackbar = ref({
  show: false,
  color: 'success',
  text: ''
});

const showNotification = (text: string, color: string = 'success') => {
  snackbar.value = {
    show: true,
    color,
    text
  };
};

const allRolesUnassigned = computed(() => {
  return players.value.every(player => player.role === null);
});

function generatePlayerLabel(index: number): string {
  const indexStr = (index + 1).toString().padStart(2, '0');
  const letters = Array(4)
    .fill(null)
    .map(() => String.fromCharCode(65 + Math.floor(Math.random() * 26)))
    .join('');
  return `${indexStr}-${letters}`;
}

const assignRandomRoles = () => {
  if (!game.value) return;

  const allRoles: string[] = game.value.roles.flatMap(role => 
    Array(role.count).fill(role.name)
  );

  const shuffledRoles = [...allRoles].sort(() => Math.random() - 0.5);

  players.value.forEach((player, index) => {
    player.name = generatePlayerLabel(index);
    updateRole(index, shuffledRoles[index]);
  });

  showNotification('Roles assigned randomly');
};

const clearAllRoles = () => {
  if (!game.value) return;

  remainingRoles.value = Object.fromEntries(
    game.value.roles.map(role => [role.name, role.count])
  );
  
  players.value.forEach(player => {
    player.role = null;
  });

  selectedPlayerIndex.value = null;

  showNotification('All roles cleared');
};

const selectPlayerForSwap = (index: number) => {
  if (selectedPlayerIndex.value === null) {
    if (players.value[index].role) {
      selectedPlayerIndex.value = index;
    }
  } else if (selectedPlayerIndex.value === index) {
    selectedPlayerIndex.value = null;
  } else {
    const firstPlayer = players.value[selectedPlayerIndex.value];
    const secondPlayer = players.value[index];

    if (firstPlayer.role && secondPlayer.role) {
      const tempRole = firstPlayer.role;
      firstPlayer.role = secondPlayer.role;
      secondPlayer.role = tempRole;
      showNotification('Roles swapped successfully');
    }

    selectedPlayerIndex.value = null;
  }
};

const updateRole = (playerIndex: number, newRole: string | null) => {
  const oldRole = players.value[playerIndex].role;
  
  if (oldRole) {
    remainingRoles.value[oldRole]++;
  }

  if (newRole) {
    remainingRoles.value[newRole]--;
  }
  
  players.value[playerIndex] = {
    ...players.value[playerIndex],
    role: newRole
  };
};

const isValidConfiguration = computed(() => {
  if (!game.value) return false;
  
  const allPlayersNamed = players.value.every(p => p.name.trim());
  const allRolesAssigned = players.value.every(p => p.role);
  const allRolesUsed = Object.values(remainingRoles.value).every(count => count === 0);
  
  return allPlayersNamed && allRolesAssigned && allRolesUsed;
});

const submitConfiguration = async () => {
  try {
    if (!isValidConfiguration.value) return;

    gameApi.assignRoles(
      props.id,
      players.value.map(p => ({
        name: p.name!,
        role: p.role!
      }))
    )
    .then(() => gameApi.getGame(props.id))
    .then(routeBasedOnGameState)
    ;

  } catch (error) {
    if (error instanceof Error) {
      showNotification('Error saving configuration: ' + error.message, 'error');
    } else {
      showNotification('An unknown error occurred', 'error');
    }
  }
};

onMounted(() => {
  gameApi.getGame(props.id)
    .then(r => game.value = r);
});
</script>

<style scoped>
.center-buttons {
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
  z-index: 3;
  display: flex;
  gap: 8px;
  background: rgba(255, 255, 255, 0.9);
  padding: 8px;
  border-radius: 8px;
}

.oval-layout-wrapper {
  padding: 24px;  /* Add padding to ensure full circle visibility */
}
</style>