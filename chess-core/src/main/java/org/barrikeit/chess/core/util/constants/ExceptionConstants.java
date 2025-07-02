package org.barrikeit.chess.core.util.constants;

public class ExceptionConstants {
  public static final String UNEXPECTED_ERROR_TITLE = "error.unexpected";
  public static final String INTERNAL_SERVER_ERROR_TITLE = "error.internal.error.title";
  public static final String INTERNAL_SERVER_ERROR = "error.internal.error.msg";
  public static final String BAD_REQUEST_TITLE = "error.bad.request.title";
  public static final String BAD_REQUEST = "error.bad.request.msg";
  public static final String ERROR_PARAMS_VALIDATION = "Invalid parameters";
  public static final String ERROR_FIELD_GET_VALUE =
      "Error al obtener el valor del campo {0} de la clase {1}.";
  public static final String ERROR_FIELD_SET_VALUE =
      "Error al insertar el valor al campo {0} de la clase {1}.";
  public static final String ERROR_MISSING_ANNOTATION =
      "No existe la anotación {0} en la clase : {1}";
  public static final String NOT_FOUND_TITLE = "error.not.found.title";
  public static final String NOT_FOUND = "error.not.found.msg";
  public static final String ERROR_TOKEN_NOT_PRESENT = "";
  public static final String ERROR_TOKEN_INVALID = "error.token.invalid";
  public static final String ERROR_TOKEN_EXPIRED = "error.token.expired";
  public static final String ERROR_TOKEN_MISMATCH = "";
  public static final String ERROR_TOKEN_ALREDY_USED = "";
  public static final String EMPTY_COOKIE = "";
  public static final String DESERIALIZED_COOKIE = "";
  public static final String ERROR_VALIDATION_FIELDS = "error.validation.fields";
  public static final String ERROR_INVALID_SEARCH_OPERATION = "error.msg.invalid.search.operation";
  public static final String ERROR_INVALID_SEARCH_PARAMETER = "error.msg.invalid.search.parameter";
  public static final String ERROR_INVALID_FILTER = "error.msg.invalid.filter";
  public static final String ERROR_INVALID_SORT = "error.msg.invalid.sort";
  public static final String ERROR_DESCARGA_EXCEL = "error.msg.excel.descarga";
  public static final String ERROR_COLUMNS_NUMBER = "error.msg.excel.columns.number";
  public static final String ERROR_COLUMN_NAME = "error.msg.excel.column.name";
  public static final String ERROR_REQUEST_MUST_NOT_BE_NULL = "";
  public static final String ERROR_MAX_SESSIONS_CONCURRENT_USER = "";
  public static final String ERROR_USER_BANNED = "";
  public static final String ERROR_USER_NOT_ENABLED = "";
  public static final String ERROR_USER_NAME_ALREADY_EXISTS = "";
  public static final String ERROR_USER_EMAIL_ALREADY_EXISTS = "";
  public static final String ERROR_USER_DEACTIVATE_HIMSELF = "";

  private ExceptionConstants() {
    throw new IllegalStateException("Constants class");
  }
}
