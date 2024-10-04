package org.barrikeit.security.http;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Objects;

public class PermissionRequest {

  private final String resource;
  private final String operation;

  @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
  public PermissionRequest(
      @JsonProperty("resource") String resource, @JsonProperty("operation") String operation) {
    this.resource = resource;
    this.operation = operation;
  }

  public String getResource() {
    return resource;
  }

  public String getOperation() {
    return operation;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PermissionRequest that = (PermissionRequest) o;
    return Objects.equals(resource, that.resource) && Objects.equals(operation, that.operation);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resource, operation);
  }
}
