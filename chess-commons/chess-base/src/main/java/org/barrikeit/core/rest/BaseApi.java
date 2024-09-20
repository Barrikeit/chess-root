package org.barrikeit.core.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import java.io.Serializable;
import java.util.List;
import org.springframework.http.ResponseEntity;

public interface BaseApi<D extends Serializable> {

  @ApiResponse(responseCode = "200", description = "ok")
  ResponseEntity<List<D>> findAll();
}
