package org.barrikeit.security.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.net.URI;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Petición para el servidor de autorización.<br>
 * En la petición siembre se debe indicar el token jwt del usuario a autorizar y el conjunto de
 * permisos requerido.<br>
 * Con esta información, el servidor de autorización podrá autenticar al usuario mediante el token y
 * validará si dicho usuario dispone de todos los permisos solicitados.
 */
public class AuthorizationServerRequest {

  // security context properties
  private final String jwt;
  private final String namespace;
  private Set<PermissionRequest> permissionRequests = new HashSet<>();

  // servlet context properties
  private URI uri;
  private String method;
  private Map<String, List<String>> headers;
  private String remoteAddress;

  public AuthorizationServerRequest(String jwt, String namespace) {
    this.jwt = jwt;
    this.namespace = namespace;
  }

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public AuthorizationServerRequest(
      @JsonProperty("jwt") String jwt,
      @JsonProperty("permissionRequests") Set<PermissionRequest> permissionRequests,
      @JsonProperty("namespace") String namespace) {
    this.jwt = jwt;
    this.permissionRequests = permissionRequests;
    this.namespace = namespace;
  }

  public String getJwt() {
    return jwt;
  }

  public String getNamespace() {
    return namespace;
  }

  public Set<PermissionRequest> getPermissionRequests() {
    return permissionRequests;
  }

  public void addPermissionRequest(PermissionRequest permissionRequest) {
    permissionRequests.add(permissionRequest);
  }

  public void addPermissionRequest(String resource, String action) {
    permissionRequests.add(new PermissionRequest(resource, action));
  }

  public void addPermissionRequests(String resource, Collection<String> actions) {
    actions.forEach(action -> addPermissionRequest(resource, action));
  }

  public void setUri(URI uri) {
    this.uri = uri;
  }

  public URI getUri() {
    return uri;
  }

  public void setMethod(String method) {
    this.method = method;
  }

  public String getMethod() {
    return method;
  }

  public void setHeaders(Map<String, List<String>> headers) {
    this.headers = headers;
  }

  public Map<String, List<String>> getHeaders() {
    return headers;
  }

  public void setRemoteAddress(String remoteAddress) {
    this.remoteAddress = remoteAddress;
  }

  public String getRemoteAddress() {
    return remoteAddress;
  }
}
