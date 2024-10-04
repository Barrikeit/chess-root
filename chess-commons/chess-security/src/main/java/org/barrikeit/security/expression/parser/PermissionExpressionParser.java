package org.barrikeit.security.expression.parser;

import org.barrikeit.security.http.PermissionRequest;

/**
 * Las implementaciones de esta clase analizan y convierten una expresión de permisos
 *
 * @see DefaultPermissionExpressionParser
 */
public interface PermissionExpressionParser {

  /**
   * @param expression Expresión de un permiso (p.ej. RECUSO@OPERACION)
   * @return Un permiso basándose en la expresión analizada
   */
  PermissionRequest parse(String expression);
}
