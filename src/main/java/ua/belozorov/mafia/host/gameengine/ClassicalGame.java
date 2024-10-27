package ua.belozorov.mafia.host.gameengine;

import java.util.*;

public class ClassicalGame implements Game {
    private final Long id;
    private GameState currentState;
    private final Map<GameStates, GameState> states = new HashMap<>();

    // Global Game Configuration
    private final GameConfig gameConfig;

    // Game Data
    private final List<Player> players = new ArrayList<>();
    private List<Round> gameRounds = new ArrayList<>();

    public ClassicalGame(Long id) {
        this.id = id;

        this.gameConfig = GameConfig.builder(12).build();
//        this.gameRounds = new ArrayList<>();

        initializeStates();
        this.currentState = states.get(GameStates.ASSIGNING_ROLES);
    }

    private void initializeStates() {
        List.of(
                new RoleAssignmentState(this),
                new FirstNightState(this),
                new DayState(this)
        ).forEach(s -> states.put(s.getId(), s));
//        states.put("NIGHT", new NightState(this));
    }

    void transitionTo(GameStates stateName) {
        GameState newState = states.get(stateName);
        if (newState == null) {
            throw new IllegalStateException("Invalid state: " + stateName);
        }
        currentState = newState;
        currentState.notifyTransitioned();
    }

    void addRound() {
        this.gameRounds.add(new Round(this.gameRounds.size() + 1));
    }

    public void startNight() {
        currentState.startNight();
    }

    public void processMafiaVotes(Map<Player, Player> mafiaVotes) {
        currentState.processMafiaVotes(mafiaVotes);
    }

    public int startDay() {
        currentState.startDay();
        return gameRounds.size();
    }

    @Override
    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    @Override
    public List<Round> getRounds() {
        return Collections.unmodifiableList(gameRounds);
    }

    public void processPlayerSpeech(Player speaker) {
        currentState.processPlayerSpeech(speaker);
    }

    public void processVoting(Map<Player, Player> votes) {
        currentState.processVoting(votes);
    }

    public List<Player> getAlivePlayers() {
        return players.stream()
                .filter(Player::isAlive)
                .toList();
    }

    @Override
    public Long id() {
        return this.id;
    }

    @Override
    public GameStates state() {
        return currentState.getId();
    }

    @Override
    public GameConfig gameConfig() {
        return gameConfig;
    }

    @Override
    public void assignPlayers(List<Player> players) {
        this.players.addAll(players);
        transitionTo(GameStates.FIRST_NIGHT);
    }
}
