package org.barrikeit.security.expression;

import org.barrikeit.security.auth.JWTAuth;
import org.barrikeit.security.expression.parser.DefaultPermissionExpressionParser;
import org.barrikeit.security.expression.parser.PermissionExpressionParser;
import org.barrikeit.security.http.AuthorizationServerClient;
import org.barrikeit.security.http.AuthorizationServerRequest;
import org.barrikeit.security.http.AuthorizationServerResponse;
import org.barrikeit.security.http.PermissionRequest;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Optional;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class AuthorizationServerMethodSecurityExpressionRoot extends SecurityExpressionRoot
    implements MethodSecurityExpressionOperations {

  private static final Logger log =
      LoggerFactory.getLogger(AuthorizationServerMethodSecurityExpressionRoot.class);
  private static final String DEFAULT_EXPRESSION_DELIMITER = "@";

  private final Supplier<Authentication> authenticationSupplier =
      () -> {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
          throw new AuthenticationCredentialsNotFoundException(
              "An Authentication object was not found in the SecurityContext");
        }
        return authentication;
      };

  private PermissionExpressionParser permissionExpressionParser =
      new DefaultPermissionExpressionParser(DEFAULT_EXPRESSION_DELIMITER);

  private final AuthorizationServerClient client;

  private Object filterObject;

  private Object returnObject;

  private Object target;

  public AuthorizationServerMethodSecurityExpressionRoot(
      Authentication authentication, AuthorizationServerClient client) {
    super(authentication);
    this.client = client;
  }

  @Override
  public void setFilterObject(Object filterObject) {
    this.filterObject = filterObject;
  }

  @Override
  public Object getFilterObject() {
    return this.filterObject;
  }

  @Override
  public void setReturnObject(Object returnObject) {
    this.returnObject = returnObject;
  }

  @Override
  public Object getReturnObject() {
    return this.returnObject;
  }

  @Override
  public Object getThis() {
    return this.target;
  }

  void setThis(Object target) {
    this.target = target;
  }

  public void setPermissionExpressionParser(PermissionExpressionParser permissionExpressionParser) {
    Assert.notNull(permissionExpressionParser, "PermissionExpressionParser no debe ser nulo");
    this.permissionExpressionParser = permissionExpressionParser;
  }

  public boolean checkPermissions(String... permissionExpressions) {
    Authentication authentication = authenticationSupplier.get();
    if (!isJwt(authentication)) {
      return false;
    }

    AuthorizationServerRequest request =
        createAuthorizationRequest((JWTAuth) authentication, permissionExpressions);
    addHttpFields(request);

    try {
      HttpResponse<Supplier<AuthorizationServerResponse>> response = client.authorize(request);
      if (response.statusCode() == 200 && response.body().get().isAuthorized()) {
        return true;
      } else {
        log.error(String.format("No autorizado %s", request.getUri()));
        String recurso = request.getUri().getPath().substring("/api/v1/".length());
        throw new AccessDeniedException(
            String.format("Sin autorización para acceder a %s", recurso));
      }
    } catch (IOException e) {
      log.warn("Error al conectar con el servidor de seguridad", e);
      return false;
    } catch (InterruptedException e) {
      log.warn("Error al conectar con el servidor de seguridad", e);
      Thread.currentThread().interrupt();
      return false;
    }
  }

  private AuthorizationServerRequest createAuthorizationRequest(
      JWTAuth authentication, String[] permissionExpressions) {
    AuthorizationServerRequest request =
        new AuthorizationServerRequest(authentication.getJwt(), client.getNamespace());
    for (String exp : permissionExpressions) {
      PermissionRequest permissionRequest = permissionExpressionParser.parse(exp);
      request.addPermissionRequest(permissionRequest);
    }
    return request;
  }

  private static void addHttpFields(AuthorizationServerRequest request) {
    Optional<HttpServletRequest> maybeCurrentHttpRequest = Optional.empty();
    if (isServletPresent()) {
      maybeCurrentHttpRequest = getCurrentHttpRequest();
    }
    if (maybeCurrentHttpRequest.isPresent()) {
      HttpServletRequest currentHttpRequest = maybeCurrentHttpRequest.get();
      TraceableHttpServletRequest traceableRequest =
          new TraceableHttpServletRequest(currentHttpRequest);
      request.setUri(traceableRequest.getUri());
      request.setMethod(traceableRequest.getMethod());
      request.setHeaders(traceableRequest.getHeaders());
      request.setRemoteAddress(traceableRequest.getRemoteAddress());
    }
  }

  private static boolean isJwt(Authentication authentication) {
    return JWTAuth.class.isAssignableFrom(authentication.getClass());
  }

  private static boolean isServletPresent() {
    return ClassUtils.isPresent(
        "jakarta.servlet.http.HttpServletRequest",
        AuthorizationServerMethodSecurityExpressionRoot.class.getClassLoader());
  }

  private static Optional<HttpServletRequest> getCurrentHttpRequest() {
    return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
        .filter(ServletRequestAttributes.class::isInstance)
        .map(ServletRequestAttributes.class::cast)
        .map(ServletRequestAttributes::getRequest);
  }
}
