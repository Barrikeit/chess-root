package org.barrikeit.core.config;

import io.micrometer.observation.ObservationRegistry;
import jakarta.servlet.ServletContext;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.observation.ObservationRegistryCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.server.observation.ServerRequestObservationContext;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class WebConfigurer implements ServletContextInitializer {

  private final Environment env;

  private final ApplicationProperties applicationProperties;

  @Override
  public void onStartup(ServletContext servletContext) {
    if (env.getActiveProfiles().length != 0) {
      log.info(
          "Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
    }
    log.info("Web application fully configured");
  }

  @Bean
  ObservationRegistry skipSpringActuatorObservations() {
    PathMatcher pathMatcher = new AntPathMatcher("/");
    ObservationRegistry observationRegistry = ObservationRegistry.create();
    observationRegistry
        .observationConfig()
        .observationPredicate(
            (name, context) -> {
              if (context
                  instanceof ServerRequestObservationContext serverRequestObservationContext) {
                return !(pathMatcher.match(
                        "/**/management/**",
                        serverRequestObservationContext.getCarrier().getRequestURI())
                    || pathMatcher.match(
                        "/**/api/**",
                        serverRequestObservationContext.getCarrier().getRequestURI()));
              } else {
                return true;
              }
            });
    return observationRegistry;
  }

  @Bean
  ObservationRegistryCustomizer<ObservationRegistry> skipSecuritySpansFromObservation() {
    return (registry) ->
        registry
            .observationConfig()
            .observationPredicate((name, context) -> !name.startsWith("spring.security"));
  }

  @Bean
  public CorsConfigurationSource corsOriginConfigurationSource() {

    CorsConfiguration configuration = applicationProperties.getCorsConfiguration();
    ApplicationProperties.Cors cors = applicationProperties.getCors();

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    if (cors.getEnabled()) {
      configuration.setAllowedOrigins(Arrays.asList(cors.getAllowed().getOrigins().split(",")));
      configuration.setAllowedMethods(Arrays.asList(cors.getAllowed().getMethods().split(",")));
      configuration.setAllowedHeaders(Arrays.asList(cors.getAllowed().getHeaders().split(",")));
      configuration.setAllowCredentials(true);

      source.registerCorsConfiguration(cors.getPath().getPattern(), configuration);
    }

    return source;
  }
}
