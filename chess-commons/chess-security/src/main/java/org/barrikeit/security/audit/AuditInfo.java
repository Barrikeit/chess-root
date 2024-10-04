package org.barrikeit.security.audit;

import org.springframework.validation.annotation.Validated;

@Validated
public class AuditInfo {
  private String application;
  private String hostUser;
  private String host;

  private String token;

  public AuditInfo(String application, String hostUser, String host) {
    this.application = application;
    this.hostUser = hostUser;
    this.host = host;
  }

  public String getApplication() {
    return application;
  }

  public void setApplication(String application) {
    this.application = application;
  }

  public String getHostUser() {
    return hostUser;
  }

  public void setHostUser(String hostUser) {
    this.hostUser = hostUser;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String toHeaderString() {
    return application + "#" + hostUser + "#" + host;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }
}
