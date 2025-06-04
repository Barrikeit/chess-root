package org.barrikeit.chess.domain.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum ColorEnum {
  B(1, "B", "Black"),
  W(2, "W", "White");

  private static final Map<Integer, ColorEnum> BY_NUMBER = new HashMap<>();
  private static final Map<String, ColorEnum> BY_CODE_COLOR = new HashMap<>();

  static {
    for (ColorEnum e : values()) {
      BY_NUMBER.put(e.number, e);
      BY_CODE_COLOR.put(e.codeColor, e);
    }
  }

  public final int number;
  public final String codeColor;
  public final String color;

  ColorEnum(int number, String codeColor, String color) {
    this.number = number;
    this.codeColor = codeColor;
    this.color = color;
  }

  public static ColorEnum valueOfNumber(int number) {
    return BY_NUMBER.get(number);
  }

  public static ColorEnum valueOfCodeColor(String codeColor) {
    return BY_CODE_COLOR.get(codeColor);
  }
}
