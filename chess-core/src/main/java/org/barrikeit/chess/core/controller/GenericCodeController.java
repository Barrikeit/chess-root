package org.barrikeit.chess.core.controller;

import java.io.Serializable;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.service.GenericCodeService;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.domain.model.base.GenericCodeEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <b>Generic Code Controller Class</b>
 *
 * <p>This abstract class provides common RESTful endpoint implementations for managing model. It
 * relies on a {@link GenericCodeService} to handle business logic and data access. This controller
 * is designed to work with model that extend {@link GenericCodeEntity} and their corresponding
 * DTOs that extend {@link GenericDto}.
 *
 * @param <E> the entity type that extends {@link GenericCodeEntity}.
 * @param <I> the type of the entity's identifier, which must be {@link Serializable}.
 * @param <C> the type of the entity's code, which must be {@link Serializable}.
 * @param <D> the DTO type that extends {@link GenericDto}.
 */
@Log4j2
@Validated
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public abstract class GenericCodeController<
        E extends GenericCodeEntity<I, C>,
        I extends Serializable,
        C extends Serializable,
        D extends GenericDto>
    extends GenericController<E, I, D> {

  private final GenericCodeService<E, I, C, D> service;

  protected GenericCodeController(GenericCodeService<E, I, C, D> service) {
    super(service);
    this.service = service;
  }

  /**
   * Retrieves a specific DTO by its code.
   *
   * @param code the code of the entity to retrieve.
   * @return a response entity containing the requested DTO.
   */
  @GetMapping("/{code}")
  public ResponseEntity<D> findByCode(@PathVariable("code") C code) {
    return ResponseEntity.ok(service.findByCode(code));
  }

  /**
   * Updates an existing entity identified by its code with the provided DTO.
   *
   * @param code the code of the entity to update.
   * @param dto the DTO containing the updated entity information.
   * @return a response entity containing the updated DTO.
   */
  @PutMapping("/{code}/update")
  public ResponseEntity<D> updateByCode(@PathVariable("code") C code, @RequestBody D dto) {
    return ResponseEntity.ok(service.updateByCode(code, dto));
  }

  /**
   * Deletes an entity identified by its code.
   *
   * @param code the code of the entity to delete.
   * @return a response entity indicating the operation's result.
   */
  @DeleteMapping("/{code}")
  public ResponseEntity<Void> deleteByCode(@PathVariable("code") C code) {
    service.deleteByCode(code);
    return ResponseEntity.noContent().build();
  }
}
