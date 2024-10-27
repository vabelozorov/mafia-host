package ua.belozorov.mafia.host.gameengine;

public interface GameService {
    Long createGame();

    Game getById(Long id);
}
