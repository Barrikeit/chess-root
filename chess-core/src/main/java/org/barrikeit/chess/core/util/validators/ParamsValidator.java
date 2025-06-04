package org.barrikeit.chess.core.util.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.barrikeit.chess.core.util.constants.UtilConstants;

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
    return value.matches(UtilConstants.EXPRESION_REGULAR_PARAMETROS);
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
