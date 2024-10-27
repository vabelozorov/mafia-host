package ua.belozorov.mafia.host.gameengine;

import java.util.Map;

class GameConfig {
    private final int numberOfPlayers;
    private final Map<Role,Integer> rolesCount;

    public GameConfig(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
        this.rolesCount = Map.of(
                Role.SHERIFF, 1,
                Role.CITIZEN, 7,
                Role.DON, 1,
                Role.MAFIA, 3
        );
    }

    public Map<Role, Integer> rolesConfiguration() {
        return rolesCount;
    }
}
