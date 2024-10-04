package org.barrikeit.security.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.http.HttpResponse;
import java.util.function.Supplier;

class JsonBodyHandler<T> implements HttpResponse.BodyHandler<Supplier<T>> {

  private final Class<T> clazz;

  public JsonBodyHandler(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public HttpResponse.BodySubscriber<Supplier<T>> apply(HttpResponse.ResponseInfo responseInfo) {
    return asJSON(clazz);
  }

  public static <T> HttpResponse.BodySubscriber<Supplier<T>> asJSON(Class<T> targetType) {
    HttpResponse.BodySubscriber<InputStream> upstream =
        HttpResponse.BodySubscribers.ofInputStream();

    return HttpResponse.BodySubscribers.mapping(
        upstream, inputStream -> toSupplierOfType(inputStream, targetType));
  }

  public static <T> Supplier<T> toSupplierOfType(InputStream inputStream, Class<T> targetType) {
    return () -> {
      try (InputStream stream = inputStream) {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(stream, targetType);
      } catch (IOException e) {
        throw new UncheckedIOException(
            "Error reading input stream for type: " + targetType.getName(), e);
      }
    };
  }
}
