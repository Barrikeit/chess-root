package org.barrikeit.core.error;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
public class ExceptionMessage {

  // Para excepciones controladas (extienden de ChessProblem)
  private String type;
  private String title;
  private String status;
  private String detail;
  private String instance;

  // Para excepciones no controladas (Runtime)
  private String timestamp;
  private String error;
  private String message;
  private String path;

  public ExceptionMessage() {
    throw new UnsupportedOperationException();
  }
}
