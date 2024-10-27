package ua.belozorov.mafia.host.gameengine;

import java.util.List;

public class RoleAssignmentState implements GameState {
    private final ClassicalGame classicalGame;

    public RoleAssignmentState(ClassicalGame classicalGame) {
        this.classicalGame = classicalGame;
    }

    @Override
    public GameStates getId() {
        return GameStates.ASSIGNING_ROLES;
    }

    @Override
    public void assignRoles(List<Player> players) {
        classicalGame.assignPlayers(players);
    }
}
