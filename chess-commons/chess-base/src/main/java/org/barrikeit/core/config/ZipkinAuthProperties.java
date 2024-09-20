package org.barrikeit.core.config;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
// @ConfigurationProperties(prefix = "chess.authorization.zipkin", ignoreUnknownFields = false)
public class ZipkinAuthProperties {

  private String username;
  private String password;
}
