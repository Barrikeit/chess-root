package org.barrikeit.security.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AppHeaderValidatorFilter implements Filter {

  private final AppValidatorFilterProperties appValidatorFilterProperties;

  public AppHeaderValidatorFilter(AppValidatorFilterProperties appValidatorFilterProperties) {
    this.appValidatorFilterProperties = appValidatorFilterProperties;
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    var httpRequest = (HttpServletRequest) request;
    var httpResponse = (HttpServletResponse) response;

    String path = httpRequest.getRequestURI();
    if (path != null
        && (path.contains("/management/")
            || path.contains("/openapi/")
            || path.contains("/available.html")
            || path.contains("/error")
            || path.endsWith("/dummy"))) {
      filterChain.doFilter(request, response);
      return;
    }

    if (Boolean.TRUE.equals(appValidatorFilterProperties.getAppHeaderNameValidationFilter())) {
      String calledAppId = httpRequest.getHeader(appValidatorFilterProperties.getAppHeaderName());
      if (calledAppId == null
          || calledAppId.isBlank()
          || !calledAppId.equals(appValidatorFilterProperties.getAppSelfName())) {
        log.debug("Peticion a {}{} RECHAZADA", calledAppId, path);
        httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        return;
      }

      log.debug("Peticion a {}{} ACEPTADA", calledAppId, path);
    }

    filterChain.doFilter(request, response);
  }
}
