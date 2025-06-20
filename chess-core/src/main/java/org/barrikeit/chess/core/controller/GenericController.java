package org.barrikeit.chess.core.controller;

import java.io.Serializable;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.barrikeit.chess.core.service.GenericService;
import org.barrikeit.chess.core.service.dto.GenericDto;
import org.barrikeit.chess.domain.model.base.GenericEntity;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * <b>Generic Controller Class</b>
 *
 * <p>This abstract class provides common RESTful endpoint implementations for managing model. It
 * relies on a {@link GenericService} to handle business logic and data access. This controller is
 * designed to work with model that extend {@link GenericEntity} and their corresponding DTOs
 * that extend {@link GenericDto}.
 *
 * @param <E> the entity type that extends {@link GenericEntity}.
 * @param <S> the type of the entity's identifier, which must be {@link Serializable}.
 * @param <D> the DTO type that extends {@link GenericDto}.
 */
@Log4j2
@Validated
@AllArgsConstructor
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public abstract class GenericController<
    E extends GenericEntity<S>, S extends Serializable, D extends GenericDto> {

  private final GenericService<E, S, D> service;

  /**
   * Retrieves a list of all DTOs.
   *
   * @return a response entity containing a list of DTOs.
   */
  @GetMapping
  public ResponseEntity<List<D>> findAll() {
    return ResponseEntity.ok(service.findAll());
  }

  /**
   * Retrieves a specific DTO by its identifier.
   *
   * @param id the identifier of the entity to retrieve.
   * @return a response entity containing the requested DTO.
   */
  @GetMapping("/id/{id}")
  public ResponseEntity<D> findById(@PathVariable("id") S id) {
    return ResponseEntity.ok(service.find(id));
  }

  /**
   * Saves a new entity represented by the provided DTO.
   *
   * @param dto the DTO representing the entity to save.
   * @return a response entity containing the saved DTO.
   */
  @PostMapping()
  public ResponseEntity<D> save(@Validated @RequestBody D dto) {
    return ResponseEntity.ok(service.save(dto));
  }

  /**
   * Updates an existing entity identified by its identifier with the provided DTO.
   *
   * @param id the identifier of the entity to update.
   * @param dto the DTO containing the updated entity information.
   * @return a response entity containing the updated DTO.
   */
  @PutMapping("/{id}/update")
  public ResponseEntity<D> update(@PathVariable("id") S id, @RequestBody D dto) {
    return ResponseEntity.ok(service.update(id, dto));
  }

  /**
   * Deletes an entity identified by its identifier.
   *
   * @param id the identifier of the entity to delete.
   * @return a response entity indicating the operation's result.
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable("id") S id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
