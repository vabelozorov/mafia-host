package ua.belozorov.mafia.host.gameengine;

public class DayState implements GameState {

    private final ClassicalGame classicalGame;

    public DayState(ClassicalGame classicalGame) {
        this.classicalGame = classicalGame;
    }

    @Override
    public GameStates getId() {
        return GameStates.DAY;
    }

    @Override
    public void notifyTransitioned() {
        this.classicalGame.addRound();
    }
}
