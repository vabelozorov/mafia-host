export interface Role {
    name: string;
    team: 'red' | 'black';  // Make it more type-safe with specific teams
    count: number;
}

export interface Player {
  position: number,
  name: string;
  role: string | null;
}

export interface Round {
  id : number  
}

export enum GameState {
  ASSIGNING_ROLES = 'assigning_roles',
  FIRST_NIGHT = 'first_night',
  DAY = "day",
}

// Type predicates if needed
export const isValidGameState = (state: string): state is GameState => {
  return Object.values(GameState).includes(state as GameState);
};

export class Game {
  private readonly _id: number;
  private _state: GameState;
  private readonly _roles: Role[];
  private readonly _firstNightDuration: number;
  private _rounds: Round[];
  private _players: Player[];

  static GameBuilder = class GameBuilder {
    id: number;
    state: GameState | null = null;
    roles: Role[] = [];
    firstNightDuration: number = 0;
    rounds: Round[] = [];
    players: Player[] = [];

    constructor(id: number) {
      this.id = id;
    }

    withState(state: GameState): GameBuilder {
      this.state = state;
      return this;
    }

    withRoles(roles: Role[]): GameBuilder {
      this.roles = [...roles];
      return this;
    }

    withFirstNightDuration(duration: number): GameBuilder {
      this.firstNightDuration = duration;
      return this;
    }

    withRounds(rounds: Round[]): GameBuilder {
      this.rounds = [...rounds];
      return this;
    }

    withPlayers(players: Player[]): GameBuilder {
      this.players = [...players];
      return this;
    }

    build(): Game {
      return new Game(this);
    }
  };

  // Use the GameBuilder type directly
  private constructor(builder: InstanceType<typeof Game.GameBuilder>) {
    this._id = builder.id;
    this._state = builder.state as GameState; // Add null check if needed
    this._roles = builder.roles;
    this._firstNightDuration = builder.firstNightDuration;
    this._rounds = builder.rounds;
    this._players = builder.players;
  }

  getLastRoundNumber(): number {
    if (!this._rounds || this._rounds.length === 0) {
      return 0;
    }
    return this._rounds[this._rounds.length - 1].id;
  }

  // Getters
  get id(): number {
    return this._id;
  }

  get state(): GameState {
    return this._state;
  }

  get roles(): Role[] {
    return [...this._roles]; // Return copy to maintain immutability
  }

  get firstNightDuration(): number {
    return this._firstNightDuration;
  }

  get rounds(): Round[] {
    return [...this._rounds];
  }

  get players(): Player[] {
    return [...this._players];
  }
}