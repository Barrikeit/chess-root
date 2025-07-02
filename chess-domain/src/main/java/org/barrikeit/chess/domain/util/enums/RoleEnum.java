package org.barrikeit.chess.domain.util.enums;

import java.util.HashMap;
import java.util.Map;

public enum RoleEnum {
  ADM(1, "ADM", "Admin"),
  MOD(2, "MOD", "Moderator"),
  PLR(3, "PLR", "Player"),
  CCH(4, "CCH", "COACH"),
  TO(5, "TO", "Tournament Organizer"),
  SPC(6, "SPC", "Spectator"),
  GST(7, "GST", "Guest");

  private static final Map<Integer, RoleEnum> BY_NUMBER = new HashMap<>();
  private static final Map<String, RoleEnum> BY_CODE_ROLE = new HashMap<>();

  static {
    for (RoleEnum e : values()) {
      BY_NUMBER.put(e.number, e);
      BY_CODE_ROLE.put(e.codeRole, e);
    }
  }

  public final int number;
  public final String codeRole;
  public final String role;

  RoleEnum(int number, String codeRole, String role) {
    this.number = number;
    this.codeRole = codeRole;
    this.role = role;
  }

  public static RoleEnum valueOfNumber(int number) {
    return BY_NUMBER.get(number);
  }

  public static RoleEnum valueOfCodeRole(String codeRole) {
    return BY_CODE_ROLE.get(codeRole);
  }
}
