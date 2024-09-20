package org.barrikeit.core.util.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.UUID;
import org.barrikeit.core.util.annotations.Code;

public class CodeValidator implements ConstraintValidator<Code, Object> {
  @Override
  public boolean isValid(Object value, ConstraintValidatorContext context) {
    // Solo se valida en caso de que S sea de tipo String
    if (value != null) {
      if (value.getClass().equals(String.class)) {
        return ((String) value).matches("^[a-zA-Z0-9]*$");
      }
      // No se valida si S es de tipo UUID
      else return value.getClass().equals(UUID.class);
    }
    return false;
  }
}
