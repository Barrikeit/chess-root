package org.barrikeit.chess.core.util.constants;

public class FileConstants {
  public static final String FILENAME = ";filename=";
  public static final String HEADER_KEY = "Content-Disposition";
  public static final String ATTACHMENT = "attachment";
  public static final String CONTENT_TYPE_EXCEL =
      "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
  public static final String CONTENT_TYPE_ACCESS = "application/msaccess";
  public static final String EXTENSION_EXCEL = ".xlsx";
  public static final String EXTENSION_ACCESS = ".accdb";
  public static final String EXTENSION_ZIP = ".zip";

  private FileConstants() {
    throw new IllegalStateException("Constants class");
  }
}
