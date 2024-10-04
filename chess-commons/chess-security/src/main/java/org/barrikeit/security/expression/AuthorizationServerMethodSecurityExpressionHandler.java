package org.barrikeit.security.expression;

import org.barrikeit.security.http.AuthorizationServerClient;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

public class AuthorizationServerMethodSecurityExpressionHandler
    extends DefaultMethodSecurityExpressionHandler {

  private final AuthorizationServerClient client;

  public AuthorizationServerMethodSecurityExpressionHandler(AuthorizationServerClient client) {
    this.client = client;
  }

  @Override
  protected MethodSecurityExpressionOperations createSecurityExpressionRoot(
      Authentication authentication, MethodInvocation invocation) {
    AuthorizationServerMethodSecurityExpressionRoot root =
        new AuthorizationServerMethodSecurityExpressionRoot(authentication, client);
    root.setThis(invocation.getThis());
    root.setPermissionEvaluator(getPermissionEvaluator());
    root.setTrustResolver(getTrustResolver());
    root.setRoleHierarchy(getRoleHierarchy());
    root.setDefaultRolePrefix(getDefaultRolePrefix());
    return root;
  }
}
