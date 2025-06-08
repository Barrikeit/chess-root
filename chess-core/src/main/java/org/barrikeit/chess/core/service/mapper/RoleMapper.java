package org.barrikeit.chess.core.service.mapper;

import org.barrikeit.chess.core.service.dto.RoleDto;
import org.barrikeit.chess.domain.model.Role;
import org.mapstruct.*;

@Mapper(
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleDto> {

  Role toEntity(RoleDto source);

  RoleDto toDto(Role source);

  @Mapping(target = "id", ignore = true)
  void updateEntity(RoleDto source, @MappingTarget Role target);
}
