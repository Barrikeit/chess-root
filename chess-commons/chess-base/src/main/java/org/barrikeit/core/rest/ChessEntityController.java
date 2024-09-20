package org.barrikeit.core.rest;

import jakarta.validation.Valid;
import java.io.Serializable;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.rest.dto.ChessDto;
import org.barrikeit.core.service.ChessEntityService;
import org.barrikeit.core.util.annotations.Code;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Validated
public abstract class ChessEntityController<
        T extends ChessEntity<S>,
        K extends Serializable,
        D extends ChessDto<S>,
        S extends Serializable>
    extends BaseController<T, K, D> implements ChessEntityApi<D, S> {

  private final ChessEntityService<T, K, D, S> service;

  protected ChessEntityController(ChessEntityService<T, K, D, S> service) {
    super(service);
    this.service = service;
  }

  @GetMapping("/{code}")
  public ResponseEntity<D> findByCode(@PathVariable("code") @Valid @Code S code) {
    return ResponseEntity.ok(service.findByCode(code));
  }

  @PutMapping("/{code}/update")
  @Override
  public ResponseEntity<D> update(@PathVariable @Valid S code, @RequestBody @Valid D dto) {
    return ResponseEntity.ok(service.update(code, dto));
  }
}
