package org.barrikeit.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.barrikeit.core.util.constants.ApplicationConstants;
import org.barrikeit.core.util.constants.ErrorConstants;
import org.barrikeit.security.auth.JWTAuth;
import org.barrikeit.security.service.SpringSessionService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

  private final JwtTokenProvider jwtTokenProvider;

  private final SpringSessionService springSessionService;

  @Value("${spring.mvc.servlet.path}")
  private String servletPath;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    String jwt = extractToken(request);

    if (StringUtils.hasText(jwt)) {
      try {
        if (!request
            .getRequestURI()
            .equals(servletPath + ApplicationConstants.REFRESH_TOKEN_ENDPOINT)) {
          String username = this.jwtTokenProvider.validateTokenAndRetrieveSubject(jwt);
          // validar que la sesion en la que fue creado el token existe y no ha expirado
          validateActiveSessionToken(jwt);

          JWTAuth authentication =
              new JWTAuth(
                  username, (Object) null, jwt, this.jwtTokenProvider.getAuthorities(jwt));
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
          SecurityContext context = SecurityContextHolder.createEmptyContext();
          context.setAuthentication(authentication);
          SecurityContextHolder.setContext(context);
        }
        filterChain.doFilter(request, response);
      } catch (TokenExpiredException e) {
        SecurityContextHolder.clearContext();
        logger.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED, ErrorConstants.ERROR_EXPIRED_TOKEN);
      } catch (JWTVerificationException e) {
        SecurityContextHolder.clearContext();
        logger.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.sendError(
            HttpServletResponse.SC_UNAUTHORIZED, ErrorConstants.ERROR_INVALID_TOKEN);
      } catch (Exception e) {
        SecurityContextHolder.clearContext();
        logger.error(e.getMessage(), e);
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.sendError(
            HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ErrorConstants.ERROR_UNEXPECTED);
      }
    } else {
      filterChain.doFilter(request, response);
    }
  }

  private String extractToken(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    String jwt = null;
    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      jwt = headerAuth.substring(7);
    }
    return jwt;
  }

  private void validateActiveSessionToken(String jwt) {
    // recuperar sessionId del token para validar que pertenece a una sesion activa
    String sessionId = "";
    DecodedJWT decodedJWT = this.jwtTokenProvider.validateTokenAndRetrieveDecoded(jwt);
    if (decodedJWT.getClaim(JwtClaimConstants.SESSION_ID) != null) {
      sessionId = decodedJWT.getClaim(JwtClaimConstants.SESSION_ID).asString();
    }
    // lanzar Excepcion si no existe la Sesion en la tabla de Sesiones
    boolean existeSesionToken = this.springSessionService.existeSesionBySessionId(sessionId);
    if (!existeSesionToken) {
      throw new TokenExpiredException("Token expirado por sesion");
    }
  }
}
