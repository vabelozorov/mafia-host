package ua.belozorov.mafia.host.gameengine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RestController;
import ua.belozorov.mafia.host.generated.api.GameApi;
import ua.belozorov.mafia.host.generated.model.CreateGame201Response;
import ua.belozorov.mafia.host.generated.model.GameResponse;
import ua.belozorov.mafia.host.generated.model.GameState;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GameController implements GameApi {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public CreateGame201Response createGame() {
        return new CreateGame201Response(1);
    }

    @Override
    public GameResponse getGame(Integer id) {
        return new GameResponse()
                .id(id)
                .state(GameState.ASSIGNING_ROLES);
    }
}
