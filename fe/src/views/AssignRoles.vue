<template>
  <v-container class="fill-height">
    <v-row justify="center" align="center">
      <v-col cols="12">
        <v-card class="pa-4">
          <v-card-title class="text-center text-h5 mb-12">
            Assign Roles to Players
          </v-card-title>

          <!-- Oval Player Layout -->
          <div class="position-relative mx-auto mb-12" :style="containerStyle">
            <!-- Center Buttons -->
            <div
              :style="{
                position: 'absolute',
                left: '50%',
                top: '50%',
                transform: 'translate(-50%, -50%)',
                zIndex: 3,
                display: 'flex',
                gap: '8px'
              }"
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
                  @click="selectPlayerForSwap(index)"
                  :class="{ 'selected-for-swap': selectedPlayerIndex === index }"
                >
                  <v-card-text>
                    <v-text-field
                      v-model="player.name"
                      label="Name"
                      variant="outlined"
                      density="compact"
                    ></v-text-field>
                    
                    <v-select
                      :model-value="player.role"
                      :items="getAvailableRolesForPlayer(player)"
                      label="Role"
                      variant="outlined"
                      density="compact"
                      @update:model-value="(newRole) => updateRole(index, newRole)"
                    ></v-select>
                  </v-card-text>
                </v-card>
              </div>
            </template>
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
import { gameApi } from '@/api/http';
import type { Game, GameState } from '@/model/gameModels';
import { ref, computed, onMounted } from 'vue';

const props = defineProps({
  id: {
    type: Number,
    required: true
  }
});

// Define types
type Role = 'citizen' | 'sheriff' | 'don' | 'mafia' | null;

interface Player {
  name: string;
  role: Role;
}

interface RoleCount {
  citizen: number;
  sheriff: number;
  don: number;
  mafia: number;
}

// Constants
const TOTAL_PLAYERS = 12;
const INITIAL_ROLES: RoleCount = {
  citizen: 7,
  sheriff: 1,
  don: 1,
  mafia: 3
};

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

// Player state
const players = ref<Player[]>(
  Array(TOTAL_PLAYERS).fill(undefined).map(() => ({
    name: '',
    role: null
  }))
);

const hoveredIndex = ref<number | null>(null);
const selectedPlayerIndex = ref<number | null>(null);

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

// Track remaining roles
const remainingRoles = ref<RoleCount>({ ...INITIAL_ROLES });

// Available roles for select
const availableRoles = [
  { title: 'Citizen', value: 'citizen' },
  { title: 'Sheriff', value: 'sheriff' },
  { title: 'Don', value: 'don' },
  { title: 'Mafia', value: 'mafia' }
] as const;

// New function to check if all roles are unassigned
const allRolesUnassigned = computed(() => {
  return players.value.every(player => player.role === null);
});

function generatePlayerLabel (index: number): string {
  // Get 2-digit representation of index (with leading zero if needed)
  const indexStr = (index + 1).toString().padStart(2, '0');
  
  // Generate 4 random letters
  const letters = Array(4)
    .fill(null)
    .map(() => String.fromCharCode(65 + Math.floor(Math.random() * 26)))
    .join('');
    
  return `${indexStr}-${letters}`;
}

// Function to assign random roles
const assignRandomRoles = () => {
  // Create array of all roles based on INITIAL_ROLES
  const allRoles: Role[] = [];
  Object.entries(INITIAL_ROLES).forEach(([role, count]) => {
    for (let i = 0; i < count; i++) {
      allRoles.push(role as Role);
    }
  });

  // Shuffle array
  const shuffledRoles = [...allRoles].sort(() => Math.random() - 0.5);

  // Assign roles to players
  players.value.forEach((player, index) => {
    player.name = generatePlayerLabel(index);
    updateRole(index, shuffledRoles[index]);
  });

  showNotification('Roles assigned randomly');
};

