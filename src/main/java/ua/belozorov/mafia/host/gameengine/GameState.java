package ua.belozorov.mafia.host.gameengine;

import java.util.List;
import java.util.Map;

interface GameState {

    GameStates getId();

    default void assignRoles(List<Player> players) {
        throw new IllegalStateException("Cannot assign roles in current state");
    }

    default void startNight() {
        throw new IllegalStateException("Cannot start night in current state");
    }

    default void processMafiaVotes(Map<Player, Player> mafiaVotes) {
        throw new IllegalStateException("Cannot process mafia votes in current state");
    }

    default void startDay() {
        throw new IllegalStateException("Cannot start day in current state");
    }

    default void processPlayerSpeech(Player speaker) {
        throw new IllegalStateException("Cannot process player speech in current state");
    }

    default void processVoting(Map<Player, Player> votes) {
        throw new IllegalStateException("Cannot process voting in current state");
    }

    default void notifyTransitioned() {};
}
