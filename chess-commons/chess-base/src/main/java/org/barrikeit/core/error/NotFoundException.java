package org.barrikeit.core.error;

import java.net.URI;
import org.barrikeit.core.util.constants.ErrorConstants;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ChessProblem {

  static final URI TYPE = URI.create("http://ajedrezillo.es/not-found");

  public NotFoundException(String defaultMessageKey, Object... messageArgs) {
    super(
        defaultMessageKey,
        HttpStatus.NOT_FOUND,
        ErrorConstants.ERROR_NOT_FOUND_TITLE,
        TYPE,
        messageArgs);
  }
}
