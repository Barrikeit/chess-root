package org.barrikeit.security.audit;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.InetAddress;

import org.barrikeit.security.auth.JWTAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class AuditInfoFilter extends OncePerRequestFilter {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    if (isPreflightRequest(request) || isNoApp(request) || isTestProfile()) {
      filterChain.doFilter(request, response);
      return;
    }

    try {
      InetAddress address = InetAddress.getByName(request.getRemoteAddr());
      AuditInfo auditInfo =
          new AuditInfo(applicationName, getLoggedUser(), request.getRemoteAddr());
      AuditHolder.setAuditInfo(auditInfo);
      filterChain.doFilter(request, response);
    } finally {
      AuditHolder.clear();
    }
  }

  private String getLoggedUser() {
    String username = "anonymous";
    if (SecurityContextHolder.getContext().getAuthentication() != null) {
      Object authentication = SecurityContextHolder.getContext().getAuthentication();
      if (authentication instanceof JWTAuth jwtAuth) {
        username = jwtAuth.getName();
      }
    }

    return username;
  }

  private boolean isPreflightRequest(HttpServletRequest request) {
    return request.getHeader("Origin") != null && request.getMethod().equalsIgnoreCase("OPTIONS");
  }

  private boolean isNoApp(HttpServletRequest request) {
    String path = request.getRequestURI();
    return (path != null
        && (path.contains("/management/")
            || path.contains("/openapi/")
            || path.contains("available.html")));
  }

  private boolean isTestProfile() {
    return "test".equals(activeProfile);
  }
}
