package org.barrikeit.core.util.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.barrikeit.core.util.annotations.AlphanumericSpaces;
import org.springframework.util.ObjectUtils;

public class AlphanumericSpacesValidator
    implements ConstraintValidator<AlphanumericSpaces, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return ObjectUtils.isEmpty(value) || value.matches("^[a-zA-Z0-9\\sÁÉÍÓÚáéíóúÑñ]*$");
  }
}
