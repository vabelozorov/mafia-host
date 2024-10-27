package ua.belozorov.mafia.host.gameengine;

import ua.belozorov.mafia.host.generated.model.*;

import java.util.List;

public class GameMapper {
    public static final GameMapper INSTANCE = new GameMapper();

    public GameResp map(Game game) {
        return new GameResp()
                .id(game.id())
                .state(game.state().getValue())
                .gameConfig(mapRoleConfiguration(game.gameConfig()))
                .players(mapPlayers(game.getPlayers()))
                .rounds(mapRounds(game.getRounds()))
                ;
    }

    private GameRespGameConfig mapRoleConfiguration(GameConfig gameConfig) {

        var descriptions = gameConfig.getRoleDistribution().entrySet().stream()
                .map(e -> new RoleDescription()
                        .name(e.getKey().toString().toLowerCase())
                        .team(e.getKey().isRed() ? "red" : "black")
                        .count(e.getValue())
                ).toList();

        return new GameRespGameConfig()
                .firstNightDuration(gameConfig.getFirstNightDuration().toSeconds())
                .roles(descriptions);
    }

    private List<PlayerDto> mapPlayers(List<Player> players) {
        return players.stream()
                .map(p -> new PlayerDto(p.getName(), p.getRole().name(), p.isAlive()))
                .toList();
    }

    private List<RoundDto> mapRounds(List<Round> rounds) {
        return rounds.stream()
                .map(r -> new RoundDto().id(r.getRoundNumber()))
                .toList();
    }
}
