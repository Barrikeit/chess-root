package org.barrikeit.chess.core.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.barrikeit.chess.core.util.validators.Alphanumeric;
import org.barrikeit.chess.core.util.validators.Sanitize;

@Builder(toBuilder = true)
public record RoleDto(
    @NotNull @Size(max = 2) @Sanitize @Alphanumeric String code,
    @NotNull @Size(max = 50) @Sanitize @Alphanumeric String name)
    implements GenericDto {}
