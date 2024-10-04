package org.barrikeit.security.auth;

import com.auth0.jwt.JWT;
import java.util.Collection;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class JWTAuth extends UsernamePasswordAuthenticationToken {
  private final String jwt;
  private JWT oAuthJwt;
  private Jwt springJwt;

  public JWTAuth(Object principal, Object credentials) {
    this(principal, credentials, null);
  }

  public JWTAuth(Object principal, Object credentials, String jwt) {
    super(principal, credentials);
    this.jwt = jwt;
  }

  public JWTAuth(
      Object principal,
      Object credentials,
      String jwt,
      Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
    this.jwt = jwt;
  }

  public String getJwt() {
    return this.jwt;
  }
}
