import {createApiClient} from '@/api'
import type {Player, Role, Round } from '@/model/gameModels';
import { GameState, Game } from '@/model/gameModels';
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
    return http.submitPlayersConfiguration(
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
  },

  async nextDay(gameId: number): Promise<number> {
      return http.nextDay(
        undefined,      
        {
          params: {
            gameId: gameId
          }
        }
      ).then(r => r.id)
  }
};

// Mapper functions
const mapResponseToGame = (response: typeof schemas.GameResp['_type']): Game => {

  if (!isValidGameState(response.state)) {
    throw new Error(`Invalid game state: ${response.state}`);
  }
  
  return new Game.GameBuilder(response.id)
    .withState(response.state as GameState)
    .withRoles(response.gameConfig.roles.map(mapResponseToRole) ?? [])
    .withFirstNightDuration(response.gameConfig.firstNightDuration)
    .withPlayers(response.players.map(mapResponseToPlayer))
    .withRounds(response.rounds.map(mapResponseToRound))
    .build();
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

const mapResponseToPlayer = (p: typeof schemas.PlayerDto['_type'], index: number): Player => {
  return {
    position: index,
    name: p.name,
    role: p.role,
  }
};

const mapResponseToRound = (r: typeof schemas.RoundDto['_type']): Round => {
  return {
    id: r.id,
  }
}


declare module '@vue/runtime-core' {
  interface ComponentCustomProperties {
    $gameApi: typeof gameApi; 
  }
}

export default gameApi;