// Function to clear all roles
const clearAllRoles = () => {
  // Reset remaining roles
  remainingRoles.value = { ...INITIAL_ROLES };
  
  // Clear all player roles
  players.value.forEach(player => {
    player.role = null;
  });

  // Clear selected player
  selectedPlayerIndex.value = null;

  showNotification('All roles cleared');
};

// Function to handle role swapping
const selectPlayerForSwap = (index: number) => {
  if (selectedPlayerIndex.value === null) {
    // First player selected
    if (players.value[index].role) {
      selectedPlayerIndex.value = index;
    }
  } else if (selectedPlayerIndex.value === index) {
    // Deselect if clicking the same player
    selectedPlayerIndex.value = null;
  } else {
    // Second player selected - perform swap
    const firstPlayer = players.value[selectedPlayerIndex.value];
    const secondPlayer = players.value[index];

    if (firstPlayer.role && secondPlayer.role) {
      // Swap roles
      const tempRole = firstPlayer.role;
      firstPlayer.role = secondPlayer.role;
      secondPlayer.role = tempRole;
      showNotification('Roles swapped successfully');
    }

    // Clear selection
    selectedPlayerIndex.value = null;
  }
};

const getPosition = (index: number, total: number) => {
  const angle = (index / total) * 2 * Math.PI;
  const adjustedAngle = angle - Math.PI / 2;
  return {
    x: CONTAINER_WIDTH / 2 + X_RADIUS * Math.cos(adjustedAngle),
    y: CONTAINER_HEIGHT / 2 + Y_RADIUS * Math.sin(adjustedAngle)
  };
};

const getTeamColor = (role: Role): string => {
  if (!role) return 'grey-lighten-3';
  return ['citizen', 'sheriff'].includes(role) ? 'red-lighten-4' : 'grey-darken-3';
};

const getAvailableRolesForPlayer = (player: Player) => {
  return availableRoles.filter(roleOption => {
    // Always include the player's current role in options
    if (player.role === roleOption.value) return true;
    // Include roles that have remaining count > 0
    return remainingRoles.value[roleOption.value] > 0;
  });
};

const updateRole = (playerIndex: number, newRole: Role) => {
  const oldRole = players.value[playerIndex].role;
  
  // Return the old role to the pool if it exists
  if (oldRole) {
    remainingRoles.value[oldRole]++;
  }

  // Assign new role and update remaining count
  if (newRole) {
    remainingRoles.value[newRole]--;
  }
  
  // Update player's role
  players.value[playerIndex] = {
    ...players.value[playerIndex],
    role: newRole
  };
};

const isValidConfiguration = computed(() => {
  const allPlayersNamed = players.value.every(p => p.name.trim());
  const allRolesAssigned = players.value.every(p => p.role);
  const allRolesUsed = Object.values(remainingRoles.value).every(count => count === 0);
  
  return allPlayersNamed && allRolesAssigned && allRolesUsed;
});

const submitConfiguration = async () => {
  try {
    const response = await fetch(`/api/games/${props.id}/roles`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        players: players.value.map(p => ({
          name: p.name,
          role: p.role
        }))
      })
    });

    if (!response.ok) {
      throw new Error('Failed to submit configuration');
    }

    showNotification('Configuration saved successfully');
  } catch (error) {
    if (error instanceof Error) {
      showNotification('Error saving configuration: ' + error.message, 'error');
    } else {
      showNotification('An unknown error occurred', 'error');
    }
  }
};

const game = ref<Game | null>(null)

onMounted(() => {
  
  gameApi.getGame(props.id)
    .then(r => game.value = r)                    
  ;

});

</script>

<style scoped>
.player-card {
  transition: all 0.3s ease;
  cursor: pointer;
}

.player-card:hover {
  transform: scale(1.05);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.selected-for-swap {
  border: 2px solid #1976D2;
  box-shadow: 0 0 8px rgba(25, 118, 210, 0.5);
}
</style>