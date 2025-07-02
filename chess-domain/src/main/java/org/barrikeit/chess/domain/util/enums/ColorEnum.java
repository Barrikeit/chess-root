package org.barrikeit.chess.domain.util.enums;

public enum ColorEnum {
  WHITE,
  BLACK;

  public ColorEnum opposite() {
    return switch (this) {
      case WHITE -> BLACK;
      case BLACK -> WHITE;
    };
  }
}
