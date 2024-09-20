package org.barrikeit.core.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Propiedades concretas para la aplicación SUE API Gateway.
 *
 * <p>Las propiedades se deben configurar en el fichero {@code application.yml} file.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "chess.application", ignoreUnknownFields = false)
public class ApplicationProperties {

  private final CorsConfiguration corsConfiguration = new CorsConfiguration();

  private final CacheProperties cache = new CacheProperties();

  private String title;
  private String description;
  private String version;
  private String name;
  private String timeZone;

  private Cors cors;

  @Getter
  @Setter
  public static class Cors {
    private Allowed allowed;
    private Boolean enabled;
    private Path path;
  }

  @Getter
  @Setter
  public static class Allowed {
    private String methods;
    private String headers;
    private String origins;
  }

  @Getter
  @Setter
  public static class Path {
    private String pattern;
  }

  @Getter
  @Setter
  public static class CacheProperties {
    private int expireAfterAccessInSeconds = 60;
    private int maximumSize = 10_000;
  }
}
