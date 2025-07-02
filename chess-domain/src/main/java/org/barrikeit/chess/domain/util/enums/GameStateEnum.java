package org.barrikeit.chess.domain.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum GameStateEnum {
  A(1, "A", "Start"),
  B(2, "B", "Searching for White"),
  C(3, "C", "Searching for Black"),
  D(4, "D", "White to move"),
  E(5, "E", "Black to move"),
  F(6, "F", "White check"),
  G(7, "G", "Black check"),
  H(8, "H", "White Wins"),
  I(9, "I", "Black Wins"),
  J(9, "I", "Draw by StaleMate"),
  K(9, "I", "Draw by Tables");

  private static final Map<Integer, GameStateEnum> BY_NUMBER = new HashMap<>();
  private static final Map<String, GameStateEnum> BY_CODE_COLOR = new HashMap<>();

  static {
    for (GameStateEnum e : values()) {
      BY_NUMBER.put(e.number, e);
      BY_CODE_COLOR.put(e.codeState, e);
    }
  }

  public final int number;
  public final String codeState;
  public final String state;

  GameStateEnum(int number, String codeState, String state) {
    this.number = number;
    this.codeState = codeState;
    this.state = state;
  }

  public static GameStateEnum valueOfNumber(int number) {
    return BY_NUMBER.get(number);
  }

  public static GameStateEnum valueOfCodeState(String codeState) {
    return BY_CODE_COLOR.get(codeState);
  }
}
