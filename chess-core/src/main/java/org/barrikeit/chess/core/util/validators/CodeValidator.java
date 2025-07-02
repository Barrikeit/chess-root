package org.barrikeit.chess.core.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.UUID;

public class CodeValidator implements ConstraintValidator<Code, Object> {
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    if (value != null) {
      if (value.getClass().equals(String.class)) {
        return ((String) value).matches("^[a-zA-Z0-9]*$");
      } else return value.getClass().equals(UUID.class);
    }
    return false;
  }
}
