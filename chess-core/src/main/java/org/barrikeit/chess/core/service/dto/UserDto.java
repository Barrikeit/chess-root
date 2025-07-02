package org.barrikeit.chess.core.service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import org.barrikeit.chess.core.util.validators.Alphanumeric;
import org.barrikeit.chess.core.util.validators.Password;

@Builder(toBuilder = true)
public record UserDto(
    Long id,
    @NotEmpty @Alphanumeric String username,
    @Password String password,
    @NotEmpty @Email String email,
    @NotNull boolean enabled,
    @NotNull boolean banned,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime banDate,
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss") LocalDateTime loginDate,
    @NotNull Integer loginAttempts,
    @Valid Set<RoleDto> roles)
    implements GenericDto {

  @Override
  public String toString() {
    return "UserDto{" + "username='" + username + '\'' + ", email='" + email + '\'' + '}';
  }
}
