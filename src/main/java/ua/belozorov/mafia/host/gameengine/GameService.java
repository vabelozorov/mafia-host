package ua.belozorov.mafia.host.gameengine;

import java.util.List;

public interface GameService {
    Long createGame();

    Game getById(Long id);

    void assignPlayers(Long gameId, List<Player> players);

    int nextDay(Long gameId);
}
