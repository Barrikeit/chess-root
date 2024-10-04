package org.barrikeit.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtTokenProvider {

  private final Algorithm hmac256;

  private final String issuer;

  public JwtTokenProvider(JwtTokenProperties jwtProperties) {
    this.hmac256 = Algorithm.HMAC256(jwtProperties.getSecret());
    this.issuer = jwtProperties.getIssuer();
  }

  public String validateTokenAndRetrieveSubject(String token) {
    return JWT.require(this.hmac256).withIssuer(this.issuer).build().verify(token).getSubject();
  }

  public DecodedJWT validateTokenAndRetrieveDecoded(String token) {
    return JWT.require(this.hmac256).withIssuer(this.issuer).build().verify(token);
  }

  public JwtToken decode(String token) {
    DecodedJWT decoded = JWT.require(hmac256).withIssuer(issuer).build().verify(token);
    return JwtToken.builder()
        .expiresAt(decoded.getExpiresAt())
        .issuedAt(decoded.getIssuedAt())
        .issuer(decoded.getIssuer())
        .scopes(decoded.getClaim(JwtClaimConstants.CLAIM_SCOPES).asList(String.class))
        .refresh(decoded.getClaim(JwtClaimConstants.CLAIM_REFRESH).asBoolean())
        .sessionId(decoded.getClaim(JwtClaimConstants.SESSION_ID).toString())
        .subject(decoded.getSubject())
        .build();
  }

  public List<String> getRolesClaim(DecodedJWT decodedJWT) {
    if (decodedJWT.getClaim(JwtClaimConstants.CLAIM_ROLES) != null) {
      return decodedJWT.getClaim(JwtClaimConstants.CLAIM_ROLES).asList(String.class);
    }
    return null;
  }

  public List<SimpleGrantedAuthority> getAuthorities(String token) {
    ArrayList<SimpleGrantedAuthority> result = new ArrayList<>();
    DecodedJWT decodedJWT = validateTokenAndRetrieveDecoded(token);
    List<String> rolesClaim = getRolesClaim(decodedJWT);
    for (String role : rolesClaim) {
      result.add(new SimpleGrantedAuthority(role));
    }
    return result;
  }
}
