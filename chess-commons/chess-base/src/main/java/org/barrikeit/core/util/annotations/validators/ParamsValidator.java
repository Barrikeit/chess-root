package org.barrikeit.core.util.annotations.validators;

import static org.barrikeit.core.util.constants.ApplicationConstants.EXPRESION_REGULAR_PARAMETROS;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.barrikeit.core.util.annotations.Params;

public class ParamsValidator implements ConstraintValidator<Params, String> {
  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    // Si la cadena es un espacio en blanco o esta vacia la cadena es valida.
    if (value == null || value.isBlank()) {
      return true;
    }

    // En cualquier otro caso valida el contenido de la cadena
    return validarCadena(value);
  }

  private boolean validarParametro(String value) {
    return value.matches(EXPRESION_REGULAR_PARAMETROS);
  }

  private boolean validarCadena(String value) {
    String[] array = value.split(",");
    boolean result = true;
    for (String s : array) {
      if (!validarParametro(s)) {
        result = false;
        break;
      }
    }
    return result;
  }
}
