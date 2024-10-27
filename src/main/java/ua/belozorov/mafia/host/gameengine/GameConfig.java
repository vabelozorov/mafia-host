package ua.belozorov.mafia.host.gameengine;

import lombok.Getter;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Getter
public class GameConfig {
    private final int numberOfPlayers;

    private final Map<Role, Integer> roleDistribution;
    private final Duration firstNightDuration;
    private final Duration speechDuration;
    private final Duration votingDuration;

    private GameConfig(Builder builder) {
        this.numberOfPlayers = builder.numberOfPlayers;
        this.roleDistribution = Map.copyOf(builder.roleDistribution); // immutable copy
        this.firstNightDuration = builder.firstNightDuration;
        this.speechDuration = builder.speechDuration;
        this.votingDuration = builder.votingDuration;
    }

    public static Builder builder(int numberOfPlayers) {
        return new Builder(numberOfPlayers);
    }

    // Builder
    public static class Builder {
        // Required parameters
        private final int numberOfPlayers;

        // Optional parameters - initialized to default values
        private Map<Role, Integer> roleDistribution = new HashMap<>();
        private Duration firstNightDuration = Duration.ofSeconds(60);
        private Duration speechDuration = Duration.ofSeconds(30);
        private Duration votingDuration = Duration.ofSeconds(20);

        public Builder(int numberOfPlayers) {
            if (numberOfPlayers < 4) {
                throw new IllegalArgumentException("Game requires at least 4 players");
            }
            this.numberOfPlayers = numberOfPlayers;

            // Set default role distribution based on number of players
            setDefaultRoleDistribution();
        }

        private void setDefaultRoleDistribution() {
            // Example default distribution logic
            int mafiaCount = Math.max(1, numberOfPlayers / 4);
            int civilianCount = numberOfPlayers - mafiaCount;

            roleDistribution.put(Role.MAFIA, mafiaCount - 1);
            roleDistribution.put(Role.CITIZEN, civilianCount - 1);
            roleDistribution.put(Role.DON, 1);
            roleDistribution.put(Role.SHERIFF, 1);
        }

        public Builder firstNightDuration(Duration duration) {
            this.firstNightDuration = duration;
            return this;
        }

        public Builder speechDuration(Duration duration) {
            this.speechDuration = duration;
            return this;
        }

        public Builder votingDuration(Duration duration) {
            this.votingDuration = duration;
            return this;
        }

        public GameConfig build() {
            return new GameConfig(this);
        }
    }
}
