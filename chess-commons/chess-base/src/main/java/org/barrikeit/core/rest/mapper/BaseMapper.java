package org.barrikeit.core.rest.mapper;

import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.rest.dto.BaseDto;
import org.mapstruct.MappingTarget;

public interface BaseMapper<T extends BaseEntity, D extends BaseDto> {

  D toDto(T source);

  T toEntity(D source);

  void updateEntity(D source, @MappingTarget T target);
}
