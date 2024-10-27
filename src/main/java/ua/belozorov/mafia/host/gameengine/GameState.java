package ua.belozorov.mafia.host.gameengine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GameState {
  
  ASSIGNING_ROLES("assigning_roles"), FIRST_NIGHT("first_night");

  private final String value;

  GameState(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static GameState fromValue(String value) {
    for (GameState b : GameState.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

