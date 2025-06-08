package org.barrikeit.chess.core.service.mapper;

import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.mapstruct.MappingTarget;

public interface GenericMapper<E extends GenericEntity<?>, D extends GenericDto> {

  D toDto(E source);

  E toEntity(D source);

  void updateEntity(D source, @MappingTarget E target);
}
