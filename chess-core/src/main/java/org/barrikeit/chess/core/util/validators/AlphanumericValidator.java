package org.barrikeit.chess.core.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class AlphanumericValidator implements ConstraintValidator<Alphanumeric, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return ObjectUtils.isEmpty(value) || value.matches("^[a-zA-Z0-9]*$");
  }
}
