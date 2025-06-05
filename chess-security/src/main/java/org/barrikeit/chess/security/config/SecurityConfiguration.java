package org.barrikeit.chess.security.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.security.config.filter.AppHeaderValidatorFilter;
import org.barrikeit.chess.security.config.filter.JwtFilter;
import org.barrikeit.chess.security.service.SessionService;
import org.barrikeit.chess.security.util.JwtDecoder;
import org.barrikeit.chess.security.util.JwtProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.writers.StaticHeadersWriter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.web.cors.CorsConfigurationSource;

@Log4j2
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Import(SecurityExceptionHandler.class)
public class SecurityConfiguration<S extends Session> {
  private final SecurityProperties securityProperties;
  private final SecurityExceptionHandler exceptionHandler;
  private final JwtProvider jwtProvider;
  private final JwtDecoder jwtDecoder;

  private final SessionService<S> sessionService;
  private final CorsConfigurationSource corsConfigurationSource;
  private final FindByIndexNameSessionRepository<S> sessionRepository;

  @Value("${spring.security.http.session-management.concurrency-control.max-sessions}")
  private Integer maxNumberConcurrentSessionsUser;

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.cors(cors -> cors.configurationSource(corsConfigurationSource))
        .csrf(
            csrf ->
                csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    .requireCsrfProtectionMatcher(csrfConfig().getCsrfRequestMatcher()))
        .headers(
            headers ->
                headers.frameOptions(
                    options ->
                        options
                            .sameOrigin()
                            .addHeaderWriter(
                                new StaticHeadersWriter(
                                    "X-Content-Security-Policy", "default-src 'self'"))
                            .addHeaderWriter(
                                new StaticHeadersWriter("X-WebKit-CSP", "default-src 'self'"))))
        .sessionManagement(
            sessionManagement ->
                sessionManagement
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                    .maximumSessions(maxNumberConcurrentSessionsUser)
                    .sessionRegistry(sessionRegistry(sessionRepository)))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(HttpMethod.OPTIONS, "/**")
                    .permitAll()
                    .requestMatchers(
                        antMatcher("/images/**"),
                        antMatcher("/api/v1/error"),
                        antMatcher("/api/v1/auth/**"))
                    .permitAll()
                    .requestMatchers(antMatcher("/api/v1/user/**"), antMatcher("/api/v1/role/**"))
                    .authenticated()
                    .anyRequest()
                    .authenticated())
        .httpBasic(Customizer.withDefaults());

    http.addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);
    http.addFilterAfter(appHeaderValidatorFilter(), JwtFilter.class);

    return http.build();
  }

  @Bean
  JwtFilter jwtFilter() {
    return new JwtFilter(jwtProvider, jwtDecoder, sessionService);
  }

  @Bean
  AppHeaderValidatorFilter appHeaderValidatorFilter() {
    return new AppHeaderValidatorFilter(securityProperties.getAppValidatorFilter());
  }

  @Bean
  public HttpSessionEventPublisher httpSessionEventPublisher() {
    return new HttpSessionEventPublisher();
  }

  @Bean
  public SpringSessionBackedSessionRegistry<S> sessionRegistry(
      FindByIndexNameSessionRepository<S> sessionRepository) {
    return new SpringSessionBackedSessionRegistry<>(sessionRepository);
  }

  @Bean
  public CsrfConfiguration csrfConfig() {
    return new CsrfConfiguration();
  }
}
