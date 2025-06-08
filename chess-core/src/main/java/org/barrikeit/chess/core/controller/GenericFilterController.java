package org.barrikeit.chess.core.controller;

import jakarta.validation.Valid;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import org.barrikeit.chess.core.service.GenericFilterService;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.core.util.filter.BaseFilter;
import org.barrikeit.chess.core.util.validators.Params;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@AllArgsConstructor
@Validated
public abstract class GenericFilterController<
    T extends GenericEntity<I>,
    I extends Serializable,
    D extends GenericDto,
    F extends BaseFilter> {

  private final GenericFilterService<T, I, D, F> service;

  /**
   * Método para buscar todas las entidades Chess.
   *
   * @param page - Información de paginación para la búsqueda.
   * @param unpaged - Indica si la búsqueda debe ser paginada o no.
   * @param search - Cadena de búsqueda para filtrar las entidades Chess.
   * @return ResponseEntity con la página de resultados de la búsqueda.
   */
  @GetMapping
  public ResponseEntity<Page<D>> findAll(
      Pageable page,
      @RequestParam(required = false, defaultValue = "false") boolean unpaged,
      @RequestParam(required = false) @Valid @Params String search) {
    return ResponseEntity.ok(service.search(page, unpaged, search));
  }
}
