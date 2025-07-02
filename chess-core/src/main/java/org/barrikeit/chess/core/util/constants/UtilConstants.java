package org.barrikeit.chess.core.util.constants;

import java.time.format.DateTimeFormatter;

public class UtilConstants {
  public static final String EXPRESION_REGULAR_PARAMETROS = "(\\w+)([:!><])([^,]+)";
  public static final String DEFAULT_TIME_ZONE = "Europe/Madrid";
  public static final String PATTERN_DATE = "dd/MM/yyyy";
  public static final String PATTERN_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
  public static final String PATTERN_DATE_DOWNLOAD = "dd-MM-yyyy";
  public static final String PATTERN_DATE_TIME_DOWNLOAD = "dd-MM-yyyy_HHmmss";

  public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE);
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(PATTERN_DATE_TIME);

  public static final DateTimeFormatter DATE_FORMATTER_DOWNLOAD =
          DateTimeFormatter.ofPattern(PATTERN_DATE_DOWNLOAD);
  public static final DateTimeFormatter DATE_TIME_FORMATTER_DOWNLOAD =
          DateTimeFormatter.ofPattern(PATTERN_DATE_TIME_DOWNLOAD);
  private UtilConstants() {
    throw new IllegalStateException("Constants class");
  }
}
