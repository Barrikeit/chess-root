package org.barrikeit.chess.core.util.exceptions;

import java.net.URI;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.exceptions.base.GenericException;
import org.springframework.http.HttpStatus;

public class BadRequestException extends GenericException {

  static final URI TYPE = URI.create("");

  public BadRequestException(String message, Object... messageArgs) {
    super(HttpStatus.BAD_REQUEST, TYPE, ExceptionConstants.BAD_REQUEST_TITLE, message, messageArgs);
  }
}
