package org.barrikeit.security.expression;

import java.net.URI;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("lbda.authorization.server.client")
public class AuthorizationServerClientProperties {
  /** URI que invocar para verificar la autorización */
  private URI uriAuthorize;

  /** Namespace o dominio que consultar, por ejemplo: SUE, SUT, GE... */
  private String namespace = "";
  /** Timout de la petición en segundos */
  private int requestTimeout = 60;
  /** Timout de la conexión en segundos */
  private int connectionTimeout = 30;

  public URI getUriAuthorize() {
    return uriAuthorize;
  }

  public void setUriAuthorize(URI uriAuthorize) {
    this.uriAuthorize = uriAuthorize;
  }

  public String getNamespace() {
    return namespace;
  }

  public void setNamespace(String namespace) {
    this.namespace = namespace;
  }

  public int getRequestTimeout() {
    return requestTimeout;
  }

  public void setRequestTimeout(int requestTimeout) {
    this.requestTimeout = requestTimeout;
  }

  public int getConnectionTimeout() {
    return connectionTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }
}
