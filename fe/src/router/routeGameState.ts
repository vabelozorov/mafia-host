import type { Game, GameState } from '@/model/gameModels';
import router from '@/router';

const routes : Map<GameState, String> = new Map();
routes.set('assigning_roles', 'assign');

export default function routeForState(game: Game) {
    const path = getStateRoute(game.state);

    router.push(`/games/${game.id}/${path}`)
}

 function getStateRoute(state: GameState) : String {
    const route = routes.get(state)

    if (route != undefined) {
        return route
    } 

    throw new Error('cannot provide a route. unknown game state ' + state);
}