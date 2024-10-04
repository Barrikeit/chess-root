package org.barrikeit.security.expression;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

final class TraceableHttpServletRequest {

  private final HttpServletRequest request;

  TraceableHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }

  public String getMethod() {
    return this.request.getMethod();
  }

  public URI getUri() {
    String queryString = this.request.getQueryString();
    if (!StringUtils.hasText(queryString)) {
      return URI.create(this.request.getRequestURL().toString());
    }
    try {
      StringBuffer urlBuffer = appendQueryString(queryString);
      return new URI(urlBuffer.toString());
    } catch (URISyntaxException ex) {
      String encoded = UriUtils.encodeQuery(queryString, StandardCharsets.UTF_8);
      StringBuffer urlBuffer = appendQueryString(encoded);
      return URI.create(urlBuffer.toString());
    }
  }

  private StringBuffer appendQueryString(String queryString) {
    return this.request.getRequestURL().append("?").append(queryString);
  }

  public Map<String, List<String>> getHeaders() {
    return extractHeaders();
  }

  public String getRemoteAddress() {
    return this.request.getRemoteAddr();
  }

  private Map<String, List<String>> extractHeaders() {
    Map<String, List<String>> headers = new LinkedHashMap<>();
    Enumeration<String> names = this.request.getHeaderNames();
    while (names.hasMoreElements()) {
      String name = names.nextElement();
      headers.put(name, Collections.list(this.request.getHeaders(name)));
    }
    return headers;
  }
}
