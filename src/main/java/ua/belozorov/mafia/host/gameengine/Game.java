package ua.belozorov.mafia.host.gameengine;

import java.util.Map;

public interface Game {
    Long id();

    GameState state();

    Map<Role, Integer> roleConfiguration();
}
