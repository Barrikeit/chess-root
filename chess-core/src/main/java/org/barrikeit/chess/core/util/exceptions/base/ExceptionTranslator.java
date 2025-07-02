package org.barrikeit.chess.core.util.exceptions.base;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.net.URI;
import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.http.*;
import org.springframework.util.ObjectUtils;
import org.springframework.web.ErrorResponseException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  public static final String ERROR_PREFIX = "error.";
  public static final String ERROR_TITLE_SUFIX = ".title";
  public static final String ERROR_MSG_SUFIX = ".msg";
  private final MessageSource messageSource;

  @Autowired
  public ExceptionTranslator(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  protected ResponseEntity<Object> handleErrorResponseException(
      ErrorResponseException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

    log.error(ex.getMessage(), ex);
    if (!StringUtils.isEmpty(ex.getDetailMessageCode()) && !ObjectUtils.isEmpty(ex.getBody())) {
      ex.getBody()
          .setDetail(
              resolveMessageLabelErrorResponse(
                  ex.getDetailMessageCode(), request.getLocale(), ex.getDetailMessageArguments()));
    }

    if (!ObjectUtils.isEmpty(ex.getBody()) && !StringUtils.isEmpty(ex.getBody().getTitle())) {
      ex.getBody()
          .setTitle(resolveMessageLabelErrorResponse(ex.getBody().getTitle(), request.getLocale()));
    }

    return super.createResponseEntity(ex.getBody(), headers, status, request);
  }

  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatusCode status,
      WebRequest request) {
    log.error(ex.getMessage(), ex);
    LinkedHashMap<String, Object> errors = new LinkedHashMap<>();

    errors.put("type", "http://ajedrezillo.es/validation-exception");
    errors.put("title", ex.getBody().getTitle());
    errors.put("status", ex.getBody().getStatus());
    errors.put(
        "detail",
        messageSource.getMessage(
            ExceptionConstants.ERROR_VALIDATION_FIELDS, null, request.getLocale()));
    errors.put("instance", ((ServletWebRequest) request).getRequest().getRequestURI());

    // Sacar el error asociado a cada campo del objeto
    if (!ex.getBindingResult().getFieldErrors().isEmpty()) {

      List<Map<String, String>> fieldErrors = getFieldErrors(ex, request);

      errors.put("fieldErrors", fieldErrors.toArray());

    }
    // Si no existe getFieldErrors usar errors
    else if (ex.getBindingResult().hasErrors()) {
      List<Map<String, String>> errorsNoField = new ArrayList<>();
      errorsNoField.add(getAllErrors(ex, request));
      errors.put("errors", errorsNoField.toArray());
    }
    return super.createResponseEntity(errors, headers, status, request);
  }

  private List<Map<String, String>> getFieldErrors(
      MethodArgumentNotValidException ex, WebRequest request) {
    // Crear un Array de Map<String,String> con los campos field , rejected value y message
    List<Map<String, String>> errors = new ArrayList<>();
    ex.getBindingResult()
        .getFieldErrors()
        .forEach(
            (error) -> {
              HashMap<String, String> datoError = new LinkedHashMap<>();
              datoError.put("field", error.getField());
              datoError.put(
                  "rejectedValue", Objects.requireNonNull(error.getRejectedValue()).toString());
              datoError.put(
                  "message",
                  this.resolveMessageLabelErrorResponse(
                      error.getDefaultMessage(), request.getLocale()));
              errors.add(datoError);
            });
    return errors;
  }

  private Map<String, String> getAllErrors(MethodArgumentNotValidException ex, WebRequest request) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult()
        .getAllErrors()
        .forEach(
            error ->
                errors.put(
                    error.getObjectName(),
                    resolveMessageLabelErrorResponse(
                        error.getDefaultMessage(), request.getLocale())));

    return errors;
  }

  @Override
  protected ResponseEntity<Object> handleExceptionInternal(
      Exception ex,
      Object body,
      HttpHeaders headers,
      HttpStatusCode statusCode,
      WebRequest request) {
    log.error(ex.getMessage(), ex);
    return super.handleExceptionInternal(ex, body, headers, statusCode, request);
  }

  @ExceptionHandler
  public ResponseEntity<Object> handleConstraintViolation(
      ConstraintViolationException ex, WebRequest request) {
    log.error(ex.getMessage(), ex);
    HttpStatusCode status = HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value());
    // Nota: se espera que el mensaje sea una key para traducir en el MessageResources

    ProblemDetail problemDetail;
    Optional<ConstraintViolation<?>> opcional = ex.getConstraintViolations().stream().findFirst();
    if (opcional.isPresent()) {
      String messageFromResorces =
          resolveMessageLabelErrorResponse(opcional.get().getMessage(), request.getLocale());
      if (messageFromResorces != null) {
        problemDetail = ProblemDetail.forStatusAndDetail(status, messageFromResorces);
      } else {
        problemDetail = ProblemDetail.forStatusAndDetail(status, opcional.get().getMessage());
      }

    } else {
      problemDetail =
          ProblemDetail.forStatusAndDetail(status, ex.getConstraintViolations().toString());
    }
    problemDetail.setType(URI.create("http://ajedrezillo.es/validation-exception"));
    return super.createResponseEntity(problemDetail, new HttpHeaders(), status, request);
  }

  private String resolveMessageLabelErrorResponse(
      final String key, final Locale locale, final Object... args) {
    String response;

    if (key != null
        && (key.startsWith(ERROR_PREFIX)
            && (key.endsWith(ERROR_TITLE_SUFIX) || key.endsWith(ERROR_MSG_SUFIX)))) {
      try {
        response = messageSource.getMessage(key, args, locale);
      } catch (NoSuchMessageException ex) {
        log.debug("Este mensaje no está en el resources bundle i18n\\error_es.properties: {}", key);
        response =
            messageSource.getMessage(ExceptionConstants.INTERNAL_SERVER_ERROR_TITLE, null, locale);
      }
    } else {
      response = key;
    }

    return response;
  }
}
