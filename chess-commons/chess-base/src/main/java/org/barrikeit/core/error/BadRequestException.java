package org.barrikeit.core.error;

import java.net.URI;
import org.barrikeit.core.util.constants.ErrorConstants;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ChessProblem {

  static final URI TYPE = URI.create("http://ajedrezillo.es/bad-request");

  public BadRequestException(String defaultMessageKey, Object... messageArgs) {
    super(
        defaultMessageKey,
        HttpStatus.BAD_REQUEST,
        ErrorConstants.ERROR_TITLE_BAD_REQUEST,
        TYPE,
        messageArgs);
  }
}
