import { GameState, type Game } from '@/model/gameModels';
import router from '@/router';
import type { RouteRecordNameGeneric } from 'vue-router';

const routes : Map<GameState, String> = new Map();
routes.set(GameState.ASSIGNING_ROLES, 'assignRoles');

export default function routeForState(game: Game) {
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