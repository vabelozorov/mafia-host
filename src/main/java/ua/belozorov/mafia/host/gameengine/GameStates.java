package ua.belozorov.mafia.host.gameengine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum GameStates {
  
  ASSIGNING_ROLES("assigning_roles"),
  FIRST_NIGHT("first_night"),
  DAY("day");

  private final String value;

  GameStates(String value) {
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
  public static GameStates fromValue(String value) {
    for (GameStates b : GameStates.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

