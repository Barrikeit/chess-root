package org.barrikeit.security.http;

import static java.time.temporal.ChronoUnit.SECONDS;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.barrikeit.security.audit.AuditHolder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.function.Supplier;

/**
 * Cliente HTTP para comunicarse con un servidor de autorización.. <br>
 * La clase se debe instanciar mediante su correspondiente builder {@link
 * AuthorizationServerClientBuilder}, al cual se le debe indicar la URL en la que se encuentra el
 * servidor de autorización. <br>
 * Opcionalmente se pueden indicar otros parámetros como el usuario y la contraseña, en el caso de
 * que el servidor de que el servidor requiere Basic Authentication. <br>
 * También se puede indicar el <i>timeout</i> de conexión y petición, que por defecto es 30s y 60s
 * respectivamente.
 */
public class AuthorizationServerClient {

  private final ObjectMapper objectMapper = new ObjectMapper();

  private URI authorizationURI;

  private Duration requestTimeout;

  private HttpClient httpClient;

  private String namespace;

  private String appHeaderName;

  private String appSecurityName;

  private AuthorizationServerClient() {}

  public static AuthorizationServerClientBuilder witURI(URI authorizationURI) {
    return new AuthorizationServerClientBuilder(authorizationURI);
  }

  public HttpResponse<Supplier<AuthorizationServerResponse>> authorize(
      AuthorizationServerRequest authorizationServerRequest)
      throws IOException, InterruptedException {
    HttpRequest request =
        HttpRequest.newBuilder()
            .uri(authorizationURI)
            .header("Content-Type", "application/json")
            .header("Accept", "application/json")
            .header("Authorization", "Bearer " + authorizationServerRequest.getJwt())
            .header("x-aei-requester", "security-client")
            .header(appHeaderName, appSecurityName)
            .header("x-aei-session", AuditHolder.getAuditInfo().toHeaderString())
            .timeout(requestTimeout)
            .POST(
                HttpRequest.BodyPublishers.ofByteArray(
                    objectMapper.writeValueAsBytes(authorizationServerRequest)))
            .build();

    return httpClient.send(request, new JsonBodyHandler<>(AuthorizationServerResponse.class));
  }

  public String getNamespace() {
    return namespace;
  }

  public static class AuthorizationServerClientBuilder {
    private final URI authorizationURI;
    private String namespace;
    private String appHeaderName;
    private String appSecurityName;
    private Duration requestTimeout = Duration.of(60, SECONDS);
    private Duration connectionTimeout = Duration.of(30, SECONDS);

    private AuthorizationServerClientBuilder(URI authorizationURI) {
      this.authorizationURI = authorizationURI;
    }

    public AuthorizationServerClientBuilder namespace(String namespace) {
      this.namespace = namespace;
      return this;
    }

    public AuthorizationServerClientBuilder appHeaderName(String appHeaderName) {
      this.appHeaderName = appHeaderName;
      return this;
    }

    public AuthorizationServerClientBuilder appSecurityName(String appSecurityName) {
      this.appSecurityName = appSecurityName;
      return this;
    }

    public AuthorizationServerClientBuilder requestTimeout(Duration requestTimeout) {
      this.requestTimeout = requestTimeout;
      return this;
    }

    public AuthorizationServerClientBuilder connectionTimeout(Duration connectionTimeout) {
      this.connectionTimeout = connectionTimeout;
      return this;
    }

    public AuthorizationServerClient build() {
      AuthorizationServerClient authorizationServerClient = new AuthorizationServerClient();
      authorizationServerClient.authorizationURI = this.authorizationURI;
      authorizationServerClient.requestTimeout = this.requestTimeout;
      authorizationServerClient.namespace = this.namespace;
      authorizationServerClient.appHeaderName = this.appHeaderName;
      authorizationServerClient.appSecurityName = this.appSecurityName;
      authorizationServerClient.httpClient =
          HttpClient.newBuilder().connectTimeout(connectionTimeout).build();
      return authorizationServerClient;
    }
  }
}
