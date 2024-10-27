package ua.belozorov.mafia.host.gameengine;

import java.util.Arrays;
import java.util.Objects;

public enum Role {
    SHERIFF(true),
    CITIZEN(true),
    DON(false),
    MAFIA(false);

    private final boolean isRed;

    Role(boolean isRed) {
        this.isRed = isRed;
    }

    public boolean isRed() {
        return isRed;
    }

    public static Role fromValue(String name) {
        return Arrays.stream(Role.values()).filter(v -> Objects.equals(v.name().toLowerCase(), name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("unknown role" + name));
    }
}
