package org.barrikeit.chess.core.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.ObjectUtils;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Se permiten campos vacios, se valida si no está vacio

    return ObjectUtils.isEmpty(value)
        || value.matches("^(\\+34|0034|34)?[ -]*[6-9][ -]*(\\d[ -]*){8}$");
  }
}
