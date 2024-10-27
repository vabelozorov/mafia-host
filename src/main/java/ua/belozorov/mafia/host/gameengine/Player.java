package ua.belozorov.mafia.host.gameengine;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Player {
    private final int position;
    private final String name;
    private final Role role;
}
