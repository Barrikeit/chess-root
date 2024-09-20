package org.barrikeit.core.util.annotations.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.barrikeit.core.util.annotations.AlphanumericSpacesDashesDots;
import org.springframework.util.ObjectUtils;

public class AlphanumericSpacesDashesDotsValidator
    implements ConstraintValidator<AlphanumericSpacesDashesDots, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return ObjectUtils.isEmpty(value) || value.matches("^[a-zA-Z0-9\\s\\-.ÁÉÍÓÚáéíóúÑñ]*$");
  }
}
