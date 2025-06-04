package org.barrikeit.chess.domain.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum RankEnum {
  GM(1, "GM", "Grandmaster"),
  IM(2, "IM", "International Master"),
  FM(3, "FM", "FIDE Master"),
  CM(4, "CM", "Candidate Master"),
  NM(5, "NM", "National Master"),
  E(6, "E", "Expert"),
  A(7, "A", "Advanced"),
  I(8, "E", "Intermediate"),
  B(9, "B", "Beginner");

  private static final Map<Integer, RankEnum> BY_NUMBER = new HashMap<>();
  private static final Map<String, RankEnum> BY_CODE_MASTERY = new HashMap<>();

  static {
    for (RankEnum e : values()) {
      BY_NUMBER.put(e.number, e);
      BY_CODE_MASTERY.put(e.codeMastery, e);
    }
  }

  public final int number;
  public final String codeMastery;
  public final String mastery;

  RankEnum(int number, String codeMastery, String mastery) {
    this.number = number;
    this.codeMastery = codeMastery;
    this.mastery = mastery;
  }

  public static RankEnum valueOfNumber(int number) {
    return BY_NUMBER.get(number);
  }

  public static RankEnum valueOfCodeMastery(String codeMastery) {
    return BY_CODE_MASTERY.get(codeMastery);
  }
}
