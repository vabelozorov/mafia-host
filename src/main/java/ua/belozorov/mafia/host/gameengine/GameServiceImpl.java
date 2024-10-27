package ua.belozorov.mafia.host.gameengine;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private volatile Game currentGame;
    @Value(("${app.player-number:12}"))
    private int playerNumber;

    @Override
    public Long createGame() {
        if (this.currentGame == null) {
            this.currentGame = new ClassicalGame(1L, playerNumber);
        }
        return this.currentGame.id();
    }

    @Override
    public Game getById(Long id) {
        return currentGame;
    }

    @Override
    public void assignPlayers(Long gameId, List<Player> players) {
        var game = getById(gameId);
        game.assignPlayers(players);
    }

    @Override
    public int nextDay(Long gameId) {
        var game = getById(gameId);
        return game.startDay();
    }
}
