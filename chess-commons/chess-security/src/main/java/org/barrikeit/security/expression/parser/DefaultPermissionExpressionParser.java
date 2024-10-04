package org.barrikeit.security.expression.parser;

import org.barrikeit.security.http.PermissionRequest;

/**
 * Implementación por defecto de un <i>parser</i> de expresiones de permisos, basada en la
 * existencia de un delimitador.
 *
 * <p>Esta implementación espera la existencia de un recurso, un delimitador y una operación, en
 * este orden. Por ejemplo, si el delimitador es @: <code>EXPEDIENTE@LECTURA</code>, donde
 * EXPEDIENTE es el recurso y LECTURA es la operación
 */
public class DefaultPermissionExpressionParser implements PermissionExpressionParser {

  private final String delimiter;

  public DefaultPermissionExpressionParser(String delimiter) {
    this.delimiter = delimiter;
  }

  @Override
  public PermissionRequest parse(String expression) {
    String[] resourceAction = expression.split(delimiter);
    if (resourceAction.length != 2) {
      String error =
          String.format(
              "Error al parsear una expresión de seguridad. Se esperaba un delimitar %s en la expresión %s",
              delimiter, expression);
      throw new IllegalArgumentException(error);
    }
    return new PermissionRequest(resourceAction[0], resourceAction[1]);
  }
}
