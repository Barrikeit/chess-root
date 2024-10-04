package org.barrikeit.security.jwt;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class JwtClaimConstants {

  public static final String CLAIM_SCOPES = "scopes";

  public static final String CLAIM_DOMAIN = "domain";

  public static final String CLAIM_REFRESH = "refresh";

  public static final String CLAIM_ROLES = "roles";

  public static final String CLAIM_MODULOS = "modulos";

  public static final String SESSION_ID = "sessionId";
}
