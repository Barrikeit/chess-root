package org.barrikeit.core.util.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.net.URL;
import org.barrikeit.core.util.annotations.Url;

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
