package org.barrikeit.chess.core.util.exceptions;

import java.net.URI;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.exceptions.base.GenericException;
import org.springframework.http.HttpStatus;

public class UnExpectedException extends GenericException {

  static final URI TYPE = URI.create("");

  public UnExpectedException(String message) {
    super(HttpStatus.INTERNAL_SERVER_ERROR, message);
  }

  public UnExpectedException(String message, Object... messageArgs) {
    super(
        HttpStatus.INTERNAL_SERVER_ERROR,
        TYPE,
        ExceptionConstants.UNEXPECTED_ERROR_TITLE,
        message,
        messageArgs);
  }
}
