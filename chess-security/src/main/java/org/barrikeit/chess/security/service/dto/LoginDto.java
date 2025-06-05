package org.barrikeit.chess.security.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.barrikeit.chess.core.util.validators.Alphanumeric;
import org.barrikeit.chess.core.util.validators.Password;
import org.barrikeit.chess.core.util.validators.Sanitize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDto {

  private Long id;

  @NotBlank @Sanitize @Alphanumeric private String username;

  //@Password
  @NotBlank @Sanitize private String password;
}
