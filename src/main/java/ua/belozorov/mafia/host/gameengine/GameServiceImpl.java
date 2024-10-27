package ua.belozorov.mafia.host.gameengine;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameServiceImpl implements GameService {

    private volatile Game currentGame;

    @Override
    public Long createGame() {
        if (this.currentGame == null) {
            this.currentGame = new ClassicGame(1L);
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
}
