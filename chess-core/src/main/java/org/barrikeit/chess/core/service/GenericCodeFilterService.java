package org.barrikeit.chess.core.service;

import jakarta.transaction.Transactional;
import java.io.Serializable;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.service.mapper.GenericMapper;
import org.barrikeit.chess.core.util.constants.ExceptionConstants;
import org.barrikeit.chess.core.util.exceptions.NotFoundException;
import org.barrikeit.chess.core.util.filter.BaseFilter;
import org.barrikeit.chess.domain.model.base.GenericCodeEntity;
import org.barrikeit.chess.domain.repository.base.GenericCodeRepository;

@Log4j2
public abstract class GenericCodeFilterService<
        E extends GenericCodeEntity<I, C>,
        I extends Serializable,
        C extends Serializable,
        D extends GenericDto,
        F extends BaseFilter>
    extends GenericBaseFilterService<
        E, I, D, F, GenericCodeRepository<E, I, C>, GenericMapper<E, D>> {

  private final GenericCodeRepository<E, I, C> repository;
  private final GenericMapper<E, D> mapper;

  protected GenericCodeFilterService(
      GenericCodeRepository<E, I, C> repository, GenericMapper<E, D> mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }

  public D findByCode(C code) {
    return repository
        .findByCode(code)
        .map(mapper::toDto)
        .orElseThrow(() -> new NotFoundException(ExceptionConstants.NOT_FOUND, code));
  }

  public E findEntityByCode(C code) {
    return repository
        .findByCode(code)
        .orElseThrow(() -> new NotFoundException(ExceptionConstants.NOT_FOUND, code));
  }

  @Transactional
  public D updateByCode(C code, D dto) {
    E entity = findEntityByCode(code);
    mapper.updateEntity(dto, entity);
    return mapper.toDto(repository.save(entity));
  }

  @Transactional
  public void deleteByCode(C code) {
    E entity = findEntityByCode(code);
    repository.delete(entity);
  }
}
