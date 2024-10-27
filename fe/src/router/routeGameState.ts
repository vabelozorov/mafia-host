import { GameState, type Game } from '@/model/gameModels';
import router from '@/router';
import type { RouteRecordNameGeneric } from 'vue-router';

const routes : Map<GameState, String> = new Map();
routes.set(GameState.ASSIGNING_ROLES, 'assignRoles');
routes.set(GameState.FIRST_NIGHT, 'firstNight');
routes.set(GameState.DAY, 'day');

export default function routeBasedOnGameState(game: Game) {
    const path = getStateRoute(game.state);

    router.push({
        name: path as RouteRecordNameGeneric,
        params: {id : game.id.toString()},
    })
}

 function getStateRoute(state: GameState) : String {
    const route = routes.get(state)

    if (route != undefined) {
        return route
    } 

    throw new Error('cannot provide a route. unknown game state ' + state);
}