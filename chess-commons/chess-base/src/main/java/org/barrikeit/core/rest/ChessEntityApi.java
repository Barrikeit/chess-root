package org.barrikeit.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.io.Serializable;
import org.barrikeit.core.util.annotations.Alphanumeric;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ChessEntityApi<D extends Serializable, S extends Serializable> {

  @ApiResponse(responseCode = "200", description = "ok")
  ResponseEntity<D> findByCode(@PathVariable("code") @Valid @Alphanumeric S code)
      throws MethodArgumentNotValidException;

  @ApiResponse(responseCode = "200", description = "ok")
  ResponseEntity<D> update(
      @PathVariable("code") @Valid @Alphanumeric S code, @RequestBody @Valid D dto)
      throws MethodArgumentNotValidException;
}
