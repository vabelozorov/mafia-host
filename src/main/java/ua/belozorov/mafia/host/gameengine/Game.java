package ua.belozorov.mafia.host.gameengine;

import java.util.List;

public interface Game {
    Long id();

    GameStates state();

    GameConfig gameConfig();

    void assignPlayers(List<Player> players);

    int startDay();

    List<Player> getPlayers();

    List<Round> getRounds();

}
