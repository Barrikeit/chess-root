package org.barrikeit.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.io.Serializable;
import org.barrikeit.core.util.annotations.Params;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

public interface ChessCrudApi<D extends Serializable> {

  @ApiResponse(responseCode = "200", description = "ok")
  ResponseEntity<Page<D>> findAll(
      Pageable page,
      @RequestParam(required = false, defaultValue = "false") boolean unpaged,
      @RequestParam(required = false) @Valid @Params String search);
}
