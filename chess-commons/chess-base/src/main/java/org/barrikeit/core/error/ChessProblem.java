package org.barrikeit.core.error;

import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_INTERNAL_ERROR_MSG;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_INTERNAL_ERROR_TITLE;

import java.io.Serial;
import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.util.ObjectUtils;
import org.springframework.web.ErrorResponseException;

@Slf4j
public class ChessProblem extends ErrorResponseException {

  @Serial private static final long serialVersionUID = -1L;

  public ChessProblem(String detail, HttpStatus status) {
    super(status, ProblemDetail.forStatusAndDetail(status, detail), new Throwable());
  }

  public ChessProblem(
      String defaultMessageKey, HttpStatus status, String title, URI type, Object... messageArgs) {
    super(
        status,
        ProblemDetail.forStatus(HttpStatusCode.valueOf(status.value())),
        new Throwable(),
        defaultMessageKey,
        messageArgs);
    this.setType(type);
    this.setTitle(title);
  }

  public ChessProblem(ExceptionMessage exceptionMessage) {
    super(
        HttpStatus.valueOf(Integer.parseInt(exceptionMessage.getStatus())),
        ProblemDetail.forStatus(Integer.parseInt(exceptionMessage.getStatus())),
        new Throwable());
    this.setTitle(exceptionMessage.getTitle());

    if (ObjectUtils.isEmpty(exceptionMessage.getDetail())
        && ObjectUtils.isEmpty(exceptionMessage.getMessage())) {
      this.setTitle(ERROR_INTERNAL_ERROR_TITLE);
      this.setDetail(ERROR_INTERNAL_ERROR_MSG);
    }

    if (exceptionMessage.getType() != null) this.setType(URI.create(exceptionMessage.getType()));
    if (exceptionMessage.getInstance() != null)
      this.setInstance(URI.create(exceptionMessage.getInstance()));
  }
}
