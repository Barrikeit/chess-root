package org.barrikeit.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.barrikeit.security.audit.AuditHolder;
import org.barrikeit.security.audit.AuditInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
public class AppHeaderValidatorFilterSession extends OncePerRequestFilter {

  @Value("${spring.application.name}")
  private String applicationName;

  @Value("${spring.profiles.active:Unknown}")
  private String activeProfile;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {
    var httpRequest = (HttpServletRequest) request;

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

    if (!activeProfile.equals("test")) {

      String sessionInfo = httpRequest.getHeader("x-aei-session");
      List<String> sessionInfoList =
          Collections.list(new StringTokenizer(sessionInfo, "#")).stream()
              .map(token -> (String) token)
              .collect(Collectors.toList());

      AuditInfo auditInfo =
          new AuditInfo(applicationName, sessionInfoList.get(1), sessionInfoList.get(2));
      AuditHolder.setAuditInfo(auditInfo);
    }

    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {}
}
