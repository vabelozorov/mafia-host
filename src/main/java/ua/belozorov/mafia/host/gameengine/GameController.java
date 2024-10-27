package ua.belozorov.mafia.host.gameengine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ua.belozorov.mafia.host.generated.api.GameApi;
import ua.belozorov.mafia.host.generated.model.CreateGame201Response;
import ua.belozorov.mafia.host.generated.model.GameResponse;
import ua.belozorov.mafia.host.generated.model.GameResponseGameConfig;
import ua.belozorov.mafia.host.generated.model.RoleDescription;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController implements GameApi {

    private final GameService gameService;

    @Override
    public CreateGame201Response createGame() {
        Long id = gameService.createGame();
        return new CreateGame201Response(id);
    }

    @Override
    public GameResponse getGame(Long id) {
        var game = gameService.getById(id);
        return new GameResponse()
                .id(game.id())
                .state(game.state().getValue())
                .gameConfig(mapRoleConfiguration(game.roleConfiguration()))
                ;
    }

    private GameResponseGameConfig mapRoleConfiguration(Map<Role, Integer> rolesConfig) {
        var descriptions = rolesConfig.entrySet().stream()
                .map(e -> new RoleDescription()
                        .name(e.getKey().toString().toLowerCase())
                        .team(e.getKey().isRed() ? "red" : "black")
                        .count(e.getValue())
                ).toList();
        return new GameResponseGameConfig().roles(descriptions);
    }
}
