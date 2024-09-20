package org.barrikeit.core.util.constants;

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
  public static final String PATTERN_LOCAL_DATE = "dd/MM/yyyy";
  public static final String PATTERN_DATE_TIME = "dd/MM/yyyy HH:mm:ss";
  public static final String PATTERN_LOCAL_DATE_DOWNLOAD = "dd-MM-yyyy";
  public static final String PATTERN_DATE_TIME_DOWNLOAD = "dd-MM-yyyy_HHmmss";
}
