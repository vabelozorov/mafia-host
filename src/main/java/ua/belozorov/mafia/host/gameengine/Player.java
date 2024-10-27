package ua.belozorov.mafia.host.gameengine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class Player {
    private final int position;
    private final String name;
    private final Role role;
    private boolean isAlive;

    public boolean isAlive() {
        return isAlive;
    }
}
