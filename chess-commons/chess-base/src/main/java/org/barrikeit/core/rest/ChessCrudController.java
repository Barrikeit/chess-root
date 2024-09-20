package org.barrikeit.core.rest;

import jakarta.validation.Valid;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import org.barrikeit.core.domain.ChessEntity;
import org.barrikeit.core.rest.dto.ChessDto;
import org.barrikeit.core.service.ChessCrudService;
import org.barrikeit.core.util.filter.BaseFilter;
import org.barrikeit.core.util.annotations.Params;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controlador CRUD genérico para la entidad Lbda. Esta clase abstracta se utiliza para implementar
 * operaciones CRUD estándar en la entidad Lbda.
 *
 * <p>Parámetros de tipo: T - Tipo de la entidad Lbda. K - Tipo del identificador de la entidad
 * Lbda. D - Tipo del DTO base de la entidad Lbda. F - Tipo del filtro base para la búsqueda de la
 * entidad Lbda.
 *
 * @author shiraly
 */
@AllArgsConstructor
@Validated
public abstract class ChessCrudController<
        T extends ChessEntity<K>,
        K extends Serializable,
        D extends ChessDto<K>,
        F extends BaseFilter>
    implements ChessCrudApi<D> {

  // Servicio CRUD para la entidad Lbda.
  private final ChessCrudService<T, K, D, F> service;

  /**
   * Método para buscar todas las entidades Lbda.
   *
   * @param page - Información de paginación para la búsqueda.
   * @param unpaged - Indica si la búsqueda debe ser paginada o no.
   * @param search - Cadena de búsqueda para filtrar las entidades Lbda.
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
