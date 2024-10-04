package org.barrikeit.core.service;

import static org.barrikeit.core.util.constants.ChessEntityConstants.CODE;
import static org.barrikeit.core.util.constants.ErrorConstants.ERROR_NOT_FOUND_MSG;

import java.io.Serializable;
import java.util.List;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.error.NotFoundException;
import org.barrikeit.core.repository.ChessEntityRepository;
import org.barrikeit.core.rest.dto.ChessDto;
import org.barrikeit.core.rest.mapper.BaseMapper;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

public abstract class ChessEntityServiceImpl<
        T extends ChessEntity<S>,
        K extends Serializable,
        D extends ChessDto<S>,
        S extends Serializable>
    extends BaseServiceImpl<T, K, D> implements ChessEntityService<T, K, D, S> {

  private final ChessEntityRepository<T, K, S> repository;
  private final BaseMapper<T, D> mapper;

  protected ChessEntityServiceImpl(
          ChessEntityRepository<T, K, S> repository, BaseMapper<T, D> mapper) {
    super(repository, mapper);
    this.repository = repository;
    this.mapper = mapper;
  }

  @Override
  public List<D> findAll() {
    return repository.findAll(Sort.by(Sort.Direction.ASC, CODE)).stream()
        .map(this.mapper::toDto)
        .toList();
  }

  @Override
  public D findByCode(S code) {
    return repository
        .findByCode(code)
        .map(mapper::toDto)
        .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_MSG, code));
  }

  @Override
  public T findEntityByCode(S code) {
    return repository
        .findByCode(code)
        .orElseThrow(() -> new NotFoundException(ERROR_NOT_FOUND_MSG, code));
  }

  @Override
  @Transactional
  public D update(S code, D dto) {
    T modelo = findEntityByCode(code);
    mapper.updateEntity(dto, modelo);
    return mapper.toDto(repository.save(modelo));
  }
}
