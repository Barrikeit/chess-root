package org.barrikeit.chess.core.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.barrikeit.chess.core.util.validators.Alphanumeric;
import org.barrikeit.chess.core.util.validators.Sanitize;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class RoleDto extends GenericDto {

  @NotNull
  @Size(max = 2)
  @Sanitize
  @Alphanumeric
  String code;

  @NotNull
  @Size(max = 50)
  @Sanitize
  @Alphanumeric
  String name;
}
