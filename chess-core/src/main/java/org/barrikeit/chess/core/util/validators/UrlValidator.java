package org.barrikeit.chess.core.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URL;

public class UrlValidator implements ConstraintValidator<Url, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return value != null && isValidURL(value);
  }

  boolean isValidURL(String url) {
    try {
      new URL(url).toURI();
      return true;
    } catch (Exception e) {
      return false;
    }
  }
}
