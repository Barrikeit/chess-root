package org.barrikeit.core.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class UserUtil {
  private UserUtil() {throw new IllegalStateException("Utility class");}

  /**
   * Obtiene el username del usuario autenticado a traves del objeto Authentication
   * (JwtAuthenticationToken) del contexto que hay en SecurityContextHolder
   *
   * @return username del usuario autenticado
   */
  public static String getUserNameOfAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }
}
