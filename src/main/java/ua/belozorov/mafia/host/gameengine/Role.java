package ua.belozorov.mafia.host.gameengine;

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
}
