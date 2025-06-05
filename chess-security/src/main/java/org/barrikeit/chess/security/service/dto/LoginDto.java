package org.barrikeit.chess.security.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.barrikeit.chess.core.util.validation.Alphanumeric;
import org.barrikeit.chess.core.util.validation.Sanitize;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class LoginDto {

  private Long id;

  @NotBlank @Sanitize @Alphanumeric private String username;

  // @ValidPassword
  @NotBlank @Sanitize private String password;
}
