package ua.belozorov.mafia.host.gameengine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import ua.belozorov.mafia.host.generated.api.GamesApi;
import ua.belozorov.mafia.host.generated.model.CreateGame201Response;
import ua.belozorov.mafia.host.generated.model.GameResp;
import ua.belozorov.mafia.host.generated.model.NextDay201Response;
import ua.belozorov.mafia.host.generated.model.PlayerAssignmentReq;

import java.util.ArrayList;

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
        return GameMapper.INSTANCE.map(game);
    }

    @Override
    public NextDay201Response nextDay(Long gameId) {
        return new NextDay201Response().id(gameService.nextDay(gameId));
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
}
