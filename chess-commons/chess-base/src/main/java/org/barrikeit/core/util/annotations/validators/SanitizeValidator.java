package org.barrikeit.core.util.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.barrikeit.core.util.annotations.Sanitize;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class SanitizeValidator implements ConstraintValidator<Sanitize, String> {

  @Override
  public void initialize(Sanitize constraintAnnotation) {}

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (value == null) {
      return true;
    }

    // Limpia el valor usando Jsoup
    String cleanedValue = Jsoup.clean(value, Safelist.basic());

    // Modifica el contexto para limpiar el valor antes de ser validado
    return cleanedValue.equals(value);
  }
}
