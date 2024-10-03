package org.barrikeit.core.util.constants;

import java.time.format.DateTimeFormatter;

/** Constantes de la aplicación */
public class ApplicationConstants {
  private ApplicationConstants() {}

  /** Constant <code>SPRING_PROFILE_PRODUCTION="prod"</code> */
  public static final String SPRING_PROFILE_PRODUCTION = "prod";

  /** Constant <code>SPRING_PROFILE_DEVELOPMENT="dev"</code> */
  public static final String SPRING_PROFILE_DEVELOPMENT = "dev";

  /** Constant <code>SPRING_PROFILE_TEST="test"</code> */
  public static final String SPRING_PROFILE_TEST = "test";

  public static final String REFRESH_TOKEN_ENDPOINT = "/public/auth/refresh";

  public static final String EXPRESION_REGULAR_PARAMETROS = "(\\w+)([:!><])([^,]+)";

  public static final String DEFAULT_TIME_ZONE = "Europe/Madrid";
  public static final String PATTERN_LOCAL_DATE = "dd/MM/yyyy";
  public static final String PATTERN_LOCAL_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
  public static final String PATTERN_LOCAL_DATE_DOWNLOAD = "dd-MM-yyyy";
  public static final String PATTERN_LOCAL_DATE_TIME_DOWNLOAD = "dd-MM-yyyy_HHmmss";

  public static final DateTimeFormatter DATE_FORMATTER =
      DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE);
  public static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(PATTERN_LOCAL_DATE_TIME);

  public static final String HEADER_KEY = "Content-Disposition";
  public static final String RESPONSE_CONTENT_TYPE_EXCEL =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
}
