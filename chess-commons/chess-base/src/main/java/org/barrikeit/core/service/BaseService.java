package org.barrikeit.core.service;

import java.io.Serializable;
import java.util.List;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.rest.dto.BaseDto;
import org.springframework.data.domain.Sort;

public interface BaseService<
    T extends BaseEntity, K extends Serializable, D extends BaseDto> {
  List<D> findAll();

  List<D> findAll(Sort sort);

  D findById(K id);

  D save(D dto);

  T save(T entity);

  void deleteById(K id);
}
