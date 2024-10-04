package org.barrikeit.security.auth;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class AuthProvider implements AuthenticationProvider {
  @Value("${spring.security.user.name:Unknown}")
  private String name;

  @Value("${spring.security.user.password:Unknown}")
  private String password;

  @Override
  public Authentication authenticate(Authentication auth) throws AuthenticationException {
    Authentication newAuth = auth;

    if (auth instanceof UsernamePasswordAuthenticationToken token
        && name.equals(token.getName())
        && password.equals(token.getCredentials())) {
      newAuth =
          new UsernamePasswordAuthenticationToken(
              token.getName(),
              token.getCredentials(),
              List.of(new SimpleGrantedAuthority("USER")));
    }

    return newAuth;
  }

  @Override
  public boolean supports(Class<?> auth) {
    return auth.equals(JWTAuth.class) || auth.equals(UsernamePasswordAuthenticationToken.class);
  }
}
