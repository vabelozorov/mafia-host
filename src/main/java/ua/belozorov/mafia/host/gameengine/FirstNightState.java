package ua.belozorov.mafia.host.gameengine;

class FirstNightState implements GameState{
    private final ClassicalGame classicalGame;

    public FirstNightState(ClassicalGame classicalGame) {
        this.classicalGame = classicalGame;
    }

    @Override
    public GameStates getId() {
        return GameStates.FIRST_NIGHT;
    }

    @Override
    public void startDay() {
        this.classicalGame.transitionTo(GameStates.DAY);
    }
}
