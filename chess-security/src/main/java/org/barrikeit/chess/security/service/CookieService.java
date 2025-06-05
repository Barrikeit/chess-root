package org.barrikeit.chess.security.service;

import jakarta.servlet.http.HttpServletResponse;
import org.barrikeit.chess.security.config.SecurityProperties;
import org.barrikeit.chess.security.util.constants.JwtConstants;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

  private SecurityProperties securityProperties;

  public void deleteJwtCookie(HttpServletResponse response) {
    // Construir la cookie con ResponseCookie para eliminarla
    ResponseCookie cookie =
        ResponseCookie.from(JwtConstants.JWT_COOKIE_NAME, "")
            .httpOnly(securityProperties.getCookie().isHttpOnly())
            .secure(securityProperties.getCookie().isSecure()) // Establecer en true si HTTPS
            .path(securityProperties.getCookie().getPath())
            .domain(securityProperties.getCookie().getDomain())
            .maxAge(0) // Establecer maxAge a 0 para eliminarla
            // .sameSite(cookieSamesite) // SameSite puede ser "Lax", "Strict", etc.
            .build();

    // Agregar la cookie al encabezado de la respuesta para eliminarla
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }

  public ResponseCookie createJwtCookie(String encodedJwt) {
    // Construir la cookie usando ResponseCookie
    return ResponseCookie.from(JwtConstants.JWT_COOKIE_NAME, encodedJwt)
        .httpOnly(securityProperties.getCookie().isHttpOnly())
        .secure(securityProperties.getCookie().isSecure()) // Establecer en true si HTTPS
        .path(securityProperties.getCookie().getPath())
        .domain(securityProperties.getCookie().getDomain())
        .maxAge(securityProperties.getCookie().getMaxAge()) // Duración de la cookie
        .sameSite(securityProperties.getCookie().getSamesite()) // Agregar SameSite como "Lax"
        .build();
  }
}
