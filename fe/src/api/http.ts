import {createApiClient} from '@/api'
import type { Game, Role } from '@/model/gameModels';
import { GameState } from '@/model/gameModels';
import { schemas } from '@/api';

const http = createApiClient('http://localhost:8080/');

export const gameApi = {
  async getGame(id: number): Promise<Game> {
    return http.getGame({ params: { id } })
          .then(mapResponseToGame)
      ;
  },

  async createGame(): Promise<number> {
    return http.createGame(undefined)
            .then(r => r.id);
  },

  async assignRoles(gameId: number, players: { name: string; role: string; }[]):Promise<void> {
    http.submitPlayersConfiguration(
      {
        players: players.map(p => ({ 
          name: p.name, 
          role: p.role 
        }))
      },
      {
        params: {
          gameId: gameId
        }
      }
    );
  }
};

// Mapper functions
const mapResponseToGame = (response: typeof schemas.GameResp['_type']): Game => {

  if (!isValidGameState(response.state)) {
    throw new Error(`Invalid game state: ${response.state}`);
  }

  return {
    id: response.id,
    state: response.state as GameState,
    roles: response.gameConfig.roles.map(mapResponseToRole) ?? []
  };
};

const mapResponseToRole = (role: typeof schemas.RoleDescription['_type']): Role => {
  return {
    name: role.name,
    team: mapTeamFromResponse(role.team),
    count: role.count
  };
};

// Helper function to ensure type safety for team values
const mapTeamFromResponse = (team: string): Role['team'] => {
  return team.toLowerCase() === 'red' ? 'red' : 'black';
};

const isValidGameState = (state: string): state is GameState => {
  return Object.values(GameState).includes(state as GameState);
};


declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $gameApi: typeof gameApi; 
  }
}

export default gameApi;
