package ua.belozorov.mafia.host.gameengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassicGame implements Game {
    private final Long id;
    private final GameConfig gameConfig;
    private GameState state;
    private final List<Player> players = new ArrayList<>();

    public ClassicGame(Long id) {
        this.id = id;
        this.state = GameState.ASSIGNING_ROLES;
        this.gameConfig = new GameConfig(12);
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public GameState state() {
        return state;
    }

    @Override
    public Map<Role, Integer> roleConfiguration() {
        return gameConfig.rolesConfiguration();
    }

    @Override
    public void assignPlayers(List<Player> players) {
        this.players.clear();
        this.players.addAll(players);
        state = GameState.FIRST_NIGHT;
    }
}
