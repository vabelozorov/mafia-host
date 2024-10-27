package ua.belozorov.mafia.host.gameengine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ua.belozorov.mafia.host.generated.api.GamesApi;
import ua.belozorov.mafia.host.generated.model.*;

import java.util.ArrayList;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController implements GamesApi {

    private final GameService gameService;

    @Override
    public CreateGame201Response createGame() {
        Long id = gameService.createGame();
        return new CreateGame201Response(id);
    }

    @Override
    public GameResp getGame(Long id) {
        var game = gameService.getById(id);
        return new GameResp()
                .id(game.id())
                .state(game.state().getValue())
                .gameConfig(mapRoleConfiguration(game.roleConfiguration()))
                ;
    }

    @Override
    public void submitPlayersConfiguration(Long gameId, PlayerAssignmentReq playerAssignment) {
        var pa = playerAssignment.getPlayers();

        var players = new ArrayList<Player>();
        for (int i = 0; i < pa.size(); i++) {
            var p = pa.get(i);
            players.add(new Player(i, p.getName(), Role.fromValue(p.getRole())));
        }

        gameService.assignPlayers(gameId, players);
    }

    private GameRespGameConfig mapRoleConfiguration(Map<Role, Integer> rolesConfig) {
        var descriptions = rolesConfig.entrySet().stream()
                .map(e -> new RoleDescription()
                        .name(e.getKey().toString().toLowerCase())
                        .team(e.getKey().isRed() ? "red" : "black")
                        .count(e.getValue())
                ).toList();
        return new GameRespGameConfig().roles(descriptions);
    }
}
