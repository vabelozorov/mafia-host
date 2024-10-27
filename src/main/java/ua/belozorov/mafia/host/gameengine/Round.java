package ua.belozorov.mafia.host.gameengine;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class Round {
    private final int roundNumber;
    private DayPhase dayPhase;
    private final Map<Player, Player> dayVotes = new HashMap<>();
    private Player dayEliminatedPlayer;
    private final Map<Player, Player> nightVotes = new HashMap<>();
    private Player nightEliminatedPlayer;

    public Round(int roundNumber) {
        this.roundNumber = roundNumber;
        this.dayPhase = DayPhase.DAY;
    }

    // Getters and setters
}
