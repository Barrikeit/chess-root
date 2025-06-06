package org.barrikeit.chess.core.util.exceptions.base;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionMessage {
  // Para excepciones controladas que extiendan de GenericException/ErrorResponseException
  private String status;
  private String title;
  private String detail;
  private String type;
  private String instance;
  // Para excepciones no controladas (Runtime)
  private String timestamp;
  private String error;
  private String message;
  private String path;

  private ExceptionMessage() {}
}
