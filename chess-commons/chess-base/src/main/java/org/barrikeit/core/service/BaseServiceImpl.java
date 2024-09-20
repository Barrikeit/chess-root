package org.barrikeit.core.service;

import static org.barrikeit.core.util.constants.ChessEntityConstants.ID;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_MSG_NOT_FOUND;

import java.io.Serializable;
import java.util.List;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.error.NotFoundException;
import org.barrikeit.core.repository.BaseRepository;
import org.barrikeit.core.rest.mapper.BaseMapper;
import org.barrikeit.core.rest.dto.BaseDto;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

public abstract class BaseServiceImpl<
        T extends BaseEntity, K extends Serializable, D extends BaseDto>
    implements BaseService<T, K, D> {

  private final BaseRepository<T, K> repository;
  private final BaseMapper<T, D> mapper;

  protected BaseServiceImpl(BaseRepository<T, K> repository, BaseMapper<T, D> mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<D> findAll() {
    return repository.findAll(Sort.by(Sort.Direction.ASC, ID)).stream()
        .map(this.mapper::toDto)
        .toList();
  }

  @Override
  public List<D> findAll(Sort sort) {
    return repository.findAll(sort).stream().map(this.mapper::toDto).toList();
  }

  @Override
  public D findById(K id) {
    return repository
        .findById(id)
        .map(this.mapper::toDto)
        .orElseThrow(() -> new NotFoundException(ERROR_MSG_NOT_FOUND, id));
  }

  @Override
  @Transactional
  public D save(D dto) {
    T entity = mapper.toEntity(dto);
    return mapper.toDto(repository.save(entity));
  }

  @Override
  @Transactional
  public T save(T entity) {
    return repository.save(entity);
  }

  @Override
  @Transactional
  public void deleteById(K id) {
    repository.deleteById(id);
  }
}
