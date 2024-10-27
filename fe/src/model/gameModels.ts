export interface Role {
    name: string;
    team: 'red' | 'black';  // Make it more type-safe with specific teams
    count: number;
  }
  
  export interface Game {
    id: number;
    state: GameState;  // You can use enum or union type for states
    roles: Role[];    
  }
  
  export enum GameState {
    ASSIGNING_ROLES = 'assigning_roles',
    FIRST_NIGHT = 'first_night',
  }
  
  // Type predicates if needed
  export const isValidGameState = (state: string): state is GameState => {
    return Object.values(GameState).includes(state as GameState);
  };