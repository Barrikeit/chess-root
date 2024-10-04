package org.barrikeit.security.http;

public class AuthorizationServerResponse {

  private boolean authorized = false;

  public AuthorizationServerResponse() {}

  public AuthorizationServerResponse(boolean authorized) {
    this.authorized = authorized;
  }

  public boolean isAuthorized() {
    return authorized;
  }

  public void setAuthorized(boolean authorized) {
    this.authorized = authorized;
  }
}
