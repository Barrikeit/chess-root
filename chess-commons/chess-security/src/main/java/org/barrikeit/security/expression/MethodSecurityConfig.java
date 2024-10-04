package org.barrikeit.security.expression;

import org.barrikeit.security.filter.AppValidatorFilterProperties;
import org.barrikeit.security.http.AuthorizationServerClient;
import java.time.Duration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@Configuration
@ConditionalOnProperty(name = "chess.authorization.server.client.uriAuthorize")
@EnableConfigurationProperties({
  AuthorizationServerClientProperties.class,
  AppValidatorFilterProperties.class
})
@EnableGlobalMethodSecurity(prePostEnabled = true)
// TODO: quitar estas clases deprecadas
public class MethodSecurityConfig extends GlobalMethodSecurityConfiguration {

  private final AuthorizationServerClientProperties authorizationServerClientProperties;
  private final AppValidatorFilterProperties appValidatorFilterProperties;

  public MethodSecurityConfig(
      AuthorizationServerClientProperties authorizationServerClientProperties,
      AppValidatorFilterProperties appValidatorFilterProperties) {
    this.authorizationServerClientProperties = authorizationServerClientProperties;
    this.appValidatorFilterProperties = appValidatorFilterProperties;
  }

  @Override
  protected MethodSecurityExpressionHandler createExpressionHandler() {
    AuthorizationServerClient client =
        AuthorizationServerClient.witURI(authorizationServerClientProperties.getUriAuthorize())
            .namespace(authorizationServerClientProperties.getNamespace())
            .appHeaderName(appValidatorFilterProperties.getAppHeaderName())
            .appSecurityName(appValidatorFilterProperties.getAppSecurityName())
            .connectionTimeout(
                Duration.ofSeconds(authorizationServerClientProperties.getConnectionTimeout()))
            .requestTimeout(
                Duration.ofSeconds(authorizationServerClientProperties.getRequestTimeout()))
            .build();

    return new AuthorizationServerMethodSecurityExpressionHandler(client);
  }
}
