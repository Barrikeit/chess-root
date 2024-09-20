package org.barrikeit.core.rest;

import java.io.Serializable;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.barrikeit.core.domain.BaseEntity;
import org.barrikeit.core.rest.dto.BaseDto;
import org.barrikeit.core.service.BaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Getter
@Validated
public abstract class BaseController<
        T extends BaseEntity, K extends Serializable, D extends BaseDto>
    implements BaseApi<D> {

  private final BaseService<T, K, D> service;

  protected BaseController(BaseService<T, K, D> service) {
    this.service = service;
  }

  @GetMapping
  public ResponseEntity<List<D>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }
}
