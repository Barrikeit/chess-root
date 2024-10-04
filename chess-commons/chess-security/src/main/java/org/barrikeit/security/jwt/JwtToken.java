package org.barrikeit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;
import java.util.List;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.NonFinal;

@Value
public class JwtToken {
  String subject;
  List<String> roles;
  List<String> scopes;
  List<String> modulos;
  Date issuedAt;
  Date expiresAt;
  String issuer;
  Algorithm hmac256;
  boolean refresh;
  String sessionId;
  @NonFinal String jwtCache;

  @Builder
  public JwtToken(
      String subject,
      List<String> roles,
      List<String> scopes,
      List<String> modulos,
      Date issuedAt,
      Date expiresAt,
      String issuer,
      Algorithm hmac256,
      boolean refresh,
      String sessionId) {
    this.subject = subject;
    this.roles = roles;
    this.scopes = scopes;
    this.modulos = modulos;
    this.issuedAt = issuedAt;
    this.expiresAt = expiresAt;
    this.issuer = issuer;
    this.hmac256 = hmac256;
    this.refresh = refresh;
    this.sessionId = sessionId;
  }

  public String getJwtCache(boolean refresh) {
    if (jwtCache == null) {
      jwtCache =
          JWT.create()
              .withSubject(subject)
              .withIssuer(issuer)
              .withIssuedAt(issuedAt)
              .withExpiresAt(expiresAt)
              .withClaim(JwtClaimConstants.CLAIM_SCOPES, scopes)
              .withClaim(JwtClaimConstants.CLAIM_MODULOS, modulos)
              .withClaim(JwtClaimConstants.CLAIM_ROLES, roles)
              .withClaim(JwtClaimConstants.SESSION_ID, sessionId)
              .withClaim(JwtClaimConstants.CLAIM_REFRESH, refresh)
              .sign(hmac256);
    }
    return jwtCache;
  }
}
