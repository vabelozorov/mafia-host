package ua.belozorov.mafia.host.gameengine;

import org.springframework.stereotype.Service;

@Service
public class GameServiceImpl implements GameService {
    @Override
    public Long createGame() {
        return 1L;
    }

    @Override
    public Game getById(Long id) {
        return new ClassicGame(id);
    }
}
