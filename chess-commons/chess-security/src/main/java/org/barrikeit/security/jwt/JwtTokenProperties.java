package org.barrikeit.security.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties("lbda.authorization.jwt")
public class JwtTokenProperties {

  private String issuer;

  private String secret;
}
