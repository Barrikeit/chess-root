package org.barrikeit.chess.security.service.dto;

import java.util.Date;
import lombok.*;
import org.barrikeit.chess.core.service.dto.UserDto;

@Builder
@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class JwtDto {
  private String jwt;

  private String refreshToken;

  private Date expireAt;

  private Date expireRefreshAt;

  private UserDto userDto;
}
